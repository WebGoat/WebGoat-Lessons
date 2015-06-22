package org.owasp.webgoat.converter;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.base.Verify;
import org.apache.commons.io.FileUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardOpenOption.CREATE;
import static java.nio.file.StandardOpenOption.TRUNCATE_EXISTING;
import static org.owasp.webgoat.converter.LessonConverterFileUtils.copyTo;
import static org.owasp.webgoat.converter.LessonConverterFileUtils.replaceInFiles;
import static org.owasp.webgoat.plugins.PluginFileUtils.createDirsIfNotExists;

public class LessonCreator {
    private static final String PACKAGE = "src/main/java/org/owasp/webgoat/plugin";
    private static final String LESSONS = "src/main/resources/plugin/%s/%s";
    private static final String I18N = "src/main/resources/plugin/i18n";
    private final String lessonName;
    private final Path srcDir;
    private final String destName;
    private Path destDir;
    private Path lessonSourcePackage;
    private Path lessonPlanDirectory;
    private Path lessonSolutionDirectory;
    private Path lessonSolutionImageDirectory;
    private Path i18nDirectory;

    public LessonCreator(String lessonName, Path destDir, String destName, Path srcDir) {
        Preconditions.checkNotNull(lessonName);
        Preconditions.checkNotNull(destDir);
        Preconditions.checkNotNull(srcDir);

        this.lessonName = lessonName;
        this.destName = destName;
        this.destDir = destDir.resolve(lessonNameToProjectDirectoryName());
        this.srcDir = srcDir;
    }

    public String lessonNameToProjectDirectoryName() {
        if (StringUtils.hasText(destName)) {
            return destName;
        }
        if (CharMatcher.JAVA_UPPER_CASE.matchesAllOf(lessonName)) {
            return lessonName.toLowerCase();
        } else {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, lessonName);
        }
    }

    public void writePomFile(JavaSource javaSource) throws IOException {
        String lessonProjectName = lessonNameToProjectDirectoryName();
        Path pomFile = Files.createFile(destDir.resolve("pom.xml"));
        Logger.log("Creating pom file '%s' with project name '%s'", pomFile, lessonProjectName);
        String pomExample = javaSource.containsReferenceToJavax() ? "pom2.example" : "pom1.example";
        Files.write(pomFile, new PomCreator().createPom(lessonProjectName, pomExample), Charsets.UTF_8, CREATE);
    }

    public void deleteDirectory() throws IOException {
        Logger.log("Deleting the destination directory '%s'", destDir);
        FileUtils.deleteDirectory(destDir.toFile());
    }

    public void createDirectory() throws IOException {
        Logger.log("Creating directory '%s'", destDir);
        destDir = createDirsIfNotExists(destDir);
    }

    public void createPackageForSources() throws IOException {
        Path packageDirectory = destDir.resolve(PACKAGE);
        Logger.log("Creating package structure '%s'", packageDirectory);
        this.lessonSourcePackage = createDirsIfNotExists(packageDirectory);
    }

    public JavaSource copyJavaSourceFiles() throws IOException {
        Logger.start("Starting to copy the java source files...");
        List<JavaSource> sources = new JavaSourceLessonFinder(srcDir).findSources(lessonName);

        Verify.verify(sources.size() == 1, "Multiple lessons found for lesson: " + lessonName);

        JavaSource javaSourceFile = sources.get(0);

        Path javaTargetSourceFile = destDir.resolve(this.lessonSourcePackage)
                .resolve(javaSourceFile.getJavaSourceFile().getFileName());
        Files.write(javaTargetSourceFile, javaSourceFile.getLines(), StandardCharsets.UTF_8, CREATE, TRUNCATE_EXISTING);

        Logger.end();
        return javaSourceFile;
    }

    public void copyLessonPlans() throws IOException {
        Logger.start("Starting to copy the lesson plans...");
        HtmlLessonSourceFinder htmlLessonSourceFinder = new HtmlLessonSourceFinder(srcDir, lessonName);
        Map<String, Path> lessons = htmlLessonSourceFinder.findSources();

        for (Map.Entry<String, Path> lesson : lessons.entrySet()) {
            Path target = createDirsIfNotExists(lessonPlanDirectory.resolve(lesson.getKey()));
            copyTo(lesson.getValue(), target);
        }

        Logger.end();
    }

    public void createHtmlLessonDirectory() throws IOException {
        Logger.log("Starting to copy the lesson plans...");
        String lessonPlansDir = String.format(LESSONS, lessonName, "lessonPlans");
        this.lessonPlanDirectory = createDirsIfNotExists(destDir.resolve(lessonPlansDir));
    }

    public void createHtmlSolutionDirectory() throws IOException {
        Logger.log("Creating the lesson solution directory...");
        String dir = String.format(LESSONS, lessonName, "lessonSolutions");
        this.lessonSolutionDirectory = createDirsIfNotExists(destDir.resolve(dir).resolve("en"));
        this.lessonSolutionImageDirectory = createDirsIfNotExists(lessonSolutionDirectory.resolve(lessonName + "_files"));
    }

    public void copyLessonSolutions() throws IOException {
        Logger.start("Starting to copy the lesson solutions...");
        HtmlLessonSolutionFinder htmlLessonSolutionFinder = new HtmlLessonSolutionFinder(srcDir, lessonName);
        List<Path> targetSolutions = copyTo(htmlLessonSolutionFinder.findHtmlSolutions(), lessonSolutionDirectory);
        replaceInFiles("lesson_solutions/", "", targetSolutions);
        replaceInFiles("content=\"text/html; charset=windows-1252\"", "content=\"text/html; charset=UTF-8\"", targetSolutions);

        List<Path> solutionImages = htmlLessonSolutionFinder.findSolutionImages();
        for (Path image : solutionImages) {
            copyTo(image, lessonSolutionImageDirectory);
        }
        Logger.end();
    }

    public void createResourceBundleDirectory() throws IOException {
        Logger.log("Creating i18n directory...");
        this.i18nDirectory = createDirsIfNotExists(destDir.resolve(I18N));
    }

    public void copyI18N(JavaSource javaSource) throws IOException {
        new PropertyCreator(srcDir, i18nDirectory, javaSource).write();
    }
}

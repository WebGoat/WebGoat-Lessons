package org.owasp.webgoat.converter;

import com.google.common.base.*;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.owasp.webgoat.converter.LessonConverterFileUtils.copyTo;
import static org.owasp.webgoat.plugins.PluginFileUtils.createDirsIfNotExists;

@Log
public class LessonCreator {
    private static final String PACKAGE = "src/main/java/org/owasp/webgoat/plugin";
    private static final String LESSONS = "src/main/resources/plugin/%s/%s";
    private final String lessonName;
    private final Path srcDir;
    private Path destDir;
    private Path lessonSourcePackage;
    private Path lessonPlanDirectory;
    private Path lessonSolutionDirectory;
    private Path lessonSolutionImageDirectory;

    public LessonCreator(String lessonName, Path destDir, Path srcDir) {
        Preconditions.checkNotNull(lessonName);
        Preconditions.checkNotNull(destDir);
        Preconditions.checkNotNull(srcDir);

        this.lessonName = lessonName;
        this.destDir = destDir.resolve(lessonNameToProjectDirectoryName());
        this.srcDir = srcDir;
    }

    public String lessonNameToProjectDirectoryName() {
        if (CharMatcher.JAVA_UPPER_CASE.matchesAllOf(lessonName)) {
            return lessonName.toLowerCase();
        } else {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, lessonName);
        }
    }

    public void writePomFile() throws IOException {
        Path pomFile = Files.createFile(destDir.resolve("pom.xml"));
        Files.write(pomFile, new PomCreator().createPom(lessonNameToProjectDirectoryName()), Charsets.UTF_8, CREATE);
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
        copyTo(javaSourceFile.getJavaSourceFile(), destDir.resolve(this.lessonSourcePackage), REPLACE_EXISTING);

        Logger.end();
        return javaSourceFile;
    }

    public void copyLessonPlans() throws IOException {
        Logger.start("Starting to copy the lesson plans...");
        HtmlLessonSourceFinder htmlLessonSourceFinder = new HtmlLessonSourceFinder(srcDir, lessonName);
        Map<String, Path> lessons = htmlLessonSourceFinder.findSources();

        for (Map.Entry<String, Path> lesson : lessons.entrySet()) {
            Path target = createDirsIfNotExists(lessonPlanDirectory.resolve(lessonName).resolve(lesson.getKey()));
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
        String dir = String.format(LESSONS, lessonName, "lessonSolution");
        this.lessonSolutionDirectory = createDirsIfNotExists(destDir.resolve(dir).resolve("en"));
        this.lessonSolutionImageDirectory = createDirsIfNotExists(lessonSolutionDirectory.resolve(lessonName + "_files"));
    }

    public void copyLessonSolutions() throws IOException {
        Logger.start("Starting to copy the lesson solutions...");
        HtmlLessonSolutionFinder htmlLessonSolutionFinder = new HtmlLessonSolutionFinder(srcDir, lessonName);
        copyTo(htmlLessonSolutionFinder.findHtmlSolutions(), lessonSolutionDirectory);

        List<Path> solutionImages = htmlLessonSolutionFinder.findSolutionImages();
        for (Path image : solutionImages) {
            copyTo(image, lessonSolutionImageDirectory);
        }
        Logger.end();
    }
}

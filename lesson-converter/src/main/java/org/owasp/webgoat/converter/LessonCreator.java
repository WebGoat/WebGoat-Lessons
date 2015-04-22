package org.owasp.webgoat.converter;

import com.google.common.base.CaseFormat;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import org.apache.commons.io.FileUtils;
import org.owasp.webgoat.plugins.PluginFileUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static java.nio.file.StandardOpenOption.CREATE;

public class LessonCreator {
    private static final String PACKAGE = "src/main/java/org/owasp/webgoat/plugin";
    private final String lessonName;
    private final Path srcDir;
    private Path destDir;
    private Path lessonSourcePackage;

    public LessonCreator(String lessonName, Path destDir, Path srcDir) {
        Preconditions.checkNotNull(lessonName);
        Preconditions.checkNotNull(destDir);
        Preconditions.checkNotNull(srcDir);

        this.lessonName = lessonName;
        this.destDir = destDir.resolve(lessonNameToDirectoryName());
        this.srcDir = srcDir;
    }

    public String lessonNameToDirectoryName() {
        if (CharMatcher.JAVA_UPPER_CASE.matchesAllOf(lessonName)) {
            return lessonName.toLowerCase();
        } else {
            return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, lessonName);
        }
    }

    public void writePomFile() throws IOException {
        Path pomFile = Files.createFile(destDir.resolve("pom.xml"));
        Files.write(pomFile, new PomCreator().createPom(lessonNameToDirectoryName()), Charsets.UTF_8, CREATE);
    }

    public void deleteDirectory() throws IOException {
        FileUtils.deleteDirectory(destDir.toFile());
    }

    public void createDirectory() throws IOException {
        destDir = PluginFileUtils.createDirsIfNotExists(destDir);
    }

    public void createPackageForSources() throws IOException {
        this.lessonSourcePackage = PluginFileUtils.createDirsIfNotExists(destDir.resolve(PACKAGE));
    }

    public JavaSource copyJavaSourceFiles() throws IOException {
        List<Path> sources = new JavaSourceLessonFinder(srcDir).findSources(lessonName);
        if (sources.size() > 1) {
            throw new ConverterException("Multiple lessons found for lesson: " + lessonName);
        }
        Files.copy(sources.get(0), this.lessonSourcePackage, StandardCopyOption.REPLACE_EXISTING);
        return null; //TODO
    }
}

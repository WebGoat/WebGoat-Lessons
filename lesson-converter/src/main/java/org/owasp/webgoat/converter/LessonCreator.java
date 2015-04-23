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

import static java.nio.file.StandardOpenOption.CREATE;

public class LessonCreator {
    private final String lessonName;
    private Path destDir;

    public LessonCreator(String lessonName, Path destDir) {
        Preconditions.checkNotNull(lessonName);
        Preconditions.checkNotNull(destDir);

        this.lessonName = lessonName;
        this.destDir = destDir.resolve(lessonNameToDirectoryName());
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

    public void copyJavaSourceFiles() {

    }
}

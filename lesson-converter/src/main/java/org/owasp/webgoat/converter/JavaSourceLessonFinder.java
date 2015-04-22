package org.owasp.webgoat.converter;

import com.google.common.base.Preconditions;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

@Log
public class JavaSourceLessonFinder {

    private static final String JAVA = ".java";

    private final Path srcDir;

    public JavaSourceLessonFinder(Path srcDir) {
        Preconditions.checkNotNull(srcDir, "src-dir cannot be null");

        this.srcDir = srcDir;
    }

    //TODO: detect multiple sources within a package now only is able to detect single lessons
    public List<JavaSource> findSources(final String lessonName) throws IOException {
        final List<JavaSource> sourceFiles = new ArrayList<>();
        final String lessonJavaName = lessonName + JAVA;

        Files.walkFileTree(srcDir, new SimpleFileVisitor<Path>() {

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                log.info(String.format("Visiting %s and see if it matches %s", file.getFileName(), lessonJavaName));
                if (file.getFileName().toString().equals(lessonJavaName)) {
                    sourceFiles.add(new JavaSource(file, lessonName));
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return sourceFiles;
    }
}

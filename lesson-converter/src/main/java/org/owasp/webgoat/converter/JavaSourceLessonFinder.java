package org.owasp.webgoat.converter;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Log
public class JavaSourceLessonFinder {

    private static final String JAVA = ".java";

    private final Path srcDir;

    public JavaSourceLessonFinder(Path srcDir) {
        this.srcDir = Objects.requireNonNull(srcDir, "src-dir cannot be null");
    }

    //TODO: detect multiple sources within a package now only is able to detect single lessons
    public List<JavaSource> findSources(final String lessonName) throws IOException {
        final List<JavaSource> sourceFiles = new ArrayList<>();
        final String lessonJavaName = lessonName + JAVA;
        List<Path> files = LessonConverterFileUtils.findFiles(srcDir, new Predicate<Path>() {
            @Override
            public boolean apply(Path file) {
                return file.getFileName().toString().equals(lessonJavaName);
            }
        });
        return FluentIterable.from(files).transform(new Function<Path, JavaSource>() {
            @Override
            public JavaSource apply(Path file) {
                return new JavaSource(file, lessonName);
            }
        }).toList();
    }
}

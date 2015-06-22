package org.owasp.webgoat.converter;

import com.google.common.base.Predicate;
import org.owasp.webgoat.plugins.PluginFileUtils;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.owasp.webgoat.converter.LessonConverterFileUtils.hasParentDirectoryWithName;

public class HtmlLessonSolutionFinder {

    private static final String NAME_LESSON_SOLUTION_DIR = "lesson_solutions_1";
    private static final String NAME_LESSON_SOLUTION_FILES_DIR = "lesson_solutions";
    private static final String HTML = ".html";

    private final Path srcDir;
    private final String lessonName;

    public HtmlLessonSolutionFinder(Path srcDir, String lessonName) {
        this.srcDir = Objects.requireNonNull(srcDir, "src-dir cannot be null");
        this.lessonName = Objects.requireNonNull(lessonName, "lesson name cannot be null");
    }

    //TODO some lessons have more solution files
    public List<Path> findHtmlSolutions() throws IOException {
        final Path lessonDirectory = LessonConverterFileUtils.findSubDirectory(srcDir, NAME_LESSON_SOLUTION_DIR);
        Objects.requireNonNull(lessonDirectory, "Lesson solution directory not found check the source directory");

        List<Path> files = LessonConverterFileUtils.findFiles(lessonDirectory, new Predicate<Path>() {
            @Override
            public boolean apply(Path input) {
                return hasParentDirectoryWithName(input, lessonDirectory, true) && input.getFileName().toString()
                        .equals(lessonName + HTML);
            }
        });

        return files;
    }

    public List<Path> findSolutionImages() throws IOException {
        final Path lessonDirectory = LessonConverterFileUtils.findSubDirectory(srcDir, NAME_LESSON_SOLUTION_FILES_DIR);

        if (lessonDirectory != null) {
            Path images = lessonDirectory.resolve(lessonName + "_files");
            if (images != null && images.toFile().exists()) {
                return PluginFileUtils.getFilesInDirectory(images);
            } else {
                Logger.log("No images for the lesson " + lessonName);
            }
        }
        return new ArrayList<>();
    }

}

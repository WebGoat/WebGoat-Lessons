package org.owasp.webgoat.converter;

import com.google.common.base.Predicate;
import com.google.common.base.Verify;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class HtmlLessonSourceFinder {

    private static final List<String> LANGUAGES = Arrays.asList("en", "de", "ru");
    private static final String NAME_LESSON_PLAN_DIR = "lesson_plans";
    private static final String HTML = ".html";

    private final Path srcDir;
    private final String lessonName;
    private final String lessonNameWithExtension;

    public HtmlLessonSourceFinder(Path srcDir, String lessonName) {
        this.srcDir = Objects.requireNonNull(srcDir, "src-dir cannot be null");
        this.lessonName = Objects.requireNonNull(lessonName, "lesson name cannot be null");
        this.lessonNameWithExtension = this.lessonName + HTML;
    }

    private Map<String, Path> findSources(final String language) throws IOException {
        Path lessonDirectory = LessonConverterFileUtils.findSubDirectory(srcDir, NAME_LESSON_PLAN_DIR);
        Objects.requireNonNull(lessonDirectory, "Lesson plan directory not found check the source directory");

        List<Path> files = LessonConverterFileUtils.findFiles(lessonDirectory, new Predicate<Path>() {
            @Override
            public boolean apply(Path input) {
                return LessonConverterFileUtils.hasParentDirectoryWithName(input, language, true) && input.getFileName()
                        .getFileName().toString().equals(lessonNameWithExtension);
            }
        });

        Verify.verify(files.size() <= 1, String.format("Multiple lessons found for language %s", language));

        Map htmlPerLanguage = Maps.newHashMap();
        if (files.size() == 1) {
            htmlPerLanguage.put(language, files.get(0));
        }
        return htmlPerLanguage;
    }

    public Map<String, Path> findSources() throws IOException {
        final Map<String, Path> htmlLessons = Maps.newHashMap();
        for (String language : LANGUAGES) {
            htmlLessons.putAll(findSources(language));
        }
        return htmlLessons;
    }

}

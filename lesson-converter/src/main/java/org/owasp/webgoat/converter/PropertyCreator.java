package org.owasp.webgoat.converter;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;

public class PropertyCreator {

    private static Map<String, String> LANGUAGES = new HashMap<String, String>() {{
        this.put("english", "en");
        this.put("russian", "ru");
        this.put("french", "fr");
        this.put("german", "de");
    }};
    private static final String WEBGOAT_PROPERTIES = "WebGoatLabels.properties";
    private static final String WEBGOAT_PROPERTIES_LANGUAGE = "WebGoatLabels_%s.properties";
    private final Path srcDir;
    private final JavaSource javaSource;
    private final Path destDir;
    private List<String> referencedProperties;

    public PropertyCreator(Path srcDir, Path destDir, JavaSource javaSource) {
        this.srcDir = Objects.requireNonNull(srcDir);
        this.javaSource = Objects.requireNonNull(javaSource);
        this.destDir = Objects.requireNonNull(destDir);
    }

    public void write() throws IOException {
        referencedProperties = javaSource.referencedProperties();
        if (referencedProperties.isEmpty()) {
            Logger.log("No property files needed for this lesson.");
        } else {
            for (String language : LANGUAGES.keySet()) {
                Logger.start("Property bundle for language '%s'", language);
                final List<Path> propertyFiles = LessonConverterFileUtils.findFile(srcDir, i18nTarget(language));
                final Path target = destDir.resolve(i18nTarget(language));
                final List<String> newProperties = new ArrayList<>();
                FluentIterable.from(propertyFiles) //
                        .transformAndConcat(fromPathToProperties()) //
                        .filter(usedProperties()).copyInto(newProperties);

                Logger.log("Writing properties to '%s'", target);
                Files.write(target, newProperties, StandardCharsets.UTF_8, CREATE);
                Logger.end();
            }
        }
    }

    private String i18nTarget(String language) {
        if ("english".equals(language)) {
            return WEBGOAT_PROPERTIES;
        }
        return String.format(WEBGOAT_PROPERTIES_LANGUAGE, LANGUAGES.get(language));
    }

    private Function<Path, List<String>> fromPathToProperties() throws IOException {
        return new Function<Path, List<String>>() {

            @Override
            public List<String> apply(Path input) {
                final List<String> properties = new ArrayList<>();
                try {
                    properties.addAll(Files.readAllLines(input, StandardCharsets.ISO_8859_1));
                } catch (IOException e) {
                    throw new ConverterException("", e);
                }
                return properties;
            }
        };
    }

    private Predicate<String> usedProperties() {
        return new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                boolean contains = referencedProperties.contains(input.split("=")[0]);
                if (contains) Logger.log("Property '%s' found in properties ('%s')", input.split("=")[0], input);
                return contains;

            }
        };
    }

    private Function<List<String>, List<String>> translateProperty() {
        return new Function<List<String>, List<String>>() {
            @Override
            public List<String> apply(List<String> input) {
                return null;
            }
        };
    }
}


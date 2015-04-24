package org.owasp.webgoat.converter;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Maps;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.StandardOpenOption.CREATE;

public class PropertyCreator {

    private enum LANGUAGE {

        ENGLISH("english", "en"), RUSSIAN("russian", "ru"), GERMAN("german", "de"), FRENCH("french", "fr");

        private final String oldName;
        private final String newName;

        LANGUAGE(String oldName, String newName) {
            this.oldName = oldName;
            this.newName = newName;
        }

        String to() {
            return newName;
        }
    }

    private static final String WEBGOAT_PROPERTIES = "WebGoatLabels_%s.properties";
    private final Path srcDir;
    private final JavaSource javaSource;
    private final Path destDir;

    public PropertyCreator(Path srcDir, Path destDir, JavaSource javaSource) {
        this.srcDir = Objects.requireNonNull(srcDir);
        this.javaSource = Objects.requireNonNull(javaSource);
        this.destDir = Objects.requireNonNull(destDir);
    }

    public void writeToPropertyFile() throws IOException {
        Logger.start("Start writing property files...");
        List<String> referencedProperties = javaSource.referencedProperties();
        if (referencedProperties.isEmpty()) {
            Logger.log("No property files needed for this lesson.");
        } else {
            Map<LANGUAGE, Path> webgoatPropertyFiles = findWebgoatPropertyFiles();

            for (Map.Entry<LANGUAGE, Path> languagePropertyFile : webgoatPropertyFiles.entrySet()) {
                writeToPropertyFile(destDir.resolve(String.format(WEBGOAT_PROPERTIES, languagePropertyFile.getKey().to())),
                        languagePropertyFile.getValue(), referencedProperties);
            }
        }
        Logger.end();
    }

    //TODO split the writing and the filtering
    private void writeToPropertyFile(Path destFile, Path originalPropertyFile, List<String> referencedProperties)
            throws IOException {
        Logger.start("Start selecting and writing properties from '%s' to '%s'", originalPropertyFile.getFileName(), destFile);
        final Properties properties = new Properties();
        properties.load(new FileReader(originalPropertyFile.toFile()));

        List<String> newProperties = new ArrayList<>();
        FluentIterable.from(referencedProperties).transform(new Function<String, String>() {
            @Override
            public String apply(String input) {
                String property = input + "=" + properties.getProperty(input);
                Logger.log("Writing property '%s'", property);
                return property;
            }
        }).copyInto(newProperties);
        Files.write(destFile, newProperties, StandardCharsets.UTF_8, CREATE);
        Logger.end();
    }

    private Map<LANGUAGE, Path> findWebgoatPropertyFiles() throws IOException {
        final Map<LANGUAGE, Path> languageProperties = Maps.newHashMap();

        Files.walkFileTree(srcDir, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                for (LANGUAGE language : EnumSet.allOf(LANGUAGE.class)) {
                    String languageFile = String.format(WEBGOAT_PROPERTIES, language.oldName);
                    if (file.getFileName().toString().equals(languageFile)) {
                        languageProperties.put(language, file);
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return languageProperties;
    }
}



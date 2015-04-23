package org.owasp.webgoat.converter;

import lombok.Getter;
import lombok.extern.java.Log;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log
@Getter
public class JavaSource {

    /**
     * Regular expression for looking in the Java source lesson for properties.
     */
    private static final Pattern propertyPattern = Pattern.compile("WebGoatI18N.get\\(\"([a-zA-Z]*)\"\\)");

    private final Path javaSourceFile;
    private final String className;

    public JavaSource(Path javaSourceFile, String className) {
        this.javaSourceFile = javaSourceFile;
        this.className = className;
    }

    public List<String> referencedProperties() throws IOException {
        List<String> sourceLines = java.nio.file.Files.readAllLines(javaSourceFile, StandardCharsets.UTF_8);
        List<String> referencedProperties = new ArrayList<>();

        for (String input : sourceLines) {
            Matcher matcher = propertyPattern.matcher(input);
            if (matcher.matches()) {
                String property = matcher.group(1);
                log.info(String.format("Found property %s in %s", property, className));
                referencedProperties.add(property);
            }
        }
        return referencedProperties;
    }

}

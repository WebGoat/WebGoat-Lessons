package org.owasp.webgoat.converter;


import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaSource {

    /**
     * Regular expression for looking in the Java source lesson for properties.
     */
    private static final Pattern propertyPattern = Pattern.compile("getLabelManager\\(\\)\\.get\\(\"([^\")]*)\"\\)");

    private final Path javaSourceFile;
    private final String className;

    public JavaSource(Path javaSourceFile, String className) {
        this.javaSourceFile = javaSourceFile;
        this.className = className;
    }

    public List<String> referencedProperties() throws IOException {
        Logger.start("Start finding referenced properties...");
        List<String> sourceLines = java.nio.file.Files.readAllLines(javaSourceFile, StandardCharsets.UTF_8);
        List<String> referencedProperties = new ArrayList<>();

        for (String input : sourceLines) {
            Matcher matcher = propertyPattern.matcher(input);
            while (matcher.find()) {
                String property = matcher.group(1);
                Logger.log("Property %s found", property);
                referencedProperties.add(property);
            }
        }
        Logger.end();
        return referencedProperties;
    }

    public List<String> getLines() throws IOException {
        List<String> lines = Files.readAllLines(javaSourceFile, StandardCharsets.UTF_8);
        rewritePackageName(lines);
        addImportForSuperclass(lines);
        addImportForCategory(lines);
        replaceWebgoatI18N(lines);

        return lines;
    }

    public Path getJavaSourceFile() {
        return javaSourceFile;
    }

    private String getSuperClass(List<String> lines) {
        Optional<String> line = FluentIterable.from(lines).firstMatch(new Predicate<String>() {
            @Override
            public boolean apply(String line) {
                return line.contains("extends");
            }
        });
        if (line.isPresent()) {
            return StringUtils.substringAfterLast(line.get(), "extends");
        }
        throw new ConverterException("Unable to detect super class for lesson " + javaSourceFile.getFileName());
    }

    private void rewritePackageName(List<String> lines) {
        Logger.log("Rewriting package name...");
        ListIterator<String> it = lines.listIterator();
        while (it.hasNext()) {
            String line = it.next();
            if (line.contains("package org.owasp.webgoat.lessons;")) {
                it.set(line.replace("package org.owasp.webgoat.lessons;", "package org.owasp.webgoat.plugin;"));
            }
        }
    }

    private void addImportForSuperclass(List<String> lines) {
        String superClass = getSuperClass(lines);
        String importToAdd = String.format("import org.owasp.webgoat.lessons.%s;", superClass.trim());
        Logger.log("Adding import for package superclass %s", importToAdd);
        addImport(importToAdd, lines);
    }

    private void addImportForCategory(List<String> lines) {
        Optional<String> category = FluentIterable.from(lines).firstMatch(new Predicate<String>() {
            @Override
            public boolean apply(String line) {
                return line.contains("Category");
            }
        });
        if (category.isPresent()) {
            addImport("import org.owasp.webgoat.lessons.Category;", lines);
        }
    }

    private void addImport(String importToAdd, List<String> lines) {
        ListIterator<String> it = lines.listIterator();

        while (it.hasNext()) {
            String line = it.next();
            if (line.startsWith("import")) {
                if (line.compareTo(importToAdd) >= 0) {
                    it.previous();
                    it.add(importToAdd);
                    break;
                }
            }
        }
    }

    /**
     * This methods tries to get whether a reference to 'javax.servlet-api' is necessary.
     * Add more methods to scan the source.
     */
    public boolean containsReferenceToJavax() throws IOException {
        List<String> lines = Files.readAllLines(javaSourceFile, StandardCharsets.UTF_8);
       return FluentIterable.from(lines).anyMatch(new Predicate<String>() {
            @Override
            public boolean apply(String line) {
                return line.contains("s.getRequest()") || line.contains("s.setRequest(") || line.contains("s.getResponse()");
            }
        });
    }

    private void replaceWebgoatI18N(List<String> lines) {
        Logger.log("Changing WebGoatI18N to LabelManager...");
        ListIterator<String> it = lines.listIterator();
        while (it.hasNext()) {
            String line = it.next();
            it.set(line.replace("WebGoatI18N.get", "getLabelManager().get"));
        }
    }

}

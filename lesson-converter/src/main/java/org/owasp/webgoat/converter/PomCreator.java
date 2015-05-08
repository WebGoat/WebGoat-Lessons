package org.owasp.webgoat.converter;

import com.google.common.base.Charsets;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.FluentIterable;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class PomCreator {

    public static final String ARTIFACT_ID = "${artifactId}";

    public PomCreator() {
    }

    public List<String> createPom(final String artifactId, final String pomExample) {
        Preconditions.checkNotNull(artifactId, "artifactId cannot be null");

        try {
            URL pomUrl = getClass().getResource("/" + pomExample);
            List<String> lines = Files.readAllLines(Paths.get(pomUrl.toURI()), Charsets.UTF_8);
            return FluentIterable.from(lines).transform(new Function<String, String>() {
                @Override
                public String apply(String line) {
                    return line.replace(ARTIFACT_ID, artifactId);
                }
            }).toList();
        } catch (IOException | URISyntaxException e) {
            throw new ConverterException("Unable to read pom1.example, should be in the src/main/resources directory",
                    e);
        }
    }
}

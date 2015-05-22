package org.owasp.webgoat.converter;

import org.hamcrest.core.IsCollectionContaining;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class JavaSourceTest {

    private Path tempDirectory;

    @Before
    public void init() throws IOException {
        tempDirectory = Files.createTempDirectory("test");
        tempDirectory.toFile().deleteOnExit();
    }

    @Test
    public void regularExpressionShouldPickUpProperty() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("getLabelManager().get(\"testa\")");
        lines.add("getLabelManager().get(\"test1\")1234");
        Path tempFile = tempDirectory.resolve("test");
        Files.write(tempFile, lines, StandardCharsets.UTF_8);
        List<String> properties = new JavaSource(tempFile, "Test").referencedProperties();
        assertThat(properties, IsCollectionContaining.hasItems("test1", "testa"));
    }

    @Test
    public void regularExpressionOnSameLineShouldPickUpBoth() throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("getLabelManager().get(\"HiddenFieldTamperingHint3\")+ PRICE_TV +getLabelManager().get(\"HiddenFieldTamperingHint32\") + PRICE_TV_HACKED");

        Path tempFile = tempDirectory.resolve("test");
        Files.write(tempFile, lines, StandardCharsets.UTF_8);
        List<String> properties = new JavaSource(tempFile, "Test").referencedProperties();
        assertThat(properties, IsCollectionContaining.hasItems("HiddenFieldTamperingHint3", "HiddenFieldTamperingHint32"));
    }
}
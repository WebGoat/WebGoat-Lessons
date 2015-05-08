package org.owasp.webgoat.converter;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.List;

public class ConverterMain {

    private static class Options {

        @Arg(dest = "src_dir")
        public File sourceDir;

        @Arg(dest = "dest_dir")
        public File destDir;

        @Arg(dest = "lesson_name")
        public String lessonName;

        @Arg(dest = "overwrite")
        public Boolean overwrite = false;

        @Arg(dest = "dest_name")
        public String destName;
    }

    public static final ArgumentParser parser = ArgumentParsers.newArgumentParser("Lesson converter")
            .description("Converts a legacy lesson to a new plugin lesson");

    public ConverterMain(Options options) {
        LessonCreator lesson = new LessonCreator(options.lessonName, options.destDir.toPath(), options.destName, options.sourceDir.toPath());
        try {
            if (options.overwrite) {
                lesson.deleteDirectory();
            }
            lesson.createDirectory();
            lesson.createPackageForSources();
            lesson.createResourceBundleDirectory();
            lesson.createHtmlLessonDirectory();
            lesson.createHtmlSolutionDirectory();
            JavaSource javaSource = lesson.copyJavaSourceFiles();
            lesson.writePomFile(javaSource);
            lesson.copyLessonPlans();
            lesson.copyLessonSolutions();
            lesson.copyI18N(javaSource);
            adjustMainPom(options.destDir, lesson.lessonNameToProjectDirectoryName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void adjustMainPom(File destDir, String module) throws IOException {
        Path pom = destDir.toPath().resolve("pom.xml");
        List<String> lines = Files.readAllLines(pom, StandardCharsets.UTF_8);
        List<String> modules = Lists.newArrayList();
        FluentIterable.from(lines).filter(new Predicate<String>() {
            @Override
            public boolean apply(String input) {
                return input.contains("<module>");
            }
        }).copyInto(modules);
        lines.removeAll(modules);
        String newModule = String.format("        <module>%s</module>", module);
        if (!modules.contains(newModule)) {
            modules.add(newModule);
        }
        Collections.sort(modules);
        int beginIndex = 0;
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains("<modules>")) {
                beginIndex = i + 1;
            }
        }
        lines.addAll(beginIndex, modules);
        Logger.log(String.format("Writing pom.xml with new module '%s' to '%s'", module, pom));
        Files.write(pom, lines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     * Start the conversion, accepted command line options:
     *
     * @param args
     */
    public static void main(String[] args) throws ArgumentParserException {
        parser.addArgument("-s", "--src-dir") //
                .help("Top level directory of the old WebGoat legacy project") //
                .type(Arguments.fileType().verifyIsDirectory().verifyCanRead())
                .required(true);
        parser.addArgument("-d", "--dest-dir") //
                .help("Top level directory of the new WebGoat lecacy project") //
                .required(true)//
                .type(Arguments.fileType().verifyIsDirectory().verifyCanWrite());
        parser.addArgument("-n", "--dest-name") //
                .help("Overwrite the default directory name") //
                .required(false);
        parser.addArgument("-l", "--lesson-name").help("Name of the lesson to be converted").required(true);
        parser.addArgument("-o", "--overwrite").help("Overwrite an existing directory").action(Arguments.storeTrue());

        try {
            Options options = new Options();
            parser.parseArgs(args, options);
            new ConverterMain(options);
        } catch (ArgumentParserException e) {
            parser.handleError(e);
            System.exit(1);
        }
    }
}

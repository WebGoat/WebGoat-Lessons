package org.owasp.webgoat.converter;

import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.annotation.Arg;
import net.sourceforge.argparse4j.impl.Arguments;
import net.sourceforge.argparse4j.inf.ArgumentParser;
import net.sourceforge.argparse4j.inf.ArgumentParserException;

import java.io.File;
import java.io.IOException;

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
    }

    public static final ArgumentParser parser = ArgumentParsers.newArgumentParser("Lesson converter")
            .description("Converts a legacy lesson to a new plugin lesson");

    public ConverterMain(Options options) {
        LessonCreator lesson = new LessonCreator(options.lessonName, options.destDir.toPath(), options.sourceDir.toPath());
        try {
            if (options.overwrite) {
                lesson.deleteDirectory();
            }
            lesson.createDirectory();
            lesson.writePomFile();
            lesson.createPackageForSources();
            lesson.createHtmlLessonDirectory();
            lesson.createHtmlSolutionDirectory();
            lesson.copyJavaSourceFiles();
            lesson.copyLessonPlans();
            lesson.copyLessonSolutions();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

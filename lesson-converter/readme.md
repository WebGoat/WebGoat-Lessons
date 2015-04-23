# Lesson converter

## Introduction

This project will try to automatically convert a lesson it will:

- Create a new project
- Create a pom file for the Maven project
- Locate the Java source file for the lesson
- Copy the source file to the project
- Extract all the properties from the source file
- Copy the lesson html files
- Copy the lesson solutions files
- Create a property file based on the extracted properties from the Java source

## Running

You can run it through the command line:

```
usage: Lesson converter [-h] -s SRC_DIR -d DEST_DIR -l LESSON_NAME [-o]

Converts a legacy lesson to a new plugin lesson

optional arguments:
  -h, --help             show this help message and exit
  -s SRC_DIR, --src-dir SRC_DIR
                         Top level  directory  of  the  old  WebGoat legacy
                         project
  -d DEST_DIR, --dest-dir DEST_DIR
                         Top level  directory  of  the  new  WebGoat lecacy
                         project
  -l LESSON_NAME, --lesson-name LESSON_NAME
                         Name of the lesson to be converted
  -o, --overwrite        Overwrite an existing directory
```

## TODO

- Refactoring: move code to classes like htmllessonsolutionfinder the copying part
- Some lessons have multiple Java sources (only a couple so maybe perform a manual migration)


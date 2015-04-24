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

## Run

The following output will be generated during a run of the converter:

```
Deleting the destination directory 'c:\Temp\html-clues'
Creating directory 'c:\Temp\html-clues'
Creating pom file 'c:\Temp\html-clues\pom.xml' with project name 'html-clues'
Creating package structure 'c:\Temp\html-clues\src\main\java\org\owasp\webgoat\plugin'
Creating i18n directory...
Starting to copy the lesson plans...
Creating the lesson solution directory...
Starting to copy the java source files...
	Finding java source in HtmlClues.java
	Sources found C:\workspace\WebGoat-Legacy\src\main\java\org\owasp\webgoat\lessons\HtmlClues.java
	Copying 'C:\workspace\WebGoat-Legacy\src\main\java\org\owasp\webgoat\lessons\HtmlClues.java' to 'c:\Temp\html-clues\src\main\java\org\owasp\webgoat\plugin\HtmlClues.java'
Starting to copy the lesson plans...
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_plans\de\HtmlClues.html' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonPlans\HtmlClues\de\HtmlClues.html'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_plans\en\HtmlClues.html' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonPlans\HtmlClues\en\HtmlClues.html'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_plans\ru\HtmlClues.html' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonPlans\HtmlClues\ru\HtmlClues.html'
Starting to copy the lesson solutions...
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions_1\HtmlClues.html' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues.html'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\colorschememapping.xml' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\colorschememapping.xml'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\filelist.xml' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\filelist.xml'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image001.png' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image001.png'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image003.png' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image003.png'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image005.png' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image005.png'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image007.png' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image007.png'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image009.png' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image009.png'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image011.jpg' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image011.jpg'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image012.jpg' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image012.jpg'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image013.jpg' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image013.jpg'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image014.jpg' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image014.jpg'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\image015.jpg' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\image015.jpg'
	Copying 'C:\workspace\WebGoat-Legacy\src\main\webapp\lesson_solutions\HtmlClues_files\themedata.thmx' to 'c:\Temp\html-clues\src\main\resources\plugin\HtmlClues\lessonSolution\en\HtmlClues_files\themedata.thmx'
Start writing property files...
	Start finding referenced properties...
		Property HtmlCluesBINGO found
		Property WelcomeUser found
		Property YouHaveBeenAuthenticatedWith found
		Property WeakAuthenticationCookiePleaseSignIn found
		Property RequiredFields found
		Property UserName found
		Property Password found
		Property Login found
	Start selecting and writing properties from 'WebGoatLabels_english.properties' to 'c:\Temp\html-clues\src\main\resources\i18n\WebGoatLabels_en.properties'
		Writing property 'HtmlCluesBINGO=BINGO -- admin authenticated'
		Writing property 'WelcomeUser=null'
		Writing property 'YouHaveBeenAuthenticatedWith=null'
		Writing property 'WeakAuthenticationCookiePleaseSignIn=Please sign in to your account.  See the OWASP admin if you do not have an account.'
		Writing property 'RequiredFields=Required Fields'
		Writing property 'UserName=User Name '
		Writing property 'Password=Password '
		Writing property 'Login=Login'
	Start selecting and writing properties from 'WebGoatLabels_russian.properties' to 'c:\Temp\html-clues\src\main\resources\i18n\WebGoatLabels_ru.properties'
		Writing property 'HtmlCluesBINGO=????? -- ????????????? ?????'
		Writing property 'WelcomeUser=????? ?????????,'
		Writing property 'YouHaveBeenAuthenticatedWith=?? ???? ????????????????? ? '
		Writing property 'WeakAuthenticationCookiePleaseSignIn=??????????, ??????? ??? ????? ?????????. ???????? OWASP admin ???? ? ??? ??? ???.'
		Writing property 'RequiredFields=???????????? ????'
		Writing property 'UserName=??? ????????????'
		Writing property 'Password=??????'
		Writing property 'Login=?????'
	Start selecting and writing properties from 'WebGoatLabels_german.properties' to 'c:\Temp\html-clues\src\main\resources\i18n\WebGoatLabels_de.properties'
		Writing property 'HtmlCluesBINGO=BINGO -- admin authentisiert'
		Writing property 'WelcomeUser=Willkommen, '
		Writing property 'YouHaveBeenAuthenticatedWith=Sie wurden authentisiert mit '
		Writing property 'WeakAuthenticationCookiePleaseSignIn=Bitte melden Sie sich an. Kontaktieren Sie den OWASP Administrator wenn Sie keine Anmeldedaten haben.'
		Writing property 'RequiredFields=*Benötigte Felder'
		Writing property 'UserName=Benutzername '
		Writing property 'Password=Passwort'
		Writing property 'Login=Anmelden'
```

After the run you can compile the new project and place in the container as jar.


## Work in progress

- Refactoring: move code to classes like htmllessonsolutionfinder the copying part
- Some lessons have multiple Java sources (only a couple so maybe perform a manual migration)


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
  -n, --dest-name		 Overwrite the default directory name, specify a specific target directory in which the lesson
  						 should be placed.
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
Start finding referenced properties...
	Property HtmlCluesBINGO found
	Property WelcomeUser found
	Property YouHaveBeenAuthenticatedWith found
	Property WeakAuthenticationCookiePleaseSignIn found
	Property RequiredFields found
	Property UserName found
	Property Password found
	Property Login found
	Property HtmlCluesHint1 found
	Property HtmlCluesHint2 found
	Property HtmlCluesHint3 found
Property bundle for language 'russian'
	Property 'UserName' found in properties ('UserName=\u0418\u043C\u044F \u043F\u043E\u043B\u044C\u0437\u043E\u0432\u0430\u0442\u0435\u043B\u044F')
	Property 'Password' found in properties ('Password=\u041F\u0430\u0440\u043E\u043B\u044C')
	Property 'Login' found in properties ('Login=\u041B\u043E\u0433\u0438\u043D')
	Property 'RequiredFields' found in properties ('RequiredFields=\u041E\u0431\u044F\u0437\u0430\u0442\u0435\u043B\u044C\u043D\u044B\u0435 \u043F\u043E\u043B\u044F')
	Property 'WeakAuthenticationCookiePleaseSignIn' found in properties ('WeakAuthenticationCookiePleaseSignIn=\u041F\u043E\u0436\u0430\u043B\u0443\u0439\u0441\u0442\u0430, \u0432\u043E\u0439\u0434\u0438\u0442\u0435 \u043F\u043E\u0434 \u0441\u0432\u043E\u0438\u043C \u0430\u043A\u043A\u0430\u0443\u043D\u0442\u043E\u043C. \u0421\u043C\u043E\u0442\u0440\u0438\u0442\u0435 OWASP admin \u0435\u0441\u043B\u0438 \u0443 \u0432\u0430\u0441 \u0435\u0433\u043E \u043D\u0435\u0442.')
	Property 'WelcomeUser' found in properties ('WelcomeUser=\u0414\u043E\u0431\u0440\u043E \u043F\u043E\u0436\u0430\u043B\u043E\u0432\u0430\u044C,')
	Property 'YouHaveBeenAuthenticatedWith' found in properties ('YouHaveBeenAuthenticatedWith=\u0412\u044B \u0431\u044B\u043B\u0438 \u0430\u0443\u0442\u0435\u043D\u0442\u0438\u0444\u0438\u0446\u0438\u0440\u043E\u0432\u0430\u043D\u044B \u0441 ')
	Property 'HtmlCluesBINGO' found in properties ('HtmlCluesBINGO=\u0411\u0418\u041D\u0413\u041E -- \u0430\u0434\u043C\u0438\u043D\u0438\u0441\u0442\u0440\u0430\u0442\u043E\u0440 \u0432\u043E\u0448\u0451\u043B')
	Property 'HtmlCluesHint1' found in properties ('HtmlCluesHint1=\u0412\u044B \u043C\u043E\u0436\u0435\u0442\u0435 \u043F\u043E\u0441\u043C\u043E\u0442\u0440\u0435\u0442\u044C \u0438\u0441\u0445\u043E\u0434\u043D\u044B\u0439 HTML-\u043A\u043E\u0434 \u0441\u0442\u0440\u0430\u043D\u0438\u0446\u044B \u0432\u044B\u0431\u0440\u0430\u0432 \u043F\u0443\u043D\u043A\u0442 '\u0418\u0441\u0445\u043E\u0434\u043D\u044B\u0439 \u043A\u043E\u0434' \u0432 \u043C\u0435\u043D\u044E \u0431\u0440\u0430\u0443\u0437\u0435\u0440\u0430.')
	Property 'HtmlCluesHint2' found in properties ('HtmlCluesHint2=\u041E\u0447\u0435\u043D\u044C \u043C\u043D\u043E\u0433\u043E \u0437\u0430\u0446\u0435\u043F\u043E\u043A \u043D\u0430\u0445\u043E\u0434\u0438\u0442\u0441\u044F \u0432\u043D\u0443\u0442\u0440\u0438 HTML-\u043A\u043E\u0434\u0430')
	Property 'HtmlCluesHint3' found in properties ('HtmlCluesHint3=\u0418\u0449\u0438\u0442\u0435 \u0441\u043B\u043E\u0432\u043E HIDDEN, \u043F\u043E\u0441\u043C\u043E\u0442\u0440\u0438\u0442\u0435 \u043D\u0430 \u0441\u0441\u044B\u043B\u043A\u0438, \u043F\u043E\u0441\u043C\u043E\u0442\u0440\u0438\u0442\u0435 \u043D\u0430 \u043A\u043E\u043C\u043C\u0435\u043D\u0442\u0430\u0440\u0438\u0438.')
	Writing properties to 'c:\Temp\html-clues\src\main\resources\i18n\WebGoatLabels_russian.properties'
Property bundle for language 'german'
	Property 'UserName' found in properties ('UserName=Benutzername ')
	Property 'Password' found in properties ('Password=Passwort')
	Property 'Login' found in properties ('Login=Anmelden')
	Property 'RequiredFields' found in properties ('RequiredFields=*Benötigte Felder')
	Property 'WeakAuthenticationCookiePleaseSignIn' found in properties ('WeakAuthenticationCookiePleaseSignIn=Bitte melden Sie sich an. Kontaktieren Sie den OWASP Administrator wenn Sie keine Anmeldedaten haben.')
	Property 'WelcomeUser' found in properties ('WelcomeUser=Willkommen, ')
	Property 'YouHaveBeenAuthenticatedWith' found in properties ('YouHaveBeenAuthenticatedWith=Sie wurden authentisiert mit ')
	Property 'HtmlCluesBINGO' found in properties ('HtmlCluesBINGO=BINGO -- admin authentisiert')
	Property 'HtmlCluesHint1' found in properties ('HtmlCluesHint1=Sie können Sich den HTML Quellcode anschauen indem Sie "View Source" im Browser anklicken.')
	Property 'HtmlCluesHint2' found in properties ('HtmlCluesHint2=Es gibt viele Hinweise in HTML')
	Property 'HtmlCluesHint3' found in properties ('HtmlCluesHint3=Suchen Sie nach den Worten HIDDEN, schauen Sie sich URLs an und suchen Sie nach Kommentaren.')
	Writing properties to 'c:\Temp\html-clues\src\main\resources\i18n\WebGoatLabels_german.properties'
Property bundle for language 'french'
	Writing properties to 'c:\Temp\html-clues\src\main\resources\i18n\WebGoatLabels_french.properties'
Property bundle for language 'english'
	Property 'UserName' found in properties ('UserName=User Name ')
	Property 'Password' found in properties ('Password=Password ')
	Property 'Login' found in properties ('Login=Login')
	Property 'RequiredFields' found in properties ('RequiredFields=Required Fields')
	Property 'WeakAuthenticationCookiePleaseSignIn' found in properties ('WeakAuthenticationCookiePleaseSignIn=Please sign in to your account.  See the OWASP admin if you do not have an account.')
	Property 'HtmlCluesBINGO' found in properties ('HtmlCluesBINGO=BINGO -- admin authenticated')
	Property 'HtmlCluesHint1' found in properties ('HtmlCluesHint1=You can view the HTML source by selecting 'view source' in the browser menu.')
	Property 'HtmlCluesHint2' found in properties ('HtmlCluesHint2=There are lots of clues in the HTML')
	Property 'HtmlCluesHint3' found in properties ('HtmlCluesHint3=Search for the word HIDDEN, look at URLs, look for comments.')
	Writing properties to 'c:\Temp\html-clues\src\main\resources\i18n\WebGoatLabels_english.properties'
```

After the run you can compile the new project and place in the container as jar.


## Work in progress

- Refactoring: move code to classes like htmllessonsolutionfinder the copying part
- Some lessons have multiple Java sources (only a couple so maybe perform a manual migration)

## Need work:




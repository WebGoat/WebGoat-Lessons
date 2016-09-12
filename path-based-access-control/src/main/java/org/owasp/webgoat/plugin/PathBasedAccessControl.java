
package org.owasp.webgoat.plugin;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


/**
 * ************************************************************************************************
 * <p/>
 * <p/>
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p/>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p/>
 * Getting Source ==============
 * <p/>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p/>
 * For details, please see http://webgoat.github.io
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class PathBasedAccessControl extends LessonAdapter {

    private final static String FILE = "File";


    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s) {
        ElementContainer ec = new ElementContainer();

        try {
            Table t = new Table().setCellSpacing(0).setCellPadding(2).setWidth("90%").setAlign("center");

            if (s.isColor()) {
                t.setBorder(1);
            }
            List<File> htmlFiles = findHtmlFiles(LessonUtil.getLessonDirectory(s, this).getParentFile());
            List<String> htmlFilenames = Lists.newArrayList(
                    Iterables.transform(htmlFiles, new Function<File, String>() {
                        @Override
                        public String apply(File input) {
                            return input.getName();
                        }
                    }));
            String[] list = htmlFilenames.toArray(new String[htmlFilenames.size()]);
            String listing = " <p><B>" + getLabelManager().get("CurrentDirectory") + "</B> " + Encoding
                    .urlDecode(htmlFiles.get(0).getParent())
                    + "<br><br>" + getLabelManager().get("ChooseFileToView") + "</p>";

            TR tr = new TR();
            tr.addElement(new TD().setColSpan(2).addElement(new StringElement(listing)));
            t.addElement(tr);

            tr = new TR();
            tr.addElement(new TD().setWidth("35%").addElement(ECSFactory.makePulldown(FILE, list, "", 15)));
            tr.addElement(new TD().addElement(ECSFactory.makeButton(getLabelManager().get("ViewFile"))));
            t.addElement(tr);

            ec.addElement(t);

            // FIXME: would be cool to allow encodings here -- hex, percent,
            // url, etc...
            final String file = s.getParser().getRawParameter(FILE, "");

            if (!file.equals(""))  // first time in or missing parameter - just kick out
            {
                // defuse file searching
                boolean illegalCommand = true;
                // allow them to look at any file in the webgoat hierachy.
                // Don't allow them to look about the webgoat root,
                // except to see the LICENSE file
                if (upDirCount(file) == 3 && !file.endsWith("LICENSE")) {
                    s.setMessage(getLabelManager().get("AccessDenied"));
                    s.setMessage(getLabelManager().get("ItAppears1"));
                } else {
                    if (upDirCount(file) > 5) {
                        s.setMessage(getLabelManager().get("AccessDenied"));
                        s.setMessage(getLabelManager().get("ItAppears2"));
                    } else {
                        illegalCommand = false;
                    }
                }

                // provide a little guidance to help them along.  If the allowed file comes back as
                // null we have the potential for a real attack vector
                File allowedFile = guideTheAtack(s, file, htmlFiles);

                if (!illegalCommand) {
                    File attemptedFile = new File(LessonUtil.getLessonDirectory(s, this) + "/lessonPlans/en/" + file);
                    if (allowedFile == null) {
                        // We have a potential attack
                        if (file != null && attemptedFile.isFile() && attemptedFile.exists()) {
                            // They have accessed something meaningful
                            s.setMessage(getLabelManager().get("CongratsAccessToFileAllowed") + " ==> "
                                    + Encoding.urlDecode(attemptedFile.getCanonicalPath()));
                            makeSuccess(s);
                        } else
                            if (file != null && file.length() != 0) {
                                s.setMessage(getLabelManager().get("AccessToFileDenied1")
                                        + Encoding.urlDecode(file) + getLabelManager().get("AccessToFileDenied2"));
                            } else {
                                // do nothing, probably entry screen
                            }
                    } else {
                        attemptedFile = allowedFile;
                    }

                    displayAttemptedFile(ec, attemptedFile);
                }
            }

        } catch (Exception e) {
            s.setMessage(getLabelManager().get("ErrorGenerating") + this.getClass().getName());
            e.printStackTrace();
        }
        return (ec);
    }

    private void displayAttemptedFile(ElementContainer ec, File attemptedFile) {
        try {
            // Show them the attempted file.  if it is a bad file, they will see the exception message
            // Strip out some of the extra html from the "help" file
            ec.addElement(new BR());
            ec.addElement(new BR());
            ec.addElement(new HR().setWidth("100%"));
            if (attemptedFile.isFile()) {
                ec.addElement(getLabelManager().get("ViewingFile") + attemptedFile.getCanonicalPath());
            } else {
                ec.addElement(getLabelManager().get("ViewingFile") + attemptedFile.getName());
            }
            ec.addElement(new HR().setWidth("100%"));
            if (attemptedFile.length() > 80000) {
                throw new Exception(getLabelManager().get("FileTooLarge"));
            }
            String fileData = getFileText(new BufferedReader(new FileReader(attemptedFile)), false);
            if (fileData.indexOf(0x00) != -1) {
                throw new Exception(getLabelManager().get("FileBinary"));
            }
            ec.addElement(new StringElement(fileData.replaceAll(System.getProperty("line.separator"), "<br>")
                    .replaceAll("(?s)<!DOCTYPE.*/head>", "").replaceAll("<br><br>", "<br>")
                    .replaceAll("<br>\\s<br>", "<br>").replaceAll("<\\?", "&lt;").replaceAll("<(r|u|t)",
                            "&lt;$1")));
        } catch (Exception e) {
            ec.addElement(new BR());
            ec.addElement(getLabelManager().get("TheFollowingError"));
            ec.addElement(e.getMessage());
        }
    }


    private File guideTheAtack(WebSession s, String fileName, List<File> htmlFiles) throws Exception {
        // Most people are going to start off with a simple test of
        // ./lesson.html or ../en/lesson.html  where lesson is equal
        // to the name of the selection from the UI.
        //     Example real path: plugin_extracted/plugin/CSRF/lessonPlans/en/CSRF.html
        //     the URL input by default for CSRF is &File=CSRF.html

        // We need to see if this was a simple attempt and serve the file as an allowed 
        // file.  I don;t like this path hack, but it puts them in the right spot
        // on the file system...

        int lastSlash = fileName.lastIndexOf(System.getProperty("file.separator"));
        if (lastSlash == -1) lastSlash = 0;
        String lessonDir = fileName.substring(lastSlash);
        if (lessonDir.length() >= ".html".length())  // at least something semi valid is there
        {
            lessonDir = lessonDir.substring(0, lessonDir.length() - ".html".length());
        }
        String attemptedFileName = LessonUtil.getLessonDirectory(s, this).getParent() + "/" + lessonDir + "/lessonPlans/en/" + fileName;
        File attemptedFile = new File(attemptedFileName);

        // Check access to an allowed file.  if allowedFile != null, access is allowed
        // FIXME: This will incorrectly match ../lesson.html when it should be ../en/lesson.html
        File allowedFile = null;
        for (File htmlFile : htmlFiles) {
            if (htmlFile.getName().equals(fileName) || htmlFile.getName().equals(attemptedFile.getName())) {
                allowedFile = htmlFile;
            }
        }
        if (allowedFile != null && allowedFile.isFile() && allowedFile.exists()) {
            // Don't set completion if they are listing files in the
            // directory listing we gave them.
            if (upDirCount(fileName) >= 1) {
                s.setMessage(getLabelManager().get("OnTheRightPath") + " ==> " + Encoding.urlDecode(allowedFile.getCanonicalPath()));
            } else {
                s.setMessage(getLabelManager().get("FileInAllowedDirectory") + " ==> " + Encoding.urlDecode(allowedFile.getCanonicalPath()));
            }
        }

        if (s.isDebug()) {
            // f is only null if the "File" input was NOT a known lesson file
            s.setMessage(getLabelManager().get("File") + fileName);
            if (allowedFile != null) {
                s.setMessage(getLabelManager().get("Dir") + allowedFile.getParentFile());
                s.setMessage(getLabelManager().get("IsFile") + allowedFile.isFile());
                s.setMessage(getLabelManager().get("Exists") + allowedFile.exists());
            }
        }

        return allowedFile;
    }

    private List<File> findHtmlFiles(File start) {
        final List<File> files = Lists.newArrayList();
        start.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {

                if (pathname.isDirectory()) {
                    //stop after 20 files
                    if (files.size() <= 20) {
                        files.addAll(findHtmlFiles(pathname));
                    }
                } else
                    if (pathname.isFile() && pathname.getName().endsWith("html") && pathname.getParentFile().getName()
                            .equals("en") && pathname.getParentFile().getParentFile().getName()
                            .equals("lessonPlans")) {
                        files.add(pathname);
                    }
                return false;
            }
        });
        return files;
    }

    private int upDirCount(String fileName) {
        int count = 0;
        int startIndex = fileName.indexOf("..");
        while (startIndex != -1) {
            count++;
            startIndex = fileName.indexOf("..", startIndex + 1);
        }
        return count;
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    protected Category getDefaultCategory() {
        return Category.ACCESS_CONTROL;
    }

    /**
     * Gets the hints attribute of the AccessControlScreen object
     *
     * @return The hints value
     */
    protected List<String> getHints(WebSession s) {
        List<String> hints = new ArrayList<String>();
        hints.add(getLabelManager().get("PathBasedAccessControlHint1"));
        hints.add(getLabelManager().get("PathBasedAccessControlHint2"));
        hints.add(getLabelManager().get("PathBasedAccessControlHint3"));
        hints.add(getLabelManager().get("PathBasedAccessControlHint4"));

        return hints;
    }

    /**
     * Gets the instructions attribute of the WeakAccessControl object
     *
     * @return The instructions value
     */
    public String getInstructions(WebSession s) {
        String instructions = getLabelManager().get("PathBasedAccessControlInstr1") + s
                .getUserName() + getLabelManager().get("PathBasedAccessControlInstr2");

        return (instructions);
    }

    private final static Integer DEFAULT_RANKING = new Integer(115);

    protected Integer getDefaultRanking() {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the AccessControlScreen object
     *
     * @return The title value
     */
    public String getTitle() {
        return ("Bypass a Path Based Access Control Scheme");
    }
}

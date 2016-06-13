
package org.owasp.webgoat.plugin;

import com.google.common.collect.Lists;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.P;
import org.apache.ecs.html.PRE;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;


/***************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 * For details, please see http://webgoat.github.io
 *
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created October 28, 2003
 */
public class CommandInjection extends LessonAdapter {
    private final static String HELP_FILE = "HelpFile";
    private static final List<String> VALID_WINDOWS_CMDS = Lists.newArrayList("dir", "ls", "netstat -a", "ipconfig");
    private static final List<String> VALID_UNIX_CMDS = Lists.newArrayList("ls", "ls -l", "netstat -a", "ifconfig");

    private String osName = System.getProperty("os.name");

    @Override
    public void handleRequest(WebSession s) {
        Form form = new Form();
        form.addElement(createContent(s));
        form.setAction(getFormAction());
        form.setMethod(Form.POST);
        form.setName("form");
        form.setEncType("");
        setContent(form);
    }

    private boolean isValidCommand(String command) {
        return osName.indexOf("Windows") != -1 ? VALID_WINDOWS_CMDS.contains(command) : VALID_UNIX_CMDS
                .contains(command);
    }

    /**
     * Description of the Method
     *
     * @param s Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s) {
        ElementContainer ec = new ElementContainer();
        boolean illegalCommand = false;
        try {
            String helpFile = s.getParser().getRawParameter(HELP_FILE, "BasicAuthentication.help");
            if (helpFile.indexOf('&') != -1 || helpFile.indexOf(';') != -1) {
                int index = helpFile.indexOf('&');
                if (index == -1) {
                    index = helpFile.indexOf(';');
                }
                index = index + 1;
                int helpFileLen = helpFile.length() - 1; // subtract 1 for the closing quote
                String cmd = helpFile.substring(index, helpFileLen).trim().toLowerCase();
                if (isValidCommand(cmd)) {
                    illegalCommand = false;
                } else {
                    s.setMessage(getLabelManager().get("CommandInjectionRightTrack1"));
                }
            }

            if (helpFile.indexOf('&') == -1 && helpFile.indexOf(';') == -1) {
                if (helpFile.length() > 0) {
                    if (upDirCount(helpFile) >= 1) {
                        s.setMessage(getLabelManager().get("CommandInjectionRightTrack2"));
                    }
                }
            }
            File safeDir = new File(LessonUtil.getLessonDirectory(s, this), "/resources");
            ec.addElement(new BR());
            ec.addElement(new BR());
            ec.addElement(new StringElement(getLabelManager().get("YouAreCurrentlyViewing") + "<b>"
                    + (helpFile.toString().length() == 0 ? "&lt;" + getLabelManager()
                    .get("SelectFileFromListBelow") + "&gt;" : helpFile.toString())
                    + "</b>"));

            String results = "";
            String fileData = null;
            helpFile = helpFile.replaceAll("\\.help", "\\.html");

            if (osName.indexOf("Windows") != -1) {
                // Add quotes around the filename to avoid having special characters in DOS
                // filenames
                if (!illegalCommand) {
                    results = exec(s, "cmd.exe /c dir /b \"" + safeDir.getPath() + "\"");
                }
                fileData = exec(s, "cmd.exe /c type \"" + new File(safeDir, helpFile).getPath() + "\"");

            } else {
                if (!illegalCommand) {
                    String[] cmd1 = {"/bin/sh", "-c", "ls \"" + safeDir.getPath() + "\""};
                    results = exec(s, cmd1);
                }
                String[] cmd2 = {"/bin/sh", "-c", "cat \"" + new File(safeDir, helpFile).getPath() + "\""};
                fileData = exec(s, cmd2);
            }

            ec.addElement(new P().addElement(getLabelManager().get("SelectLessonPlanToView")));
            ec.addElement(ECSFactory.makePulldown(HELP_FILE, parseResults(results.replaceAll("(?s)\\.html",
                    "\\.help"))));
            // ec.addElement( results );
            Element b = ECSFactory.makeButton(getLabelManager().get("View"));
            ec.addElement(b);
            // Strip out some of the extra html from the "help" file
            ec.addElement(new BR());
            ec.addElement(new BR());
            ec.addElement(new HR().setWidth("90%"));
            PRE pre = new PRE();
            pre.setWidth("90%");
            pre.addElement(new StringElement(fileData.replaceAll(System.getProperty("line.separator"), "<br>")
                    .replaceAll("(?s)<!DOCTYPE.*/head>", "").replaceAll("<br><br>", "<br>")
                    .replaceAll("<br>\\s<br>", "<br>")));
            ec.addElement(pre);


        } catch (Exception e) {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        return (ec);
    }

    private String parseResults(String results) {
        results.replaceAll("(?s).*Output...\\s", "").replaceAll("(?s)Returncode.*", "");
        StringTokenizer st = new StringTokenizer(results, "\n");
        StringBuffer modified = new StringBuffer();

        while (st.hasMoreTokens()) {
            String s = st.nextToken().trim();

            if (s.length() > 0 && s.endsWith(".help")) {
                modified.append(s + "\n");
            }
        }

        return modified.toString();
    }

    public static int upDirCount(String fileName) {
        int count = 0;
        // check for "." = %2d
        // we wouldn't want anyone bypassing the check by useing encoding :)
        // FIXME: I don't think hex endoing will work here.
        fileName = fileName.replaceAll("%2d", ".");
        int startIndex = fileName.indexOf("..");
        while (startIndex != -1) {
            count++;
            startIndex = fileName.indexOf("..", startIndex + 1);
        }
        return count;
    }

    /**
     * Description of the Method
     *
     * @param command Description of the Parameter
     * @param s       Description of the Parameter
     * @return Description of the Return Value
     */
    private String exec(WebSession s, String command) {
        System.out.println("Executing OS command: " + command);
        ExecResults er = Exec.execSimple(command);
        if ((command.indexOf("&") != -1 || command.indexOf(";") != -1) && !er.getError()) {
            makeSuccess(s);
        }

        return (er.toString());
    }

    /**
     * Description of the Method
     *
     * @param command Description of the Parameter
     * @param s       Description of the Parameter
     * @return Description of the Return Value
     */
    private String exec(WebSession s, String[] command) {
        System.out.println("Executing OS command: " + Arrays.asList(command));
        ExecResults er = Exec.execSimple(command);
        // the third argument (index 2) will have the command injection in it
        if ((command[2].indexOf("&") != -1 || command[2].indexOf(";") != -1) && !er.getError()) {
            makeSuccess(s);
        }

        return (er.toString());
    }

    /**
     * Gets the category attribute of the CommandInjection object
     *
     * @return The category value
     */
    protected Category getDefaultCategory() {
        return Category.INJECTION;
    }

    /**
     * Gets the hints attribute of the DirectoryScreen object
     *
     * @return The hints value
     */
    protected List<String> getHints(WebSession s) {
        List<String> hints = new ArrayList();
        hints.add(getLabelManager().get("CommandInjectionHint1"));
        hints.add(getLabelManager().get("CommandInjectionHint2"));
        hints.add(getLabelManager().get("CommandInjectionHint3"));
        hints.add(getLabelManager().get("CommandInjectionHint4"));

        return hints;
    }


    private final static Integer DEFAULT_RANKING = new Integer(40);

    protected Integer getDefaultRanking() {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the DirectoryScreen object
     *
     * @return The title value
     */
    public String getTitle() {
        return "Command Injection";
    }
}

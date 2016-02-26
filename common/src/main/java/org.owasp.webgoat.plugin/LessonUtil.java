package org.owasp.webgoat.plugin;


import org.owasp.webgoat.lessons.AbstractLesson;
import org.owasp.webgoat.session.WebSession;

import java.io.File;

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
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */
public class LessonUtil {

    /**
     * Get the link when using JavaScript to perform a XHR call.
     *
     * @param webSession {@link org.owasp.webgoat.session.WebSession} object.
     * @return a {@link java.lang.String} the link to the lesson
     */
    public static String getXHRLink(WebSession webSession, AbstractLesson lesson) {
        StringBuffer link = new StringBuffer();
        link.append(webSession.getRequest().getRequestURL().toString().replaceAll("#", ""));
        link.append("?");
        link.append("Screen");
        link.append("=");
        link.append(lesson.getScreenId());
        link.append("&");
        link.append("menu");
        link.append("=");
        link.append(lesson.getCategory().getRanking());
        return link.toString();
    }

    /**
     * A reference from an image, script and link tag must include the context path.
     * <p>
     * A reference in include directives are made from within the web application on the server.
     * However, img tags (and the like) make references from the client browser.
     * In such external references, the context path must be included.
     *
     * @param w               a {@link org.owasp.webgoat.session.WebSession} object.
     * @param imgResourceName a {@link java.lang.String} object.
     * @param lesson          a {@link org.owasp.webgoat.lessons.AbstractLesson} object
     * @return a {@link java.lang.String} object.
     */
    public static String buildImagePath(WebSession w, AbstractLesson lesson, String imgResourceName) {
        return w.getRequest()
                .getContextPath() + "/plugin_extracted/plugin/" + lesson.getLessonName() + "/images/" + imgResourceName;
    }

    /**
     * <p>buildJspPath.</p>
     *
     * @param w                  a {@link org.owasp.webgoat.session.WebSession} object.
     * @param lesson             a {@link org.owasp.webgoat.lessons.AbstractLesson} object
     * @param jspResourceName    a {@link java.lang.String} object.
     * @param includeContextPath a boolean.
     * @return a {@link java.lang.String} object.
     */
    public static String buildJspPath(WebSession w, AbstractLesson lesson, String jspResourceName,
                                        boolean includeContextPath) {
        String path = includeContextPath ? w.getContext().getContextPath() : "";
        return path + "/plugin_extracted/plugin/" + lesson.getLessonName() + "/jsp/" + jspResourceName;
    }

    /**
     * <p>buildJsPath.</p>
     *
     * @param w                  a {@link org.owasp.webgoat.session.WebSession} object.
     * @param lesson             a {@link org.owasp.webgoat.lessons.AbstractLesson} object
     * @param jsResourceName     a {@link java.lang.String} object.
     * @param includeContextPath a boolean.
     * @return a {@link java.lang.String} object.
     */
    public static String buildJsPath(WebSession w, AbstractLesson lesson, String jsResourceName, boolean includeContextPath) {
        String path = includeContextPath ? w.getContext().getContextPath() : "";
        return path + "/plugin_extracted/plugin/" + lesson.getLessonName() + "/js/" + jsResourceName;
    }


    /**
     * <p>buildJsPath.</p>
     *
     * @param w              a {@link org.owasp.webgoat.session.WebSession} object.
     * @param lesson         a {@link org.owasp.webgoat.lessons.AbstractLesson} object
     * @param jsResourceName a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String buildJsPath(WebSession w, AbstractLesson lesson, String jsResourceName) {
        return buildJsPath(w, lesson, jsResourceName, true);
    }


    /**
     * <p>buildJsFileSystemPath.</p>
     *
     * @param w              a {@link org.owasp.webgoat.session.WebSession} object.
     * @param lesson         a {@link org.owasp.webgoat.lessons.AbstractLesson} object
     * @param jsResourceName a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public static String buildJsFileSystemPath(WebSession w, AbstractLesson lesson, String jsResourceName) {
        return buildJsPath(w, lesson, jsResourceName, false);
    }


    /**
     * <p>getLessonDirectory.</p>
     *
     * @param w      a {@link org.owasp.webgoat.session.WebSession} object.
     * @param lesson a {@link org.owasp.webgoat.lessons.AbstractLesson} object
     * @return a {@link java.io.File} object.
     */
    public static File getLessonDirectory(WebSession w, AbstractLesson lesson) {
        return new File(w.getContext().getRealPath("/plugin_extracted/plugin/" + lesson.getLessonName() + "/"));
    }
}

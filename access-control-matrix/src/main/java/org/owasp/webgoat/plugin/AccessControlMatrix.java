
package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.WebSession;

import java.util.ArrayList;
import java.util.List;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * 
 * Getting Source ==============
 * 
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * 
 * For details, please see http://webgoat.github.io
 * 
 * @author Jeff Williams <a href="http://www.aspectsecurity.com">Aspect Security</a>
 * @created October 28, 2003
 */

public class AccessControlMatrix extends LessonAdapter
{

    @Override
    protected Category getDefaultCategory()
    {
        return Category.ACCESS_CONTROL;
    }

    @Override
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Many sites attempt to restrict access to resources by role.");
        hints.add("Developers frequently make mistakes implementing this scheme.");
        hints.add("Attempt combinations of users, roles, and resources.");
        return hints;
    }

    private final static Integer DEFAULT_RANKING = new Integer(10);

    @Override
    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    @Override
    public String getTitle()
    {
        return ("Using an Access Control Matrix");
    }

}

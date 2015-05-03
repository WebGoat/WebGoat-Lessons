
package org.owasp.webgoat.plugin;

import java.util.ArrayList;
import java.util.List;

import org.apache.ecs.Element;
import org.apache.ecs.StringElement;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.WebSession;


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
 * @author Bruce Mayhew 
 * @created April, 2015
 */

public class NewLegacyLesson extends LessonAdapter
{
    /**
     * Creates the HTML content for a lesson.
     * 
     * @param s The WebGoat WebSession object
     * @return ecs Element containing HTML
     */
    protected Element createContent(WebSession s)
    {
        Element ec = super.createContent(s);
        // Sets a red message 
        // We recommend you use the LabelManager for easy international 
        // s.setMessage(getLabelManager().get("LabelName"));
        
        // Set the completion message and status
        // if (success_criteria) makeSuccess(s);
        
        // Add HTML content - we use ecs because we do
        // ec.addElement(new StringElement("Welcome to the WebGoat hall of fame !!"));
        return (ec);
    }

    /**
     * Gets the category of the lesson.  Categories
     * are defined in the WebGoat container Category class
     *  
 	 * @see org.owasp.webgoat.lessons.Category
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
    	// Choose the category from org.owasp.webgoat.lessons.Category 
    	// that appropriately fits this lesson
        return Category.INTRODUCTION;
    }

    /**
     * Sets the order the lesson appears in the lesson list.
     * Low numbers appear before high numbers
     * 
     * @see org.owasp.webgoat.lessons.LessonAdapter#getDefaultRanking()
     */
    protected Integer getDefaultRanking()
    {
        return new Integer(85);
    }

    /**
     * Return the hints to help solve the lessons
     * 
     * @see org.owasp.webgoat.lessons.LessonAdapter#getHints(org.owasp.webgoat.session.WebSession)
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        
        // You can have as many hints as you want.
        // Hints should go in language specific resources/plugin/i18n/WebGoatLabels.properties file
        hints.add(getLabelManager().get("NewLegacyLessonHint1"));
        hints.add(getLabelManager().get("NewLegacyLessonHint2"));
        hints.add(getLabelManager().get("NewLegacyLessonHint3"));

        return hints;
    }
    
    public String getInstructions(WebSession s) 
    {
        // DON'T put instructions here in this method.
        
        // We recommend using the resources/plugin/lessonPlans/en/NewLegacyLesson.html file. 
        // WebGoat will take the text between the start/stop instructions comments in this file and insert
        // them as instructions in the running lesson.  You can also use the getInstructions() method in the
        // NewLegacyLesson.java file to return instructions.  This allows users to modify the lesson instructions
        // without modifying the code. 

        // We recommend using this file for instructions to allow easy translation to other languages.
    	
    	// DON'T do this - this method is not needed, the LessonAdapter will do this for you
    	return super.getInstructions(s);
    }
    
    /**
     * Gets the title of the lesson to display in UI
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("How to create a Legacy Lesson");
    }

}

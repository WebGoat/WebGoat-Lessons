
package org.owasp.webgoat.plugin;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Script;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.WebSession;

import java.io.PrintWriter;
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
 * @author Sherif Koussa <a href="http://www.softwaresecured.com">Software Secured</a>
 * @created December 25, 2006
 */

public class JSONInjection extends LessonAdapter
{

    private final static Integer DEFAULT_RANKING = new Integer(30);

    private final static String TRAVEL_FROM = "travelFrom";

    private final static String TRAVEL_TO = "travelTo";

    public void handleRequest(WebSession s)
    {

        try
        {
            if (s.getParser().getRawParameter("from", "").equals("ajax"))
            {
                String lineSep = System.getProperty("line.separator");
                String jsonStr = "{" + lineSep + "\"From\": \"Boston\"," + lineSep + "\"To\": \"Seattle\", " + lineSep
                        + "\"flights\": [" + lineSep
                        + "{\"stops\": \"0\", \"transit\" : \"N/A\", \"price\": \"$600\"}," + lineSep
                        + "{\"stops\": \"2\", \"transit\" : \"Newark,Chicago\", \"price\": \"$300\"} " + lineSep + "]"
                        + lineSep + "}";
                s.getResponse().setContentType("text/html");
                s.getResponse().setHeader("Cache-Control", "no-cache");
                PrintWriter out = new PrintWriter(s.getResponse().getOutputStream());
                out.print(jsonStr);
                out.flush();
                out.close();
                return;
            }
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }

        Form form = new Form(getFormAction(), Form.POST).setName("form").setEncType("");
        form.setOnSubmit("return check();");

        form.addElement(createContent(s));

        setContent(form);

    }

    /**
     * Description of the Method
     *
     * @param s
     *            Current WebSession
     */
    protected Element createContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(new Script().setSrc(LessonUtil.buildJsPath(s, this, "jsonInjection.js")));

        Table t1 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(0).setWidth("90%").setAlign("center");

        TR tr = new TR();

        tr.addElement(new TD("From: "));
        Input in = new Input(Input.TEXT, TRAVEL_FROM, "");
        in.addAttribute("onblur", "getFlights('" + LessonUtil.getXHRLink(s, this) + "');");
        in.addAttribute("id", TRAVEL_FROM);
        tr.addElement(new TD(in));

        t1.addElement(tr);

        tr = new TR();
        tr.addElement(new TD("To: "));
        in = new Input(Input.TEXT, TRAVEL_TO, "");
        in.addAttribute("onblur", "getFlights('" + LessonUtil.getXHRLink(s, this) + "');");
        in.addAttribute("id", TRAVEL_TO);
        tr.addElement(new TD(in));

        t1.addElement(tr);
        ec.addElement(t1);

        ec.addElement(new BR());
        ec.addElement(new BR());
        Div div = new Div();
        div.addAttribute("name", "flightsDiv");
        div.addAttribute("id", "flightsDiv");
        ec.addElement(div);

        Input b = new Input();
        b.setType(Input.SUBMIT);
        b.setValue("Submit");
        b.setName("SUBMIT");
        ec.addElement(b);

        Input price2Submit = new Input();
        price2Submit.setType(Input.HIDDEN);
        price2Submit.setName("price2Submit");
        price2Submit.setValue("");
        price2Submit.addAttribute("id", "price2Submit");
        ec.addElement(price2Submit);
        if (s.getParser().getRawParameter("radio0", "").equals("on"))
        {
            String price = s.getParser().getRawParameter("price2Submit", "");
            price = price.replace("$", "");
            if (Integer.parseInt(price) < 600)
            {
                makeSuccess(s);
            }
            else
            {
                s.setMessage("You are close, try to set the price for the non-stop flight to be less than $600");
            }
        }
        return ec;
    }

    protected Category getDefaultCategory()
    {
        return Category.AJAX_SECURITY;
    }

    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("JSON stands for JavaScript Object Notation.");
        hints.add("JSON is a way of representing data just like XML.");
        hints.add("The JSON payload is easily interceptable.");
        hints.add("Intercept the reply, change the $600 to $25.");
        return hints;

    }

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the HelloScreen object
     *
     * @return The title value
     */
    public String getTitle()
    {
        return ("JSON Injection");
    }

}

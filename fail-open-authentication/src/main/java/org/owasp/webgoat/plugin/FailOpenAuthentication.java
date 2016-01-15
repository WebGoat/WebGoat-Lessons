
package org.owasp.webgoat.plugin;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.B;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TH;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.ECSFactory;
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
public class FailOpenAuthentication extends LessonAdapter
{

    /**
     * Description of the Field
     */
    protected final static String LOGOUT = "WACLogout";

    /**
     * Description of the Field
     */
    protected final static String PASSWORD = "Password";

    /**
     * Description of the Field
     */
    protected final static String USERNAME = "Username";

    /**
     * Description of the Method
     * 
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element createContent(WebSession s)
    {
        boolean logout = s.getParser().getBooleanParameter(LOGOUT, false);

        if (logout)
        {
            s.setMessage("Goodbye!");
            s.eatCookies();

            return (makeLogin(s));
        }

        try
        {
            String username = "";
            String password = "";

            try
            {
                username = s.getParser().getRawParameter(USERNAME);
                password = s.getParser().getRawParameter(PASSWORD);

                // if credentials are bad, send the login page
                if (!"webgoat".equals(username) || !password.equals("webgoat"))
                {
                    s.setMessage("Invalid username and password entered.");

                    return (makeLogin(s));
                }
            } catch (Exception e)
            {
                // The parameter was omitted. set fail open status complete
                if (username.length() > 0 && e.getMessage().indexOf("not found") != -1)
                {
                    if ((username != null) && (username.length() > 0))
                    {
                        makeSuccess(s);
                        return (makeUser(s, username, "Fail Open Error Handling"));
                    }
                }
            }

            // Don't let the fail open pass with a blank password.
            if (password.length() == 0)
            {
                // We make sure the username was submitted to avoid telling the user an invalid
                // username/password was entered when they first enter the lesson via the side menu.
                // This also suppresses the error if they just hit the login and both fields are
                // empty.
                if (username.length() != 0)
                {
                    s.setMessage("Invalid username and password entered.");
                }

                return (makeLogin(s));

            }

            // otherwise authentication is good, show the content
            if ((username != null) && (username.length() > 0)) { return (makeUser(s, username,
                                                                                    "Parameters.  You did not exploit the fail open.")); }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
        }

        return (makeLogin(s));
    }

    /**
     * Gets the category attribute of the FailOpenAuthentication object
     * 
     * @return The category value
     */
    public Category getDefaultCategory()
    {
        return Category.ERROR_HANDLING;
    }

    /**
     * Gets the hints attribute of the AuthenticateScreen object
     * 
     * @return The hints value
     */
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("You can force errors during the authentication process.");
        hints.add("You can change length, existance, or values of authentication parameters.");
        hints
                .add("Try removing a parameter ENTIRELY with <A href=\"https://www.owasp.org/index.php/OWASP_Zed_Attack_Proxy_Project\">OWASP ZAP</A>.");

        return hints;
    }

    /**
     * Gets the instructions attribute of the FailOpenAuthentication object
     * 
     * @return The instructions value
     */
    public String getInstructions(WebSession s)
    {
        return "Due to an error handling problem in the authentication mechanism, it is possible to authenticate "
                + "as the 'webgoat' user without entering a password.  Try to login as the webgoat user without "
                + "specifying a password.";
    }

    private final static Integer DEFAULT_RANKING = new Integer(20);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the title attribute of the AuthenticateScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Fail Open Authentication Scheme");
    }

    /**
     * Description of the Method
     *
     * @param s
     *            Description of the Parameter
     * @return Description of the Return Value
     */
    protected Element makeLogin(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        ec.addElement(new H1().addElement(getLabelManager().get("SignIn")));
        Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(0).setWidth("90%").setAlign("center");

        if (s.isColor())
        {
            t.setBorder(1);
        }

        TR tr = new TR();
        tr.addElement(new TH()
                .addElement(getLabelManager().get("WeakAuthenticationCookiePleaseSignIn"))
                .setColSpan(2).setAlign("left"));
        t.addElement(tr);

        tr = new TR();
        tr.addElement(new TD().addElement("*"+getLabelManager().get("RequiredFields")).setWidth("30%"));
        t.addElement(tr);

        tr = new TR();
        tr.addElement(new TD().addElement("&nbsp;").setColSpan(2));
        t.addElement(tr);

        TR row1 = new TR();
        TR row2 = new TR();
        row1.addElement(new TD(new B(new StringElement("*"+getLabelManager().get("UserName")))));
        row2.addElement(new TD(new B(new StringElement("*"+getLabelManager().get("Password")))));

        Input input1 = new Input(Input.TEXT, USERNAME, "");
        Input input2 = new Input(Input.PASSWORD, PASSWORD, "");
        row1.addElement(new TD(input1));
        row2.addElement(new TD(input2));
        t.addElement(row1);
        t.addElement(row2);

        Element b = ECSFactory.makeButton(getLabelManager().get("Login"));
        t.addElement(new TR(new TD(b)));
        ec.addElement(t);

        return (ec);
    }

    /**
     * Description of the Method
     *
     * @param s
     *            Description of the Parameter
     * @param user
     *            Description of the Parameter
     * @param method
     *            Description of the Parameter
     * @return Description of the Return Value
     * @exception Exception
     *                Description of the Exception
     */
    protected Element makeUser(WebSession s, String user, String method) throws Exception
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement(new P().addElement(getLabelManager().get("WelcomeUser") + user));
        ec.addElement(new P().addElement(getLabelManager().get("YouHaveBeenAuthenticatedWith") + method));
        ec.addElement(new P().addElement(ECSFactory.makeLink(getLabelManager().get("Logout"), LOGOUT, true)));
        ec.addElement(new P().addElement(ECSFactory.makeLink(getLabelManager().get("Refresh"), "", "")));

        return (ec);
    }

}

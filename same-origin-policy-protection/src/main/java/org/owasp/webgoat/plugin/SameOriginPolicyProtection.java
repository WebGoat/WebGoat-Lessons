
package org.owasp.webgoat.plugin;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.H3;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.Script;
import org.apache.ecs.html.TextArea;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.WebSession;

import java.util.ArrayList;
import java.util.List;


public class SameOriginPolicyProtection extends LessonAdapter
{
    private int triedBothURLs = 0;

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
            ec.addElement(new Script().setSrc(LessonUtil.buildJsPath(s, this, "sameOrigin.js")));

            Input testedURL = new Input(Input.HIDDEN, "testedURL", "");
            testedURL.setID("testedURL");
            ec.addElement(testedURL);

            ec.addElement(new StringElement("Enter a URL: "));
            ec.addElement(new BR());

            TextArea urlArea = new TextArea();
            urlArea.setID("requestedURL");
            urlArea.setRows(1);
            urlArea.setCols(60);
            urlArea.setWrap("SOFT");
            ec.addElement(urlArea);

            Input b = new Input();
            b.setType(Input.SUBMIT);
            b.setValue("Go!");
            b.setName("SUBMIT");
            b.setID("SUBMIT");
            b.setOnClick("submitXHR();");
            ec.addElement(b);

            ec.addElement(new BR());
            ec.addElement(new BR());

            H3 reponseTitle = new H3("Response: ");
            reponseTitle.setID("responseTitle");
            ec.addElement(reponseTitle);

            TextArea ta = new TextArea();
            ta.setName("responseArea");
            ta.setID("responseArea");
            ta.setCols(60);
            ta.setRows(4);
            ec.addElement(ta);
            ec.addElement(new BR());

            String webGoatURL = LessonUtil.buildJspPath(s, this, "sameOrigin.jsp", true);
            String googleURL = "http://www.google.com/search?q=aspect+security";

            ec.addElement(new BR());
            ec.addElement(new StringElement(String.format("Try both URLs: %s and %s ", webGoatURL, googleURL)));

            if (s.getParser().getStringParameter("testedURL", "").contains(webGoatURL)) {
                triedBothURLs++;
            }
            if (s.getParser().getStringParameter("testedURL", "").contains(googleURL)) {
                triedBothURLs++;
            }
        } catch (Exception e)
        {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        if (triedBothURLs == 2)
        {
            makeSuccess(s);
        }

        return (ec);
    }

    /**
     * Gets the hints attribute of the HelloScreen object
     * 
     * @return The hints value
     */
    public List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Enter a URL to see if it is allowed.");
        hints.add("Click both of the links below to complete the lesson");

        return hints;
    }

    /**
     * Gets the ranking attribute of the HelloScreen object
     * 
     * @return The ranking value
     */
    private final static Integer DEFAULT_RANKING = new Integer(876);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    protected Category getDefaultCategory()
    {
        return Category.AJAX_SECURITY;
    }

    /**
     * Gets the title attribute of the HelloScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("Same Origin Policy Protection");
    }

    public String getInstructions(WebSession s)
    {
        String instructions = "This exercise demonstrates the "
                + "Same Origin Policy Protection.  XHR requests can only be passed back to "
                + " the originating server.  Attempts to pass data to a non-originating server " + " will fail.";

        return (instructions);
    }
}

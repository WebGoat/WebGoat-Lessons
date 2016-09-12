
package org.owasp.webgoat.plugin;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Div;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.apache.ecs.html.Script;
import org.apache.ecs.html.Select;
import org.apache.ecs.html.TD;
import org.apache.ecs.html.TR;
import org.apache.ecs.html.Table;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.SequentialLessonAdapter;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ClientSideFiltering extends SequentialLessonAdapter
{

    private final static String ANSWER = "answer";

    protected Element createContent(WebSession s)
    {
        return super.createStagedContent(s);
    }

    protected Element createMainContent(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        ec.addElement("<link rel=\"stylesheet\" type=\"text/css\" href=\"" + LessonUtil.buildJspPath(s, this, "clientSideFiltering-stage1.css", true) + "\" />");

        try
        {

            ec.addElement(new Script().setSrc(LessonUtil.buildJsPath(s, this, "clientSideFiltering.js")));

            Input input = new Input(Input.HIDDEN, "userID", 102);

            input.setID("userID");

            ec.addElement(input);

            Div wrapperDiv = new Div();
            wrapperDiv.setID("lesson_wrapper");

            Div headerDiv = new Div();
            headerDiv.setID("lesson_header");

            Div workspaceDiv = new Div();
            workspaceDiv.setClass("lesson_workspace");

            wrapperDiv.addElement(headerDiv);
            wrapperDiv.addElement(workspaceDiv);

            ec.addElement(wrapperDiv);

            workspaceDiv.addElement(new BR());
            workspaceDiv.addElement(new BR());

            workspaceDiv.addElement(new P().addElement(getLabelManager().get("ClientSideFilteringSelectUser")));

            workspaceDiv.addElement(createDropDown());

            workspaceDiv.addElement(new P());

            Table t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%").setAlign("center");

            t.setID("hiddenEmployeeRecords");
            t.setStyle("display: none");

            workspaceDiv.addElement(t);

            t = new Table().setCellSpacing(0).setCellPadding(2).setBorder(1).setWidth("90%").setAlign("center");

            TR tr = new TR();
            tr.addElement(new TD().addElement(getLabelManager().get("ClientSideFilteringUserID")));
            tr.addElement(new TD().addElement(getLabelManager().get("ClientSideFilteringFirstName")));
            tr.addElement(new TD().addElement(getLabelManager().get("ClientSideFilteringLastName")));
            tr.addElement(new TD().addElement(getLabelManager().get("ClientSideFilteringSSN")));
            tr.addElement(new TD().addElement(getLabelManager().get("ClientSideFilteringSalary")));
            t.addElement(tr);
            tr = new TR();
            tr.setID("employeeRecord");
            t.addElement(tr);

            workspaceDiv.addElement(t);

        } catch (Exception e)
        {
            s.setMessage(getLabelManager().get("ClientSideFilteringErrorGenerating") + this.getClass().getName());
            e.printStackTrace();
        }

        return (ec);
    }

    /**
     * Gets the category attribute of the RoleBasedAccessControl object
     * 
     * @return The category value
     */

    protected ElementContainer doStage1(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        StringBuffer answerString = null;
        int answer = 0;

        try
        {
            answerString = new StringBuffer(s.getParser().getStringParameter(ANSWER, ""));
            answer = Integer.parseInt(answerString.toString());
        } catch (NumberFormatException e)
        {

            // e.printStackTrace();
        }

        if (answer == 450000)
        {

            getLessonTracker(s).setStage(2);
            s.setMessage(getLabelManager().get("ClientSideFilteringStage1Complete"));

            // Redirect user to Stage2 content.
            ec.addElement(doStage2(s));
        }
        else
        {
            ec.addElement(stage1Content(s));
        }

        return ec;

    }

    protected Element doStage2(WebSession s)
    {
        ElementContainer ec = new ElementContainer();

        /**
         * They pass iff:
         * 
         * 1. If clientSideFiltering.jsp has an XPath filter to
         *    limit the data being returned.
         */
        String file = LessonUtil.getLessonDirectory(s, this) + "/jsp/clientSideFiltering.jsp";
        String content = getFileContent(file);

        if (content.indexOf("[Managers/Manager/text()") != -1)
        {
            makeSuccess(s);
            ec.addElement(stage2Content(s));
        }
        else
        {
            ec.addElement(stage2Content(s));
        }

        return ec;
    }

    protected ElementContainer stage1Content(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        try
        {

            ec.addElement(createMainContent(s));

            Table t1 = new Table().setCellSpacing(0).setCellPadding(2);

            if (s.isColor())
            {
                t1.setBorder(1);
            }

            TR tr = new TR();
            tr.addElement(new TD().addElement(getLabelManager().get("ClientSideFilteringStage1Question")));
            tr.addElement(new TD(new Input(Input.TEXT, ANSWER, "")));
            Element b = ECSFactory.makeButton(getLabelManager().get("ClientSideFilteringStage1SubmitAnswer"));
            tr.addElement(new TD(b).setAlign("LEFT"));
            t1.addElement(tr);

            ec.addElement(t1);

        } catch (Exception e)
        {
            s.setMessage(getLabelManager().get("ClientSideFilteringErrorGenerating") + this.getClass().getName());
            e.printStackTrace();
        }

        return ec;
    }

    protected ElementContainer stage2Content(WebSession s)
    {
        ElementContainer ec = new ElementContainer();
        try
        {

            ec.addElement(createMainContent(s));

            ec.addElement(new BR());
            ec.addElement(new BR());

            Table t1 = new Table().setCellSpacing(0).setCellPadding(2);

            if (s.isColor())
            {
                t1.setBorder(1);
            }

            TR tr = new TR();
            /*
             * tr.addElement(new TD() .addElement("Press 'Submit' when you believe you have
             * completed the lesson."));
             */
            Element b = ECSFactory.makeButton(getLabelManager().get("ClientSideFilteringStage2Finish"));
            tr.addElement(new TD(b).setAlign("CENTER"));
            t1.addElement(tr);

            ec.addElement(t1);

        } catch (Exception e)
        {
            s.setMessage(getLabelManager().get("ClientSideFilteringErrorGenerating") + this.getClass().getName());
            e.printStackTrace();
        }

        return ec;
    }

    protected Select createDropDown()
    {
        Select select = new Select("UserSelect");

        select.setID("UserSelect");

        org.apache.ecs.html.Option option = new org.apache.ecs.html.Option(getLabelManager().get("ClientSideFilteringChoose"), "0", getLabelManager().get("ClientSideFilteringChoose"));

        select.addElement(option);

        option = new org.apache.ecs.html.Option("Larry Stooge", "101", "Larry Stooge");

        select.addElement(option);

        option = new org.apache.ecs.html.Option("Curly Stooge", "103", "Curly Stooge");

        select.addElement(option);

        option = new org.apache.ecs.html.Option("Eric Walker", "104", "Eric Walker");

        select.addElement(option);

        option = new org.apache.ecs.html.Option("Tom Cat", "105", "Tom Cat");

        select.addElement(option);

        option = new org.apache.ecs.html.Option("Jerry Mouse", "106", "Jerry Mouse");

        select.addElement(option);

        option = new org.apache.ecs.html.Option("David Giambi", "107", "David Giambi");

        select.addElement(option);

        option = new org.apache.ecs.html.Option("Bruce McGuirre", "108", "Bruce McGuirre");

        select.addElement(option);

        option = new org.apache.ecs.html.Option("Sean Livingston", "109", "Sean Livingston");

        select.addElement(option);

        option = new org.apache.ecs.html.Option("Joanne McDougal", "110", "Joanne McDougal");

        select.addElement(option);

        select.setOnChange("selectUser()");

        select.setOnFocus("fetchUserData()");

        return select;

    }

    protected Category getDefaultCategory()
    {
        return Category.AJAX_SECURITY;
    }

    /**
     * Gets the hints attribute of the RoleBasedAccessControl object
     * 
     * @return The hints value
     */
    public List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();

        hints.add(getLabelManager().get("ClientSideFilteringHint1"));
        hints.add(getLabelManager().get("ClientSideFilteringHint2"));
        hints.add(getLabelManager().get("ClientSideFilteringHint3"));
        hints.add(getLabelManager().get("ClientSideFilteringHint4"));
        hints.add(getLabelManager().get("ClientSideFilteringHint5a") + " <a href = " 
            + LessonUtil.buildJspPath(s, this, "clientSideFiltering.jsp?userId=102", true) + ">"
            + getLabelManager().get("ClientSideFilteringHint5b") + "</a>"
            + getLabelManager().get("ClientSideFilteringHint5c"));
        hints.add(getLabelManager().get("ClientSideFilteringHint6"));
        hints.add(getLabelManager().get("ClientSideFilteringHint7"));
        hints.add(getLabelManager().get("ClientSideFilteringHint8"));
        hints.add(getLabelManager().get("ClientSideFilteringHint9"));
        hints.add(getLabelManager().get("ClientSideFilteringHint10"));
        return hints;

    }

    public String getInstructions(WebSession s)
    {
        String instructions = "";

        if (getLessonTracker(s).getStage() == 1)
        {
            instructions = getLabelManager().get("ClientSideFilteringInstructions1");
        }
        else if (getLessonTracker(s).getStage() == 2)
        {
            instructions = getLabelManager().get("ClientSideFilteringInstructions2");
        }
        return (instructions);
    }

    private final static Integer DEFAULT_RANKING = new Integer(10);

    protected Integer getDefaultRanking()
    {
        return DEFAULT_RANKING;
    }

    /**
     * Gets the resources attribute of the RoleBasedAccessControl object
     * 
     * @param rl
     *            Description of the Parameter
     * @return The resources value
     */

    /**
     * Gets the role attribute of the RoleBasedAccessControl object
     * 
     * @param user
     *            Description of the Parameter
     * @return The role value
     */

    /**
     * Gets the title attribute of the AccessControlScreen object
     * 
     * @return The title value
     */

    public String getTitle()
    {
        return ("LAB: Client Side Filtering");
    }

    private String getFileContent(String content)
    {
        BufferedReader is = null;
        StringBuffer sb = new StringBuffer();

        try
        {
            is = new BufferedReader(new FileReader(new File(content)));
            String s = null;

            while ((s = is.readLine()) != null)
            {
                sb.append(s);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (is != null)
            {
                try
                {
                    is.close();
                } catch (IOException ioe)
                {

                }
            }
        }

        return sb.toString();
    }

}

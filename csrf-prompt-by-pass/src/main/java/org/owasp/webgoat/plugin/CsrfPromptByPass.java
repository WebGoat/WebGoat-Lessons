
package org.owasp.webgoat.plugin;

import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.StringElement;
import org.apache.ecs.html.BR;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.H1;
import org.apache.ecs.html.Input;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.session.WebSession;
import org.owasp.webgoat.util.HtmlEncoder;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
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
 * @author Contributed by <a href="http://www.partnet.com">PartNet.</a>
 * 
 */
public class CsrfPromptByPass extends CSRF
{
    private static final String TRANSFER_FUNDS_PARAMETER = "transferFunds";
    private static final String TRANSFER_FUNDS_PAGE = "main";
    private static final String TRANSFER_FUND_AMOUNT_ATTRIBUTE = "transferFundAmount";
    private static final String CANCEL_TRANSFER = "CANCEL";
    private static final String CONFIRM_TRANFER = "CONFIRM";
    
    /**
     * if TRANSFER_FUND_PARAMETER is a parameter, them doTransfer is invoked.  doTranser presents the 
     * web content to confirm and then execute a simulated transfer of funds.  An initial request
     * should have a dollar amount specified.  The amount will be stored and a confirmation form is presented.  
     * The confirmation can be canceled or confirmed.  Confirming the transfer will mark this lesson as completed.
     * 
     * @param s
     * @return Element will appropriate web content for a transfer of funds.
     */
    protected Element doTransfer(WebSession s) {
        String transferFunds = HtmlEncoder.encode(s.getParser().getRawParameter(TRANSFER_FUNDS_PARAMETER, ""));
        ElementContainer ec = new ElementContainer();
        
        if (transferFunds.length() != 0) {
            
            HttpSession httpSession = s.getRequest().getSession();
            Integer transferAmount = (Integer) httpSession.getAttribute(TRANSFER_FUND_AMOUNT_ATTRIBUTE);
            
            if (transferFunds.equalsIgnoreCase(TRANSFER_FUNDS_PAGE)){
                
                //present transfer form
                ec.addElement(new H1("Electronic Transfer:"));
                String action = getLink();
                Form form = new Form(action, Form.POST);
                form.addElement( new Input(Input.text, TRANSFER_FUNDS_PARAMETER, "0"));
                //if this token is present we won't mark the lesson as completed
                form.addElement( new Input(Input.submit));
                ec.addElement(form);
                
            } else if (transferFunds.equalsIgnoreCase(CONFIRM_TRANFER) && transferAmount != null ){
                
                //transfer is confirmed
                ec.addElement(new H1("Electronic Transfer Complete"));
                ec.addElement(new StringElement("Amount Transfered: "+transferAmount));
                makeSuccess(s);
                
            } else if (transferFunds.equalsIgnoreCase(CANCEL_TRANSFER)){
                
                //clear any pending fund transfer
                s.getRequest().removeAttribute(TRANSFER_FUND_AMOUNT_ATTRIBUTE);
                
            } else if (transferFunds.length() > 0){
                
                //save the transfer amount in the session
                transferAmount = new Integer(transferFunds);
                httpSession.setAttribute(TRANSFER_FUND_AMOUNT_ATTRIBUTE, transferAmount);
                
                //prompt for confirmation
                
                ec.addElement(new H1("Electronic Transfer Confirmation:"));
                ec.addElement(new StringElement("Amount to transfer: "+transferAmount));
                ec.addElement(new BR());
                String action = getLink();
                Form form = new Form(action, Form.POST);
                form.addElement( new Input(Input.submit, TRANSFER_FUNDS_PARAMETER, CONFIRM_TRANFER));
                form.addElement( new Input(Input.submit, TRANSFER_FUNDS_PARAMETER, CANCEL_TRANSFER));
                ec.addElement(form);    
            }
        }
        // white space
        ec.addElement(new BR());
        ec.addElement(new BR());
        ec.addElement(new BR());
        return ec;
    }

    /**
     * @param s current web session
     * @return true if the page should be rendered as a Transfer of funds page or false for the normal message posting page.
     */
    protected boolean isTransferFunds(WebSession s) {
        String transferFunds = s.getParser().getRawParameter(TRANSFER_FUNDS_PARAMETER, "");
        if (transferFunds.length() != 0){
            return true;
        }
        return false;
    }

    @Override
    protected Category getDefaultCategory()
    {
        return Category.XSS;
    }

    private final static Integer DEFAULT_RANKING = new Integer(122);

    @Override
    protected Integer getDefaultRanking()
    {

        return DEFAULT_RANKING;
    }

    @Override
    protected List<String> getHints(WebSession s)
    {
        List<String> hints = new ArrayList<String>();
        hints.add("Inspect the page returned by a URL '" + getServletLink() + "&transferFunds=5000'");
        hints.add("Write a malicious message that, when shown in the browser, will submit the two fund requests.");
        hints.add("Insert two images or iframes, the second with no source.  Specify the onload attribute of the first iframe to set the source of the second iframe.");
        hints.add("Include these URLs in the message: <pre>'" + getServletLink() + "&transferFunds=400'</pre> " +
                    "and <pre>'" + getServletLink() + "&transferFunds=CONFIRM'</pre>");

        return hints;
    }

    /**
     * Gets the title attribute of the MessageBoardScreen object
     * 
     * @return The title value
     */
    public String getTitle()
    {
        return ("CSRF Prompt By-Pass");
    }

}

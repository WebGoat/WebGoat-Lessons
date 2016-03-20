
package instructor;

import org.owasp.webgoat.plugin.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.plugin.crosssitescripting.ViewProfileCrossSiteScripting;


// STAGE 4 FIXES
//
//Solution Summary: Look in the WebContent/lesson/CrossSiteScripting/ViewProfile.jsp
//
//Look for the <-- STAGE 4 - FIX    in the ViewProfile.jsp
//
//

public class ViewProfile_i extends ViewProfileCrossSiteScripting
{
	public ViewProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName)
	{
		super(lesson, lessonName, actionName);
	}
}

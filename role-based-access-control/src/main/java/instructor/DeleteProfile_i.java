
package instructor;

import org.owasp.webgoat.plugin.GoatHillsFinancial.GoatHillsFinancial;
import org.owasp.webgoat.plugin.GoatHillsFinancial.LessonAction;
import org.owasp.webgoat.plugin.rollbased.DeleteProfileRoleBasedAccessControl;
import org.owasp.webgoat.plugin.rollbased.RoleBasedAccessControl;
import org.owasp.webgoat.session.UnauthorizedException;
import org.owasp.webgoat.session.WebSession;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DeleteProfile_i extends DeleteProfileRoleBasedAccessControl
{

	public DeleteProfile_i(GoatHillsFinancial lesson, String lessonName, String actionName, LessonAction chainedAction)
	{
		super(lesson, lessonName, actionName, chainedAction);
	}

	public void doDeleteEmployeeProfile(WebSession s, int userId, int employeeId) throws UnauthorizedException
	{
		if (s.isAuthorizedInLesson(userId, RoleBasedAccessControl.DELETEPROFILE_ACTION)) // FIX
		{
			try
			{
				String query = "DELETE FROM employee WHERE userid = " + employeeId;
				// System.out.println("Query:  " + query);
				try
				{
					Statement statement = WebSession.getConnection(s)
							.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
					statement.executeUpdate(query);
				} catch (SQLException sqle)
				{
					s.setMessage("Error deleting employee profile");
					sqle.printStackTrace();
				}
			} catch (Exception e)
			{
				s.setMessage("Error deleting employee profile");
				e.printStackTrace();
			}
		}
		else
		{
			throw new UnauthorizedException(); // FIX
		}
	}

}

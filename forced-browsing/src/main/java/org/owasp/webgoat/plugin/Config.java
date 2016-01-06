package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.LessonServletMapping;
import org.owasp.webgoat.session.WebSession;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * .
 */
@LessonServletMapping(path = "/config")
public class Config extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebSession webSession = ((WebSession)req.getSession().getAttribute("websession"));
        resp.sendRedirect(req.getContextPath() + "/start.mvc" + webSession.getCurrentLesson().getLink() + "&succeeded=yes");
    }
}

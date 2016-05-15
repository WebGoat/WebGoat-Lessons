package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.LessonEndpoint;
import org.owasp.webgoat.lessons.LessonEndpointMapping;
import org.owasp.webgoat.session.WebSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * .
 */
@LessonEndpointMapping
public class Config  extends LessonEndpoint {

    @RequestMapping(method = RequestMethod.GET)
    public void invoke(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebSession webSession = ((WebSession)req.getSession().getAttribute("websession"));
        resp.sendRedirect(req.getContextPath() + "/start.mvc" + webSession.getCurrentLesson().getLink() + "&succeeded=yes");
    }

    @Override
    public String getPath() {
        return "/config";
    }

}

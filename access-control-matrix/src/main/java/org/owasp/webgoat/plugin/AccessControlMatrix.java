
package org.owasp.webgoat.plugin;

import org.owasp.webgoat.lessons.LessonEndpoint;
import org.owasp.webgoat.lessons.model.AttackResult;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;


/***************************************************************************************************
 * 
 * 
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * 
 * Copyright (c) 2002 - 2014 Bruce Mayhew
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
public class AccessControlMatrix extends LessonEndpoint
{

    @RequestMapping(method = RequestMethod.GET)
    public @ResponseBody
    AttackResult completed(HttpServletRequest request) {
        System.out.println("Invoking attack1");
        return AttackResult.success();
    }

    @Override
    public String getPath() {
        return "/access-control-matrix/attack1";
    }

//    @Override
//    public JSONObject attackResponse(Map<String, String[]> requestParams) {
//        // determine if attack was successful and generate response
//        // this is where custom logic goes and build a response object like ...
//        //  {
//        //      attackStatus:(successful|partial|failed),
//        //      feedback: "try this or try that ... or leave it blank if you want"
//        //      html: "html to output if you want to simulate, pass-through attack output
//        //  }
//
//        JSONObject responseObject = new JSONObject(requestParams);
//        return responseObject;
//
//    }
//
//    @Override
//    public boolean attack() {
//        //FIXME: write some actual code
//        return true;
//    }
}


package org.owasp.webgoat.plugin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import org.apache.commons.exec.OS;
import org.apache.commons.io.IOUtils;
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
import org.springframework.util.StringUtils;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/***************************************************************************************************
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 * <p>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p>
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation; either version 2 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License along with this program; if
 * not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 * <p>
 * Getting Source ==============
 * <p>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository for free software
 * projects.
 * <p>
 * For details, please see http://webgoat.github.io
 */

public class XXE extends LessonAdapter {

    private final static Integer DEFAULT_RANKING = new Integer(30);
    private final static String TRAVEL_FROM = "travelFrom";

    private final static String[] DEFAULT_LINUX_DIRECTORIES = {"usr", "opt", "var"};
    private final static String[] DEFAULT_WINDOWS_DIRECTORIES = {"Windows", "Program Files (x86)", "Program Files"};
    private FlightsRepo flights;


    private SearchForm parseXml(String xml) throws Exception {
        JAXBContext jc = JAXBContext.newInstance(SearchForm.class);

        XMLInputFactory xif = XMLInputFactory.newFactory();
        xif.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, true);
        xif.setProperty(XMLInputFactory.SUPPORT_DTD, true);
        XMLStreamReader xsr = xif.createXMLStreamReader(new StringReader(xml));

        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (SearchForm) unmarshaller.unmarshal(xsr);
    }

    public void handleRequest(WebSession s) {
        this.flights = new FlightsRepo(new File(LessonUtil.getLessonDirectory(s, this), "/csv/flights.txt"));

        try {
            byte[] rawData = IOUtils.toByteArray(s.getRequest().getReader());
            String data = new String(rawData);
            if (StringUtils.hasText(data)) {
                SearchForm searchForm = parseXml(data);
                boolean successful = checkSolution(searchForm, s);
                returnSearchResults(searchForm, s, successful);
            }
        } catch (Exception e) {
            //ignore
        }

        Form form = new Form(getFormAction(), Form.POST).setName("form").setEncType("");
        form.setOnSubmit("getFlights('" + LessonUtil.getXHRLink(s, this) + "');");

        form.addElement(createContent(s));

        setContent(form);

    }

    private void returnSearchResults(SearchForm searchForm, WebSession s, boolean successful) throws IOException {
        List<Flight> foundFlights = flights.searchFlight(searchForm.getFrom());
        Map json = Maps.newHashMap();
        //workaround to get the success message displayed this will be part of WebGoat 8 to fix this
        json.put("successful", successful);

        if (foundFlights != null) {
            s.getResponse().setContentType("text/html");
            s.getResponse().setHeader("Cache-Control", "no-cache");
            PrintWriter out = new PrintWriter(s.getResponse().getOutputStream());
            ObjectMapper mapper = new ObjectMapper();
            json.put("searchCriteria", searchForm.getFrom());
            json.put("flights", foundFlights);
            out.print(mapper.writeValueAsString(json));
            out.flush();
            out.close();
        }
    }

    private boolean checkSolution(SearchForm searchForm, WebSession s) {
        String[] directoriesToCheck = OS.isFamilyUnix() ? DEFAULT_LINUX_DIRECTORIES : DEFAULT_WINDOWS_DIRECTORIES;
        boolean success = true;
        for (String directory : directoriesToCheck) {
            success &= searchForm.getFrom().contains(directory);
        }
        if (success) {
            makeSuccess(s);
            return true;
        }
        return false;
    }

    protected Element createContent(WebSession s) {

        ElementContainer ec = new ElementContainer();
        ec.addElement(new Script().setSrc(LessonUtil.buildJsPath(s, this, "xxe.js")));

        Table t1 = new Table().setCellSpacing(0).setCellPadding(0).setBorder(0).setWidth("90%");
        TR tr = new TR();
        tr.addElement(new TD("From: ").setWidth("10%"));
        Input in = new Input(Input.TEXT, TRAVEL_FROM, "");
        in.addAttribute("onkeyup", "return getFlights('" + LessonUtil.getXHRLink(s, this) + "');");
        in.addAttribute("id", TRAVEL_FROM);
        tr.addElement(new TD(in).setWidth("90%"));

        t1.addElement(tr);

        ec.addElement(t1);

        ec.addElement(new BR());
        ec.addElement(new BR());
        Div div = new Div();
        div.addAttribute("name", "flightsDiv");
        div.addAttribute("id", "flightsDiv");
        ec.addElement(div);

        return ec;
    }

    protected Category getDefaultCategory() {
        return Category.PARAMETER_TAMPERING;
    }

    protected List<String> getHints(WebSession s) {
        List<String> hints = new ArrayList<String>();
        hints.add("Try searching with BOS, SFO or OAK");
        hints.add("XXE stands for XML External Entity attack");
        hints.add("Look at the search form when you submit");
        hints.add("Try to include your own DTD");
        return hints;
    }

    protected Integer getDefaultRanking() {
        return DEFAULT_RANKING;
    }

    public String getTitle() {
        return ("XML External Entity (XXE)");
    }

}

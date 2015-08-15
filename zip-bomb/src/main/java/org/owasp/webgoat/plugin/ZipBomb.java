package org.owasp.webgoat.plugin;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ecs.Element;
import org.apache.ecs.ElementContainer;
import org.apache.ecs.html.Form;
import org.apache.ecs.html.Input;
import org.apache.ecs.html.P;
import org.owasp.webgoat.lessons.Category;
import org.owasp.webgoat.lessons.LessonAdapter;
import org.owasp.webgoat.session.ECSFactory;
import org.owasp.webgoat.session.WebSession;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

/**
 * ****************************************************************************
 * <p/>
 * <p/>
 * This file is part of WebGoat, an Open Web Application Security Project
 * utility. For details, please see http://www.owasp.org/
 * <p/>
 * Copyright (c) 2002 - 20014 Bruce Mayhew
 * <p/>
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * <p/>
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * <p/>
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * <p/>
 * Getting Source ==============
 * <p/>
 * Source for this application is maintained at https://github.com/WebGoat/WebGoat, a repository
 * for free software projects.
 * <p/>
 * For details, please see http://webgoat.github.io
 *
 * @author Jakub Koperwas of <a href="http://www.sages.com.pl">Sages</a>
 * @created October 31, 2014
 */

public class ZipBomb extends LessonAdapter {

    protected Element createContent(WebSession s) {


        ElementContainer ec = new ElementContainer();


        if ("success".equalsIgnoreCase((String) s.get(ZIP_DOS))) {
            System.out.println("final success");
            makeSuccess(s);
        }
        try {

            ec.addElement(new P().addElement("Upload new File"));

            Input input = new Input(Input.FILE, "myfile", "");
            ec.addElement(input);

            Element b = ECSFactory.makeButton("Start Upload");
            ec.addElement(b);


        } catch (Exception e) {
            s.setMessage("Error generating " + this.getClass().getName());
            e.printStackTrace();
        }

        return ec;
    }

    protected Category getDefaultCategory() {
        return Category.DOS;
    }


    public List<String> getHints(WebSession s) {
        List<String> hints = new ArrayList<String>();
        hints.add("You can upload up to 2MB file at once,see what can you insert INTO the file");
        return hints;
    }

    public String getInstructions(WebSession s) {
        return "Server accepts only ZIP files, \n"
                + "extracts them after uploading, does something with them and deletes,"
                + "\n it provides 20 MB temporal storage to handle all request \n"
                + "try do perform DOS attack that consume all  temporal storage with one request";
    }

    private final static Integer DEFAULT_RANKING = new Integer(10);
    private static final String ZIP_DOS = "ZIP_DOS";

    protected Integer getDefaultRanking() {
        return DEFAULT_RANKING;
    }


    public String getTitle() {
        return ("ZipBomb");
    }

    public void handleRequest(WebSession s) {
        File tmpDir = (File) s.getRequest().getServletContext().getAttribute("javax.servlet.context.tempdir");

        try {
            if (ServletFileUpload.isMultipartContent(s.getRequest())) {

                DiskFileItemFactory factory = new DiskFileItemFactory();
                factory.setSizeThreshold(500000);

                ServletFileUpload upload = new ServletFileUpload(factory);


                List /* FileItem */items = upload.parseRequest(s.getRequest());


                java.util.Iterator iter = items.iterator();
                while (iter.hasNext()) {
                    FileItem item = (FileItem) iter.next();

                    if (!item.isFormField()) {

                        File uploadedFile = new File(tmpDir, item.getName());

                        if (item.getSize() < 2000 * 1024) {
                            if (item.getName().endsWith(".zip")) {
                                item.write(uploadedFile);

                                long total = unzippedSize(uploadedFile);
                                s.setMessage("File uploaded");
                                if (total > 20 * 1024 * 1024) {
                                    s.add(ZIP_DOS, "success");
                                    System.out.println("success");
                                    makeMessages(s);
                                } else {
                                    s.setMessage("I still have plenty of free storage on the server...");
                                }

                            } else {
                                s.setMessage("Only ZIP files are accepted");
                            }
                        } else {
                            s.setMessage("Only up to 2 MB files are accepted");
                        }
                    }
                }

            }
            Form form = new Form(getFormAction(), Form.POST).setName("form")
                    .setEncType("multipart/form-data");

            form.addElement(createContent(s));

            setContent(form);

        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }

    private long unzippedSize(File uploadedFile) throws ZipException,
            IOException {
        ZipFile zf = new ZipFile(uploadedFile);

        long total = 0;
        Enumeration e = zf.entries();
        while (e.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) e.nextElement();

            total += entry.getSize();

        }
        return total;
    }


}

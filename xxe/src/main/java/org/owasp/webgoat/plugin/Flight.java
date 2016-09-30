package org.owasp.webgoat.plugin;

/**
 * ************************************************************************************************
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
 *
 * @author WebGoat
 * @version $Id: $Id
 * @since September 25, 2016
 */
public class Flight {

    private String price;
    private String destination;
    private String arrivalDate;
    private String departureDate;
    private String departureFrom;

    public Flight(String price, String destination, String departureDate, String arrivalDate, String departureFrom) {
        this.price = price;
        this.destination = destination;
        this.arrivalDate = arrivalDate;
        this.departureDate = departureDate;
        this.departureFrom = departureFrom;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getDepartureFrom() {
        return this.departureFrom;
    }

    public String getPrice() {
        return price;
    }

    public String getArrivalDate() {
        return arrivalDate;
    }

    public String getDepartureDate() {
        return departureDate;
    }
}

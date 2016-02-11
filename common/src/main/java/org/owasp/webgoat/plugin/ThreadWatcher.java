package org.owasp.webgoat.plugin;

import java.util.BitSet;


/**
 *************************************************************************************************
 *
 *
 * This file is part of WebGoat, an Open Web Application Security Project utility. For details,
 * please see http://www.owasp.org/
 *
 * Copyright (c) 2002 - 20014 Bruce Mayhew
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
 * @author jwilliams@aspectsecurity.com
 * @since November 6, 2002
 * @version $Id: $Id
 */
public class ThreadWatcher implements Runnable
{

    // time to live in milliseconds
    private BitSet myInterrupted;

    private Process myProcess;

    private int myTimeout;

    /**
     * Constructor for the ThreadWatcher object
     *
     * @param p
     *            Description of the Parameter
     * @param interrupted
     *            Description of the Parameter
     * @param timeout
     *            Description of the Parameter
     */
    public ThreadWatcher(Process p, BitSet interrupted, int timeout)
    {
        myProcess = p;

        // thread used by whoever constructed this watcher
        myTimeout = timeout;
        myInterrupted = interrupted;
    }

	/*
	 * Interrupt the thread by marking the interrupted bit and killing the process
	 */

    /**
     * Description of the Method
     */
    public void interrupt()
    {
        myInterrupted.set(0);

        // set interrupted bit (bit 0 of the bitset) to 1
        myProcess.destroy();

		/*
		 * try { myProcess.getInputStream().close(); } catch( IOException e1 ) { / do nothing --
		 * input streams are probably already closed } try { myProcess.getErrorStream().close(); }
		 * catch( IOException e2 ) { / do nothing -- input streams are probably already closed }
		 * myThread.interrupt();
		 */
    }

    /**
     * Main processing method for the ThreadWatcher object
     */
    public void run()
    {
        try
        {
            Thread.sleep(myTimeout);
        } catch (InterruptedException e)
        {
            // do nothing -- if watcher is interrupted, so is thread
        }

        interrupt();
    }
}
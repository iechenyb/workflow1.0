package com.cyb.listener;

import java.io.File;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.cyb.Constants;
import com.cyb.h2.H2Manager;

/**
 * Application Lifecycle Listener implementation class JbpmListener
 *
 */
public class JbpmListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public JbpmListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent sce) {
    	H2Manager.initServer();
		H2Manager.start();
    	String webPath = sce.getServletContext().getRealPath("/");
		if(webPath.charAt(webPath.length()-1)!=File.separator.charAt(0)){
			webPath = webPath + File.separator;
		}
		Constants.WEBPATH = webPath;
		System.out.println("Ó¦ÓÃÂ·¾¶£º"+Constants.WEBPATH);
    }
	
}

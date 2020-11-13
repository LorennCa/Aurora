/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.teseo.logging;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class AuthLogger
{    
    private static AuthLogger instance;    
    private static final Logger LOGGER = Logger.getLogger("teseoLogger");  
    private static FileHandler fh;  
    
    private AuthLogger()
    {
        try
        {
            // This block configure the logger with handler and formatter  
            fh = new FileHandler("optimus.log");  
            LOGGER.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();  
            fh.setFormatter(formatter);

        } catch (SecurityException | IOException e) {  
            logError(getClass(),"Security or IO exception: " + e.getMessage(), null);
        }
    }
    
    public static AuthLogger getInstance()
    {
        if(instance == null)
            instance = new AuthLogger();
        return instance;
    }
    
    public static void logInfo(Object infoMsg)
    {
        LOGGER.info(infoMsg.toString());
    }
    
    public static void logError(Class clazz, Object infoMsg, String method)
    {
        if(method != null)
            LOGGER.log(Level.SEVERE, clazz.getName() + " -- " + " method:\n"+ method + "\n" +infoMsg.toString());
    }
}
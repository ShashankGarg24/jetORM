package org.jetORM.config;

import java.util.logging.Logger;

public class DbLogger {

    private static DbLogger instance;
    private Logger logger;

    private DbLogger (){
        logger = Logger.getLogger("DbLogger");
    }

    public static DbLogger getInstance(){
        if(instance == null){
            synchronized (DbLogger.class){
                instance = new DbLogger();
            }
        }
        return instance;
    }

    public void trace(String message){
        logger.finest(message);
    }

    public void debug(String message){
        logger.fine(message);
    }

    public void info(String message){
        logger.info(message);
    }

    public void warn(String message){
        logger.warning(message);
    }

    public void error(String message){
        logger.severe(message);
    }
}

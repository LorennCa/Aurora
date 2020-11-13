package co.teseo.config.appconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4J2PropertiesConf {

	private static Logger logger = LogManager.getLogger();

	public void performSomeTask() {
		logger.debug("This is a debbug message");
		logger.info("This is an info Message");
		logger.warn("This is a warn message");
		logger.error("This is a error message");
		logger.fatal("This is a tafal message");
	}

}

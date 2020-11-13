package co.ias.teseo;

import org.junit.Test;

import co.teseo.config.appconfig.Log4J2PropertiesConf;

public class Log4J2PropertiesConfTest {
	
	@Test
	public void testPerformsomTask() throws Exception {
		Log4J2PropertiesConf log = new Log4J2PropertiesConf();
		log.performSomeTask();
	}

}

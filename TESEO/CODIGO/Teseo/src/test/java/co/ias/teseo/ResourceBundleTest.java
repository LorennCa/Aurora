package co.ias.teseo;

import org.junit.Test;
import org.springframework.context.MessageSource;

import co.teseo.config.appconfig.MessageSourceConfig;

import java.util.Locale;


public class ResourceBundleTest {
	
	@Test
	public void testMessageResourceBundle(){
		MessageSource mrc = MessageSourceConfig.messageSource();
		System.out.println(mrc.getMessage("teseo.security.autenticacion.token.error",null,Locale.getDefault()));
	}
}

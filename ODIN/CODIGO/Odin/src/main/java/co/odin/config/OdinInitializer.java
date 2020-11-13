package co.odin.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class OdinInitializer  extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class[] { OdinApplication.class };
	}
 
	@Override
	protected Class<?>[] getServletConfigClasses() {
		return null;
	}
 
	@Override
	protected String[] getServletMappings() {
		return new String[] { "/" };
	}
}
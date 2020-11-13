package co.aurora.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class AuroraInitializer  extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class[] { AuroraApplication.class };
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
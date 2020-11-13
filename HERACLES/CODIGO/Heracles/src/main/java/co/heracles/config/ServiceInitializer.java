package co.heracles.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class ServiceInitializer  extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class[] { HeraclesApplication.class };
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
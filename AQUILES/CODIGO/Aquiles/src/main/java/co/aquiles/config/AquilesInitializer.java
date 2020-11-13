package co.aquiles.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;


public class AquilesInitializer  extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class[] { AquilesApplication.class };
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
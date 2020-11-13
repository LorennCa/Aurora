package co.perseo.appMain;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class PerseoInitializer  extends AbstractAnnotationConfigDispatcherServletInitializer {
    @Override
	protected Class<?>[] getRootConfigClasses() {
		
		return new Class[] { PerseoApplication.class };
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
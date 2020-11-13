package co.ias.teseo;

import org.junit.Test;

import co.teseo.exception.EEException;
import co.teseo.utils.AppConstants;
import co.teseo.utils.PasswordEncoder;

public class ShaPasswordEncoderTest {

	
	@Test
	public void testing() throws EEException{
		System.out.println(PasswordEncoder.encodePassword("", AppConstants.General.SALT));
		System.out.println(PasswordEncoder.encodePassword("Q1w2e3r4*", AppConstants.General.SALT));
		System.out.println(PasswordEncoder.encodePassword("xfiles", AppConstants.General.SALT));
		System.out.println(PasswordEncoder.encodePassword("xfiles", AppConstants.General.SALT));
		System.out.println(PasswordEncoder.isPasswordValid("xOOSCPxTKG/KAZctdjnp+g==","xfiles", AppConstants.General.SALT));
	}
	
}

package co.odin.services.impl;

import java.util.Calendar;
import java.util.Date;
import javax.mail.MessagingException;
import javax.mail.Transport;

import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import co.common.entities.DynamicLink;
import co.common.response.RResponse;
import co.common.response.Response;
import co.odin.config.appconfig.MessageSourceConfig;
import co.odin.repositories.PasswordResetRepository;
import co.odin.services.PasswordResetService;
import co.odin.utils.AppConstants;
import co.odin.utils.HermesMachine;


@Component
public class PasswordResetServiceImpl implements PasswordResetService {
	private HermesMachine hermes = HermesMachine.instance();
	private static final Logger logger = LogManager.getLogger(PasswordResetServiceImpl.class);
		
	/**
	 * variable de acceso a los recursos Bundle de Mensajes de la aplciación.
	 */
	private MessageSource mrc = MessageSourceConfig.messageSource();
	@Autowired
	private PasswordResetRepository presrepo;
//	@Autowired
//	private MailUtilService mailUtil ;

	@Override
	public Response resetUserPassword(String userLogin, String email) {
		/** Guardar el evento de reinicialización de contraseña en BD **/
		DynamicLink din = presrepo.findByUserLogin(userLogin);
		
		if(din == null){
			//RResponse emailResponse = mailUtil.createEmail(userLogin, email);
			RResponse emailResponse = hermes.createEmail(userLogin, email);
			String subjectDesc = "Reinicialización de contraseña iniciada para usuario " + userLogin;
			presrepo.save(new DynamicLink(userLogin, getExpireTime(), false));
			logger.info(subjectDesc);
			try {
				Transport.send(emailResponse.getEmailMessage());
				String resMes = "odin.resetPassword.sucessReset.SUCESS";				
				return new Response(org.springframework.http.HttpStatus.OK.value(), resMes, null, emailResponse.getTemporalPassword());				
			} catch (MessagingException e) {
				String errorRes = "Excepción enviando correo de reinicialización de contraseña"
						          .concat(e.getLocalizedMessage());
				e.printStackTrace();
				logger.error(errorRes);
				return new Response(3,errorRes,null,null);
			}
		}
		return new Response(HttpStatus.SC_CREATED, mrc.getMessage("odin.resetPassword.existsReset.ERROR", null, null) , null, null);
	}

	private Date getExpireTime() {
		Calendar cal = Calendar.getInstance();		
        cal.add(AppConstants.DYNAMIC_LINK_EXPIRE_FORMAT, hermes.getSeeConfig().getTiempo_expiracion_clave_temporal());
		return cal.getTime();
	}

	@Override
	public Response resetPassword(String hashId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSEE_AplicationSiteURL() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public DynamicLink findPasswordReset(String userLogin) {
		return presrepo.findByUserLogin(userLogin);
	}

	@Override
	public void deletePasswordReset(String userLogin) {
		presrepo.deleteByUserLogin(userLogin);
	}

	@Override
	public void useAndExpirePassword(DynamicLink din) {
		// TODO Auto-generated method stub
		
	}
}
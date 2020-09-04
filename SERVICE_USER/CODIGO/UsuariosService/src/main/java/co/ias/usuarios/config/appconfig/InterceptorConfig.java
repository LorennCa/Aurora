package co.ias.usuarios.config.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import co.ias.usuarios.restapi.interceptors.ApiCallInterceptor;
import co.ias.usuarios.utils.AppConstants;
import co.ias.usuarios.utils.AppPaths;

@Configuration
@EnableWebMvc
public class InterceptorConfig extends WebMvcConfigurerAdapter{

	@Bean
	public ApiCallInterceptor getAuthenticationInterceptor() {
		return new ApiCallInterceptor();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(getAuthenticationInterceptor())
				.addPathPatterns("/**")
				.excludePathPatterns(
						AppPaths.IAS_APP_CONF_ABSOLUTE_PATH,
						AppPaths.IAS_APP_CONF_PATH_GW,
						AppConstants.KERBEROS_LOGIN_PATH_GW,
						AppConstants.KERBEROS_LOGIN_PATH,
						AppConstants.KERBEROS_LOGOUT_PATH,
						AppConstants.USER_IN_SESSION_RESOURCE_PATH,
						AppPaths.Security.IAS_SECUTIRY_APP_SWAGGER_PATH,
						AppPaths.Security.RESET_PASS_CONTEXT,
						"/ias/v1/leto/**",
						AppConstants.KERBEROS_LOGIN_PATH,
						AppConstants.IAS_SECUTIRY_APP_SWAGGER_PATH,
						AppConstants.IAS_RESET_PASS_PATH,
						AppConstants.LETO_PATH);
	}
}
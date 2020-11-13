package co.odin.config.appconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import co.odin.restapi.interceptors.ApiCallInterceptor;
import co.odin.utils.AppConstants;
import co.odin.utils.AppPaths;

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
						AppPaths.ODIN_APP_CONF_ABSOLUTE_PATH,
						AppPaths.ODIN_APP_CONF_PATH_GW,
						AppConstants.KERBEROS_LOGIN_PATH_GW,
						AppConstants.KERBEROS_LOGIN_PATH,
						AppConstants.KERBEROS_LOGOUT_PATH,
						AppConstants.USER_IN_SESSION_RESOURCE_PATH,
						AppPaths.Security.ODIN_SECUTIRY_APP_SWAGGER_PATH,
						AppPaths.Security.RESET_PASS_CONTEXT,
						"/Aurora/v1/leto/**",
						AppConstants.KERBEROS_LOGIN_PATH,
						AppConstants.ODIN_SECUTIRY_APP_SWAGGER_PATH,
						AppConstants.ODIN_RESET_PASS_PATH,
						AppConstants.LETO_PATH);
	}
}
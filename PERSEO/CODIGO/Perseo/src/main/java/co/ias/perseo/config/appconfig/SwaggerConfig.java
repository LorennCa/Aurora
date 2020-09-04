package co.ias.perseo.config.appconfig;

import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Enables Swagger
 */
@EnableSwagger2
@Configuration
public class SwaggerConfig {

	public Docket newsApi() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo());
	}

	public ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("IAS - Service Registry Application")
				.description("IAS - REST API documentation")
				.termsOfServiceUrl("https://localhost/ias")
				.version("1.0").build();
	}
}
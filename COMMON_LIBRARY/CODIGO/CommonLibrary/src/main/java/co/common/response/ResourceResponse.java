package co.common.response;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter 
public class ResourceResponse extends Response {

	private static ResourceResponse resourceResponse;

	private OtrasOpciones otrasOpciones;

	public ResourceResponse() {
	}

	public ResourceResponse(Integer code, String message, String accessToken,
			String resource, OtrasOpciones otrasOpciones) {
		super(code, message, accessToken, resource);
		this.otrasOpciones = otrasOpciones;
	}

	public static ResourceResponse getSingleInstance() {
		if(resourceResponse == null){
			resourceResponse = new ResourceResponse();
		}
		return resourceResponse;
	}


}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.odin.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Hernan_Quevedo
 */
import lombok.Getter;
import lombok.Setter;
@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Error")
public class UsuariosException extends RuntimeException
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4902123142142524807L;
	
	
	@Getter @Setter
    private String errorCode;
	@SuppressWarnings("unused")
	private Integer businessErrorCode;
	private HttpStatus status = HttpStatus.PRECONDITION_FAILED;
	
    public UsuariosException(Exception e){
        super(e);
    }

    public UsuariosException(String errorC) {
        super();
	errorCode = errorC;
    }
    
	public UsuariosException(String message, Integer businessErrorCode) {
		super(message);
		this.businessErrorCode = businessErrorCode;
	}
	
	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}
}
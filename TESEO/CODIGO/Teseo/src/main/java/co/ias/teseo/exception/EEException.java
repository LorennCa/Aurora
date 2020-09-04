/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.ias.teseo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Hernan_Quevedo
 */
import lombok.Getter;
import lombok.Setter;
@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Error")
public class EEException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4902123142142524807L;
	@Getter @Setter
    private String errorCode;
    public EEException(Exception e){
        super(e);
    }

    public EEException(String errorC) {
        super();
	errorCode = errorC;
    }
}
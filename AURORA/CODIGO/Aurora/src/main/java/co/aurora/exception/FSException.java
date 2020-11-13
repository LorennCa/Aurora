/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.aurora.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author Hernan_Quevedo
 */
import lombok.Getter;
import lombok.Setter;
@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR, reason="Internal Error")
public class FSException extends Exception
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 4902123142142524807L;
	@Getter @Setter
    private String errorCode;
    public FSException(Exception e){
        super(e);
    }

    public FSException(String errorC) {
        super();
	errorCode = errorC;
    }
}
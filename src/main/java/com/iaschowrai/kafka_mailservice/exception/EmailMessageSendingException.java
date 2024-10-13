package com.iaschowrai.kafka_mailservice.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
//@ResponseStatus annotation sets the HTTP status code to 500 INTERNAL_SERVER_ERROR when this exception is thrown.
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Email message sending failed")
public class EmailMessageSendingException  extends  RuntimeException{
    public EmailMessageSendingException(){

    }
    public EmailMessageSendingException(String message){
        super(message);
    }

    public EmailMessageSendingException(String message, Throwable cause){
        super(message,cause);
    }

    public EmailMessageSendingException(Throwable cause){
        super(cause);
    }
    public EmailMessageSendingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace){
        super(message, cause, enableSuppression, writableStackTrace);
    }

}

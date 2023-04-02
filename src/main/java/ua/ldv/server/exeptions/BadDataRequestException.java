package ua.ldv.server.exeptions;


public class BadDataRequestException extends RuntimeException{

    public BadDataRequestException(String message){
        super(message);
    }
}


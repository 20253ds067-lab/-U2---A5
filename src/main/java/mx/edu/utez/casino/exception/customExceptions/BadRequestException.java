package mx.edu.utez.casino.exception.customExceptions;

public class BadRequestException extends  RuntimeException {

    public BadRequestException(String message){
        super(message);
    }

}

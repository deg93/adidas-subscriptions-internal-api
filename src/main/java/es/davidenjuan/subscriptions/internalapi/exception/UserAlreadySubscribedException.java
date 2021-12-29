package es.davidenjuan.subscriptions.internalapi.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@SuppressWarnings("serial")
@ResponseStatus(code = HttpStatus.CONFLICT)
public class UserAlreadySubscribedException extends RuntimeException {

    public UserAlreadySubscribedException(String message) {
        super(message);
    }

}

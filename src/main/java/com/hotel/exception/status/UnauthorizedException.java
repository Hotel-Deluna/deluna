package com.hotel.exception.status;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedException extends ResponseStatusException {

    public UnauthorizedException() {
        super(HttpStatus.UNAUTHORIZED);
    }

    public UnauthorizedException(String reason) {
        super(HttpStatus.UNAUTHORIZED, reason);
    }

    public UnauthorizedException(String reason, Throwable cause) {
        super(HttpStatus.UNAUTHORIZED, reason, cause);
    }
}


package com.example.compute_service.exception;

public class PercentageNotAvailableException extends RuntimeException {
    public PercentageNotAvailableException(String message) {

        super(message);

    }



    public PercentageNotAvailableException(String message, Throwable cause) {

        super(message, cause);

    }
}

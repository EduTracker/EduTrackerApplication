package com.project.itmo2016.edutrackerapplication.loader;

/**
 * Kind of bad, incorrect or unexpected response from API.
 */
class BadResponseException extends Exception {

    BadResponseException(String message) {
        super(message);
    }

    BadResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    BadResponseException(Throwable cause) {
        super(cause);
    }
}

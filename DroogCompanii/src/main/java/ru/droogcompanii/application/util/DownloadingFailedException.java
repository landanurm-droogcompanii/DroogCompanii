package ru.droogcompanii.application.util;

/**
 * Created by Leonid on 08.03.14.
 */
public class DownloadingFailedException extends RuntimeException {

    public DownloadingFailedException() {
        super();
    }

    public DownloadingFailedException(String message) {
        super(message);
    }

    public DownloadingFailedException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public DownloadingFailedException(Throwable throwable) {
        super(throwable);
    }
}

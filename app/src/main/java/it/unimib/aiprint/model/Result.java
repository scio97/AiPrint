package it.unimib.aiprint.model;

public abstract class Result {
    private Result() {
    }

    public boolean isSuccess() {
        if (this instanceof Success) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class Success<T> extends Result {
        private final T resultResponse;

        public Success(T resultResponse) {
            this.resultResponse = resultResponse;
        }

        public T getData() {
            return resultResponse;
        }
    }

    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error<T> extends Result {
        private final String message;

        public Error(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}

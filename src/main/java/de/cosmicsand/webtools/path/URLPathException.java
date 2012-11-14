package de.cosmicsand.webtools.path;

public class URLPathException extends IllegalArgumentException {

    private static final long serialVersionUID = 3522825379432394824L;

    public URLPathException() {}

    public URLPathException(String s) {
        super(s);
    }

    public URLPathException(Throwable cause) {
        super(cause);
    }

    public URLPathException(String message, Throwable cause) {
        super(message, cause);
    }

}

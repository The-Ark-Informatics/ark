package neuragenix.security.exception;



public class SecurityException extends Exception {

    

    public SecurityException() {

    }

    

    

    public SecurityException(String msg) {

        super(msg);

    }



    public SecurityException(Exception e) {

        super(e);

    }

}


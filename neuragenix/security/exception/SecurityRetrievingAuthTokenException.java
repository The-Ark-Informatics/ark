package neuragenix.security.exception;



public class SecurityRetrievingAuthTokenException extends SecurityException

{

    

    public SecurityRetrievingAuthTokenException() {

    }

    

    

    public SecurityRetrievingAuthTokenException(String msg) {

        super(msg);

    }



    public SecurityRetrievingAuthTokenException(Exception e) {

        super(e);

    }

}


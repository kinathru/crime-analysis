package com.kinath.mis;

public class MappingException extends Exception
{
    public MappingException()
    {
    }

    public MappingException( String message )
    {
        super( message );
    }

    @Override public String getMessage()
    {
        return "Exception in mapping data : " + super.getMessage();
    }
}

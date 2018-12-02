package com.kinath.mis;

public class GeoInfoNameCode
{
    private String code;
    private String name;

    public GeoInfoNameCode( String code, String name )
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode( String code )
    {
        this.code = code;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @Override
    public boolean equals( Object obj )
    {
        if( obj instanceof GeoInfoNameCode )
        {
            return name.equals( ( (GeoInfoNameCode) obj ).getName() );
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "GeoInfoNameCode{" + "code='" + code + '\'' + ", name='" + name + '\'' + '}';
    }
}

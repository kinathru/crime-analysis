package com.kinath.mis.geostorage;

/**
 * Created By : Kinath
 * Date : 29/11/2018
 * Time : 12:04
 * <p>
 * Desc :  GeoInformation
 */
public class GeoInformation
{
    private double latitude;
    private double longitude;
    private String countryCode;
    private String country;
    private String state;
    private String city;
    private String displayName;

    public GeoInformation( double latitude, double longitude )
    {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude( double latitude )
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude( double longitude )
    {
        this.longitude = longitude;
    }

    public String getCountryCode()
    {
        return countryCode;
    }

    public void setCountryCode( String countryCode )
    {
        this.countryCode = countryCode;
    }

    public String getCountry()
    {
        return country;
    }

    public void setCountry( String country )
    {
        this.country = country;
    }

    public String getState()
    {
        return state;
    }

    public void setState( String state )
    {
        this.state = state;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity( String city )
    {
        this.city = city;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    @Override
    public boolean equals( Object o )
    {
        if( this == o )
        {
            return true;
        }
        if( o == null || getClass() != o.getClass() )
        {
            return false;
        }

        GeoInformation latLon = (GeoInformation) o;

        if( Double.compare( latLon.latitude, latitude ) != 0 )
        {
            return false;
        }
        return Double.compare( latLon.longitude, longitude ) == 0;
    }

    @Override
    public String toString()
    {
        return "GeoInformation{" + "latitude=" + latitude + ", longitude=" + longitude + ", countryCode='" + countryCode + '\'' + ", country='" + country + '\'' + ", state='" + state + '\'' + ", city='" + city + '\'' + ", displayName='" + displayName + '\'' + '}';
    }
}

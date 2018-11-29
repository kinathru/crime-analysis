package com.kinath.mis.geostorage;

import com.kinath.mis.geo.Address;
import com.kinath.mis.geo.NominatimReverseGeocodingJAPI;

import java.util.concurrent.Callable;

/**
 * Created By : Kinath
 * Date : 29/11/2018
 * Time : 13:09
 * <p>
 * Desc :  GeoInfoMappingTask
 */
public class GeoInfoMappingTask implements Callable<Boolean>
{
    private GeoInformation geoInformation;

    public GeoInfoMappingTask( GeoInformation geoInformation )
    {
        this.geoInformation = geoInformation;
    }

    @Override
    public Boolean call() throws Exception
    {
        NominatimReverseGeocodingJAPI nominatim1 = new NominatimReverseGeocodingJAPI();
        Address address = nominatim1.getAdress( geoInformation.getLatitude(), geoInformation.getLongitude() );
        geoInformation.setCountryCode( address.getCountryCode() );
        geoInformation.setCountry( address.getCountry() );
        geoInformation.setState( address.getState() );
        geoInformation.setCity( address.getCity() );
        geoInformation.setDisplayName( address.getDisplayName() );

        System.out.println( geoInformation.toString() );

        Thread.sleep( 1000 );

        return geoInformation.getCity() != null;
    }
}

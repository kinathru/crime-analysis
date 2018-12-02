package com.kinath.mis.geostorage;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeoInfoCache
{
    private static GeoInfoCache instance;
    private Map<String, String> geoInformationMap;

    public static GeoInfoCache getInstance()
    {
        if( instance == null )
        {
            instance = new GeoInfoCache();
        }
        return instance;
    }

    public GeoInfoCache()
    {
        List<GeoInformation> geoInformations = GeoCodeStorageUtil.readGeoInformationFromFile();
        geoInformationMap = geoInformations.stream().collect( Collectors.toMap( GeoInformation::getKey, GeoInformation::getDisplayName, ( oldValue, newValue ) -> oldValue ) );
    }

    public String getAddressOfLocation( double lat, double lon )
    {
        String key = lat + "," + lon;
        return geoInformationMap.get( key );
    }

    public Map<String, String> getGeoInformationMap()
    {
        return geoInformationMap;
    }
}

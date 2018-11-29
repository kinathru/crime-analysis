package com.kinath.mis;

import java.util.concurrent.Callable;

public class CityMappingTask implements Callable<Boolean>
{
    TaxiDataObject taxiData;

    public CityMappingTask( TaxiDataObject taxiData )
    {
        this.taxiData = taxiData;
    }

    @Override
    public Boolean call() throws Exception
    {
        ObjectMapper.mapCitiesToTaxiData( taxiData );
        return taxiData.getPickupCity() != null && taxiData.getDropOffCity() != null;
    }
}

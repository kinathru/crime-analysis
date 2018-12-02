package com.kinath.mis;

import com.kinath.mis.geostorage.GeoInfoCache;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.kinath.mis.Constants.*;

public class AnalyzeUtil
{
    public static void analyzeData() throws IOException, InvalidFormatException
    {
        org.apache.poi.ss.usermodel.Workbook complaintsWorkbook = WorkbookFactory.create( new File( COMPLAINTS_FILE ) );
        org.apache.poi.ss.usermodel.Workbook taxidataWorkbook = WorkbookFactory.create( new File( TAXI_DATA_FILE ) );

        GeoInfoCache geoInfoCache = GeoInfoCache.getInstance();

        Sheet complaintsSheet = complaintsWorkbook.getSheet( COMPLAINTS_SHEET );
        List<ComplainObject> complainList = ExcelReaderUtil.getComplainObjects( complaintsSheet );
        System.out.println( complainList.size() );

        Sheet taxiDataSheet = taxidataWorkbook.getSheet( TAXI_DATA_SHEET );
        List<TaxiDataObject> taxiDataList = ExcelReaderUtil.getTaxiDataObjects( taxiDataSheet );
        System.out.println( taxiDataList.size() );
        TaxiDataObject maxDurationTaxiObject = taxiDataList.stream().max( Comparator.comparing( TaxiDataObject::getDuration ) ).orElse( null );

        List<ComplainObject> complaintsWithinTime = new ArrayList<>();
        List<ComplainObject> complaintsWithinTimePlus30 = new ArrayList<>();
        List<ComplainObject> complaintsWithinTimePlus1h = new ArrayList<>();

        for( TaxiDataObject td : taxiDataList )
        {
            td.setPickupAddress( geoInfoCache.getAddressOfLocation( td.getPickupLat(), td.getPickupLon() ) );
            td.setDropOffAddress( geoInfoCache.getAddressOfLocation( td.getDropoffLat(), td.getDropoffLon() ) );
        }

        for( ComplainObject cmp : complainList )
        {
            cmp.setAddress( geoInfoCache.getAddressOfLocation( cmp.getLat(), cmp.getLon() ) );
        }

        Map<TaxiDataObject, List<ComplainObject>> taxDataComplainMap = new HashMap<>();

        for( TaxiDataObject taxi : taxiDataList )
        {
            //02.Complaints within pickup-time + 0.5h and drop-off time + 0.5h
            final LocalDateTime taxiPickupTimeMinus30 = taxi._getLocalPickupTime().plus( -30, ChronoUnit.MINUTES );
            final LocalDateTime taxiDropOffTimePlus30 = taxi._getLocalDropOffTime().plus( 30, ChronoUnit.MINUTES );
            List<ComplainObject> collect2 = complainList.stream()
                    .filter( cmp -> cmp._getLocalCmpFromTime().isAfter( taxiPickupTimeMinus30 ) && cmp._getLocalCmpToTime().isBefore( taxiDropOffTimePlus30 ) )
                    .filter( cmp -> ( cmp.getPostalCode().equals( taxi.getPickupPostalCode() ) || cmp.getPostalCode().equals( taxi.getDropOffPostalCode() ) ) )
                    .collect( Collectors.toList() );
            complaintsWithinTimePlus30.addAll( collect2 );

            if( !collect2.isEmpty() )
            {
                taxDataComplainMap.put( taxi, collect2 );
            }
        }

        System.out.println( "Within : " + complaintsWithinTime.size() + " - Within 30" + complaintsWithinTimePlus30.size() + " - Within 1h" + complaintsWithinTimePlus1h.size() );

    }
}

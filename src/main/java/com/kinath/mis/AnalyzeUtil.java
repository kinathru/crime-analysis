package com.kinath.mis;

import com.kinath.mis.geostorage.GeoInfoCache;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static com.kinath.mis.Constants.*;

public class AnalyzeUtil
{
    public static void analyzeData() throws IOException, InvalidFormatException
    {
        org.apache.poi.ss.usermodel.Workbook complaintsWorkbook = WorkbookFactory.create( new File( AnalyzeUtil.class.getClassLoader().getResource( COMPLAINTS_FILE ).getFile() ) );
        org.apache.poi.ss.usermodel.Workbook taxidataWorkbook = WorkbookFactory.create( new File( AnalyzeUtil.class.getClassLoader().getResource( TAXI_DATA_FILE ).getFile() ) );

        GeoInfoCache geoInfoCache = GeoInfoCache.getInstance();

        Sheet complaintsSheet = complaintsWorkbook.getSheet( COMPLAINTS_SHEET );
        List<ComplainObject> complainList = ExcelReaderUtil.getComplainObjects( complaintsSheet );
        System.out.println( complainList.size() );

        Sheet taxiDataSheet = taxidataWorkbook.getSheet( TAXI_DATA_SHEET );
        List<TaxiDataObject> taxiDataList = ExcelReaderUtil.getTaxiDataObjects( taxiDataSheet );
        System.out.println( taxiDataList.size() );

        List<ComplainObject> complaintsWithinTimePlus30 = new ArrayList<>();

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

        try (FileWriter file = new FileWriter( Constants.JSON_MAPPED_CRIMES ))
        {
            file.write( new JSONObject( taxDataComplainMap ).toString() );
            System.out.println( "Successfully Copied JSON Object to File..." );
        }
        System.out.println( taxDataComplainMap.size() );

        if( !taxDataComplainMap.isEmpty() )
        {
            Map<String, Map<String, Set<GeoInfoNameCode>>> postalCodeAddressMap = new LinkedHashMap<>();
            Map<Integer, String> sortedPostalCodeMap = new TreeMap<>();
            for( TaxiDataObject taxiKey : taxDataComplainMap.keySet() )
            {
                List<ComplainObject> complainObjects = taxDataComplainMap.get( taxiKey );
                for( ComplainObject cmp : complainObjects )
                {
                    postalCodeAddressMap.computeIfAbsent( cmp.getPostalCode(), k -> new HashMap<>() );
                    postalCodeAddressMap.get( cmp.getPostalCode() ).putIfAbsent( cmp.getOffenseCode(), new HashSet<>() );
                    postalCodeAddressMap.get( cmp.getPostalCode() ).get( cmp.getOffenseCode() ).add( new GeoInfoNameCode( cmp.getLatLon(), cmp.getAddress() ) );
                }
            }

            for( String pstCode : postalCodeAddressMap.keySet() )
            {
                Map<String, Set<GeoInfoNameCode>> map = postalCodeAddressMap.get( pstCode );
                int valueCount = 0;
                for( String key : map.keySet() )
                {
                    valueCount += map.get( key ).size();
                }
                sortedPostalCodeMap.put( valueCount, pstCode );
            }

            try (FileWriter file = new FileWriter( Constants.JSON_RESULTS_FILE ))
            {
                file.write( new JSONObject( postalCodeAddressMap ).toString() );
                System.out.println( "Successfully Copied JSON Object to File..." );
            }

            System.out.println();
        }
    }
}

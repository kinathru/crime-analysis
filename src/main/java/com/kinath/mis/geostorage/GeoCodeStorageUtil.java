package com.kinath.mis.geostorage;

import com.kinath.mis.ComplainObject;
import com.kinath.mis.ExcelReaderUtil;
import com.kinath.mis.TaxiDataObject;
import com.kinath.mis.geo.Address;
import com.kinath.mis.geo.NominatimReverseGeocodingJAPI;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static com.kinath.mis.Constants.*;

public class GeoCodeStorageUtil
{
    private static final String GEO_STORAGE_FILE = "GeoInformation.csv";

    public static void storeGeoCordinatesToAddresses() throws IOException, InvalidFormatException
    {
        org.apache.poi.ss.usermodel.Workbook complaintsWorkbook = WorkbookFactory.create( new File( COMPLAINTS_FILE ) );
        org.apache.poi.ss.usermodel.Workbook taxidataWorkbook = WorkbookFactory.create( new File( TAXI_DATA_FILE ) );

        Sheet complaintsSheet = complaintsWorkbook.getSheet( COMPLAINTS_SHEET );
        List<ComplainObject> complainList = ExcelReaderUtil.getComplainObjects( complaintsSheet );
        System.out.println( complainList.size() );

        Sheet taxiDataSheet = taxidataWorkbook.getSheet( TAXI_DATA_SHEET );
        List<TaxiDataObject> taxiDataList = ExcelReaderUtil.getTaxiDataObjects( taxiDataSheet );
        System.out.println( taxiDataList.size() );

        List<GeoInformation> geoInformationList = readGeoInformationFromFile();
        addNewGeoInfoFromData( complainList, taxiDataList, geoInformationList );
        System.out.println( "Size of Pending Entries : " + geoInformationList.size() );

        printIndividualRecords( geoInformationList );
    }

    private static void printIndividualRecords( List<GeoInformation> geoInformationList ) throws IOException
    {
        BufferedWriter writer = Files.newBufferedWriter( Paths.get( GEO_STORAGE_FILE ), StandardOpenOption.APPEND );
        CSVPrinter csvPrinter = new CSVPrinter( writer, CSVFormat.DEFAULT.withHeader( "LAT", "LON", "CountryCode", "Country", "State", "City", "DisplayName" ).withFirstRecordAsHeader() );
        try
        {
            for( GeoInformation geoInformation : geoInformationList )
            {
                NominatimReverseGeocodingJAPI nominatim1 = new NominatimReverseGeocodingJAPI();
                Address address = nominatim1.getAdress( geoInformation.getLatitude(), geoInformation.getLongitude() );
                geoInformation.setCountryCode( address.getCountryCode() );
                geoInformation.setCountry( address.getCountry() );
                geoInformation.setState( address.getState() );
                geoInformation.setCity( address.getCity() );
                geoInformation.setDisplayName( address.getDisplayName() );

                System.out.println( geoInformation.toString() );
                csvPrinter.printRecord( geoInformation.getLatitude(), geoInformation.getLongitude(), geoInformation.getCountryCode(), geoInformation.getCountry(), geoInformation.getState(), geoInformation.getCity(), geoInformation.getDisplayName() );
            }
        }
        catch( Exception ex )
        {
            System.out.println( ex.getMessage() );
        }
        finally
        {
            csvPrinter.flush();
        }
    }

    public static List<GeoInformation> readGeoInformationFromFile()
    {
        List<GeoInformation> geoInformationList = new ArrayList<>();
        try (Reader reader = Files.newBufferedReader( Paths.get( GEO_STORAGE_FILE ) );
             CSVParser csvParser = new CSVParser( reader, CSVFormat.DEFAULT.withHeader( "LAT", "LON", "CountryCode", "Country", "State", "City", "DisplayName" ).withFirstRecordAsHeader().withTrim() );)
        {
            for( CSVRecord csvRecord : csvParser )
            {
                try
                {
                    // Accessing values by the names assigned to each column
                    double lat = Double.parseDouble( csvRecord.get( "LAT" ) );
                    double lon = Double.parseDouble( csvRecord.get( "LON" ) );
                    String countryCode = csvRecord.get( "CountryCode" );
                    String country = csvRecord.get( "Country" );
                    String state = csvRecord.get( "State" );
                    String city = csvRecord.get( "City" );
                    String displayName = csvRecord.get( "DisplayName" );

                    GeoInformation geoInformation = new GeoInformation( lat, lon );
                    geoInformation.setCountryCode( countryCode );
                    geoInformation.setCountry( country );
                    geoInformation.setState( state );
                    geoInformation.setCity( city );
                    geoInformation.setDisplayName( displayName );

                    geoInformationList.add( geoInformation );
                }
                catch( NumberFormatException nex )
                {
                    System.out.println( nex.getMessage() );
                }
            }
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }

        return geoInformationList;
    }

    private static List<GeoInformation> addNewGeoInfoFromData( List<ComplainObject> complainList, List<TaxiDataObject> taxiDataList, List<GeoInformation> geoInformationList )
    {
        for( ComplainObject cmp : complainList )
        {
            GeoInformation geoInformation = new GeoInformation( cmp.getLat(), cmp.getLon() );
            if( !geoInformationList.stream().anyMatch( gi -> gi.equals( geoInformation ) ) )
            {
                geoInformationList.add( geoInformation );
            }
        }

        for( TaxiDataObject taxi : taxiDataList )
        {
            GeoInformation pickupGeo = new GeoInformation( taxi.getPickupLat(), taxi.getPickupLon() );
            if( !geoInformationList.stream().anyMatch( gi -> gi.equals( pickupGeo ) ) )
            {
                geoInformationList.add( pickupGeo );
            }

            GeoInformation dropOffGeo = new GeoInformation( taxi.getDropoffLat(), taxi.getDropoffLon() );
            if( !geoInformationList.stream().anyMatch( gi -> gi.equals( dropOffGeo ) ) )
            {
                geoInformationList.add( dropOffGeo );
            }
        }
        return geoInformationList;
    }

    private static void mapGeoInformationParallel( List<GeoInformation> geoInformationList )
    {
        ExecutorService executor = Executors.newFixedThreadPool( 20 );
        List<GeoInfoMappingTask> addressMappingTasks = geoInformationList.stream().map( geo -> new GeoInfoMappingTask( geo ) ).collect( Collectors.toList() );
        try
        {
            executor.invokeAll( addressMappingTasks ).stream().map( future -> {
                try
                {
                    return future.get();
                }
                catch( Exception e )
                {
                    executor.shutdownNow();
                    throw new IllegalStateException( e );
                }
            } ).forEach( System.out::println );
        }
        catch( InterruptedException e )
        {
            System.out.println( e.getMessage() );
        }

        long mappedCount = geoInformationList.stream().filter( taxi -> taxi.getCity() != null && taxi.getCity() != null ).count();
        System.out.println( "Objects mapped with cities : " + mappedCount );
    }

    public static void main( String[] args )
    {
        try
        {
            storeGeoCordinatesToAddresses();
        }
        catch( IOException e )
        {
            e.printStackTrace();
        }
        catch( InvalidFormatException e )
        {
            e.printStackTrace();
        }
    }
}

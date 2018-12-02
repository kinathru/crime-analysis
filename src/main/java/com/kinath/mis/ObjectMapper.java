package com.kinath.mis;

import com.kinath.mis.geo.Address;
import com.kinath.mis.geo.NominatimReverseGeocodingJAPI;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;

import java.text.ParseException;

public class ObjectMapper
{
    public static void mapComplainObject( ComplainObject complain, Row row ) throws MappingException
    {
        try
        {
            Cell cmpFromDateTimeCell = row.getCell( 3 );
            Cell cmpToDateTimeCell = row.getCell( 6 );
            Cell reportDateCell = row.getCell( 7 );
            Cell offenseCodeCell = row.getCell( 9 );
            Cell crimeStatusCell = row.getCell( 12 );
            Cell locationCityCell = row.getCell( 15 );
            Cell locationCell = row.getCell( 17 );
            Cell premiseCell = row.getCell( 18 );
            Cell xCordCell = row.getCell( 21 );
            Cell yCordCell = row.getCell( 22 );
            Cell latCell = row.getCell( 23 );
            Cell lonCell = row.getCell( 24 );

            complain.setComplainFromTime( Constants.SIMPLE_DATE_TIME_FORMAT.parse( cmpFromDateTimeCell.getStringCellValue() ) );
            complain.setComplainToTime( Constants.SIMPLE_DATE_TIME_FORMAT.parse( cmpToDateTimeCell.getStringCellValue() ) );
            complain.setComplainReportTime( reportDateCell.getDateCellValue() );
            complain.setOffenseCode( offenseCodeCell.getStringCellValue() );
            complain.setCrimeStatus( crimeStatusCell.getStringCellValue() );
            complain.setLocationCity( locationCityCell.getStringCellValue() );
            complain.setLocationDesc( locationCell.getStringCellValue() );
            complain.setPremiseDesc( premiseCell.getStringCellValue() );

            if( latCell == null )
            {
                throw new MappingException( "Latitude is null " );
            }
            complain.setLat( latCell.getNumericCellValue() );

            if( lonCell == null )
            {
                throw new MappingException( "Longitude is null " );
            }
            complain.setLon( lonCell.getNumericCellValue() );
            complain.setxCord( xCordCell.getNumericCellValue() );
            complain.setyCord( yCordCell.getNumericCellValue() );
        }
        catch( ParseException pex )
        {
            throw new MappingException( pex.getMessage() );
        }
        catch( MappingException mex )
        {
            throw mex;
        }
        catch( Exception ex )
        {
            throw new MappingException( ex.getMessage() );
        }
    }

    public static void mapTaxiDataObject( TaxiDataObject taxiData, Row row ) throws MappingException
    {
        try
        {
            Cell pickupDateTimeCell = row.getCell( 1 );
            Cell dropOffDateTimeCell = row.getCell( 2 );
            Cell pickupLonCell = row.getCell( 5 );
            Cell pickupLatCell = row.getCell( 6 );
            Cell dropLonCell = row.getCell( 7 );
            Cell dropLatCell = row.getCell( 8 );

            taxiData.setPickupTime( pickupDateTimeCell.getDateCellValue() );
            taxiData.setDropOffTime( dropOffDateTimeCell.getDateCellValue() );
            taxiData.setPickupLat( pickupLatCell.getNumericCellValue() );
            taxiData.setPickupLon( pickupLonCell.getNumericCellValue() );
            taxiData.setDropoffLat( dropLatCell.getNumericCellValue() );
            taxiData.setDropoffLon( dropLonCell.getNumericCellValue() );
        }
        catch( Exception ex )
        {
            throw new MappingException( ex.getMessage() );
        }
    }

    public static String getPostalCode(String address)
    {
        String[] split = address.split( "," );
        int length = split.length;
        String pcStr = length > 2 ? split[length - 2] : null;
        if( pcStr != null )
        {
            return pcStr.trim();
        }
        return "";
    }
}

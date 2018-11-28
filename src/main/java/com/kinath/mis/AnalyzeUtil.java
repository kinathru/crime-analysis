package com.kinath.mis;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static com.kinath.mis.Constants.*;

public class AnalyzeUtil
{
    public static void analyzeData() throws IOException, InvalidFormatException
    {
        org.apache.poi.ss.usermodel.Workbook complaintsWorkbook = WorkbookFactory.create( new File( COMPLAINTS_FILE ) );
        org.apache.poi.ss.usermodel.Workbook taxidataWorkbook = WorkbookFactory.create( new File( TAXI_DATA_FILE ) );

        Sheet complaintsSheet = complaintsWorkbook.getSheet( COMPLAINTS_SHEET );
        List<ComplainObject> complainList = ExcelReaderUtil.getComplainObjects( complaintsSheet );
        System.out.println( complainList.size() );

        Sheet taxiDataSheet = taxidataWorkbook.getSheet( TAXI_DATA_SHEET );
        List<TaxiDataObject> taxiDataList = ExcelReaderUtil.getTaxiDataObjects( taxiDataSheet );
        System.out.println( taxiDataList.size() );

        for( TaxiDataObject taxi : taxiDataList )
        {
            Date pickupTime = taxi.getPickupTime();
            Date dropOffTime = taxi.getDropOffTime();

            LocalDateTime localDateTime = LocalDateTime.ofInstant( pickupTime.toInstant(), ZoneId.systemDefault() );
            System.out.println();
        }

    }
}

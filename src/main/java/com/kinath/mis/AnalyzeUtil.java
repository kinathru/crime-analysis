package com.kinath.mis;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

        List<ComplainObject> complaintsWithinTime = new ArrayList<>();
        List<ComplainObject> complaintsWithinTimePlus30 = new ArrayList<>();
        List<ComplainObject> complaintsWithinTimePlus1h = new ArrayList<>();

        int processedRecordsCount = 0;

        for( TaxiDataObject taxi : taxiDataList )
        {
            //01.Complaints within pickup and drop-off times
            final LocalDateTime taxiPickupTime = taxi._getLocalPickupTime();
            final LocalDateTime taxiDropOffTime = taxi._getLocalDropOffTime();
            List<ComplainObject> collect1 = complainList.stream().filter( cmp -> cmp._getLocalCmpFromTime().isAfter( taxiPickupTime ) && cmp._getLocalCmpToTime().isBefore( taxiDropOffTime ) ).collect( Collectors.toList() );
            complaintsWithinTime.addAll( collect1 );

            //02.Complaints within pickup-time + 0.5h and drop-off time + 0.5h
            final LocalDateTime taxiPickupTimeMinus30 = taxi._getLocalPickupTime().plus( -30, ChronoUnit.MINUTES );
            final LocalDateTime taxiDropOffTimePlus30 = taxi._getLocalDropOffTime().plus( 30, ChronoUnit.MINUTES );
            List<ComplainObject> collect2 = complainList.stream().filter( cmp -> cmp._getLocalCmpFromTime().isAfter( taxiPickupTimeMinus30 ) && cmp._getLocalCmpToTime().isBefore( taxiDropOffTimePlus30 ) ).collect( Collectors.toList() );
            complaintsWithinTimePlus30.addAll( collect2 );

            //03. Complaints within pickup-time + 1h and drop-off time + 1h
            final LocalDateTime taxiPickupTimeMinus1h = taxi._getLocalPickupTime().plus( -1, ChronoUnit.HOURS );
            final LocalDateTime taxiDropOffTimePlus1h = taxi._getLocalDropOffTime().plus( 1, ChronoUnit.HOURS );
            List<ComplainObject> collect3 = complainList.stream().filter( cmp -> cmp._getLocalCmpFromTime().isAfter( taxiPickupTimeMinus1h ) && cmp._getLocalCmpToTime().isBefore( taxiDropOffTimePlus1h ) ).collect( Collectors.toList() );
            complaintsWithinTimePlus1h.addAll( collect3 );

            System.out.println( "Record No : " + ( ++processedRecordsCount ) + "   >>>> Within : " + collect1.size() + " - Within 30" + collect2.size() + " - Within 1h" + collect3.size() );
        }

        System.out.println( "Within : " + complaintsWithinTime.size() + " - Within 30" + complaintsWithinTimePlus30.size() + " - Within 1h" + complaintsWithinTimePlus1h.size() );

    }
}

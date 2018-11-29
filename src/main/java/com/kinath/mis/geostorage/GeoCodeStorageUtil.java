package com.kinath.mis.geostorage;

import com.kinath.mis.ComplainObject;
import com.kinath.mis.ExcelReaderUtil;
import com.kinath.mis.TaxiDataObject;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import static com.kinath.mis.Constants.*;

public class GeoCodeStorageUtil
{
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

        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(SAMPLE_CSV_FILE));

                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT
                        .withHeader("ID", "Name", "Designation", "Company"));
        ) {
            csvPrinter.printRecord("1", "Sundar Pichai ♥", "CEO", "Google");
            csvPrinter.printRecord("2", "Satya Nadella", "CEO", "Microsoft");
            csvPrinter.printRecord("3", "Tim cook", "CEO", "Apple");

            csvPrinter.printRecord(Arrays.asList("4", "Mark Zuckerberg", "CEO", "Facebook"));

            csvPrinter.flush();
        }

    }
}

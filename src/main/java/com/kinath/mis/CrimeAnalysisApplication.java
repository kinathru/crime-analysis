package com.kinath.mis;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static com.kinath.mis.Constants.*;

@SpringBootApplication
public class CrimeAnalysisApplication implements CommandLineRunner
{
    @Autowired
    ComplainRepository complainRepository;

    @Autowired
    TaxiDataRepository taxiDataRepository;

    public static void main( String[] args )
    {
        SpringApplication.run( CrimeAnalysisApplication.class, args );
    }


    @Override
    public void run( String... args )
    {
        complainRepository.deleteAllInBatch();
        taxiDataRepository.deleteAllInBatch();

        try
        {
            org.apache.poi.ss.usermodel.Workbook complaintsWorkbook = WorkbookFactory.create( new File( COMPLAINTS_FILE ) );
            org.apache.poi.ss.usermodel.Workbook taxidataWorkbook = WorkbookFactory.create( new File( TAXI_DATA_FILE ) );

            Sheet complaintsSheet = complaintsWorkbook.getSheet( COMPLAINTS_SHEET );
            List<ComplainObject> complainList = ExcelReaderUtil.getComplainObjects( complaintsSheet );
            System.out.println( complainList.size() );

            Sheet taxiDataSheet = taxidataWorkbook.getSheet( TAXI_DATA_SHEET );
            List<TaxiDataObject> taxiDataList = ExcelReaderUtil.getTaxiDataObjects( taxiDataSheet );

            System.out.println( taxiDataList.size() );

            complainRepository.saveAll( complainList );
            taxiDataRepository.saveAll( taxiDataList );
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

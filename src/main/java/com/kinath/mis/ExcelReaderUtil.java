package com.kinath.mis;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelReaderUtil
{
    public static List<TaxiDataObject> getTaxiDataObjects( Sheet taxiDataSheet )
    {
        List<TaxiDataObject> taxiDataList = new ArrayList<>();
        Iterator<Row> taxiDataRowIterator = taxiDataSheet.rowIterator();
        Row taxDataRowHeader = taxiDataRowIterator.next();
        while( taxiDataRowIterator.hasNext() )
        {
            Row taxiDataow = taxiDataRowIterator.next();
            TaxiDataObject taxiData = new TaxiDataObject();

            try
            {
                ObjectMapper.mapTaxiDataObject( taxiData, taxiDataow );
                taxiDataList.add( taxiData );
            }
            catch( MappingException e )
            {
                System.out.println( e.getMessage() + " " + taxiDataow.getCell( 0 ).getNumericCellValue() );
            }
        }
        return taxiDataList;
    }

    public static List<ComplainObject> getComplainObjects( Sheet complaintsSheet )
    {
        List<ComplainObject> complainList = new ArrayList<>();
        Iterator<Row> complaintsRowIterator = complaintsSheet.rowIterator();
        Row complainRowHeader = complaintsRowIterator.next();
        while( complaintsRowIterator.hasNext() )
        {
            Row complainRow = complaintsRowIterator.next();
            ComplainObject complainObject = new ComplainObject();
            try
            {
                ObjectMapper.mapComplainObject( complainObject, complainRow );
                complainList.add( complainObject );
            }
            catch( MappingException e )
            {
                System.out.println( e.getMessage() + " " + complainRow.getCell( 0 ).getNumericCellValue() );
            }
        }
        return complainList;
    }
}

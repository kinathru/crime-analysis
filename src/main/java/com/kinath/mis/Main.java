package com.kinath.mis;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import java.io.IOException;

public class Main
{
    public static void main( String[] args )
    {
        try
        {
            AnalyzeUtil.analyzeData();
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

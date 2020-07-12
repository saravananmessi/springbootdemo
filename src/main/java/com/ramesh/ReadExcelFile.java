package com.ramesh;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ReadExcelFile 
{
	
	public static void main(String[] args) {
		
		excel2Csv(null);
		Projcompfile("P158570", "Improving the Quality of Teaching and Learning");

	}
	
public static void excel2Csv(String file_path) {
		

	    String jsonInString = "";
	    int active_sheet_index = 0;

	    // Initializing the parent and the children list!
	   
	    // Initializing the excel variables!        
	    org.apache.poi.ss.usermodel.Sheet sheet = null;     
	    Workbook workbook = null;
	    FileInputStream inp = null;

	    // Initializing the JSON Mapper variables!
	    ObjectMapper mapper = new ObjectMapper();

	    try {
	    	
	    	inp = new FileInputStream(new File("C:\\ramesh\\WB Projects DB Bulk Upload File.xlsx"));

	        // Creating the excel workbook object!
	        workbook = WorkbookFactory.create(inp);     

	        sheet = workbook.getSheetAt(active_sheet_index);
	        
	        Iterator<Row> iterator = sheet.iterator();
	        String ss = "";
	        HashMap hm = new HashMap();
	        while(iterator.hasNext())
	        {
	        	Row row = iterator.next();
				ss = row.getCell(0).toString() + "-" + row.getCell(8).toString();
				if(row.getCell(0).toString().equals("P158570"))
				{
					hm.put(row.getCell(0).toString(), row.getCell(8).toString());
					//System.out.println("Hi "+ss);
					//Projcompfile(row.getCell(0).toString(), row.getCell(8).toString());
				}
					
				}
	        
	        System.out.println("Hi "+hm);
	        
	        
	        
	    }
	    catch(Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	
	public static void Projcompfile(String pid, String CompName)
	{
		System.out.println("pid "+pid);
		System.out.println("CompName "+CompName);
		String jsonInString = "";
	    int active_sheet_index = 0;

	    // Initializing the parent and the children list!
	   
	    // Initializing the excel variables!        
	    org.apache.poi.ss.usermodel.Sheet sheet = null;     
	    Workbook workbook = null;
	    FileInputStream inp = null;

	    // Initializing the JSON Mapper variables!
	    ObjectMapper mapper = new ObjectMapper();

	    try {
	    	
	    	inp = new FileInputStream(new File("C:\\ramesh\\Project Component Advanced Find View 6-17-2020 8-09-06 PM.xlsx"));

	        // Creating the excel workbook object!
	        workbook = WorkbookFactory.create(inp);     

	        sheet = workbook.getSheetAt(active_sheet_index);
	        
	        Iterator<Row> iterator = sheet.iterator();
	        String ss = "";
	        HashMap hm = new HashMap();
	        while(iterator.hasNext())
	        {
	        	Row row = iterator.next();
	       
				if(row.getCell(8, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString().equals(pid) && row.getCell(6, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString().equals(CompName) && pid != null)
				{
				//	
					System.out.println("guid "+row.getCell(0, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString());
					
				}
				else
				{
					
				}
				
			//	System.out.println("Value " +workbook.getSheetAt(0).getRow(5719).getCell(8));
				}
	        
	      //  System.out.println("Hiiiiiiii "+hm);
	        
	        
	        
	    }
	    catch(Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	}
	

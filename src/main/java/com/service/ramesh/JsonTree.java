package com.service.ramesh;


import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTree 
{
  public static void main(String[] args) {
		
		Projcompfile();

	}

  public static void Projcompfile() {
		

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
	    	
	    	inp = new FileInputStream(new File("C:\\Users\\wb548110\\Desktop\\tasks\\sss.xlsx"));

	        // Creating the excel workbook object!
	        workbook = WorkbookFactory.create(inp);     
	        String prevval = "";

	        sheet = workbook.getSheetAt(active_sheet_index);       
	        
	        Iterator<Row> iterator = sheet.iterator();
	        String ss = "";
	        HashMap hm = new HashMap();
	        int i = 0;
	        String level1 = "";
			List<Tags> tags = new ArrayList<Tags>();
			
	        int n = 0;
	        while(iterator.hasNext())
	        {
	        
	        	Row row = iterator.next();
	        	level1 = row.getCell(11).toString();
	        //	System.out.println("row num "+row.getRowNum());
				ss = row.getCell(11).toString();
				if(row.getRowNum() != 0 && row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString() != null)
				{
					System.out.println("ss "+ss);
					
					if(i == 1)
					{
						prevval = row.getCell(11).toString();
						hm.put(row.getRowNum(), level1);
						Tags tag = new Tags();
						tag.setName((row.getCell(11) != null ? row.getCell(11).toString() : null));
						tag.setLevel(1);
						tags.add(tag);
						System.out.println("hm "+hm);
						System.out.println("tags "+tags);
					}
					
					if(i != 1 && prevval != row.getCell(11).toString())
					{
						hm.put(row.getRowNum(), level1);
						
					}
				}
				 jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tags);
//				if(row.getCell(0).toString().equals("P158570"))
//				{
//					hm.put(row.getCell(0).toString(), row.getCell(8).toString());
//					//System.out.println("Hi "+ss);
//					//Projcompfile(row.getCell(0).toString(), row.getCell(8).toString());
//				}
					
				i++;
				
				}
	        
	        System.out.println("Hi "+jsonInString);
	        
	        
	    }
	    catch(Exception ex) {
	        ex.printStackTrace();
	    }
	}

}

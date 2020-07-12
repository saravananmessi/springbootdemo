package com.ramesh;

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
	   
	    inp = new FileInputStream(new File("C:\\ramesh\\sss.xlsx"));

	       // Creating the excel workbook object!
	       workbook = WorkbookFactory.create(inp);    
	       String prevval = "";

	       sheet = workbook.getSheetAt(active_sheet_index);      
	       
	       Iterator<Row> iterator = sheet.iterator();
	       String ss = "";
	       HashMap hm = new HashMap();
	       HashMap hm_l2 = new HashMap();
	       int i = 0;
	       String level1 = "";
	       String level2 = "";
	       List tags = new ArrayList();
	       List<Tags> tags1 = new ArrayList<Tags>();
	       int n = 0;
	       while(iterator.hasNext())
	       {
	       
	        Row row = iterator.next();
	        level1 = row.getCell(11).toString();
	        level2 = row.getCell(12).toString();
	       // System.out.println("row num "+row.getRowNum());
		ss = row.getCell(11).toString();
		if(row.getRowNum() != 0 && row.getCell(11, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK).toString() != null)
		{
			System.out.println("ss "+ss);
			List<ChildtagsComponents> listOfChildComponents = new ArrayList<ChildtagsComponents>();
		//	prevval = row.getCell(11).toString();
//			if(i == 1)
//			{
//				prevval = row.getCell(11).toString();
//				hm.put(row.getRowNum(), level1);
//				Tags tag = new Tags();
//				tag.setName((row.getCell(11) != null ? row.getCell(11).toString() : null));
//				tag.setLevel(1);
//				tags.add(tag);
//				System.out.println("hm "+hm);
//				//System.out.println("tags "+tags);
//			}
//	
//			if(i != 1 && prevval != row.getCell(11).toString())
//			{
//				System.out.println("prevval "+prevval);
//				hm.put(row.getRowNum(), level1);
//		
//			}
			
			
			if(!hm.containsValue(level1))
			{
				hm.put(row.getRowNum(), level1);
				List<Tags> childtag = new ArrayList<Tags>();
				Tags tag = new Tags();
				tag.setName((row.getCell(11) != null ? row.getCell(11).toString() : null));
				tag.setLevel(1);
				childtag.add(tag);
				tags.add(childtag);
				if(!hm_l2.containsValue(level2))
				{
					hm_l2.put(row.getRowNum(), level2);
				Tags tag_l2 = childtag.get(childtag.indexOf(tag));
					
					List<Tags> list_level2 = new ArrayList<Tags>();
				//	Tags tag_l2 = new Tags();
					tag_l2.setName((row.getCell(12) != null ? row.getCell(12).toString() : null));
					tag_l2.setLevel(1);
					list_level2.add(tag_l2);
					System.out.println("list level2 "+list_level2.get(0));
					
					tags.add(list_level2);
					
					
					
			//		ChildtagsComponents childComponents = new ChildtagsComponents(row.getCell(7).toString());
					
				//	childComponents.setId(guid);  //  ramesh
					
//					listOfChildComponents.add(childComponents);
				//	childOfParentComponent.put(row.getCell(7).toString(), childComponents);
					
//					Tags tag1 = new Tags();
//					tag.setName((row.getCell(12) != null ? row.getCell(12).toString() : null));
//					tag1.setLevel(1);
//					tags1.add(tag1);
//					childComponents.setTags(tags1);
				}
				//System.out.println("tags "+tags);
			}
			
			
			}
		
			// if(row.getCell(0).toString().equals("P158570"))
			// {
			// hm.put(row.getCell(0).toString(), row.getCell(8).toString());
			// //System.out.println("Hi "+ss);
			// //Projcompfile(row.getCell(0).toString(), row.getCell(8).toString());
			// }
		
		//	prevval = row.getCell(11).toString();
			i++;
		
			}
	       jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(tags);
	       System.out.println(jsonInString);
	       System.out.println("Hi "+hm);
	       
	       
	   }
	   
	   catch(Exception ex) {
	       ex.printStackTrace();
	   }
	}

}

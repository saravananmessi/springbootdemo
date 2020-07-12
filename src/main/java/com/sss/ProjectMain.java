package com.sss;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectMain {

public static void main(String[] args) {
//excel2Csv(null);
/*JsonRootBean jsonRootBean= new JsonRootBean();
jsonRootBean.setId("223");
ChildtagsComponents child = new ChildtagsComponents();
child.setId("2233");
Tags tags= new Tags();
tags.setName("tab1");
List<Tags> listtags= new ArrayList<Tags>();
listtags.add(tags);
child.setTags(listtags);
List<ChildtagsComponents> listchild = new ArrayList<ChildtagsComponents>();
listchild.add(child);
jsonRootBean.setChildtagsComponents(listchild);
Gson gson = new GsonBuilder().create();
String jsonString = gson.toJsonTree(jsonRootBean).toString();*/
// System.out.println(jsonString);
excel2Csv(null);

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
       // Reading the excel input for conversion!
       inp = new FileInputStream(new File("C:\\ramesh\\test1.xlsx"));

       // Creating the excel workbook object!
       workbook = WorkbookFactory.create(inp);   

       sheet = workbook.getSheetAt(active_sheet_index);
       Iterator<Row> iterator = sheet.iterator();
      List <JsonRootBean> listjsonRootBean= new ArrayList<JsonRootBean>();
	HashMap<String,Object> childIds= new HashMap<String,Object>();
	while (iterator.hasNext()) {
		List<ChildtagsComponents> listchild = new ArrayList<ChildtagsComponents>();
		Row row = iterator.next();
		if (row.getCell(0) != null) {
		JsonRootBean jsonRootBean = new JsonRootBean(row.getCell(0).toString());
		if (!listjsonRootBean.contains(new JsonRootBean(row.getCell(0).toString()))) {
		listjsonRootBean.add(jsonRootBean);
		ChildtagsComponents childcomponents = new ChildtagsComponents(row.getCell(3).toString());
		Tags tag = new Tags();
		tag.setName(row.getCell(4).toString());
		List<Tags> tags = new ArrayList<Tags>();
		tags.add(tag);
		childcomponents.setTags(tags);
		listchild.add(childcomponents);
		childIds.put(row.getCell(3).toString(), childcomponents);
		jsonRootBean.setChildtagsComponents(listchild);
		} else {
		jsonRootBean = listjsonRootBean.get(listjsonRootBean.indexOf(jsonRootBean));
		List<ChildtagsComponents> templist = jsonRootBean.getChildtagsComponents();
		if (childIds.containsKey(row.getCell(3).toString())) {
		ChildtagsComponents test = (ChildtagsComponents) childIds.get(row.getCell(3).toString());
		List<Tags> lisTags = test.getTags();
		Tags tag = new Tags();
		tag.setName(row.getCell(4).toString());
		lisTags.add(tag);
		test.setTags(lisTags);
		} else {
		List<ChildtagsComponents> templist1 = new CopyOnWriteArrayList<ChildtagsComponents>(
		templist);
		ChildtagsComponents childcomponents = new ChildtagsComponents(row.getCell(3).toString());
		childIds.put(row.getCell(3).toString(), childcomponents);
		Tags tag = new Tags();
		tag.setName(row.getCell(4).toString());
		List<Tags> tags = new ArrayList<Tags>();
		tags.add(tag);
		childcomponents.setTags(tags);
		templist1.add(childcomponents);
		jsonRootBean.setChildtagsComponents(templist1);
	}
	}
	}
	}
       jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listjsonRootBean);
       System.out.println( " LAST "+ jsonInString);

       // Convert object to JSON string and save into file directly
     
   } catch(Exception ex) {
       ex.printStackTrace();
   }
}

 private static String getCellValueAsString(Cell cell) {
       String cellValue = null;
       switch(cell.getCellType()) {
       case BOOLEAN:
           cellValue = String.valueOf(cell.getBooleanCellValue());
           break;
       case STRING:
           cellValue = String.valueOf(cell.getRichStringCellValue().toString());
           break;
       case NUMERIC:
           Double value = cell.getNumericCellValue();
           if (value != null) {
               String valueAsStr = value.toString();
               int indexOf = valueAsStr.indexOf(".");
               if (indexOf > 0) {
                   cellValue = valueAsStr.substring(0, indexOf);//decimal numbers truncated
               } else {
                   cellValue = value.toString();
               }
           }
           break;
       case FORMULA:
           //if the cell contains formula, this case will be executed.
           cellValue = cell.getStringCellValue();
           break;
       case BLANK:
           cellValue = "";
           break;
       case _NONE:
           cellValue = "";
           break;
       case ERROR:
           throw new RuntimeException("There is no support for this type of cell");
       default:
           cellValue = "";
       }
       return cellValue;
   }
}

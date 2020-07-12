package com.service.ramesh;



import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONTokener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;


public class ExcelTest {

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
		String jsonString = gson.toJsonTree(jsonRootBean).toString();
		System.out.println(jsonString);*/

	}
	public static void excel2Csv(String file_path) {
		
	    // Variables!
	    Books b1, b2 = null;
	    String jsonInString = "";
	    int active_sheet_index = 0;

	    // Initializing the parent and the children list!
	    List<Books> bList = new ArrayList<Books>();
	    List<Books> children = null;

	    // Initializing the excel variables!        
	    org.apache.poi.ss.usermodel.Sheet sheet = null;     
	    Workbook workbook = null;
	    FileInputStream inp = null;

	    // Initializing the JSON Mapper variables!
	    ObjectMapper mapper = null;

	    try {
	        // Reading the excel input for conversion!
	        inp = new FileInputStream(new File("C:\\Users\\wb548110\\Desktop\\tasks\\test.xlsx"));

	        // Creating the excel workbook object!
	        workbook = WorkbookFactory.create(inp);     

	        // Get the first sheet!
	        sheet = workbook.getSheetAt(active_sheet_index);
	        Iterator<Row> iterator = sheet.iterator();
	        while (iterator.hasNext()) {
	            b1 = new Books();
	            children = new ArrayList<Books>();

	            // Iterating through the excel rows!
	            Row row = iterator.next();

	            // If Cell0 of the active row is blank or null!
	            if(row.getCell(0)==null || row.getCell(0).getCellType()==CellType.BLANK) {
	                b2 = new Books(); 
	                b2.setBauthor (getCellValueAsString(row.getCell(1)));
	                b2.setBcost(getCellValueAsString(row.getCell(2)));
	                children.add(b2);
	            } /*else {
	                b1.setBname(getCellValueAsString(row.getCell(0)));
	                b1.setBauthor(getCellValueAsString(row.getCell(1)));
	                b1.setBcost(getCellValueAsString(row.getCell(2)));              
	            }*/

	            if (children!=null && children.size()>0) {
	            	bList.get(0).getBname();
	                b1.setChildren(children);
	            }
	            bList.add(b1);
	        }

	        mapper = new ObjectMapper();
	        mapper.configure(SerializationFeature.INDENT_OUTPUT, true);

	        jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(bList);
	        System.out.println("Final Json= \n" + jsonInString);

	        // Convert object to JSON string and save into file directly
	        mapper.writeValue(new File("D:\\test.json"), new JSONTokener(jsonInString).nextValue().toString());
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


package com.ramesh;

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

	private static final String PORTFOLIO = "portfolio";
	private static final String TAXONOMY = "hdtaxonomy";

	public static void main(String[] args) {
		
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
	       List <WBProjects> listWBProjects= new ArrayList<WBProjects>();
			HashMap<String,Object> childOfParentComponent= new HashMap<String,Object>();
			String guid = "";
			while (iterator.hasNext()) {
				List<ChildtagsComponents> listOfChildComponents = new ArrayList<ChildtagsComponents>();
				Row row = iterator.next();
				guid = row.getCell(0).toString() + "-" + row.getCell(4).toString();
				if (row.getCell(0) != null) {
					WBProjects wbParentProject = new WBProjects(row.getCell(0).toString());
					if (!listWBProjects.contains(new WBProjects(row.getCell(0).toString()))) {
						wbParentProject.setName(row.getCell(1) != null ? row.getCell(1).toString() : null);
						wbParentProject.setType(PORTFOLIO);
						listWBProjects.add(wbParentProject);
						ChildtagsComponents childComponents = new ChildtagsComponents(row.getCell(7).toString());
						List<Tags> tags = new ArrayList<Tags>();
						tags = setChildTags(tags, row);
						childComponents.setId(guid);  //  ramesh
						childComponents.setTags(tags);
						listOfChildComponents.add(childComponents);
						childOfParentComponent.put(row.getCell(7).toString(), childComponents);
						wbParentProject.setChildtagsComponents(listOfChildComponents);
					} else {
						wbParentProject = listWBProjects.get(listWBProjects.indexOf(wbParentProject));
						List<ChildtagsComponents> templist = wbParentProject.getChildtagsComponents();
						if (childOfParentComponent.containsKey(row.getCell(7).toString())) {
							ChildtagsComponents test = (ChildtagsComponents) childOfParentComponent.get(row.getCell(7).toString());
							List<Tags> tags = test.getTags();
							tags = setChildTags(tags, row);
							test.setId(guid);  //  ramesh
							test.setTags(tags);
						} else {
							List<ChildtagsComponents> templist1 = new CopyOnWriteArrayList<ChildtagsComponents>(
									templist);
							ChildtagsComponents childComponents = new ChildtagsComponents(row.getCell(7).toString());
							childOfParentComponent.put(row.getCell(7) != null ? row.getCell(7).toString() : null, childComponents);
							List<Tags> tags = new ArrayList<Tags>();
							tags = setChildTags(tags, row);
							childComponents.setId(guid);  //  ramesh
							childComponents.setTags(tags);
							templist1.add(childComponents);
							wbParentProject.setChildtagsComponents(templist1);
						}
					}
				}
			}
		    jsonInString = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(listWBProjects);
	        System.out.println(jsonInString);

	        // Convert object to JSON string and save into file directly
	      
	    } catch(Exception ex) {
	        ex.printStackTrace();
	    }
	}
	
	private static List<Tags> setChildTags(List<Tags> tags, Row row) {
		Tags tag = new Tags();
		tag.setName((row.getCell(11) != null ? row.getCell(11).toString() : null));
		tag.setLevel(1);
		tag.setTaxonomy(TAXONOMY);
		Tags tag2 = new Tags();
		tag2.setName((row.getCell(12) != null ? row.getCell(12).toString() : null));
		tag2.setLevel(2);
		tag2.setParentid(row.getCell(11) != null ? row.getCell(11).toString() : null);
		tag2.setTaxonomy(TAXONOMY);
		Tags tag3 = new Tags();
		tag3.setName((row.getCell(13) != null ? row.getCell(13).toString() : null));
		tag3.setParentid(row.getCell(12) != null ? row.getCell(12).toString() : null);
		tag3.setLevel(3);
		tag3.setTaxonomy(TAXONOMY);
		tags.add(tag);
		tags.add(tag2);
		tags.add(tag3);
		return tags;
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


package com.service.ramesh;

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
import org.apache.poi.util.SystemOutLogger;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectMain {

	private static final String PORTFOLIO = "portfolio";
	private static final String TAXONOMY = "hdtaxonomy";
	static List<ProjectComponent> listprojectComponents = new ArrayList<ProjectComponent>();

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
			inp = new FileInputStream(new File("C:\\Ramesh\\sss.xlsx"));
			getGuidId();
			// Creating the excel workbook object!
			workbook = WorkbookFactory.create(inp);
			String guid = "";
			sheet = workbook.getSheetAt(active_sheet_index);
			Iterator<Row> iterator = sheet.iterator();
			List<WBProjects> listWBProjects = new ArrayList<WBProjects>();
			HashMap<String, Object> childOfParentComponent = new HashMap<String, Object>();
			while (iterator.hasNext()) {
				List<ChildtagsComponents> listOfChildComponents = new ArrayList<ChildtagsComponents>();
				Row row = iterator.next();
				int index = row.getRowNum();

				if (row.getCell(0) != null && index >= 2) {
					guid = row.getCell(0).toString() + "-" + row.getCell(8).toString();
					WBProjects wbParentProject = new WBProjects(row.getCell(0).toString());
					if (!listWBProjects.contains(new WBProjects(row.getCell(0).toString()))) {
						wbParentProject.setName(row.getCell(1) != null ? row.getCell(1).toString() : null);
						wbParentProject.setType(PORTFOLIO);
						listWBProjects.add(wbParentProject);
						ChildtagsComponents childComponents = new ChildtagsComponents(row.getCell(7).toString());
						List<Tags> tags = new ArrayList<Tags>();
						tags = setChildTags(tags, row);
						childComponents.setTags(tags);
						childComponents.setGuid(getprojectGuid(row.getCell(0).toString(), row.getCell(8).toString()));
						listOfChildComponents.add(childComponents);
						childOfParentComponent.put(row.getCell(7).toString(), childComponents);
						wbParentProject.setChildtagsComponents(listOfChildComponents);
					} else {
						wbParentProject = listWBProjects.get(listWBProjects.indexOf(wbParentProject));
						List<ChildtagsComponents> templist = wbParentProject.getChildtagsComponents();
						if (childOfParentComponent.containsKey(row.getCell(7).toString())) {
							ChildtagsComponents test = (ChildtagsComponents) childOfParentComponent
									.get(row.getCell(7).toString());
							List<Tags> tags = test.getTags();
							tags = setChildTags(tags, row);
							test.setGuid(getprojectGuid(row.getCell(0).toString(), row.getCell(8).toString()));
							test.setTags(tags);
						} else {
							List<ChildtagsComponents> templist1 = new CopyOnWriteArrayList<ChildtagsComponents>(
									templist);
							ChildtagsComponents childComponents = new ChildtagsComponents(row.getCell(7).toString());
							childOfParentComponent.put(row.getCell(7) != null ? row.getCell(7).toString() : null,
									childComponents);
							List<Tags> tags = new ArrayList<Tags>();
							tags = setChildTags(tags, row);
							childComponents
									.setGuid(getprojectGuid(row.getCell(0).toString(), row.getCell(8).toString()));
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

		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private static String getprojectGuid(String id, String name) {
		String guidId = "";

		for (ProjectComponent listprs : listprojectComponents) {
			if (listprs.getPrjCompName().trim().equalsIgnoreCase(name)
					&& listprs.getPrjId().trim().equalsIgnoreCase(id)) {
				guidId = listprs.getPrjGuid();
			}
		}
		return guidId;
	}

	private static List<ProjectComponent> getGuidId() {

		org.apache.poi.ss.usermodel.Sheet sheet = null;
		Workbook workbook = null;
		FileInputStream inp = null;
		int active_sheet_index = 0;
		String guidID = "";

		try {
			inp = new FileInputStream(new File(
					"C:\\Ramesh\\Project Component Advanced Find View 6-17-2020 8-09-06 PM.xlsx"));
			workbook = WorkbookFactory.create(inp);
			sheet = workbook.getSheetAt(active_sheet_index);
			Iterator<Row> iterator = sheet.iterator();

			while (iterator.hasNext()) {
				Row row = iterator.next();
				int index = row.getRowNum();
				if (row.getCell(0) != null && index >= 1) {
					if (row.getCell(6) != null && row.getCell(8) != null) {
						String componentId = row.getCell(8).toString();
						String componentName = row.getCell(6).toString();
						String componentGuid = row.getCell(0).toString();
						ProjectComponent projectComponents = new ProjectComponent();
						projectComponents.setPrjCompName(componentName);
						projectComponents.setPrjId(componentId);
						projectComponents.setPrjGuid(componentGuid);
						listprojectComponents.add(projectComponents);
					}
				}

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return listprojectComponents;
	}

	static HashMap<String, Tags> level1 = new HashMap<String, Tags>();
	static HashMap<String, Tags> level2 = new HashMap<String, Tags>();
	static HashMap<String, Tags> level3 = new HashMap<String, Tags>();
	static HashMap<String, Tags> level4 = new HashMap<String, Tags>();
	static HashMap<String, Tags> level5 = new HashMap<String, Tags>();

	private static List<Tags> setChildTags(List<Tags> tags, Row row) {
		if (level1.containsKey(row.getCell(11) != null ? row.getCell(11).toString() : null)) {
			if (level4.containsKey(row.getCell(16) != null ? row.getCell(16).toString() : null)) {
				Tags tag = level4.get(row.getCell(16).toString());
				Tags tag1 = new Tags();
				tag1.setName((row.getCell(17) != null ? row.getCell(17).toString() : null));
				tag.getChilds().add(tag1);
			}
		} else {
			Tags tag = new Tags();
			tag.setName((row.getCell(11) != null ? row.getCell(11).toString() : null));
			tag.setLevel(1);
			tag.setTaxonomy(TAXONOMY);
			level1.put(row.getCell(11) != null ? row.getCell(11).toString() : null, tag);
			tags.add(tag);
			System.out.println("tags "+tags);
			System.out.println("size "+tags.size());
			if (!level2.containsKey(row.getCell(12) != null ? row.getCell(12).toString() : null)) {
				List<Tags> listChilds = new ArrayList<Tags>();
				Tags childs = new Tags();
				childs.setName((row.getCell(12) != null ? row.getCell(12).toString() : null));
				listChilds.add(childs);
				System.out.println("Hi "+tag);
			//	Tags test = new Tags();
				Tags test = tags.get(tags.indexOf(tag));
				level2.put(row.getCell(12) != null ? row.getCell(12).toString() : null, childs);
				test.setChilds(listChilds);
			}
			if (!level3.containsKey(row.getCell(13) != null ? row.getCell(13).toString() : null)) {
				List<Tags> listChilds = new ArrayList<Tags>();
				Tags childs = new Tags();
				childs.setName((row.getCell(13) != null ? row.getCell(13).toString() : null));
				childs.setLevel(3);
				childs.setTaxonomy(TAXONOMY);
				listChilds.add(childs);
				Tags test = tags.get(tags.indexOf(tag));
				level3.put(row.getCell(13) != null ? row.getCell(13).toString() : null, childs);
				Tags test2 = test.getChilds().get(tags.indexOf(tag));
				test2.setChilds(listChilds);
			}
			if (!level4.containsKey(row.getCell(16) != null ? row.getCell(16).toString() : null)) {
				List<Tags> listChilds = new ArrayList<Tags>();
				Tags childs = new Tags();
				childs.setName((row.getCell(16) != null ? row.getCell(16).toString() : null));
				childs.setLevel(4);
				childs.setTaxonomy(TAXONOMY);
				listChilds.add(childs);
				Tags test = tags.get(tags.indexOf(tag));
				level4.put(row.getCell(16) != null ? row.getCell(16).toString() : null, childs);
				Tags test2 = test.getChilds().get(tags.indexOf(tag)).getChilds().get(tags.indexOf(tag));
				test2.setChilds(listChilds);
				Tags tagRet = level4.get(row.getCell(16).toString());
				List<Tags> listChildsRet = new ArrayList<Tags>();
				Tags tag1 = new Tags();
				tag1.setName((row.getCell(17) != null ? row.getCell(17).toString() : null));
				listChildsRet.add(tag1);
				tagRet.setChilds(listChildsRet);
			}
		}
		return tags;
	}

	private static String getCellValueAsString(Cell cell) {
		String cellValue = null;
		switch (cell.getCellType()) {
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
					cellValue = valueAsStr.substring(0, indexOf);// decimal numbers truncated
				} else {
					cellValue = value.toString();
				}
			}
			break;
		case FORMULA:
			// if the cell contains formula, this case will be executed.
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

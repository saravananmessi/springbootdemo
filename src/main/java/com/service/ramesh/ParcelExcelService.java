//package com.service.ramesh;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.InvocationTargetException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//
//import org.apache.poi.xssf.usermodel.XSSFCell;
//import org.apache.poi.xssf.usermodel.XSSFRow;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.core.io.Resource;
//import org.springframework.core.io.ResourceLoader;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class ParcelExcelService extends FieldMapping {
//
//	private static final Logger logger = LoggerFactory.getLogger(ParcelExcelService.class);
//
//	@Autowired
//	CRMService crmService;
//
//	@Autowired
//	Environment env;
//	
//	@Autowired
//	ResourceLoader resourceLoader;
//
//	public static final String CRM_BASE_SERVICE_ADDRESS = "crm.base.service.address";
//	public static final String CRM_SERVICE_REQUESTS = "crm.service.requests";
//	public static final String CRM_URL = "crm.url";
//
//	public String extractExcelData(InputStream inputStream, String projectGuid, String accessToken) throws Throwable {
//
//		String output = "";
//		try {
//			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//			HashMap<String, Integer> map = new HashMap<>();
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			XSSFRow row = sheet.getRow(3);
//			short minColIx = row.getFirstCellNum();
//			short maxColIx = row.getLastCellNum();
//			System.out.println(minColIx);
//			System.out.println(maxColIx);
//			for (short colIx = minColIx; colIx < maxColIx; colIx++) {
//				XSSFCell cell = row.getCell(colIx);
//				//System.out.println(cell.getStringCellValue());
//				map.put(cell.getStringCellValue(), cell.getColumnIndex());
//			}
//		
//			XSSFCell cell2 = null;
//			int startIndex = 5;
//			if(startIndex>sheet.getLastRowNum()) {
//				return output="ERROR_PARCEL";
//			}
//			for (startIndex = 5; startIndex <= sheet.getLastRowNum(); startIndex++) {
//				XSSFRow dataRow = sheet.getRow(startIndex);
//				XSSFRow headerDataRow = sheet.getRow(4);
//				if(dataRow!=null) {
//					List<OwnerInformation> owners = new ArrayList<>();
//					ParcelInformation listparcel = new ParcelInformation();
//					List<CompensationInformation> compensations = new ArrayList<>();
//					List<AgriculturalLand> crops = new ArrayList<>();
//					List<BusinessInformation> businesses = new ArrayList<>();
//					List<AgreementInformation> agreement = new ArrayList<>();
//					List<BuildingStructureInformation> buildingStructures = new ArrayList<>();
//					ParcelInformation parcel = new ParcelInformation();
//					CompensationInformation compensation = new CompensationInformation();
//					XSSFSheet hiddenValue = workbook.getSheetAt(0);
//					XSSFRow hiddenrow = hiddenValue.getRow(4);
//					XSSFCell hiddenPID = hiddenrow.getCell(1);
//					if (hiddenPID != null && hiddenPID.toString().equalsIgnoreCase(projectGuid)) {
//						parcel.setRap_projectid(hiddenPID.toString());
//					}else {
//						return output="ERROR_PROJECT";
//					}
//					parcel.setRap_projectid(projectGuid);
//					LinkedHashMap<String, HouseholdInformation> householdinfo= new LinkedHashMap<>();
//					HouseholdInformation householdInformation=null;
//					for (Entry<String, Integer> entry : map.entrySet()) {
//						try {
//							if (!StringUtil.isBlankOrNull(entry.getKey()) && FieldMapping.landParcelDetails.keySet().contains(entry.getKey())) {
//								int idxForColumn1 = map.get(entry.getKey());
//								XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//								cell2 = headerDataRow.getCell(idxForColumn1);
//								if(cell2.toString().equalsIgnoreCase("parcel ID") && (cell1==null || cell1.toString().equalsIgnoreCase(""))) {
//									return output="ERROR_PARCEL";
//
//								}
//								if (cell1 != null)
//									ParcelInformation.class.getMethod(FieldMapping.landParcelDetails.get(entry.getKey()), String.class)
//									.invoke(parcel, cell1.toString());
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							throw new RAPException("Invalid Input field "+ cell2.toString() +" in Parcel Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//						}
//
//						try {
//							if (!StringUtil.isBlankOrNull(entry.getKey())
//									&& FieldMapping.getCompensationDetails.keySet().contains(entry.getKey())) {
//								int idxForColumn1 = map.get(entry.getKey());
//								XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//								cell2 = headerDataRow.getCell(idxForColumn1);
//								if (cell1 != null)
//									CompensationInformation.class
//									.getMethod(FieldMapping.getCompensationDetails.get(entry.getKey()), String.class)
//									.invoke(compensation, cell1.toString());
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							throw new RAPException("Invalid Input field "+ cell2.toString() +" in Compensation Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//						}
//						try {
//							if(!StringUtil.isBlankOrNull(entry.getKey())
//									&& entry.getKey().trim().startsWith("U")
//									&& FieldMapping.getUserInformationDetails.keySet().contains(entry.getKey().trim().substring(2))) {
//								String userKey = entry.getKey().trim().substring(0, 2);
//								OwnerInformation owner = new OwnerInformation(userKey);
//								if (!owners.contains(new OwnerInformation(userKey))) {
//									owners.add(owner);
//								}
//								owner = owners.get(owners.indexOf(owner));
//								owner.getRap_ownerinformation_householdmembers_ownerId();
//								int idxForColumn1 = map.get(entry.getKey());
//								XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//								cell2 = headerDataRow.getCell(idxForColumn1);
//								if (cell1 != null)
//									OwnerInformation.class
//									.getMethod(FieldMapping.getUserInformationDetails.get(entry.getKey().trim().substring(2)),
//											String.class)
//									.invoke(owner, cell1.toString());
//
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							throw new RAPException("Invalid Input field "+ cell2.toString() +" in Owner Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//						}
//						try {
//							if(!StringUtil.isBlankOrNull(entry.getKey())
//									&& entry.getKey().trim().startsWith("U")
//									&& FieldMapping.getHousingDetails.keySet().contains(entry.getKey().trim().substring(4))) {
//								String endIndex = entry.getKey().trim().substring(4, entry.getKey().length());
//
//								if (FieldMapping.getHousingDetails.containsKey(endIndex)) {
//									String membersKey = entry.getKey().trim().substring(0, 4);
//									if (householdinfo.containsKey(membersKey)) {
//										householdInformation = householdinfo.get(membersKey);
//										int idxForColumn1 = map.get(entry.getKey());
//										XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//										cell2 = headerDataRow.getCell(idxForColumn1);
//										if (cell1 != null)
//											HouseholdInformation.class.getMethod(FieldMapping.getHousingDetails.get(endIndex), String.class)
//											.invoke(householdInformation, cell1.toString());
//									} else {
//										householdInformation = new HouseholdInformation();
//										int idxForColumn1 = map.get(entry.getKey());
//										XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//										cell2 = headerDataRow.getCell(idxForColumn1);
//										if (cell1 != null)
//											HouseholdInformation.class.getMethod(FieldMapping.getHousingDetails.get(endIndex), String.class)
//											.invoke(householdInformation, cell1.toString());
//										householdinfo.put(membersKey, householdInformation);
//									}
//								}
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							throw new RAPException("Invalid Input field "+ cell2.toString() +" in Household Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//						}
//						try {
//							if(!StringUtil.isBlankOrNull(entry.getKey())
//									&& entry.getKey().trim().startsWith("BI")
//									&& FieldMapping.getBusinessInformationDetails.keySet().contains(entry.getKey().trim().substring(3))) {
//								String userKey = entry.getKey().trim().substring(0,3);
//								BusinessInformation business = new BusinessInformation(userKey);
//								if(!businesses.contains(new BusinessInformation(userKey))) {
//									businesses.add(business);
//								}
//								business = businesses.get(businesses.indexOf(business));
//								int idxForColumn1 = map.get(entry.getKey());
//								XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//								cell2 = headerDataRow.getCell(idxForColumn1);
//								if (cell1 != null)
//									BusinessInformation.class.getMethod(FieldMapping.getBusinessInformationDetails.get(entry.getKey().trim().substring(3)), String.class).invoke(business, cell1.toString());
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							throw new RAPException("Invalid Input field "+cell2.toString()+" in Business Information ", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//						}
//						try {
//							if(!StringUtil.isBlankOrNull(entry.getKey())
//									&& entry.getKey().trim().startsWith("BS")) {
//								String userKey = entry.getKey().trim().substring(0,2)+entry.getKey().trim().substring(3,4);
//								BuildingStructureInformation buildingStructureInformation = new BuildingStructureInformation(userKey);
//								if(!buildingStructures.contains(buildingStructureInformation)) {
//									buildingStructures.add(buildingStructureInformation);
//								}
//								buildingStructureInformation = buildingStructures.get(buildingStructures.indexOf(buildingStructureInformation));
//
//								if(!StringUtil.isBlankOrNull(entry.getKey())
//										&& entry.getKey().trim().startsWith("BSF")
//										&& FieldMapping.getBuildingStructureFencingDetails.keySet().contains(entry.getKey().trim().substring(4))) {
//									int idxForColumn1 = map.get(entry.getKey());
//									XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//									cell2 = headerDataRow.getCell(idxForColumn1);
//									if (cell1 != null)
//										BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureFencingDetails.get(entry.getKey().trim().substring(4)), String.class).invoke(buildingStructureInformation, cell1.toString());
//								}
//
//								if(!StringUtil.isBlankOrNull(entry.getKey())
//										&& entry.getKey().trim().startsWith("BSB")
//										&& FieldMapping.getBuildingStructureBuildingDetails.keySet().contains(entry.getKey().trim().substring(4))) {
//									int idxForColumn1 = map.get(entry.getKey());
//									XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//									cell2 = headerDataRow.getCell(idxForColumn1);
//									if (cell1 != null)
//										BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureBuildingDetails.get(entry.getKey().trim().substring(4)), String.class).invoke(buildingStructureInformation, cell1.toString());
//								}
//
//								if(!StringUtil.isBlankOrNull(entry.getKey())
//										&& entry.getKey().trim().startsWith("BSG")
//										&& FieldMapping.getBuildingStructureGateDetails.keySet().contains(entry.getKey().trim().substring(4))) {
//									int idxForColumn1 = map.get(entry.getKey());
//									XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//									cell2 = headerDataRow.getCell(idxForColumn1);
//									if (cell1 != null)
//										BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureGateDetails.get(entry.getKey().trim().substring(4)), String.class).invoke(buildingStructureInformation, cell1.toString());
//								}
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							throw new RAPException("Invalid Input field "+cell2.toString()+" in Building Structure ", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//						}
//
//						try {
//							if(!StringUtil.isBlankOrNull(entry.getKey())
//									&& entry.getKey().startsWith("C")
//									&& FieldMapping.getCropInformationDetails.keySet().contains(entry.getKey().substring(2))) {
//								String userKey = entry.getKey().trim().substring(0, 2);
//								AgriculturalLand crop = new AgriculturalLand(userKey);
//								if (!crops.contains(new AgriculturalLand(userKey))) {
//									crops.add(crop);
//								}
//								crop = crops.get(crops.indexOf(crop));
//								int idxForColumn1 = map.get(entry.getKey());
//								XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//								cell2 = headerDataRow.getCell(idxForColumn1);
//								if (cell1 != null)
//									AgriculturalLand.class
//									.getMethod(FieldMapping.getCropInformationDetails.get(entry.getKey().trim().substring(2)),
//											String.class)
//									.invoke(crop, cell1.toString());
//							}
//						} catch (Exception e) {
//
//							throw new RAPException("Invalid Input field " + cell2.toString() + " in Agricultural Land ", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//						}
//
//
//						try {
//							if(!StringUtil.isBlankOrNull(entry.getKey())
//									&& entry.getKey().trim().startsWith("A")
//									&& FieldMapping.getAgreementDetails.keySet().contains(entry.getKey().trim().substring(2))) {
//								String userKey = entry.getKey().trim().substring(0,2);
//								AgreementInformation agree = new AgreementInformation(userKey);
//								if(!agreement.contains(new AgreementInformation(userKey))) {
//									agreement.add(agree);
//								}
//								agree = agreement.get(agreement.indexOf(agree));
//								int idxForColumn1 = map.get(entry.getKey());
//								XSSFCell cell1 = dataRow.getCell(idxForColumn1);
//								cell2 = headerDataRow.getCell(idxForColumn1);
//								if (cell1 != null)
//									AgreementInformation.class.getMethod(FieldMapping.getAgreementDetails.get(entry.getKey().trim().substring(2)), String.class).invoke(agree, cell1.toString());
//							}
//						} catch (Exception e) {
//							// TODO Auto-generated catch block
//							throw new RAPException("Invalid Input field "+cell2.toString()+" in Agreement Information ", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//						}
//
//
//
//					}
//					if (householdinfo.size() > 0) {
//						for (Map.Entry<String, HouseholdInformation> obj : householdinfo.entrySet()) {
//							String member = obj.getKey().substring(0, 2);
//							OwnerInformation owner = new OwnerInformation(member);
//							owner = owners.get(owners.indexOf(owner));
//							owner.getRap_ownerinformation_householdmembers_ownerId().add(obj.getValue());
//						}
//					}
//					//listparcel.add(parcel);
//					compensations.add(compensation);
//					parcel.setRap_parcel_landinformation_parcelid(crops);
//					parcel.setRap_parcel_estimatedcompensation_parcelid(compensations);
//					parcel.setRap_parcel_ownerinformation_parcelid(owners);
//					parcel.setRap_parcel_businessinformation_parcelid(businesses);
//					parcel.setRap_parcel_agreement_parcelid(agreement);
//					parcel.setRap_parcel_buildingstructure_parcelid(buildingStructures);
//					FieldMapping.cleanOwnerEmptyObjects(owners);
//					FieldMapping.cleanCropEmptyObjects(crops);
//					FieldMapping.cleanBusinessEmptyObjects(businesses);
//					FieldMapping.cleanBSEmptyObjects(buildingStructures);
//					FieldMapping.cleanAgreementObjects(agreement);
//					Gson gson = new GsonBuilder().create();
//					String jsonString = gson.toJsonTree(parcel).toString();
//
//					jsonString = jsonString.replace("rap_countryid", "rap_countryid@odata.bind");
//					jsonString = jsonString.replace("rap_projectid", "rap_projectid@odata.bind");
//					System.out.println(jsonString);
//					String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS) + "rap_parcels";
//					output = crmService.postDataToCRM(urlString, jsonString, accessToken, null);
//				}
//			}
//        }catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException  | NoSuchMethodException | SecurityException | IOException e ) {
//			logger.info("Error Occured in extractExcelData:::: ",e);
//			//e.printStackTrace();
//		}
//		return output;
//	}
//
//	public byte[] setRapProjectTitleField(String rapProjectId, String rapProjectTitle, String templatename) throws IOException {
//		Resource resource = resourceLoader.getResource("classpath:files/"+templatename+".xlsx");
//		ByteArrayOutputStream outputStream = null;
//		XSSFWorkbook workbook = null;
//		try(InputStream inputStream = resource.getInputStream();) {
//			workbook = new XSSFWorkbook(inputStream);
//			XSSFSheet sheet = workbook.getSheetAt(0);
//			XSSFRow row = sheet.getRow(0);
//			XSSFCell cell = row.getCell(3);
//			cell.setCellValue(rapProjectTitle);
//			XSSFRow projectId = sheet.getRow(4);
//			XSSFCell projectcell = projectId.getCell(1);
//			projectcell.setCellValue(rapProjectId);
//			outputStream = new ByteArrayOutputStream();
//			workbook.write(outputStream);
//			return outputStream.toByteArray();
//		}catch (IOException e) {
//			logger.error(e.getMessage());
//			return new byte[0];
//		}finally {
//			if(workbook != null)
//				workbook.close();
//			if(outputStream != null)
//				outputStream.close();
//		}
//	}
//}

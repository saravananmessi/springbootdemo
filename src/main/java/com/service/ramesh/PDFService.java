//package org.wb.rap.pdfextractor.service;
//
//import java.io.File;
//import java.io.InputStream;
//import java.io.StringReader;
//import java.io.StringWriter;
//import java.io.Writer;
//import java.util.ArrayList;
//import java.util.Base64;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Scanner;
//
//import javax.xml.XMLConstants;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import javax.xml.transform.OutputKeys;
//import javax.xml.transform.Transformer;
//import javax.xml.transform.TransformerFactory;
//import javax.xml.transform.dom.DOMSource;
//import javax.xml.transform.stream.StreamResult;
//import javax.xml.xpath.XPath;
//import javax.xml.xpath.XPathConstants;
//import javax.xml.xpath.XPathFactory;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.core.env.Environment;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//import org.w3c.dom.Attr;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.wb.rap.common.util.ErrorCodes;
//import org.wb.rap.common.util.StringUtil;
//import org.wb.rap.constants.Constants;
//import org.wb.rap.exception.RAPException;
//import org.wb.rap.pdfextractor.constant.FieldMapping;
//import org.wb.rap.pdfextractor.model.AgreementInformation;
//import org.wb.rap.pdfextractor.model.AgriculturalLand;
//import org.wb.rap.pdfextractor.model.BuildingStructureInformation;
//import org.wb.rap.pdfextractor.model.BusinessInformation;
//import org.wb.rap.pdfextractor.model.CompensationInformation;
//
//import org.wb.rap.pdfextractor.model.HouseholdInformation;
//import org.wb.rap.pdfextractor.model.OwnerInformation;
//import org.wb.rap.pdfextractor.model.ParcelDetail;
//import org.wb.rap.pdfextractor.model.ParcelInformation;
//import org.wb.rap.service.CRMService;
//import org.xml.sax.InputSource;
//
//import com.aspose.pdf.Document;
//import com.aspose.pdf.Field;
//import com.esri.core.geometry.Point;
//import com.esri.core.geometry.ogc.OGCGeometry;
//import com.esri.core.geometry.ogc.OGCPoint;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.google.gson.JsonObject;
//
//@Service
//public class PDFService extends FieldMapping {
//
//	private static final Logger logger = LoggerFactory.getLogger(PDFService.class);
//
//	@Autowired
//	CRMService crmService;
//
//	@Autowired
//	FieldMapping fieldMapping;
//
//	@Autowired
//	Environment env;
//
//	public static final String CRM_BASE_SERVICE_ADDRESS = "crm.base.service.address";
//	public static final String CRM_SERVICE_REQUESTS = "crm.service.requests";
//	public static final String CRM_URL = "crm.url";
//
//
//	public String extractPDFData(InputStream inputStream, String projectGuid, String accessToken) throws Throwable {
//		ParcelInformation parcel = new ParcelInformation();
//		String output = "";
//		try {
//			Document pdfDocument = new Document(inputStream);
//			Field[] fields = pdfDocument.getForm().getFields();
//			List<OwnerInformation> owners = new ArrayList<>();
//			List<AgriculturalLand> crops = new ArrayList<>();
//			List<BusinessInformation> businesses = new ArrayList<>();
//			List<BuildingStructureInformation> buildingStructures = new ArrayList<>();
//			List<AgreementInformation> agreement = new ArrayList<>();
//			parcel.setRap_parcel_ownerinformation_parcelid(owners);
//			parcel.setRap_parcel_landinformation_parcelid(crops);
//			parcel.setRap_parcel_businessinformation_parcelid(businesses);
//			parcel.setRap_parcel_buildingstructure_parcelid(buildingStructures);
//			parcel.setRap_parcel_agreement_parcelid(agreement);
//			LinkedHashMap<String, HouseholdInformation> householdinfo= new LinkedHashMap<>();
//			HouseholdInformation householdInformation=null;
//			parcel.setRap_projectid(projectGuid);
//			CompensationInformation compensation = new CompensationInformation();
//			List<CompensationInformation> compensations = new ArrayList<>();
//			compensations.add(compensation);
//			parcel.setRap_parcel_estimatedcompensation_parcelid(compensations);
//			for(Field field : fields) {
//				String value = field.getValue();
//				if(!StringUtil.isBlankOrNull(field.getActiveState())){
//					value = field.getActiveState();
//				}
//				try {
//
//					if (!StringUtil.isBlankOrNull(field.getFullName())
//							&& FieldMapping.landParcelDetails.keySet().contains(field.getFullName().trim())) {
//						if(field.getFullName().equalsIgnoreCase("parcel ID") && (value==null || value.equalsIgnoreCase(""))) {
//							return output="ERROR_PARCEL";
//						}
//						if(field.getFullName().equalsIgnoreCase("ProjectId") &&  !value.equalsIgnoreCase(projectGuid)) {
//							return output="ERROR_PROJECT";
//						}
//						ParcelInformation.class
//						.getMethod(FieldMapping.landParcelDetails.get(field.getFullName().trim()), String.class)
//						.invoke(parcel, value);
//					} 
//				} catch (Exception e) {
//					throw new RAPException("Invalid Input field "+field.getFullName().trim()+" in Parcel Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName()) && FieldMapping.getCompensationDetails.keySet().contains(field.getFullName().trim())) {
//						CompensationInformation.class.getMethod(FieldMapping.getCompensationDetails.get(field.getFullName().trim()), String.class).invoke(compensation, value);
//					}
//				} catch (Exception e) {
//					throw new RAPException("Invalid Input field "+field.getFullName().trim()+" in Compensation Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("U")
//							&& FieldMapping.getUserInformationDetails.keySet().contains(field.getFullName().trim().substring(2))) {
//						String userKey = field.getFullName().trim().substring(0, 2);
//						OwnerInformation owner = new OwnerInformation(userKey);
//						if (!owners.contains(new OwnerInformation(userKey))) {
//							owners.add(owner);
//						}
//						owner = owners.get(owners.indexOf(owner));
//						owner.getRap_ownerinformation_householdmembers_ownerId();
//						OwnerInformation.class
//						.getMethod(FieldMapping.getUserInformationDetails.get(field.getFullName().trim().substring(2)),
//								String.class)
//						.invoke(owner, value);
//
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field +"+field.getFullName().trim()+"in Owner Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("U")
//							&& FieldMapping.getHousingDetails.keySet().contains(field.getFullName().trim().substring(4))) {
//						String endIndex = field.getFullName().trim().substring(4, field.getFullName().length());
//
//						if (FieldMapping.getHousingDetails.containsKey(endIndex)) {
//							String membersKey = field.getFullName().trim().substring(0, 4);
//							if (householdinfo.containsKey(membersKey)) {
//								householdInformation = householdinfo.get(membersKey);
//								HouseholdInformation.class.getMethod(FieldMapping.getHousingDetails.get(endIndex), String.class)
//								.invoke(householdInformation, value);
//							} else {
//								householdInformation = new HouseholdInformation();
//								HouseholdInformation.class.getMethod(FieldMapping.getHousingDetails.get(endIndex), String.class)
//								.invoke(householdInformation, value);
//								householdinfo.put(membersKey, householdInformation);
//							}
//						}
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field in Household Information"+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("C")
//							&& FieldMapping.getCropInformationDetails.keySet().contains(field.getFullName().trim().substring(2))) {
//						String userKey = field.getFullName().trim().substring(0,2);
//						AgriculturalLand crop = new AgriculturalLand(userKey);
//						if(!crops.contains(new AgriculturalLand(userKey))) {
//							crops.add(crop);
//						}
//						crop = crops.get(crops.indexOf(crop));
//						AgriculturalLand.class.getMethod(FieldMapping.getCropInformationDetails.get(field.getFullName().trim().substring(2)), String.class).invoke(crop, value);
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field for Agriculture Land "+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("BI")
//							&& FieldMapping.getBusinessInformationDetails.keySet().contains(field.getFullName().trim().substring(3))) {
//						String userKey = field.getFullName().trim().substring(0,3);
//						BusinessInformation business = new BusinessInformation(userKey);
//						if(!businesses.contains(new BusinessInformation(userKey))) {
//							businesses.add(business);
//						}
//						business = businesses.get(businesses.indexOf(business));
//						BusinessInformation.class.getMethod(FieldMapping.getBusinessInformationDetails.get(field.getFullName().trim().substring(3)), String.class).invoke(business, value);
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field in Building Information "+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("BS")) {
//						String userKey = field.getFullName().trim().substring(0,2)+field.getFullName().trim().substring(3,4);
//						BuildingStructureInformation buildingStructureInformation = new BuildingStructureInformation(userKey);
//						if(!buildingStructures.contains(buildingStructureInformation)) {
//							buildingStructures.add(buildingStructureInformation);
//						}
//						buildingStructureInformation = buildingStructures.get(buildingStructures.indexOf(buildingStructureInformation));
//
//						if(!StringUtil.isBlankOrNull(field.getFullName())
//								&& field.getFullName().trim().startsWith("BSF")
//								&& FieldMapping.getBuildingStructureFencingDetails.keySet().contains(field.getFullName().trim().substring(4))) {
//							BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureFencingDetails.get(field.getFullName().trim().substring(4)), String.class).invoke(buildingStructureInformation, value);
//						}
//
//						if(!StringUtil.isBlankOrNull(field.getFullName())
//								&& field.getFullName().trim().startsWith("BSB")
//								&& FieldMapping.getBuildingStructureBuildingDetails.keySet().contains(field.getFullName().trim().substring(4))) {
//							BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureBuildingDetails.get(field.getFullName().trim().substring(4)), String.class).invoke(buildingStructureInformation, value);
//						}
//
//						if(!StringUtil.isBlankOrNull(field.getFullName())
//								&& field.getFullName().trim().startsWith("BSG")
//								&& FieldMapping.getBuildingStructureGateDetails.keySet().contains(field.getFullName().trim().substring(4))) {
//							BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureGateDetails.get(field.getFullName().trim().substring(4)), String.class).invoke(buildingStructureInformation, value);
//						}
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field in Building Structure "+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("A")
//							&& FieldMapping.getAgreementDetails.keySet().contains(field.getFullName().trim().substring(2))) {
//						String userKey = field.getFullName().trim().substring(0,2);
//						AgreementInformation agree = new AgreementInformation(userKey);
//						if(!agreement.contains(new AgreementInformation(userKey))) {
//							agreement.add(agree);
//						}
//						agree = agreement.get(agreement.indexOf(agree));
//						AgreementInformation.class.getMethod(FieldMapping.getAgreementDetails.get(field.getFullName().trim().substring(2)), String.class).invoke(agree, value);
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field in Agreement Information "+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//
//			}
//
//			if (householdinfo.size() > 0) {
//				for (Map.Entry<String, HouseholdInformation> obj : householdinfo.entrySet()) {
//					String member = obj.getKey().substring(0, 2);
//					OwnerInformation owner = new OwnerInformation(member);
//					owner = owners.get(owners.indexOf(owner));
//					owner.getRap_ownerinformation_householdmembers_ownerId().add(obj.getValue());
//				}
//			}
//
//
//			FieldMapping.cleanOwnerEmptyObjects(owners);
//			FieldMapping.cleanCropEmptyObjects(crops);
//			FieldMapping.cleanBusinessEmptyObjects(businesses);
//			FieldMapping.cleanBSEmptyObjects(buildingStructures);
//			FieldMapping.cleanAgreementObjects(agreement);
//			Gson gson = new GsonBuilder().create();
//			String jsonString = gson.toJsonTree(parcel).toString(); //temp fix
//			jsonString = jsonString.replace("rap_projectid", "rap_projectid@odata.bind");
//			jsonString = jsonString.replace("rap_countryid", "rap_countryid@odata.bind");
//			//System.out.println("Final Output String ---- >"+jsonString);
//			String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS) + "rap_parcels";
//			output = crmService.postDataToCRM(urlString, jsonString, accessToken, null);
//			//System.out.println(output);
//		} catch (Exception e) {
//			/*System.out.println("Error Occured in getParcelDetails " + e.getMessage());
//			e.printStackTrace();
//			return e.getMessage();*/
//
//			throw new RAPException("Error occured in Calling the RAP Service "+e.getMessage(), ErrorCodes.valueOf("UPDATE_FAILED"));
//		}
//		return output;
//	}
//	public DocumentBuilderFactory getNewDocumentBuilderFactoryInstance() throws ParserConfigurationException {
//		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
//		dbFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
////		dbFactory.setFeature(XMLConstants.ACCESS_EXTERNAL_DTD, false);
////		dbFactory.setFeature(XMLConstants.ACCESS_EXTERNAL_SCHEMA, false);
////		dbFactory.setFeature(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, false);
//		dbFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
//		dbFactory.setValidating(false);
//		return dbFactory;
//	}
//
//	public String getUpdatedKmlFile(String parcelId,MultipartFile file) throws Exception {
//		DocumentBuilderFactory dbFactory = null;
//		DocumentBuilder dBuilder =null;
//		org.w3c.dom.Document doc =null;
//		String output="";
//		try {
//			dbFactory = getNewDocumentBuilderFactoryInstance();
//			dBuilder = dbFactory.newDocumentBuilder();
//			doc = dBuilder.parse(file.getInputStream());
//			doc.getDocumentElement().normalize();
//
//			addElement(doc,parcelId);
//
//			doc.getDocumentElement().normalize();
//			TransformerFactory transformerFactory = TransformerFactory.newInstance();
//			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//			transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
//			Transformer transformer = transformerFactory.newTransformer();
//			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//			Writer out = new StringWriter();
//			transformer.transform(new DOMSource(doc), new StreamResult(out));
//			output=out.toString();
//
//		} catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return output;
//	}
//
//
//
//	private void addElement(org.w3c.dom.Document doc,String parcelId) {
//		Element placemarkNode = null;
//		NodeList placemark = doc.getElementsByTagName("Placemark");
//		for(int i=0; i<placemark.getLength();i++){
//			placemarkNode = (Element) placemark.item(i);
//			if(placemarkNode.getAttribute("id").equals(parcelId)) {
//				Element labelStyle = doc.createElement("LabelStyle");
//				Element labelStyleColor = doc.createElement("color");
//				labelStyleColor.appendChild(doc.createTextNode("00000000"));
//				Element labelStyleScale = doc.createElement("scale");
//				labelStyleScale.appendChild(doc.createTextNode("0"));
//				labelStyle.appendChild(labelStyleColor);
//				labelStyle.appendChild(labelStyleScale);
//
//				Element lineStyle = doc.createElement("LineStyle");
//				Element lineStyleColor = doc.createElement("color");
//				lineStyleColor.appendChild(doc.createTextNode("ad00a838"));
//				Element lineStyleWidth = doc.createElement("width");
//				lineStyleWidth.appendChild(doc.createTextNode("2"));
//				lineStyle.appendChild(lineStyleColor);
//				lineStyle.appendChild(lineStyleWidth);
//
//				Element polyStyle = doc.createElement("PolyStyle");
//				Element polyStyleColor = doc.createElement("color");
//				polyStyleColor.appendChild(doc.createTextNode("adb0f1ae"));
//				polyStyle.appendChild(polyStyleColor);
//
//				placemarkNode.appendChild(labelStyle); 
//				placemarkNode.appendChild(lineStyle);
//				placemarkNode.appendChild(polyStyle);
//			}
//		}
//	}
//
//	public String getCentroidPolygonKml(String parcelId,String kmlFileContent) throws Throwable {
//		DocumentBuilderFactory dbFactory = null;
//		DocumentBuilder dBuilder =null;
//		org.w3c.dom.Document doc =null;
//		String jsonString = null;
//		Scanner in =null;
//		try {
//			dbFactory = getNewDocumentBuilderFactoryInstance();
//			dBuilder = dbFactory.newDocumentBuilder();
//			doc = dBuilder.parse(new InputSource(new StringReader(kmlFileContent)));
//			doc.getDocumentElement().normalize();
//			
//			Element placemarkNode = null;
//			NodeList placemark = doc.getElementsByTagName("Placemark");
//			String coordinatesValues = null;
//			StringBuilder builder= new StringBuilder();
//			for(int i=0; i<placemark.getLength();i++){
//				placemarkNode = (Element) placemark.item(i);
//				if(placemarkNode.getAttribute("id").equals(parcelId)) {
//					Node coordinates = (Element)placemarkNode.getElementsByTagName("coordinates").item(0);
//					coordinatesValues =coordinates.getTextContent();
//					String[] points = coordinatesValues.split("\\s+");
//					for(String point : points) {
//						in = new Scanner(point);
//						in.useDelimiter(",");
//						if(in.hasNextDouble()) {
//							Point p = new Point(in.nextDouble(), in.nextDouble(), in.nextDouble());
//							builder.append(p.getX() + " "+ p.getY()+",");
//
//						}
//					}
//				}
//			}
//			kmlFileContent = builder.length() > 0 ? builder.substring(0, builder.length() - 1): "";
//			OGCGeometry geometry = OGCGeometry.fromText("POLYGON (("+kmlFileContent+"))");
//			OGCGeometry centroid = geometry.centroid();
//			double latitude =(Double)(((OGCPoint)centroid).X());
//			double longtitude =(Double)(((OGCPoint)centroid).Y());
//			HashMap<String,String> map = new HashMap<>();
//			map.put("parcelId", parcelId);
//			map.put("latitude", String.valueOf(latitude));
//			map.put("longtitude", String.valueOf(longtitude));
//
//			Gson gson = new GsonBuilder().create();
//			jsonString = gson.toJsonTree(map).toString();
//
//		}catch (Exception e) {
//			logger.error(e.getMessage());
//		}finally {
//			if(in != null)
//				in.close();
//		}
//		return jsonString;
//	}
//
//	public String getOnlyParcelKmlFile(String kmlFileContent, String parcelId) throws Exception {
//		DocumentBuilderFactory dbf = null;
//		DocumentBuilder db = null;
//		org.w3c.dom.Document currentDoc =null;
//		String jsonString ="";
//		
//		try {
//			
//			dbf = getNewDocumentBuilderFactoryInstance();
//			db = dbf.newDocumentBuilder();
//			currentDoc = db.parse(new InputSource(new StringReader(kmlFileContent)));
//			
//			XPath xpath = XPathFactory.newInstance().newXPath();
//			NodeList document = (NodeList) xpath.evaluate("//Document", currentDoc,  XPathConstants.NODESET);
//			NodeList Folder = (NodeList) xpath.evaluate("//Folder", currentDoc,  XPathConstants.NODESET);
//			NodeList Folder_nodes = (NodeList) xpath.evaluate("//Folder//Folder", currentDoc,  XPathConstants.NODESET);
//	
//			Element rootNode;
//			rootNode = currentDoc.createElement("kml");
//			SetNamespaceAttribute(currentDoc,rootNode);
//	
//			Node documentNode = currentDoc.importNode(document.item(0), true);
//			Element documentElement = (Element) documentNode;
//	
//			Element documentName = getElementByTagName(documentElement,"name");
//			Element documentSnippet = getElementByTagName(documentElement,"Snippet");
//	
//			Node parentFolderNode = currentDoc.importNode(Folder.item(0), true);
//			Element parentFolderElement = (Element) parentFolderNode;
//	
//			Element parentFolderName = getElementByTagName(parentFolderElement,"name");
//			Element parentFolderSnippet = getElementByTagName(parentFolderElement,"Snippet");
//	
//			for(int j = 1; j <= Folder_nodes.getLength() ;j++) {
//	
//				Node folderNode = currentDoc.importNode(Folder_nodes.item(j-1), true);
//				Element folderElement = (Element) folderNode;
//	
//				Element elementName = getElementByTagName(folderElement,"name");
//				Element elementSnippet = getElementByTagName(folderElement,"Snippet");
//	
//				NodeList placemarkList = folderElement.getElementsByTagName("Placemark");
//	
//				for (int i=1; i <= placemarkList.getLength(); i++) {
//	
//					Node imported = currentDoc.importNode(placemarkList.item(i-1), true);
//					Element placemarkElement = (Element)placemarkList.item(i-1);
//					if(placemarkElement.getAttribute("id").equalsIgnoreCase(parcelId)  && placemarkElement.getElementsByTagName("MultiGeometry").getLength() > 0) {
//	
//						Element rootDocument = createElement(currentDoc,documentElement,"Document");
//						Element rootParentFolder = createElement(currentDoc,parentFolderElement,"Folder");
//						Element rootSubFolder = createElement(currentDoc,folderElement,"Folder");
//	
//						rootSubFolder.appendChild(elementName);
//						rootSubFolder.appendChild(elementSnippet);
//						rootSubFolder.appendChild(imported);
//	
//						rootParentFolder.appendChild(parentFolderName);
//						rootParentFolder.appendChild(parentFolderSnippet);
//						rootParentFolder.appendChild(rootSubFolder);
//	
//						rootDocument.appendChild(documentName);
//						rootDocument.appendChild(documentSnippet);
//						rootDocument.appendChild(rootParentFolder);
//	
//						rootNode.appendChild(rootDocument);
//						jsonString = prettyPrint(rootNode);
//						rootNode = currentDoc.createElement("kml");
//						SetNamespaceAttribute(currentDoc,rootNode);
//					}
//				}
//	
//			}
//			currentDoc.normalize();
//		}catch (Exception e) {
//			logger.error(e.getMessage());
//		}
//		return jsonString;
//
//	}
//
//	public String getCentroidParcelDetailsKml(String rapProjectId, String kmlContent,String accessToken) throws Throwable {
//		DocumentBuilderFactory dbFactory = null;
//		DocumentBuilder dBuilder = null;
//		org.w3c.dom.Document doc =null;
//		String jsonString = null;
//		String kmlFileContent = null;
//		Scanner in =null;
//		try {
//			dbFactory = getNewDocumentBuilderFactoryInstance();
//			dBuilder = dbFactory.newDocumentBuilder();
//			doc = dBuilder.parse(new InputSource(new StringReader(kmlContent)));
//			doc.getDocumentElement().normalize();
//			
//			Element placemarkNode = null;
//			NodeList placemark = doc.getElementsByTagName("Placemark");
//			String coordinatesValues = null;
//			JSONArray jsonArray = new JSONArray();
//			double latitude = 0;
//			double longtitude = 0;
//
//			for(int i=0; i<placemark.getLength();i++){
//				StringBuilder builder= new StringBuilder();
//				JSONObject jsonObj = new JSONObject();
//				placemarkNode = (Element) placemark.item(i);
//				String parcelIdFromKml= placemarkNode.getAttribute("id");
//				if(!StringUtil.isBlankOrNull(parcelIdFromKml)) {
//					Node coordinates = (Element)placemarkNode.getElementsByTagName("coordinates").item(0);
//					coordinatesValues =coordinates.getTextContent();
//					String[] points = coordinatesValues.split("\\s+");
//					for(String point : points) {
//						in = new Scanner(point);
//						in.useDelimiter(",");
//						if(in.hasNextDouble()) {
//							Point p = new Point(in.nextDouble(), in.nextDouble(), in.nextDouble());
//							builder.append(p.getX() + " "+ p.getY()+",");
//						}
//					}
//					kmlFileContent = builder.length() > 0 ? builder.substring(0, builder.length() - 1): "";
//					OGCGeometry geometry = OGCGeometry.fromText("POLYGON (("+kmlFileContent+"))");
//					OGCGeometry centroid = geometry.centroid();
//					latitude =(Double)(((OGCPoint)centroid).Y());
//					longtitude =(Double)(((OGCPoint)centroid).X());
//					//String color = null;
//					/*Node statusNode = (Element)placemarkNode.getElementsByTagName("status").item(0);	
//				if(statusNode != null && !statusNode.getNodeValue().equalsIgnoreCase("") && statusNode.getNodeValue().equalsIgnoreCase("status")) {
//					Node colorNode = (Element)placemarkNode.getElementsByTagName("color").item(0);
//					if(!colorNode.getNodeValue().equalsIgnoreCase(""))
//						color=colorNode.getNodeValue();
//				}*/
//
//					String parcelDetails = crmService.crmGetParcelDetails(rapProjectId, parcelIdFromKml, accessToken);
//					jsonObj.put("parcelDetails", parcelDetails);
//					jsonObj.put("label", parcelIdFromKml);
//					jsonObj.put("lat", String.valueOf(latitude));
//					jsonObj.put("lng", String.valueOf(longtitude));
//					jsonObj.put("color", crmService.getStatusColor(parcelDetails));
//					jsonArray.put(jsonObj);
//				}
//			}
//			JSONObject jsonObj = new JSONObject();
//			jsonObj.put("type", "labels");
//			jsonObj.put("labels", jsonArray);
//			jsonObj.put("customPopup", true);
//
//			jsonString = jsonObj.toString();
//			
//		}catch (Exception e) {
//			logger.error(e.getMessage());
//		}finally {
//			if(in != null)
//				in.close();
//		}
//
//		return jsonString;
//	}
//
//    public String uploadParcelKml(String projectGuid,String parcelGuid,String parcelUniqueId,MultipartFile file, String accessToken) throws Throwable{
//        DocumentBuilderFactory dbFactory = null;
//        DocumentBuilder dBuilder = null;
//        org.w3c.dom.Document doc =null;
//        String output= null;
//        String error = null;
//        String resParcelKml = null;
//        try {
//        	dbFactory = getNewDocumentBuilderFactoryInstance();
//            dBuilder = dbFactory.newDocumentBuilder();
//            doc = dBuilder.parse(file.getInputStream());
//            doc.getDocumentElement().normalize();
//            
//            if(checkParcelUniqueIdExists(doc,parcelUniqueId)) {
//            	resParcelKml = crmService.uploadParcelKml(file,parcelGuid,accessToken);
//                String res = mergeParcelKmlInProjectKml(projectGuid,parcelGuid,parcelUniqueId,accessToken,doc);
//                if(res != null) {
//	                String encodedFile = new String(Base64.getEncoder().encode(res.getBytes()));
//	        		String reqJson ="{\"documentbody\":\"" + encodedFile + "\",\"filename\":\""+ file.getOriginalFilename() + "\",\"subject\":\""+ file.getOriginalFilename() + "\",\"isdocument\":true,\"objectid_rap_projectdetails@odata.bind\":\"rap_projectdetailses("+ projectGuid +")\"}";
//	        		crmService.crmPostRequest("v9.1", "annotations",reqJson, accessToken);
//                }
//            }else {
//            	error="error";
//            }
//             output = error !=null ?  error : resParcelKml;
//        } catch (Exception e) {
//            logger.error(e.getMessage());
//        }
//    return output;
//}
//
//private boolean checkParcelUniqueIdExists(org.w3c.dom.Document doc,String parcelUniqueId) {
//    boolean error = false;
//    Element placemarkNode = null;
//    NodeList placemark = doc.getElementsByTagName("Placemark");
//    for(int i=0; i<placemark.getLength();i++){
//        placemarkNode = (Element) placemark.item(i);
//        error= placemarkNode.getAttribute("id").equals(parcelUniqueId);
//        if(error)
//        	return error;
//    }
//    return error;
//}
//
//public String mergeParcelKmlInProjectKml(String projectGuid,String parcelGuid,String parcelUniqueId, String accessToken,org.w3c.dom.Document uploadedDoc) throws Throwable {
//    String projectKmlFileContent = null;
//    DocumentBuilderFactory dbFactory = null;
//    DocumentBuilder dBuilder = null;
//    org.w3c.dom.Document projectdoc =null;
//    String output = null;
//    try {
//    	  dbFactory = getNewDocumentBuilderFactoryInstance();
//    	  dBuilder = dbFactory.newDocumentBuilder();
//  	      projectKmlFileContent = crmService.getProjectKmlFile(projectGuid, accessToken);
//	    
//	    if(projectKmlFileContent != null) {
//	    	 	projectdoc = dBuilder.parse(new InputSource(new StringReader(projectKmlFileContent)));
//	    	 	
//		    	if(checkParcelUniqueIdExists(projectdoc,parcelUniqueId)) {
//			        //Parcel is already found in the project’s kml file 
//			        // replace coordinates
//			        NodeList placemark = uploadedDoc.getElementsByTagName("Placemark");
//			        String coordinatesValues = null;
//			        Element placemarkNode = (Element) placemark.item(0);
//			        Node coordinates = (Element)placemarkNode.getElementsByTagName("coordinates").item(0);
//			        coordinatesValues =coordinates.getTextContent();
//			        
//			        NodeList projectDocPlacemark = projectdoc.getElementsByTagName("Placemark");
//			            for(int i=0; i<projectDocPlacemark.getLength();i++){
//			                Element projectDocPlacemarkNode = (Element) projectDocPlacemark.item(i);
//			                if(parcelUniqueId.equalsIgnoreCase(projectDocPlacemarkNode.getAttribute("id"))) {
//			                    Node projectDocCoordinatesNode = (Element)projectDocPlacemarkNode.getElementsByTagName("coordinates").item(0);
//			                    projectDocCoordinatesNode.setTextContent(coordinatesValues);
//			                }
//			            }
//		         
//		    }else{
//		        //Parcel is not found in the project’s kml file 
//		        // merge uploaded kml file into project kml
//		    	Map<String,List<Element>> map = new HashMap<>();
//		    	NodeList uploadFolderNodeList = uploadedDoc.getElementsByTagName("Folder");
//		    	Element uploadFolderNode = (Element) uploadFolderNodeList.item(0);
//		    	NodeList uploadSubFolderNodeList =uploadFolderNode.getElementsByTagName("Folder");
//		    	
//		    	 for(int i=0; i<uploadSubFolderNodeList.getLength();i++){
//		    		 Element uploadSubFolderElement = (Element)uploadedDoc.importNode(uploadSubFolderNodeList.item(i), true);
//		    		 NodeList placemarkNodeList = uploadSubFolderElement.getElementsByTagName("Placemark");
//		    		 List<Element> list = new ArrayList<>();
//		    		 for(int j=0; j<placemarkNodeList.getLength();j++) {
//		    			 Element placemarkNode = (Element)uploadedDoc.importNode(placemarkNodeList.item(j), true);
//		    			 if(placemarkNode.getAttribute("id").equalsIgnoreCase(parcelUniqueId))
//		    				 list.add(placemarkNode);
//		    		 }
//		    		 map.put(uploadSubFolderElement.getAttribute("id"),list );
//		    	 }
//		    	 
//		    	 NodeList projectFolderNodeList = projectdoc.getElementsByTagName("Folder");
//		    	 Element projectFolderNode = (Element) projectFolderNodeList.item(0);
//		    	 NodeList projectSubFolderNodeList =projectFolderNode.getElementsByTagName("Folder");
//		    	 
//		    	 for(int i=0; i<projectSubFolderNodeList.getLength();i++){
//		    		 List<Element> projectList = new ArrayList<>();
//		    		 Element projectSubFolderNode =(Element)projectSubFolderNodeList.item(i);
//		    		 if(map.containsKey(projectSubFolderNode.getAttribute("id"))) {
//		    			 projectList=map.get(projectSubFolderNode.getAttribute("id"));
//		    			 for(Element node : projectList) {
//		    				 Element newNode =(Element) projectdoc.importNode(node, true);
//		    				 projectSubFolderNode.appendChild(newNode);
//		    			 }
//		    		 }
//		    		 
//		    	 }
//		    }								
//	    }else {
//			 projectdoc = uploadedDoc;
//		}
//	    
//	    projectdoc.getDocumentElement().normalize();
//	    TransformerFactory transformerFactory = TransformerFactory.newInstance();
//	    Transformer transformer = transformerFactory.newTransformer();
//	    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
//	    Writer out = new StringWriter();
//	    transformer.transform(new DOMSource(projectdoc), new StreamResult(out));
//	    output=out.toString();
//    }catch (Exception e) {
//		logger.error(e.getMessage());
//	}
//    return output;
//}
//
//
//	public static final String prettyPrint(Node xml) throws Exception {
//		TransformerFactory tfFactory = TransformerFactory.newInstance(); 
//		tfFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
//		tfFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
//		Transformer tf = tfFactory.newTransformer();
//		tf.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
//		tf.setOutputProperty(OutputKeys.INDENT, "yes");
//		Writer out = new StringWriter();
//		tf.transform(new DOMSource(xml), new StreamResult(out));
//		return out.toString();
//	}
//
//
//	public static void SetNamespaceAttribute( org.w3c.dom.Document doc,Element rootElement) {
//		Attr attr = doc.createAttribute("xmlns");
//		attr.setValue("http://www.opengis.net/kml/2.2");
//		rootElement.setAttributeNode(attr);
//
//		attr = doc.createAttribute("xmlns:atom");
//		attr.setValue("http://www.w3.org/2005/Atom");
//		rootElement.setAttributeNode(attr);
//
//		attr = doc.createAttribute("xmlns:gx");
//		attr.setValue("http://www.google.com/kml/ext/2.2");
//		rootElement.setAttributeNode(attr);
//
//		attr = doc.createAttribute("xmlns:kml");
//		attr.setValue("http://www.opengis.net/kml/2.2");
//		rootElement.setAttributeNode(attr);
//
//	}
//
//	public static final Element getElementByTagName(Element ele, String tagName) {
//		Element element = (Element)ele.getElementsByTagName(tagName).item(0);
//		return element;
//	}
//
//	public static final Element createElement( org.w3c.dom.Document doc, Element element, String elmentName) {
//		Element createElement = doc.createElement(elmentName);
//		createElement.setAttribute("id", element.getAttribute("id"));
//		return createElement;
//	}
//	public String updatePDFParcel(File file,InputStream inputStream, String rapParcelId, String accessToken) throws Throwable {
//		Document pdfDocument = new Document(inputStream);
//		Field[] fields = pdfDocument.getForm().getFields();
//		ParcelDetail parcel = new ParcelDetail();
//		CompensationInformation compensation = new CompensationInformation();
//		List<OwnerInformation> owners = new ArrayList<>();
//		List<AgriculturalLand> crops = new ArrayList<>();
//		List<BusinessInformation> businesses = new ArrayList<>();
//		List<BuildingStructureInformation> buildingStructures = new ArrayList<>();
//		List<AgreementInformation> agreement = new ArrayList<>();
//		LinkedHashMap<String, HouseholdInformation> householdinfo= new LinkedHashMap<>();
//		HouseholdInformation householdInformation=null;
//		//parcel.setRap_parcelid(rapParcelId);
//		String output = "";
//		for (Field field : fields) {
//			try {
//				String value = field.getValue();
//				if (!StringUtil.isBlankOrNull(field.getActiveState())) {
//					value = field.getActiveState();
//				}
//				try {
//
//					if (!StringUtil.isBlankOrNull(field.getFullName())
//							&& FieldMapping.updateParcelDetails.keySet().contains(field.getFullName().trim())) {
//						
//						ParcelDetail.class
//								.getMethod(FieldMapping.updateParcelDetails.get(field.getFullName().trim()), String.class)
//								.invoke(parcel, value);
//						
//					}
//				} catch (Exception e) {
//					throw new RAPException("Invalid Input field " + field.getFullName().trim() + " in Parcel Information",
//							ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName()) && FieldMapping.getCompensationDetails.keySet().contains(field.getFullName().trim())) {
//						CompensationInformation.class.getMethod(FieldMapping.getCompensationDetails.get(field.getFullName().trim()), String.class).invoke(compensation, value);
//					}
//				} catch (Exception e) {
//					throw new RAPException("Invalid Input field "+field.getFullName().trim()+" in Compensation Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("C")
//							&& FieldMapping.getCropInformationDetails.keySet().contains(field.getFullName().trim().substring(2))) {
//						String userKey = field.getFullName().trim().substring(0,2);
//						
//						AgriculturalLand crop = new AgriculturalLand(userKey);
//						if(!crops.contains(new AgriculturalLand(userKey))) {
//							crops.add(crop);
//						}
//						crop = crops.get(crops.indexOf(crop));
//						AgriculturalLand.class.getMethod(FieldMapping.getCropInformationDetails.get(field.getFullName().trim().substring(2)), String.class).invoke(crop, value);
//				
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field for Agriculture Land "+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("BI")
//							&& FieldMapping.getBusinessInformationDetails.keySet().contains(field.getFullName().trim().substring(3))) {
//						String userKey = field.getFullName().trim().substring(0,3);
//						BusinessInformation business = new BusinessInformation(userKey);
//						if(!businesses.contains(new BusinessInformation(userKey))) {
//							businesses.add(business);
//						}
//						business = businesses.get(businesses.indexOf(business));
//						BusinessInformation.class.getMethod(FieldMapping.getBusinessInformationDetails.get(field.getFullName().trim().substring(3)), String.class).invoke(business, value);
//
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field in Building Information "+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("BS")) {
//						String userKey = field.getFullName().trim().substring(0,2)+field.getFullName().trim().substring(3,4);
//						BuildingStructureInformation buildingStructureInformation = new BuildingStructureInformation(userKey);
//						if(!buildingStructures.contains(buildingStructureInformation)) {
//							buildingStructures.add(buildingStructureInformation);
//						}
//						buildingStructureInformation = buildingStructures.get(buildingStructures.indexOf(buildingStructureInformation));
//
//				
//
//						if(!StringUtil.isBlankOrNull(field.getFullName())
//								&& field.getFullName().trim().startsWith("BSF")
//								&& FieldMapping.getBuildingStructureFencingDetails.keySet().contains(field.getFullName().trim().substring(4))) {
//							BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureFencingDetails.get(field.getFullName().trim().substring(4)), String.class).invoke(buildingStructureInformation, value);
//						}
//
//						if(!StringUtil.isBlankOrNull(field.getFullName())
//								&& field.getFullName().trim().startsWith("BSB")
//								&& FieldMapping.getBuildingStructureBuildingDetails.keySet().contains(field.getFullName().trim().substring(4))) {
//							BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureBuildingDetails.get(field.getFullName().trim().substring(4)), String.class).invoke(buildingStructureInformation, value);
//						}
//
//						if(!StringUtil.isBlankOrNull(field.getFullName())
//								&& field.getFullName().trim().startsWith("BSG")
//								&& FieldMapping.getBuildingStructureGateDetails.keySet().contains(field.getFullName().trim().substring(4))) {
//							BuildingStructureInformation.class.getMethod(FieldMapping.getBuildingStructureGateDetails.get(field.getFullName().trim().substring(4)), String.class).invoke(buildingStructureInformation, value);
//						}
//					}
//					//ary.add(buildingStructureInformation);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field in Building Structure "+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("A")
//							&& FieldMapping.getAgreementDetails.keySet().contains(field.getFullName().trim().substring(2))) {
//						String userKey = field.getFullName().trim().substring(0,2);
//						AgreementInformation agree = new AgreementInformation(userKey);
//						if(!agreement.contains(new AgreementInformation(userKey))) {
//							agreement.add(agree);
//						}
//						agree = agreement.get(agreement.indexOf(agree));
//						AgreementInformation.class.getMethod(FieldMapping.getAgreementDetails.get(field.getFullName().trim().substring(2)), String.class).invoke(agree, value);
//							}
//					//ary.add(agree);
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field in Agreement Information "+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("U")
//							&& FieldMapping.getUserInformationDetails.keySet().contains(field.getFullName().trim().substring(2))) {
//						String userKey = field.getFullName().trim().substring(0, 2);
//						OwnerInformation owner = new OwnerInformation(userKey);
//						if (!owners.contains(new OwnerInformation(userKey))) {
//							owners.add(owner);
//						}
//						owner = owners.get(owners.indexOf(owner));
//						owner.getRap_ownerinformation_householdmembers_ownerId();
//						OwnerInformation.class
//						.getMethod(FieldMapping.getUserInformationDetails.get(field.getFullName().trim().substring(2)),
//								String.class)
//						.invoke(owner, value);
//
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field +"+field.getFullName().trim()+"in Owner Information", ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//				try {
//					if(!StringUtil.isBlankOrNull(field.getFullName())
//							&& field.getFullName().trim().startsWith("U")
//							&& FieldMapping.getHousingDetails.keySet().contains(field.getFullName().trim().substring(4))) {
//						String endIndex = field.getFullName().trim().substring(4, field.getFullName().length());
//
//						if (FieldMapping.getHousingDetails.containsKey(endIndex)) {
//							String membersKey = field.getFullName().trim().substring(0, 4);
//							if (householdinfo.containsKey(membersKey)) {
//								householdInformation = householdinfo.get(membersKey);
//								HouseholdInformation.class.getMethod(FieldMapping.getHousingDetails.get(endIndex), String.class)
//								.invoke(householdInformation, value);
//							} else {
//								householdInformation = new HouseholdInformation();
//								HouseholdInformation.class.getMethod(FieldMapping.getHousingDetails.get(endIndex), String.class)
//								.invoke(householdInformation, value);
//								householdinfo.put(membersKey, householdInformation);
//							}
//						}
//					}
//				} catch (Exception e) {
//					// TODO Auto-generated catch block
//					throw new RAPException("Invalid Input field in Household Information"+e.getCause(), ErrorCodes.valueOf("DATAACCESS_EXCEPTION_FAILED"));
//				}
//			} catch (Exception e) {
//			
//			}
//			finally {
//				if (file.exists())
//					file.delete();
//			}
//
//		}
//		System.out.println("householdInformation"+householdInformation);
//		System.out.println("householdinfo"+householdinfo);
//		if (householdinfo.size() > 0) {
//			for (Map.Entry<String, HouseholdInformation> obj : householdinfo.entrySet()) {
//				String member = obj.getKey().substring(0, 2);
//				OwnerInformation owner = new OwnerInformation(member);
//				owner = owners.get(owners.indexOf(owner));
//				owner.getRap_ownerinformation_householdmembers_ownerId().add(obj.getValue());
//			}
//		}
//
//		Gson gson = new GsonBuilder().create();
//		FieldMapping.cleanOwnerEmptyObjects(owners);
//		FieldMapping.cleanCropEmptyObjects(crops);
//		FieldMapping.cleanBusinessEmptyObjects(businesses);
//		FieldMapping.cleanBSEmptyObjects(buildingStructures);
//		FieldMapping.cleanAgreementObjects(agreement);
//		 gson = new GsonBuilder().create();
//		
//		updateParcelDetail(gson.toJsonTree(parcel).toString(), rapParcelId, accessToken, "rap_parcels");
//		JsonObject inputObj = gson.fromJson(gson.toJsonTree(compensation), JsonObject.class);
//		inputObj.addProperty(Constants.PARCEL_ID_BIND, "/rap_parcels(" + rapParcelId + ")");
//		updateParcelDetail(gson.toJsonTree(inputObj).toString(), rapParcelId, accessToken,
//				Constants.ESTIMATE_COMPENSATION);
//		for (AgriculturalLand updCrops : crops) {
//			JsonObject updCropss = gson.fromJson(gson.toJsonTree(updCrops).toString(), JsonObject.class);
//			updCropss.addProperty(Constants.PARCEL_ID_BIND, "/rap_parcels(" + rapParcelId + ")");
//			String uniqueId = rapParcelIdreteriveLandUniquiqueId(updCrops, rapParcelId, "rap_landinformationid",
//					Constants.LAND_INFORMATION, accessToken);
//			if (!uniqueId.equalsIgnoreCase(Constants.NEW_REQUEST)) {
//				updateParcelDetail(updCropss.toString(), uniqueId, accessToken, Constants.LAND_INFORMATION);
//			} else {
//				String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS)
//						+ Constants.LAND_INFORMATION;
//				crmService.postDataToCRM(urlString, updCropss.toString(), accessToken, null);
//			}
//		}
//		for (BuildingStructureInformation updbuildingStruc : buildingStructures) {
//			JsonObject updbuildings = gson.fromJson(gson.toJsonTree(updbuildingStruc).toString(), JsonObject.class);
//			updbuildings.addProperty(Constants.PARCEL_ID_BIND, "/rap_parcels(" + rapParcelId + ")");
//			String uniqueId = reteriveBuildingUniquiqueId(updbuildingStruc, rapParcelId, "rap_buildingstructureid",
//					Constants.BUILDING_STRUCTURE, accessToken);
//			if (!uniqueId.equalsIgnoreCase(Constants.NEW_REQUEST)) {
//				updateParcelDetail(updbuildings.toString(), uniqueId, accessToken, Constants.BUILDING_STRUCTURE);
//			} else {
//				String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS)
//						+ Constants.BUILDING_STRUCTURE;
//				crmService.postDataToCRM(urlString, updbuildings.toString(), accessToken, null);
//			}
//		}
//
//		for (BusinessInformation updbusinesses : businesses) {
//			JsonObject updbusins = gson.fromJson(gson.toJsonTree(updbusinesses).toString(), JsonObject.class);
//			updbusins.addProperty(Constants.PARCEL_ID_BIND, "/rap_parcels(" + rapParcelId + ")");
//			String uniqueId = reteriveBusinessUniquiqueId(updbusinesses, rapParcelId, "rap_businessinformationid",
//					Constants.BUSINESS_INFORMATION, accessToken);
//			if (!uniqueId.equalsIgnoreCase(Constants.NEW_REQUEST)) {
//				updateParcelDetail(updbusins.toString(), uniqueId, accessToken, Constants.BUSINESS_INFORMATION);
//			} else {
//				String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS)
//						+ Constants.BUSINESS_INFORMATION;
//				crmService.postDataToCRM(urlString, updbusins.toString(), accessToken, null);
//			}
//		}
//
//		for (AgreementInformation updAggrement : agreement) {
//			JsonObject updAggr = gson.fromJson(gson.toJsonTree(updAggrement).toString(), JsonObject.class);
//			updAggr.addProperty(Constants.PARCEL_ID_BIND, "/rap_parcels(" + rapParcelId + ")");
//			String uniqueId = reteriveAgreeUniquiqueId(updAggrement, rapParcelId, "rap_agreementid",
//					Constants.AGGREMENTS, accessToken);
//			if (!uniqueId.equalsIgnoreCase(Constants.NEW_REQUEST)) {
//				updateParcelDetail(updAggr.toString(), uniqueId, accessToken, Constants.AGGREMENTS);
//			} else {
//				String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS)
//						+ Constants.AGGREMENTS;
//				crmService.postDataToCRM(urlString, updAggr.toString(), accessToken, null);
//			}
//
//		}
//		for (OwnerInformation updOwners : owners) {
//			JsonObject updOwner = gson.fromJson(gson.toJsonTree(updOwners).toString(), JsonObject.class);
//			updOwner.addProperty(Constants.PARCEL_ID_BIND, "/rap_parcels(" + rapParcelId + ")");
//			String uniqueId = reteriveOwnerUniquiqueId(updOwners, rapParcelId, "rap_ownerinformationid",
//					Constants.OWNER_INFORMATION, accessToken);
//			System.out.println("OmwenId"+uniqueId);
//			if (!uniqueId.equalsIgnoreCase(Constants.NEW_REQUEST)) {
//				System.out.println("JSON -> updOwner.toString()" + updOwner.toString());
//				String vhousupdOwner = updOwner.get("rap_ownerinformation_householdmembers_ownerId").toString();
//				ObjectMapper mapper = new ObjectMapper();
//				HouseholdInformation[] households = mapper.readValue(vhousupdOwner, HouseholdInformation[].class);
//				updOwner.remove("rap_ownerinformation_householdmembers_ownerId");
//				updateParcelDetail(updOwner.toString(), uniqueId, accessToken, Constants.OWNER_INFORMATION);
//
//				for (HouseholdInformation hs : households) {
//					String houseHoldId = reteriveHouseHoldUniquiqueId(hs, uniqueId, "rap_householdmembersid",
//							"rap_householdmemberses", accessToken);
//					String JsonHouseHold = "{\"input\":\"[{\\\"name\\\":\\\"" + hs.getRap_firstname()
//							+ hs.getRap_lastname() + "\\\",\\\"age\\\":" + hs.getRap_age() + ",\\\"ownerid\\\":\\\""
//							+ uniqueId + "\\\"}]\"}";
//					String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS)
//							+ "rap_upserthouseholdmember";
//					System.out.println(JsonHouseHold);
//					crmService.postDataToCRM(urlString, JsonHouseHold, accessToken, null);
//				}
//
//			} else {
//				String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS)
//						+ Constants.OWNER_INFORMATION;
//				System.out.println("JSON Exising-> updOwner.toString()"+updOwner.toString());
//				crmService.postDataToCRM(urlString, updOwner.toString(), accessToken, null);
//			}
//
//		
//		}
//		
//		return output;
//	}
//
//	private String rapParcelIdreteriveLandUniquiqueId(AgriculturalLand updCrops, String rapParcelId, String uniqueId,
//			String entity, String accessToken) throws Throwable {
//		String index = updCrops.getRap_recordidentifier();
//		return crmService.getParcelUniqueId(rapParcelId, uniqueId, entity, accessToken, index,false);
//	}
//	private String reteriveBuildingUniquiqueId(BuildingStructureInformation updCrops, String rapParcelId, String uniqueId,
//			String entity, String accessToken) throws Throwable {
//		String index = updCrops.getRap_recordidentifier();
//		return crmService.getParcelUniqueId(rapParcelId, uniqueId, entity, accessToken, index,false);
//	}
//	private String reteriveBusinessUniquiqueId(BusinessInformation updCrops, String rapParcelId, String uniqueId,
//			String entity, String accessToken) throws Throwable {
//		String index = updCrops.getRap_recordidentifier();
//		return crmService.getParcelUniqueId(rapParcelId, uniqueId, entity, accessToken, index,false);
//	}
//	private String reteriveAgreeUniquiqueId(AgreementInformation updCrops, String rapParcelId, String uniqueId,
//			String entity, String accessToken) throws Throwable {
//		String index = updCrops.getRap_recordidentifier();
//		return crmService.getParcelUniqueId(rapParcelId, uniqueId, entity, accessToken, index,false);
//	}
//	private void updateParcelDetail(String jsonString, String rapParcelId, String accessToken,String entity) throws Throwable {
//		String urlString = env.getProperty(CRM_URL) + env.getProperty(CRM_BASE_SERVICE_ADDRESS) + entity+"("
//				+ rapParcelId + ")";
//		//System.out.println(" Before Calling the CRM ::::: URL -> "+urlString+" JSON Value ->"+jsonString);
//		crmService.patchCRMData(urlString, jsonString, accessToken, null);
//
//	}
//	private String reteriveOwnerUniquiqueId(OwnerInformation updCrops, String rapParcelId, String uniqueId,
//			String entity, String accessToken) throws Throwable {
//		String index = updCrops.getRap_recordidentifier();
//		return crmService.getParcelUniqueId(rapParcelId, uniqueId, entity, accessToken, index,false);
//	}
//	private String reteriveHouseHoldUniquiqueId(HouseholdInformation updCrops, String rapParcelId, String uniqueId,
//			String entity, String accessToken) throws Throwable {
//		String index = updCrops.getRap_recordidentifier();
//		return crmService.getParcelUniqueId(rapParcelId, uniqueId, entity, accessToken, index,true);
//	}
//}

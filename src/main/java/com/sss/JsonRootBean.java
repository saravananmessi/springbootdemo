package com.sss;

import java.util.List;

import org.apache.poi.util.StringUtil;

//import org.wb.rap.common.util.StringUtil;
//import org.wb.rap.pdfextractor.model.OwnerInformation;
public class JsonRootBean {

    @Override
	public String toString() {
		return "JsonRootBean [name=" + name + ", type=" + type + ", childtagsComponents=" + childtagsComponents + "]";
	}

	private transient String id;
    private String name;
    private String type;
    //@JsonProperty("childtags_components")
    private List<ChildtagsComponents> childtagsComponents;
    public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setName(String name) {
         this.name = name;
     }
     public String getName() {
         return name;
     }

    public void setType(String type) {
         this.type = type;
     }
     public String getType() {
         return type;
     }

    public void setChildtagsComponents(List<ChildtagsComponents> childtagsComponents) {
         this.childtagsComponents = childtagsComponents;
     }
     public List<ChildtagsComponents> getChildtagsComponents() {
         return childtagsComponents;
     }

     public JsonRootBean(String id) {
 		this.id = id; 
 	}
 	
 	@Override
 	public boolean equals(Object obj) {
 		JsonRootBean ownerObj = (JsonRootBean) obj;
 		if((this.id !=  null) 
 				&& (ownerObj.getId() != null) 
 				&& this.id.trim().equalsIgnoreCase(ownerObj.getId().trim())) {
 			return true;
 		}else {
 			return false;
 		}
 	}
}
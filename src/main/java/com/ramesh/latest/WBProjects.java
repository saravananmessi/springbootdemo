package com.ramesh.latest;

import java.util.List;


public class WBProjects {

    @Override
	public String toString() {
		return "WBProjects [name=" + name + ", type=" + type + ", childtagsComponents=" + childtagsComponents + "]";
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

     public WBProjects(String id) {
 		this.id = id; 
 	}
 	
 	@Override
 	public boolean equals(Object obj) {
 		WBProjects ownerObj = (WBProjects) obj;
 		if((this.id != null) 
 				&& (ownerObj.getId() != null) 
 				&& this.id.trim().equalsIgnoreCase(ownerObj.getId().trim())) {
 			return true;
 		}else {
 			return false;
 		}
 	}
}
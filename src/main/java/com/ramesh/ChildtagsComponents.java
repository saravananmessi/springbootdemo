package com.ramesh;

import java.util.List;



public class ChildtagsComponents {

    private String id;
    private List<Tags> tags;
    private String guid;
    
    public String getGuid() {
		return guid;
	}
	public void setGuid(String guid) {
		this.guid = guid;
	}
	public void setId(String id) {
         this.id = id;
     }
     public String getId() {
         return id;
     }

    public void setTags(List<Tags> tags) {
         this.tags = tags;
     }
     public List<Tags> getTags() {
         return tags;
     }

     
     public ChildtagsComponents(String id) {
  		this.id = id; 
  	}
  	
  	@Override
  	public boolean equals(Object obj) {
  		ChildtagsComponents ownerObj = (ChildtagsComponents) obj;
  		if((this.guid != null) 
  				&& (ownerObj.getGuid() != null) 
  				&& this.id.trim().equalsIgnoreCase(ownerObj.getGuid().trim())) {
  			return true;
  		}else {
  			return false;
  		}
  	}
}
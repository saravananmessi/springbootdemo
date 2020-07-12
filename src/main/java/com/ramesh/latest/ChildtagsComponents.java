package com.ramesh.latest;

import java.util.List;



public class ChildtagsComponents {

    private String guid;
    private List<Tags> tags;
    public void setGuid(String guid) {
         this.guid = guid;
     }
     public String getGuid() {
         return guid;
     }

    public void setTags(List<Tags> tags) {
         this.tags = tags;
     }
     public List<Tags> getTags() {
         return tags;
     }

     
     public ChildtagsComponents(String guid) {
  		this.guid = guid; 
  	}
  	
  	@Override
  	public boolean equals(Object obj) {
  		ChildtagsComponents ownerObj = (ChildtagsComponents) obj;
  		if((this.guid != null) 
  				&& !(ownerObj.getGuid() != null) 
  				&& this.guid.trim().equalsIgnoreCase(ownerObj.getGuid().trim())) {
  			return true;
  		}else {
  			return false;
  		}
  	}
}
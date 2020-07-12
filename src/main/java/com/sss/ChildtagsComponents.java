package com.sss;

import java.util.List;

//import org.wb.rap.common.util.StringUtil;

public class ChildtagsComponents {

    private String id;
    private List<Tags> tags;
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
  		if((this.id != null) 
  				&& (ownerObj.getId() != null) 
  				&& this.id.trim().equalsIgnoreCase(ownerObj.getId().trim())) {
  			return true;
  		}else {
  			return false;
  		}
  	}
}
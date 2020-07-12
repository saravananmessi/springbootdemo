package com.ramesh;

import java.util.List;

/* Time: 2020-06-11 18:20:30 @author freecodeformat.com @website http://www.freecodeformat.com/json2javabean.php */
public class Tags {

    private String id;
    private String name;
    private String text;
    private String code;
    private String taxonomy;
   // @JsonProperty("geoId")
    private String geoid;
    //@JsonProperty("geoCode")
    private String geocode;
    private int level;
 //   @JsonProperty("parentId")
    private String parentid;
    private boolean selected;
    private List<Tags> listoftags;
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

    public void setText(String text) {
         this.text = text;
     }
     public String getText() {
         return text;
     }

    public void setCode(String code) {
         this.code = code;
     }
     public String getCode() {
         return code;
     }

    public void setTaxonomy(String taxonomy) {
         this.taxonomy = taxonomy;
     }
     public String getTaxonomy() {
         return taxonomy;
     }

    public void setGeoid(String geoid) {
         this.geoid = geoid;
     }
     public String getGeoid() {
         return geoid;
     }

    public void setGeocode(String geocode) {
         this.geocode = geocode;
     }
     public String getGeocode() {
         return geocode;
     }

    public void setLevel(int level) {
         this.level = level;
     }
     public int getLevel() {
         return level;
     }

    public void setParentid(String parentid) {
         this.parentid = parentid;
     }
     public String getParentid() {
         return parentid;
     }

    public void setSelected(boolean selected) {
         this.selected = selected;
     }
     public boolean getSelected() {
         return selected;
     }
	
	
	public List<Tags> getListoftags() {
		return listoftags;
	}
	public void setListoftags(List<Tags> listoftags) {
		this.listoftags = listoftags;
	}
	@Override
  	public boolean equals(Object obj) {
  		Tags ownerObj = (Tags) obj;
  		if((this.name != null) 
  				&& (ownerObj.getName() != null) 
  				&& this.name.trim().equalsIgnoreCase(ownerObj.getName().trim())) {
  			return true;
  		}else {
  			return false;
  		}
  	}
	
}
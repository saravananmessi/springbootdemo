package com.service.ramesh;

import java.util.List;

public class Childs {

    private String id;
    private String name;
    private String text;
    private String taxonomy;
    private String geoid;
    private String geocode;
    private int level;
    private boolean selected;
    private String parentid;
    private String parentname;
    private boolean lefttoggle;
    private boolean righttoggle;
    private int childrenselected;
    private List<Childs> childs;
   

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

    public void setSelected(boolean selected) {
         this.selected = selected;
     }
     public boolean getSelected() {
         return selected;
     }

  
    public void setParentname(String parentname) {
         this.parentname = parentname;
     }
     public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getParentid() {
		return parentid;
	}
	public void setParentid(String parentid) {
		this.parentid = parentid;
	}
	public String getParentname() {
         return parentname;
     }

    public void setLefttoggle(boolean lefttoggle) {
         this.lefttoggle = lefttoggle;
     }
     public boolean getLefttoggle() {
         return lefttoggle;
     }

    public void setRighttoggle(boolean righttoggle) {
         this.righttoggle = righttoggle;
     }
     public boolean getRighttoggle() {
         return righttoggle;
     }

    public void setChildrenselected(int childrenselected) {
         this.childrenselected = childrenselected;
     }
     public int getChildrenselected() {
         return childrenselected;
     }

    public void setChilds(List<Childs> childs) {
         this.childs = childs;
     }
     public List<Childs> getChilds() {
         return childs;
     }

}
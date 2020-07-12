
package com.service.ramesh;


import java.util.List;

public class Books {

	    public String bname, bauthor, bcost;

	    public List<Books> children;

		public String getBname() {
			return bname;
		}

		public void setBname(String bname) {
			this.bname = bname;
		}

		public String getBauthor() {
			return bauthor;
		}

		public void setBauthor(String bauthor) {
			this.bauthor = bauthor;
		}

		public String getBcost() {
			return bcost;
		}

		public void setBcost(String bcost) {
			this.bcost = bcost;
		}

		public List<Books> getChildren() {
			return children;
		}

		public void setChildren(List<Books> children) {
			this.children = children;
		}
	    
	    
	    
}

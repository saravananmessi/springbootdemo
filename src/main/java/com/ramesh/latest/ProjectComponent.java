package com.ramesh.latest;

public class ProjectComponent {

	private String prjId;
	private String prjCompName;
	private String prjGuid;

	public String getPrjId() {
		return prjId;
	}

	public void setPrjId(String prjId) {
		this.prjId = prjId;
	}

	public String getPrjCompName() {
		return prjCompName;
	}

	public void setPrjCompName(String prjCompName) {
		this.prjCompName = prjCompName;
	}

	public String getPrjGuid() {
		return prjGuid;
	}

	public void setPrjGuid(String prjGuid) {
		this.prjGuid = prjGuid;
	}

	@Override
	public String toString() {
		return "ProjectComponent [prjId=" + prjId + ", prjCompName=" + prjCompName + ", prjGuid=" + prjGuid + "]";
	}

}

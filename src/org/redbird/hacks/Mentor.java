package org.redbird.hacks;

import java.io.Serializable;
import java.util.List;

public class Mentor implements Serializable {

	private static final long serialVersionUID = -862821953832879532L;
	private String name;
	private List<String> expertise;
	private ContactMethod preferredContactMethod;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getExpertise() {
		return expertise;
	}
	public void setExpertise(List<String> expertise) {
		this.expertise = expertise;
	}
	public ContactMethod getPreferredContactMethod() {
		return preferredContactMethod;
	}
	public void setPreferredContactMethod(ContactMethod preferredContactMethod) {
		this.preferredContactMethod = preferredContactMethod;
	}
}

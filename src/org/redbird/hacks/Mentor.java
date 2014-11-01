package org.redbird.hacks;

import java.io.Serializable;

public class Mentor implements Serializable, Comparable<Mentor> {

	private static final long serialVersionUID = -862821953832879532L;
	private String name;
	private String expertise;
	private ContactMethod preferredContactMethod;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getExpertise() {
		return expertise;
	}
	public void setExpertise(String expertise) {
		this.expertise = expertise;
	}
	public ContactMethod getPreferredContactMethod() {
		return preferredContactMethod;
	}
	public void setPreferredContactMethod(ContactMethod preferredContactMethod) {
		this.preferredContactMethod = preferredContactMethod;
	}
	@Override
	public int compareTo(Mentor another) {
		//If two mentors have the same expertise, then sort by name
		if(this.expertise.compareTo(another.getExpertise()) == 0){
			return this.name.compareTo(another.getName()) > 0 ? 1: -1;
		}
		//If the expertises are different, then sort by expertise
		else if(this.expertise.compareTo(another.getExpertise()) > 0){
			return 1;
		}
		return -1;
	}
}

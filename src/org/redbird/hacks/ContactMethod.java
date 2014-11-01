package org.redbird.hacks;

import java.io.Serializable;

public class ContactMethod implements Serializable, Comparable<ContactMethod> {

	private static final long serialVersionUID = 9176554236321826893L;
	
	private ContactMethodType contactMethodType;
	private String contactActionURL;
	
	public ContactMethod(ContactMethodType contactMethodType, String contactActionURL) {
		super();
		this.contactMethodType = contactMethodType;
		this.contactActionURL = contactActionURL;
	}
	public ContactMethodType getContactMethodType() {
		return contactMethodType;
	}
	public void setContactMethodType(ContactMethodType contactMethodType) {
		this.contactMethodType = contactMethodType;
	}
	public String getContactActionURL() {
		return contactActionURL;
	}
	public void setContactActionURL(String contactActionURL) {
		this.contactActionURL = contactActionURL;
	}

	@Override
	public int compareTo(ContactMethod another) {
		if (this.contactMethodType.compareTo(another.contactMethodType) > 0) {
			return 1;
		} else if (this.contactMethodType.compareTo(another.contactMethodType) < 0) {
			return -1;
		} else {
			return 0;
		}
	}
}

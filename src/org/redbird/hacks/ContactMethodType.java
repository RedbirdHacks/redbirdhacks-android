/**
*  ContactMethodType.java
*  RedbirdHacks
*
*  Created by Andrew Erickson on 11/1/14.
**/
package org.redbird.hacks;

public enum ContactMethodType {

	TWITTER ("twitter"), 
	LINKED_IN ("linkedin"), 
	GOOGLE_PLUS ("googleplus"), 
	SMS ("sms"), 
	EMAIL ("email"), 
	PHONE ("phone"), 
	FACEBOOK ("facebook"), 
	NONE ("none");
	
	private final String contactMethodType;       

    private ContactMethodType(String contactMethodType) {
        this.contactMethodType = contactMethodType;
    }

    public String toString(){
       return contactMethodType;
    }
}

package com.hackmit.badmouth;
import com.parse.ParseObject;
import com.parse.ParseClassName;
import com.parse.ParseRelation;
	 
//This @ParseClassName line is really important. "Target" is the name of the ParseObject as defined in the Parse Data Browser
@ParseClassName("Target")
public class Target extends ParseObject {
	
	public static final String name = "name";
	public static final String badmouths = "badmouths";
	
	public String getName() {
	    return getString(Target.name);
	}
	
	public void setName(String name) {
	    put(Target.name, name);
	}

	public ParseRelation<Badmouth> getBadmouths(){
		return getRelation(Target.badmouths);
	}
	
	public void setBadmouths(ParseRelation<Badmouth> badmouths){
		put(Target.badmouths, badmouths);
	}
}



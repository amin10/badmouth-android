package com.hackmit.badmouth;
import com.parse.ParseObject;
import com.parse.ParseClassName;
	 
//This @ParseClassName line is really important. "Badmouth" is the name of the ParseObject as defined in the Parse Data Browser
@ParseClassName("Badmouth")
public class Badmouth extends ParseObject {
	public static final String text = "text";
	public static final String parent = "parent";
	
	//Example getter method
	public String getText() {
	    return getString(Badmouth.text);
	}
	
	//Example setter method
	public void setText(String text) {
	    put(Badmouth.text, text);
	}

	public ParseObject getParent(){
		return getParseObject(Badmouth.parent);
	}
	
	public void setParent(Target parent){
		put(Badmouth.parent, parent);
	}
}



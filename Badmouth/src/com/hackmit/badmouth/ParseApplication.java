package com.hackmit.badmouth;
import android.app.Application;
import android.util.Log;

import com.parse.Parse;
import com.parse.ParseObject;
 
public class ParseApplication extends Application {
	private final String parse_applicationid ="s32blhuMK0fzjARmluinIoP7GMNCsWXPsREdsFhD";
	private final String parse_clientkey ="dBMVKhUnoFW8kHZzXbVXAc9E0qVrZVfjcfH5J5EQ";
  @Override
  public void onCreate() {
    super.onCreate();
    Log.i("YAY", parse_applicationid);    
    Parse.initialize(this, parse_applicationid, parse_clientkey);
    ParseObject.registerSubclass(Target.class);
    ParseObject.registerSubclass(Badmouth.class);
  }
}
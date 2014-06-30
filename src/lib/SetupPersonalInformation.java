package lib;

import android.content.Context;
import android.content.SharedPreferences;


public class SetupPersonalInformation{

	private SharedPreferences settings;
	private Context mContext;
	private String getPatientName = "1";
	private String getPatientId = "1";
	private int getPatientGender;
	private boolean getSetupAlready;
	
	
	public SetupPersonalInformation(Context context){
		mContext = context;
		

		settings = mContext.getSharedPreferences("setup_information", 0);
		
		getPatientName 	  = settings.getString("setupPatientName", "");
		getPatientId   	  = settings.getString("setupPatientId", "");
		getPatientGender  = settings.getInt("setupPatientGender", 0);
		getSetupAlready   = settings.getBoolean("setupAlready", false);
	}
	
	public void updateInformation(String name, String id, int gender, boolean setup){
		SharedPreferences.Editor getData = settings.edit();
	    getData.putString("setupPatientName", name);
	    getData.putString("setupPatientId", id);
	    getData.putInt("setupPatientGender", gender);
	    getData.putBoolean("setupAlready", setup);
	    getData.commit();
	}
	
	
	public String getPatientName(){
		return getPatientName;
	}
	
	public String getPatientId(){
		return getPatientId;
	}
	
	public int getPatientGender(){
		return getPatientGender;
	}
	
	public boolean getSetupAlready(){
		return getSetupAlready;
	}
}
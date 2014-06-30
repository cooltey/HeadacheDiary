package com.cooltey.headachediary;

import lib.SetupPersonalInformation;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


public class SetupActivity extends Activity {
	
	private EditText setupPatientName;
	private EditText setupPatientId;
	private Spinner  setupPatientGender;
	private Button	 setupSubmit;
	private Button	 setupCancel;
	
	private SetupPersonalInformation spl;
	
	private Context mContext;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setup);
		
		mContext = this;
		
		spl = new SetupPersonalInformation(mContext);

		
		setupPatientName = (EditText) findViewById(R.id.setup_patient_name);
		setupPatientId = (EditText) findViewById(R.id.setup_patient_id);
		setupPatientGender = (Spinner) findViewById(R.id.setup_patient_gender);
		setupSubmit = (Button) findViewById(R.id.submit);
		setupCancel = (Button) findViewById(R.id.cancel);
		

		// set spinner
		ArrayAdapter<CharSequence> spinnerGenderAdapter = ArrayAdapter.createFromResource(this, R.array.setup_gender, android.R.layout.simple_spinner_item);
		spinnerGenderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		setupPatientGender.setAdapter(spinnerGenderAdapter);
		
		try{
		String getDefaultName = spl.getPatientName();
		String getDefaultId   = spl.getPatientId();
		int getDefaultGender  = spl.getPatientGender();
		
		setupPatientName.setText(getDefaultName);
		setupPatientId.setText(getDefaultId);
		setupPatientGender.setSelection(getDefaultGender);
		}catch(Exception e){
			
		}
	
		// set action
		setupSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// set strings
				String getPatientName 	= setupPatientName.getText().toString();
				String getPatientId	= setupPatientId.getText().toString();
				int getPatientGender = setupPatientGender.getSelectedItemPosition();
				
				spl.updateInformation(getPatientName, getPatientId, getPatientGender, true);

				Intent intent = new Intent();
				intent.setClass(SetupActivity.this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
				finish();
			}
			
		});
		

		// set action
		setupCancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				onLeaveDialog();
			}
			
		});
		
	}
	
	private void onLeaveDialog(){
		// TODO Auto-generated method stub
		AlertDialog.Builder alertDialog = new Builder(mContext);
		alertDialog.setTitle(mContext.getString(R.string.dialog_cancel_title));
		alertDialog.setMessage(mContext.getString(R.string.dialog_cancel_msg));
		alertDialog.setPositiveButton(mContext.getString(R.string.dialog_action_ok), new AlertDialog.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub

				Intent returnIntent = new Intent();
				setResult(999, returnIntent);
				finish();
			}
			
		});
		
		alertDialog.setNegativeButton(mContext.getString(R.string.dialog_action_cancel), new AlertDialog.OnClickListener(){

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
			
		});
		

		alertDialog.create();
		alertDialog.show();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)){
				onLeaveDialog();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
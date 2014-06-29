package com.cooltey.headachediary;

import lib.DatabaseHelper;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


public class SheetActivity extends Activity {
	
	private TextView sheetDate;
	private EditText sheetPatientName;
	private EditText sheetPatientId;
	private Spinner  sheetHeadcheLevelMorning;
	private Spinner  sheetHeadcheLevelAfternoon;
	private Spinner  sheetHeadcheLevelNight;
	private Spinner  sheetHeadcheLevelSleep;
	private CheckBox sheetSymptionOption_1;
	private CheckBox sheetSymptionOption_2;
	private CheckBox sheetSymptionOption_3;
	private CheckBox sheetSymptionOption_4;
	private CheckBox sheetSymptionOption_5;
	private CheckBox sheetSymptionOption_6;
	private CheckBox sheetSymptionOption_7;
	private CheckBox sheetSignOption_1;
	private CheckBox sheetSignOption_2;
	private Spinner  sheetHeadcheHours;
	private Spinner  sheetHeadcheMedicine;
	private Spinner  sheetHeadcheUsefulOption_1;
	private Spinner  sheetHeadcheUsefulOption_2;
	private Spinner  sheetHeadcheUsefulOption_3;
	private Spinner  sheetHeadcheUsefulOption_4;
	private CheckBox sheetPeriod;
	private Button	 sheetSubmit;
	private Button	 sheetCancel;
	
	private DatabaseHelper db;
	private long getRecordId = 0;
	private Context mContext;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sheet);
		
		Intent intent = this.getIntent();
		final String getDate = intent.getStringExtra("setDate");
		final boolean hasRecord = intent.getBooleanExtra("hasRecord", false);
		
		mContext = this;
		
		// set db
		db = new DatabaseHelper(this);
		
		sheetDate = (TextView) findViewById(R.id.sheet_date);
		sheetPatientName = (EditText) findViewById(R.id.patient_name);
		sheetPatientId = (EditText) findViewById(R.id.patient_number_id);
		sheetHeadcheLevelMorning = (Spinner) findViewById(R.id.sheet_headache_level_time_morning);
		sheetHeadcheLevelAfternoon = (Spinner) findViewById(R.id.sheet_headache_level_time_afternoon);
		sheetHeadcheLevelNight = (Spinner) findViewById(R.id.sheet_headache_level_time_night);
		sheetHeadcheLevelSleep = (Spinner) findViewById(R.id.sheet_headache_level_time_sleep);
		sheetSymptionOption_1 = (CheckBox) findViewById(R.id.sheet_headache_symptom_1);
		sheetSymptionOption_2 = (CheckBox) findViewById(R.id.sheet_headache_symptom_2);
		sheetSymptionOption_3 = (CheckBox) findViewById(R.id.sheet_headache_symptom_3);
		sheetSymptionOption_4 = (CheckBox) findViewById(R.id.sheet_headache_symptom_4);
		sheetSymptionOption_5 = (CheckBox) findViewById(R.id.sheet_headache_symptom_5);
		sheetSymptionOption_6 = (CheckBox) findViewById(R.id.sheet_headache_symptom_6);
		sheetSymptionOption_7 = (CheckBox) findViewById(R.id.sheet_headache_symptom_7);
		sheetSignOption_1 = (CheckBox) findViewById(R.id.sheet_headache_sign_1);
		sheetSignOption_2 = (CheckBox) findViewById(R.id.sheet_headache_sign_2);
		sheetHeadcheHours = (Spinner) findViewById(R.id.sheet_headache_hours);
		sheetHeadcheMedicine = (Spinner) findViewById(R.id.sheet_headache_medicine_1);
		sheetHeadcheUsefulOption_1 = (Spinner) findViewById(R.id.sheet_headache_useful_option_1);
		sheetHeadcheUsefulOption_2 = (Spinner) findViewById(R.id.sheet_headache_useful_option_2);
		sheetHeadcheUsefulOption_3 = (Spinner) findViewById(R.id.sheet_headache_useful_option_3);
		sheetHeadcheUsefulOption_4 = (Spinner) findViewById(R.id.sheet_headache_useful_option_4);
		sheetPeriod = (CheckBox) findViewById(R.id.sheet_headache_period);
		sheetSubmit = (Button) findViewById(R.id.submit);
		sheetCancel = (Button) findViewById(R.id.cancel);
		
		// set spinner
		ArrayAdapter<CharSequence> spinnerHeadcheLevelAdapter = ArrayAdapter.createFromResource(this, R.array.headache_levels, android.R.layout.simple_spinner_item);
		spinnerHeadcheLevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sheetHeadcheLevelMorning.setAdapter(spinnerHeadcheLevelAdapter);
		sheetHeadcheLevelAfternoon.setAdapter(spinnerHeadcheLevelAdapter);
		sheetHeadcheLevelNight.setAdapter(spinnerHeadcheLevelAdapter);
		sheetHeadcheLevelSleep.setAdapter(spinnerHeadcheLevelAdapter);
		
		ArrayAdapter<CharSequence> spinnerHeadcheHoursAdapter = ArrayAdapter.createFromResource(this, R.array.headache_hours, android.R.layout.simple_spinner_item);
		spinnerHeadcheHoursAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sheetHeadcheHours.setAdapter(spinnerHeadcheHoursAdapter);
		
		ArrayAdapter<CharSequence> spinnerHeadcheMedicineAdapter = ArrayAdapter.createFromResource(this, R.array.headache_medicine, android.R.layout.simple_spinner_item);
		spinnerHeadcheMedicineAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sheetHeadcheMedicine.setAdapter(spinnerHeadcheMedicineAdapter);		

		
		ArrayAdapter<CharSequence> spinnerHeadcheUsefulOptionAdapter = ArrayAdapter.createFromResource(this, R.array.useful_levels, android.R.layout.simple_spinner_item);
		spinnerHeadcheUsefulOptionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sheetHeadcheUsefulOption_1.setAdapter(spinnerHeadcheUsefulOptionAdapter);
		sheetHeadcheUsefulOption_2.setAdapter(spinnerHeadcheUsefulOptionAdapter);
		sheetHeadcheUsefulOption_3.setAdapter(spinnerHeadcheUsefulOptionAdapter);
		sheetHeadcheUsefulOption_4.setAdapter(spinnerHeadcheUsefulOptionAdapter);
		
		// set date
		sheetDate.setText(getDate);
		
		// set data from db
		if(hasRecord){
			if(getDate.length() > 0){
				DatabaseHelper db = new DatabaseHelper(mContext);
				Cursor cData = db.getAll("headache_record", " WHERE 1=1 AND record_date ='"+ getDate + "'");
				if(cData != null && cData.getCount() > 0){
					cData.moveToFirst();
					getRecordId = cData.getLong(0);
					sheetPatientName.setText(cData.getString(2));
					sheetPatientId.setText(cData.getString(3));
					sheetHeadcheLevelMorning.setSelection(Integer.parseInt(cData.getString(4)));
					sheetHeadcheLevelAfternoon.setSelection(Integer.parseInt(cData.getString(5)));
					sheetHeadcheLevelNight.setSelection(Integer.parseInt(cData.getString(6)));
					sheetHeadcheLevelSleep.setSelection(Integer.parseInt(cData.getString(7)));
					sheetSymptionOption_1.setChecked(Boolean.parseBoolean(cData.getString(8)));
					sheetSymptionOption_2.setChecked(Boolean.parseBoolean(cData.getString(9)));
					sheetSymptionOption_3.setChecked(Boolean.parseBoolean(cData.getString(10)));
					sheetSymptionOption_4.setChecked(Boolean.parseBoolean(cData.getString(11)));
					sheetSymptionOption_5.setChecked(Boolean.parseBoolean(cData.getString(12)));
					sheetSymptionOption_6.setChecked(Boolean.parseBoolean(cData.getString(13)));
					sheetSymptionOption_7.setChecked(Boolean.parseBoolean(cData.getString(14)));
					sheetSignOption_1.setChecked(Boolean.parseBoolean(cData.getString(15)));
					sheetSignOption_2.setChecked(Boolean.parseBoolean(cData.getString(16)));
					sheetHeadcheHours.setSelection(Integer.parseInt(cData.getString(17)));
					sheetHeadcheMedicine.setSelection(Integer.parseInt(cData.getString(18)));
					sheetHeadcheUsefulOption_1.setSelection(Integer.parseInt(cData.getString(18)));
					sheetHeadcheUsefulOption_2.setSelection(Integer.parseInt(cData.getString(19)));
					sheetHeadcheUsefulOption_3.setSelection(Integer.parseInt(cData.getString(20)));
					sheetHeadcheUsefulOption_4.setSelection(Integer.parseInt(cData.getString(21)));
					sheetPeriod.setChecked(Boolean.parseBoolean(cData.getString(22)));
				}
			}
		}
		
		
		// set action
		sheetSubmit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				// set strings
				String getPatientName 	= sheetPatientName.getText().toString();
				String getPatientId	= sheetPatientId.getText().toString();
				String getHeadacheLvlMorning = sheetHeadcheLevelMorning.getSelectedItemPosition() + "";
				String getHeadacheLvlAfternoon = sheetHeadcheLevelAfternoon.getSelectedItemPosition() + "";
				String getHeadacheLvlNight = sheetHeadcheLevelNight.getSelectedItemPosition() + "";
				String getHeadacheLvlSleep = sheetHeadcheLevelSleep.getSelectedItemPosition() + "";
				String getSymptionOption_1 = sheetSymptionOption_1.isChecked() + "";
				String getSymptionOption_2 = sheetSymptionOption_2.isChecked() + "";
				String getSymptionOption_3 = sheetSymptionOption_3.isChecked() + "";
				String getSymptionOption_4 = sheetSymptionOption_4.isChecked() + "";
				String getSymptionOption_5 = sheetSymptionOption_5.isChecked() + "";
				String getSymptionOption_6 = sheetSymptionOption_6.isChecked() + "";
				String getSymptionOption_7 = sheetSymptionOption_7.isChecked() + "";
				String getSignOption_1 = sheetSignOption_1.isChecked() + "";
				String getSignOption_2 = sheetSignOption_2.isChecked() + "";
				String getHeadcheHours = sheetHeadcheHours.getSelectedItemPosition() + "";
				String getHeadcheMedicine = sheetHeadcheMedicine.getSelectedItemPosition() + "";
				String getHeadcheUsefulOption_1 = sheetHeadcheUsefulOption_1.getSelectedItemPosition() + "";
				String getHeadcheUsefulOption_2 = sheetHeadcheUsefulOption_2.getSelectedItemPosition() + "";
				String getHeadcheUsefulOption_3 = sheetHeadcheUsefulOption_3.getSelectedItemPosition() + "";
				String getHeadcheUsefulOption_4 = sheetHeadcheUsefulOption_4.getSelectedItemPosition() + "";
				String getPeriod = sheetPeriod.isChecked() + "";
				
				final String[] columns = {"record_date", 
									"patient_name", 
									"patient_id", 
									"headache_level_time_morning", 
									"headache_level_time_afternoon", 
									"headache_level_time_night", 
									"headache_level_time_sleep", 
									"headache_symptom_1", 
									"headache_symptom_2",
									"headache_symptom_3", 
									"headache_symptom_4", 
									"headache_symptom_5", 
									"headache_symptom_6", 
									"headache_symptom_7", 
									"headache_sign_1", 
									"headache_sign_2",
									"headache_hours",
									"headache_medicine_1",
									"headache_useful_option_1",
									"headache_useful_option_2",
									"headache_useful_option_3",
									"headache_useful_option_4",
									"headache_period"};
				
				final String[] values = {getDate, 
									getPatientName, 
									getPatientId, 
									getHeadacheLvlMorning, 
									getHeadacheLvlAfternoon, 
									getHeadacheLvlNight, 
									getHeadacheLvlSleep, 
									getSymptionOption_1, 
									getSymptionOption_2, 
									getSymptionOption_3, 
									getSymptionOption_4, 
									getSymptionOption_5, 
									getSymptionOption_6, 
									getSymptionOption_7, 
									getSignOption_1, 
									getSignOption_2,
									getHeadcheHours,
									getHeadcheMedicine,
									getHeadcheUsefulOption_1,
									getHeadcheUsefulOption_2,
									getHeadcheUsefulOption_3,
									getHeadcheUsefulOption_4,
									getPeriod};
				
				
				// set dialog
				AlertDialog.Builder alertDialog = new Builder(mContext);
				alertDialog.setTitle(mContext.getString(R.string.dialog_submit_title));
				alertDialog.setMessage(mContext.getString(R.string.dialog_submit_msg));
				alertDialog.setPositiveButton(mContext.getString(R.string.dialog_action_ok), new AlertDialog.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						if(hasRecord){
							db.update("headache_record", getRecordId, columns, values);
						}else{
							db.insert("headache_record", columns, values);    
						}
						
						Intent returnIntent = new Intent();
						setResult(100, returnIntent);
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
			
		});
		

		// set action
		sheetCancel.setOnClickListener(new OnClickListener(){

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
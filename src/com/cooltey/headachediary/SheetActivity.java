package com.cooltey.headachediary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sheet);
		
		Intent intent = this.getIntent();
		String getDate = intent.getStringExtra("setDate");
		
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
	}
}
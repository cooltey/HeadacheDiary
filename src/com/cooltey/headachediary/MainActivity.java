package com.cooltey.headachediary;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import lib.DatabaseHelper;
import lib.SetupPersonalInformation;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private FragmentPagerAdapter adapter;
	private ViewPager pager;
	private static ArrayList<String> dateData = new ArrayList<String>();
	private static Context mContext;
	private static DatabaseHelper db;
	private final static ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private final static Lock r = rwl.readLock();
    private final static Lock w = rwl.writeLock();
    
    private static TextView tmpView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		SetupPersonalInformation spl = new SetupPersonalInformation(this);
		if(spl.getSetupAlready()){
			db = new DatabaseHelper(this);
			
			Calendar calendar = Calendar.getInstance();
			int getCurrentMonth = calendar.get(Calendar.MONTH) + 1;
			int getCurrentYear = calendar.get(Calendar.YEAR);
			int itemCounter = 0;
			int setCurrentDateItem = 0;
			for(int i = calendar.get(Calendar.YEAR); i < (calendar.get(Calendar.YEAR) + 5); i++){
				for(int m = 1; m <= 12; m++){
					if(getCurrentYear == i && getCurrentMonth == m){
						setCurrentDateItem = itemCounter;
					}
					String setDate = i + "/" + m;
					if(m < 10){
						setDate = i + "/0" + m;
					}
					dateData.add(setDate);
					
					itemCounter++;
				}
			}
	        
			adapter = new ArticleContentAdapter(getSupportFragmentManager());
	    	
	        pager = (ViewPager)findViewById(R.id.pager);
	        pager.setAdapter(adapter);
	        
	        pager.setCurrentItem(setCurrentDateItem);
	
	        mContext = this;
		}else{
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SetupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
		}
	}
	
	
	private static Cursor prepareRecordList(String getDate){
		Cursor returnVal = null;

        r.lock();
	    try {
			if(getDate.length() > 0){
				Cursor cData = null;
			    try {
			    	cData = db.getAll("headache_record", " WHERE 1=1 AND record_date ='"+ getDate + "'");
					if(cData != null && cData.getCount() > 0){
						cData.moveToFirst();
						returnVal = cData;
					}
			    } finally {
			    	cData.close();
		        }
			}
        } finally {
            r.unlock();
        }
		return returnVal;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			Intent intent = new Intent();
			intent.setClass(MainActivity.this, SetupActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	class ArticleContentAdapter extends FragmentPagerAdapter {
        public ArticleContentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return dateData.get(position).toUpperCase();
        }

        @Override
        public int getCount() {
          return dateData.size();
        }
    }
	
	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		private int mCateId = 0;
		
		public PlaceholderFragment() {
		}

		public static PlaceholderFragment newInstance(int cateId) {
	    	
			PlaceholderFragment fragment = new PlaceholderFragment();
	        
	        fragment.mCateId = cateId;
	        return fragment;
	    }

		@Override
		public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
			
			final RelativeLayout calendarZone		 = (RelativeLayout) rootView.findViewById(R.id.calendar_content_zone);
			final LinearLayout calendarView          = (LinearLayout) rootView.findViewById(R.id.calendar_zone);
			final TextView itemDate		             = (TextView) rootView.findViewById(R.id.calendar_date);
			final LinearLayout articleProcessView	 = (LinearLayout) rootView.findViewById(R.id.process_status);
			final TextView articleProcessMessageView = (TextView) rootView.findViewById(R.id.process_message);
	    	TextView calendarPrevMonth	     		 = (TextView) rootView.findViewById(R.id.calendar_prev_month);
	    	TextView calendarNextMonth	     		 = (TextView) rootView.findViewById(R.id.calendar_next_month);
			
	    	if(mCateId > 0){
	    		calendarPrevMonth.setText("< " + dateData.get(mCateId-1));
	    	}
	    	
	    	if(mCateId < dateData.size()){
	    		calendarNextMonth.setText(dateData.get(mCateId+1) + " >");
	    	}
	    	
	    	final Handler handler = new Handler() {
	        	@Override
	        	public void handleMessage(Message msg) {
	        		if(msg.what == mCateId){
			    		showProgress(false, articleProcessView, articleProcessMessageView);
			    		calendarZone.setVisibility(View.VISIBLE);
			    		
			    		itemDate.setText(dateData.get(mCateId));
	        		}
	        		super.handleMessage(msg);
	        	}
	    	};
	    	

			showProgress(true, articleProcessView, articleProcessMessageView);
	    	
	    	new Thread
			(
		        new Runnable() 
				{
		        	@Override
					public void run() 
					{      					
		        		Message m = new Message();
		        		m.what = mCateId;
			    		calendarZone.setVisibility(View.GONE);
			    		setCalendarContent(mCateId, calendarView, inflater, container);
						handler.sendMessage(m);
					}
				}	
	         ).start(); 
			
			
			
			
			return rootView;
		}
	}
	
	
	private static void setCalendarContent(int mCateId, LinearLayout calendarView, LayoutInflater inflater, ViewGroup container){
		// get date data
		String[] splitData = dateData.get(mCateId).split("/");
		int getYear = Integer.parseInt(splitData[0]);
		int getMonth = Integer.parseInt(splitData[1]) - 1;
		
		// get current month
		Calendar calendar = Calendar.getInstance();
		calendar.set(getYear, getMonth, 1);
		int getDayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		int getDayOfMonth   = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		int countDays = 1;
		
		// set current date
		//String currentDate = calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH);
	
		for(int i = 0; i < 6; i++){
			LinearLayout itemView = (LinearLayout) inflater.inflate(R.layout.calendar_rows, container,	false);

			for(int d = 0; d < 7; d++){
				TextView dayView = new TextView(mContext);
				TableLayout.LayoutParams tableLayout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 45, 1f);
				tableLayout.setMargins(10, 10, 10, 10);
				dayView.setLayoutParams(tableLayout);
				dayView.setPadding(15, 15, 15, 15);
				dayView.setGravity(Gravity.CENTER);
				// set tags
				String setDate = "";
				if(countDays <= getDayOfMonth){
					if(i == 0 && d >= getDayOfTheWeek){
						setDate = dateData.get(mCateId) + "/" + countDays;
						dayView.setText(countDays + "");
						dayView.setTag(setDate);
						countDays++;
					}else if(i > 0){
						setDate = dateData.get(mCateId) + "/" + countDays;
						dayView.setText(countDays + "");
						dayView.setTag(setDate);
						countDays++;
					}else{
						dayView.setText("");
						dayView.setTag("");
					}
				}
				
				// set special background for someone has recorded
				if(prepareRecordList(setDate) != null){
					dayView.setBackgroundResource(R.drawable.layer_item_recorded);
					dayView.setTextColor(Color.parseColor("#6699FF"));
					dayView.setOnClickListener(updateRecord);
				}else{
					dayView.setBackgroundResource(R.drawable.layer_item);
					dayView.setOnClickListener(addRecord);
				}
				
				dayView.setOnTouchListener(setOnTouchEffect);
				itemView.addView(dayView);
			}
			
			calendarView.addView(itemView);
		}
	}
	
	private static OnClickListener addRecord = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try{
				String getDate = v.getTag().toString();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putBoolean("hasRecord", false);
				bundle.putString("setDate", getDate);
				intent.putExtras(bundle);
				intent.setClass(mContext, SheetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				((Activity) mContext).startActivityForResult(intent, 1);
				
				tmpView = (TextView) v;
			}catch(Exception e){
				
			}
		}
		
	};
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

	    if (requestCode == 1) {
	        if(resultCode == 100){
				tmpView.setBackgroundResource(R.drawable.layer_item_recorded);
				tmpView.setTextColor(Color.parseColor("#6699FF"));
				tmpView.setOnClickListener(updateRecord);
	        }
	    }
	}
	
	private static OnClickListener updateRecord = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try{
				String getDate = v.getTag().toString();
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putBoolean("hasRecord", true);
				bundle.putString("setDate", getDate);
				intent.putExtras(bundle);
				intent.setClass(mContext, SheetActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mContext.startActivity(intent);
				
				tmpView = (TextView) v;
			}catch(Exception e){
				
			}
		}
		
	};
	
	private static OnTouchListener setOnTouchEffect = new OnTouchListener(){

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			switch (event.getAction())
			{
				case MotionEvent.ACTION_DOWN:
					v.getBackground().setAlpha(150);
					break;
					
				case MotionEvent.ACTION_CANCEL:
				case MotionEvent.ACTION_OUTSIDE:
				case MotionEvent.ACTION_UP:
					v.getBackground().setAlpha(255);						
					break;
			}
			return false;
		}
		
	};
	
	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private static void showProgress(final boolean show, final View processView, final View contentView) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = mContext.getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			processView.setVisibility(View.VISIBLE);
			processView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							processView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			contentView.setVisibility(View.VISIBLE);
			contentView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							contentView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			processView.setVisibility(show ? View.VISIBLE : View.GONE);
			contentView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

}

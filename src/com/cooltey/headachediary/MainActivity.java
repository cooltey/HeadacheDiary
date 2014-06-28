package com.cooltey.headachediary;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.TableLayout;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	private FragmentPagerAdapter adapter;
	private ViewPager pager;
	private static ArrayList<String> dateData = new ArrayList<String>();
	private static Context mContext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		

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
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,	false);
			LinearLayout calendarView = (LinearLayout) rootView.findViewById(R.id.calendar_zone);
			TextView itemDate		  = (TextView) rootView.findViewById(R.id.calendar_date);
			
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
			String currentDate = calendar.get(Calendar.YEAR) + "/" + calendar.get(Calendar.MONTH);
			itemDate.setText(dateData.get(mCateId));
			
			for(int i = 0; i < 6; i++){
				LinearLayout itemView = (LinearLayout) inflater.inflate(R.layout.calendar_rows, container,	false);

				for(int d = 0; d < 7; d++){
					TextView dayView = new TextView(mContext);
					TableLayout.LayoutParams tableLayout = new TableLayout.LayoutParams(LayoutParams.WRAP_CONTENT, 70, 1f);
					tableLayout.setMargins(10, 10, 10, 10);
					dayView.setLayoutParams(tableLayout);
					dayView.setPadding(20, 20, 20, 20);
					dayView.setBackgroundResource(R.drawable.layer_item);
					dayView.setGravity(Gravity.CENTER);
					// set tags
					String setDate = dateData.get(mCateId) + "/" + countDays;
					dayView.setTag(setDate);
					
					if(countDays <= getDayOfMonth){
						if(i == 0 && d >= getDayOfTheWeek){
							dayView.setText(countDays + "");
							countDays++;
						}else if(i > 0){
							dayView.setText(countDays + "");
							countDays++;
						}else{
							dayView.setText("");
						}
					}
					
					dayView.setOnClickListener(addRecord);
					dayView.setOnTouchListener(setOnTouchEffect);
					itemView.addView(dayView);
				}
				
				calendarView.addView(itemView);
			}
			
			return rootView;
		}
	}
	
	private static OnClickListener addRecord = new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			String getDate = v.getTag().toString();
			
			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("setDate", getDate);
			intent.putExtras(bundle);
			intent.setClass(mContext, SheetActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mContext.startActivity(intent);
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
	

}

package se.ltu.m7016e.project;

import java.util.Calendar;


public class SuggestActivity {

	private ActivitiesTime getTime;
	
	public SuggestActivity(MongoDB database){
		getTime = new ActivitiesTime(database);
	}
	public void suggestWeather(){
		long time = 0;
		
		
		Calendar calNowTime = Calendar.getInstance();
		Calendar calStartTime = Calendar.getInstance();
		//calNowTime.add(Calendar.MONTH, 1);
		//calStartTime.add(Calendar.MONTH, 1);
		
		calStartTime.add(Calendar.MINUTE, -60);
		
		time = getTime.findActivityTime(calStartTime, calNowTime, ActivitiesTime.RFID);
		System.out.println(time);
	}
}

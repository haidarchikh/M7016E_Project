package se.ltu.m7016e.project;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;


public class SuggestActivity {

	private ActivitiesTime getTime;
	private OpenWeatherMap weather;
	private PushbulletClient pushBullet;
	private final static String NOTE_TITLE   ="Sugget an activity";
	private final static String NOTE_CONTENT ="It's a nice weather, have a walk ;)";
	
	public SuggestActivity(){
		getTime    = new ActivitiesTime();
		weather    = new OpenWeatherMap();
		pushBullet = new PushbulletClient();
		
	}
	public void suggestWeather() throws ClientProtocolException, IOException{
		long timeAtHome = 0;
		long timeMoving = 0;
		
		
		Calendar calNowTime = Calendar.getInstance();
		Calendar calStartTime = Calendar.getInstance();
		//calNowTime.add(Calendar.MONTH, 1);
		//calStartTime.add(Calendar.MONTH, 1);
		
		calStartTime.add(Calendar.MINUTE, -120);
		
		timeAtHome = getTime.findActivityTime(calStartTime, calNowTime, ActivitiesTime.RFID);
		timeMoving = getTime.findActivityTime(calStartTime, calNowTime, ActivitiesTime.MOTION_SENSOR);		
		if(timeAtHome > 7200 && timeMoving < 1800){
			String temp = weather.getWeather();
			if(temp =="Sunny"){
				pushBullet.sendNote(NOTE_TITLE,NOTE_CONTENT);
			}
		}
	}
}

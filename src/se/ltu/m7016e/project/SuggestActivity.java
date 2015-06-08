package se.ltu.m7016e.project;

import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;

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
		
		
		//Calendar calNowTime   = Calendar.getInstance();
		//Calendar calStartTime = Calendar.getInstance();
		//calStartTime.add(Calendar.MINUTE, -140);
		
		//calNowTime.add(Calendar.MONTH, 1);
		//calStartTime.add(Calendar.MONTH, 1);
		
		//timeAtHome = getTime.findActivityTime(calStartTime, calNowTime, ActivitiesTime.RFID);
		//timeMoving = getTime.findActivityTime(calStartTime, calNowTime, ActivitiesTime.MOTION_SENSOR);
		
		Calendar startTime = new GregorianCalendar(2015, 04, 26, 6, 45, 00);
		Calendar endTime= new GregorianCalendar(2015, 04, 30, 7, 30, 00);
		timeAtHome = getTime.findActivityTime(startTime, endTime, ActivitiesTime.RFID);
		timeMoving = getTime.findActivityTime(startTime, endTime, ActivitiesTime.MOTION_SENSOR);

		if(timeAtHome > 7200 && timeMoving > 1800){
			String temp = weather.getWeather();
			if(temp.contains("Sunny")||temp.contains("Clear")){
				pushBullet.sendNote(NOTE_TITLE , NOTE_CONTENT);
			}
		}
	}
}

package se.ltu.m7016e.project;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;
	
public class LogActivity {
	 

	private ActivitiesTime getTime;
	private long sleep	;
	private long atHome	;
	private long moving ;
	private Calendar startTime;
	private Calendar endTime;
	
	public LogActivity(Calendar startTime , Calendar endTime){
		getTime = new ActivitiesTime();
		this.startTime 	= startTime;
		this.endTime	= endTime;		
	}
	
	public void getLog(){
		sleep  = getTime.findActivityTime(startTime, endTime, ActivitiesTime.MATTRESS_SENSOR);
		atHome = getTime.findActivityTime(startTime, endTime, ActivitiesTime.RFID);
		moving = getTime.findActivityTime(startTime, endTime, ActivitiesTime.MOTION_SENSOR);
	}
	
	public void sendLog() throws ClientProtocolException, IOException{
		
		PushbulletClient sendBullet = new PushbulletClient();
		StringBuilder log = new StringBuilder();
		log.append("Log Activity from ")
		   .append(startTime.getTime())
		   .append(" till ")
		   .append(endTime.getTime())
		   .append("\nSleep time is : "+sleep)
		   .append("\nAt home time is : "+atHome)
		   .append("\nMoving at home time is : "+moving);
		
		sendBullet.sendNote("OpenHAB",log.toString());
	}
}

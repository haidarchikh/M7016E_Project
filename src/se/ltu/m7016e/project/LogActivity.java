package se.ltu.m7016e.project;

import java.io.IOException;
import java.util.Calendar;

import org.apache.http.client.ClientProtocolException;
	
public class LogActivity {
	 
	public final static  String MATTRESS_SENSOR = "mattress_Sensor";
	public final static  String MOTION_SENSOR = "motion_Sensor";
	public final static  String RFID = "RFID";
	private MongoDB database;
	private ActivitiesTime getTime;
	private long sleep	;
	private long atHome	;
	private long moving ;
	private Calendar startTime;
	private Calendar endTime;
	
	public LogActivity(MongoDB database ,Calendar startTime , Calendar endTime){
				this.startTime 	= startTime;
				this.endTime	= endTime;
				this.database 	= database;
	}
	
	public void getLog(){
		getTime = new ActivitiesTime(database);
		
		sleep  = getTime.findActivityTime(startTime, endTime, MATTRESS_SENSOR);
		atHome = getTime.findActivityTime(startTime, endTime, RFID);
		moving = getTime.findActivityTime(startTime, endTime, MOTION_SENSOR);
	}
	
	public void sendLog() throws ClientProtocolException, IOException{
		
		PushbulletClient sendBullet = new PushbulletClient();
		StringBuilder log = new StringBuilder();
		log.append("Log Activity from ")
		   .append(startTime.getTime())
		   .append(" till ")
		   .append(endTime.getTime())
		   .append("\\nSleep time is : "+sleep)
		   .append("\\nAt home time is : "+atHome)
		   .append("\\nMoving at home time is : "+moving);
		
		sendBullet.sendNote("OpenHAB",log.toString());
	}
}

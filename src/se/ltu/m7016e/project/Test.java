package se.ltu.m7016e.project;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import it.sauronsoftware.cron4j.Scheduler;

public class Test {
	
	private static final String MotionSensor = "Ms_Motion";
	private static final String LIGHT_SWITCH = "Light_Switch";
	private static final String METER_SWITCH = "Meter_Switch";
	private static final String ON 			 = "ON";
	private static final String OFF 		 = "OFF";
	private static final String OPENHAB_IP   = "192.168.1.9";
	private static final int 	OPENHAB_PORT = 8080;
	public static void main(String[] args) {
		
		Runnable sendTestBullet	= new Runnable(){
			@Override
			public void run() {
				PushbulletClient note = new PushbulletClient();
				try {
					note.sendNote("Test", "console");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
			
		};
		Runnable suggestActivity = new Runnable(){
			@Override
			public void run() {
				MongoDB database = new MongoDB("openhab", "test1");
				database.connect();
				SuggestActivity activity = new SuggestActivity();
				try {
					activity.suggestWeather();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				database.disconnect();
			}
		};

		Runnable logActivity = new Runnable(){
			@Override
			public void run() {
			//	Calendar startTime = Calendar.getInstance();
			//	Calendar endTime   = Calendar.getInstance();
			//	startTime.add(Calendar.WEEK_OF_MONTH, -1);
				
				Calendar startTime = new GregorianCalendar(2015, 04, 26, 6, 45, 00);
				Calendar endTime   = new GregorianCalendar(2015, 04, 30, 7, 30, 00);

				MongoDB database = new MongoDB("openhab", "test1");
				database.connect();
				LogActivity log = new LogActivity(startTime , endTime);
				log.getLog();
				try {
					log.sendLog();
				} catch (IOException e) {
					e.printStackTrace();
			    }
				database.getCount();
				database.disconnect();
		}
	};
		Runnable openHabClient = new Runnable(){
		@Override
		public void run() {
			 OpenhabClient openHab = new OpenhabClient(OPENHAB_IP, OPENHAB_PORT);
				try {
					openHab.pushItemValue(METER_SWITCH , OFF);
					openHab.pullItemValue(METER_SWITCH);
				} catch (IOException e) {
					e.printStackTrace();
			}	
		}
		
	};
	     Thread mThread = new Thread(sendTestBullet);
	    //mThread.start();
	    
	    Thread mThread1 = new Thread(logActivity);
	    mThread1.start();
	    
	    
	    try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    
	    Thread mThread2 = new Thread(suggestActivity);
	    mThread2.start();
	    
		Scheduler sendTestBulletScheduler = new Scheduler();    
		sendTestBulletScheduler.schedule("* * * * *", suggestActivity);
	//	sendTestBulletScheduler.start();
		
		Scheduler suggestActicityScheduler = new Scheduler();
		suggestActicityScheduler.schedule("* * * * *", suggestActivity);
	//	suggestActicityScheduler.start();
	    
		Scheduler logActivityScheduler = new Scheduler();
		logActivityScheduler.schedule("* * * * *", logActivity);
	//	logActivityScheduler.start();
		
	}
}
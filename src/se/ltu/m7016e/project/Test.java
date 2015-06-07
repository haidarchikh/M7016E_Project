package se.ltu.m7016e.project;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class Test {
	
	private static final String MotionSensor = "Ms_Motion";
	private static final String LIGHT_SWITCH = "Light_Switch";
	private static final String METER_SWITCH = "Meter_Switch";
	private static final String ON 			 = "ON";
	private static final String OFF 		 = "OFF";
	private static final String OPENHAB_IP   = "192.168.1.9";
	private static final int 	OPENHAB_PORT = 8080;
	
	public static void main(String[] args) {
		
		Calendar startTime = new GregorianCalendar(2015, 04, 29, 6, 45, 00);
		Calendar endTime= new GregorianCalendar(2015, 04, 29, 7, 30, 00);
		long timeDeference = endTime.getTimeInMillis() - startTime.getTimeInMillis();
		
		MongoDB database = new MongoDB("openhab", "test1");
		database.connect();
		LogActivity log = new LogActivity(database ,startTime , endTime);
		log.getLog();
		System.out.println("Over all time difference is "+timeDeference/1000);
		
		/*
		try {
			log.sendLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
		/*
		 OpenhabClient openHab = new OpenhabClient(OPENHAB_IP, OPENHAB_PORT);
			try {
				openHab.pushItemValue(METER_SWITCH , OFF);
				openHab.pullItemValue(METER_SWITCH);
			} catch (IOException e) {
				e.printStackTrace();
			}
		*/
		//ActivitiesTime getTime = new ActivitiesTime(database);
		//long time = getTime.findActivityTime(calStart, calEnd,"mattress_Sensor");
		//String s = Objects.toString(time, null);
		
		//database.findItems("item","motion_Sensor");
		//database.findItems("item","mattress_Sensor");
		//database.findItems("item","power_Sensor");
		//database.findItems("item","door_Sensor");
		
		database.getCount();
		database.disconnect();
	}
}
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
		
		Calendar startTime = new GregorianCalendar(2015, 04, 26, 6, 45, 00);
		Calendar endTime= new GregorianCalendar(2015, 04, 30, 7, 30, 00);
		
		MongoDB database = new MongoDB("openhab", "test1");
		database.connect();
		LogActivity log = new LogActivity(startTime , endTime);
		log.getLog();
		try {
			log.sendLog();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		SuggestActivity activity = new SuggestActivity();
		try {
			activity.suggestWeather();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		OpenWeatherMap weather = new OpenWeatherMap();
		try {
			weather.getWeather();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long timeDeference = endTime.getTimeInMillis() - startTime.getTimeInMillis();
		timeDeference /= (1000*60);
		System.out.println("Over all time difference is "+timeDeference);
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
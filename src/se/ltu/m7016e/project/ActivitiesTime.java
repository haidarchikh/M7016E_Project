package se.ltu.m7016e.project;

import java.util.Calendar;
import java.util.Date;

import org.bson.Document;

import com.mongodb.client.MongoCollection;

public class ActivitiesTime {
	
	private MongoCollection<Document> coll= null;
	
	public ActivitiesTime(MongoDB database){
	coll = database.getColl();	
	}
	
		
	//gets start date , end date , item name
	//Item type have to be Boolean
	//calculate the true time of the item
	public long findActivityTime(Calendar startDate, Calendar endDate, String itemName){
		
		long duration = 0;
		Calendar calActivityOn 	= Calendar.getInstance();
		Calendar calActivityOff = Calendar.getInstance();
		Calendar lookUpFrom		= Calendar.getInstance();
		Document docActivityOn;
		Document docActivityOff;
		
		//first calculation 
		//checks if the activity is On at start time , if so , add the time
		//gets the first activity's document after the startDate
		Document doc = coll.find(new Document("item",itemName)
									     .append("timestamp", new Document("$gt", startDate.getTime())))
									     .first();
		if(doc == null){
			System.out.println("The user didn't do the activity within the given time");
		} else {
		//set a calendar time to the retrieved Document time
		calActivityOn.setTime((Date) ( doc.get("timestamp")));
		if(doc.containsValue(false)){
			duration = timeDifference(startDate, calActivityOn);
		}
		lookUpFrom = (Calendar) startDate.clone();
		//second calculation 
		//Finds the first true (activity ON) , Finds the first false (activity Off), gets the time difference
		//start again from the false time  (activity Off)
		
		while(true){
			//gets the moment where the user started the activity
			 docActivityOn = coll.find(new Document("item",itemName)
			 			.append("value",true)
			 			.append("timestamp", new Document("$gt", lookUpFrom.getTime())))
			 			.first();
			if(docActivityOn == null){
				break;
			}
			 calActivityOn.setTime((Date) ( docActivityOn.get("timestamp")));
			
			//gets the moment where the user stopped the activity
			 docActivityOff = coll.find(new Document("item",itemName)
			 			.append("value",false)
			 			.append("timestamp", new Document("$gt", lookUpFrom.getTime())))
			 			.first();
			if(docActivityOff == null){
				 //if the user is still doing the activity till this moment
				 //No false for the activity in the database
				 duration+= timeDifference(calActivityOn, endDate);
				 break;
			}
			calActivityOff.setTime((Date) ( docActivityOff.get("timestamp")));
			
			if(calActivityOff.after(endDate)){
				//if the user still doing the activity at endDate, add the time from the last true
				//to the endDate
				duration+= timeDifference(calActivityOn, endDate);
				break;
			}
			lookUpFrom = calActivityOff;
			duration+= timeDifference(calActivityOn, calActivityOff);
		}
		//System.out.println(doc);
		System.out.print("The user spent "+duration+" seconds doing the activity monitored by <"+ itemName);
		System.out.println("> between "+startDate.getTime()+" and "+endDate.getTime());
		}
		return duration;
	}
	
	public long findActivityTime1(Calendar startDate, Calendar endDate, String itemName){
		
		long duration = 0;
		Calendar calActivityOn 	= Calendar.getInstance();
		Calendar calActivityOff = Calendar.getInstance();
		Calendar lookUpFrom		= Calendar.getInstance();
		Document docActivityOn;
		Document docActivityOff;
		
		//first calculation 
		//checks if the activity is On at start time , if so , add the time
		//gets the first activity's document after the startDate
		Document doc = coll.find(new Document("item",itemName)
									     .append("timestamp", new Document("$gt", startDate.getTime())))
									     .first();
		if(doc == null){
			System.out.println("The user didn't do the activity within the given time");
		} else {
		//set a calendar time to the retrieved Document time
		calActivityOn.setTime((Date) ( doc.get("timestamp")));
		if(doc.containsValue("CLOSED")){
			duration = timeDifference(startDate, calActivityOn);
		}
		lookUpFrom = (Calendar) startDate.clone();
		//second calculation 
		//Finds the first true (activity ON) , Finds the first false (activity Off), gets the time difference
		//start again from the false time  (activity Off)
		
		while(true){
			//gets the moment where the user started the activity
			 docActivityOn = coll.find(new Document("item",itemName)
			 			.append("value","OPEN")
			 			.append("timestamp", new Document("$gt", lookUpFrom.getTime())))
			 			.first();
			if(docActivityOn == null){
				break;
			}
			 calActivityOn.setTime((Date) ( docActivityOn.get("timestamp")));
			
			//gets the moment where the user stopped the activity
			 docActivityOff = coll.find(new Document("item",itemName)
			 			.append("value","CLOSED")
			 			.append("timestamp", new Document("$gt", lookUpFrom.getTime())))
			 			.first();
			if(docActivityOff == null){
				 //if the user is still doing the activity till this moment
				 //No false for the activity in the database
				 duration+= timeDifference(calActivityOn, endDate);
				 break;
			}
			calActivityOff.setTime((Date) ( docActivityOff.get("timestamp")));
			
			if(calActivityOff.after(endDate)){
				//if the user still doing the activity at endDate, add the time from the last true
				//to the endDate
				duration+= timeDifference(calActivityOn, endDate);
				break;
			}
			lookUpFrom = calActivityOff;
			duration+= timeDifference(calActivityOn, calActivityOff);
		}
		//System.out.println(doc);
		System.out.print("The user spent "+duration+" seconds doing the activity monitored by <"+ itemName);
		System.out.println("> between "+startDate.getTime()+" and "+endDate.getTime());
		}
		return duration;
	}
	
	
	public void average(){
		Document doc = coll.find(new Document("item","Ms_Lum")
	     .append("timestamp", new Document("$avg", ""))).first();
		System.out.println(doc);
	}
	//take two calendar objects and returns the difference in seconds
	//It will always return a positive value
	public long timeDifference(Calendar cal1, Calendar cal2){
		long milliSec1 = cal1.getTimeInMillis();
		long milliSec2 = cal2.getTimeInMillis();
		long timeDifInMilliSec;
		if(milliSec1 >= milliSec2)
		{
			timeDifInMilliSec = milliSec1 - milliSec2;
		} else {
			timeDifInMilliSec = milliSec2 - milliSec1;
		}

		long timeDifSeconds = timeDifInMilliSec / 1000;
		return timeDifSeconds;
	/*  
	    long timeDifMinutes = timeDifInMilliSec / (60 * 1000);
	    long timeDifHours = timeDifInMilliSec / (60 * 60 * 1000);
    	long timeDifDays = timeDifInMilliSec / (24 * 60 * 60 * 1000);
    	long[] difference = {timeDifSeconds,timeDifMinutes,timeDifHours,timeDifDays};
		return difference ;
	*/
	}
}

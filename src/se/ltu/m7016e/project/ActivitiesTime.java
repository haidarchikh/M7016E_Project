package se.ltu.m7016e.project;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.bson.Document;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.client.AggregateIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class ActivitiesTime {
	
	private MongoCollection<Document> coll= null;
	public final static String MATTRESS_SENSOR 	= "mattress_Sensor";
	public final static String RFID 			= "RFID";
	public final static String MOTION_SENSOR 	= "motion_Sensor";
	public final static String TIMESTAMP 		= "timestamp";
	public final static String VALUE 			= "value";
	public final static String ITEM 			= "item";
	public ActivitiesTime(){
	//coll = database.getColl();
	coll = MongoDB.coll;
	}
	
public long findActivityTime(Calendar startDate, Calendar endDate, String itemName){
		
	long duration = 0;
	Document DOC = null;
	boolean gotTrue = false;
	boolean gotFalse = false;
	Calendar calActivityTrue  = Calendar.getInstance();
	Calendar calActivityFalse = Calendar.getInstance();
	
	// Build the query
	BasicDBObject cluaseStart = new BasicDBObject("$gte",startDate.getTime());
	BasicDBObject cluaseEnd   = new BasicDBObject("$lte",endDate.getTime());
	BasicDBObject matchItem   = new BasicDBObject(ITEM      , itemName);
	BasicDBObject gtStartDate = new BasicDBObject(TIMESTAMP , cluaseStart);
	BasicDBObject ltStartDate = new BasicDBObject(TIMESTAMP , cluaseEnd);
	BasicDBObject sort 		  = new BasicDBObject("$sort" ,new BasicDBObject(TIMESTAMP , 1));
	BasicDBList andClauses    = new BasicDBList();
	andClauses.add(matchItem);
	andClauses.add(gtStartDate);
	andClauses.add(ltStartDate);
	BasicDBObject and   = new BasicDBObject("$and", andClauses);
	BasicDBObject match = new BasicDBObject("$match", and);
	
	// Aggregate
	List<BasicDBObject> pipeline = Arrays.asList(match, sort);
	AggregateIterable<Document> output =  coll.aggregate(pipeline);
	// Get a iterator over the data
	MongoCursor<Document> cursor = output.iterator();
	
	if(cursor.hasNext()){
		Document temp = cursor.next();
		
		// if the activity is going at start date
		// get the duration and start the loop with a true value
		if(temp.containsValue(false)){
			calActivityFalse.setTime((Date) temp.get(TIMESTAMP));
			duration+= timeDifference(calActivityFalse, startDate);
		} else {
			cursor = output.iterator();
		}
		temp = null;
		
		while(cursor.hasNext()){
			DOC = null;
			DOC = (Document) cursor.next();
			
			// Get the false
			if(DOC.containsValue(false)){
				calActivityFalse.setTime((Date) ( DOC.get(TIMESTAMP)));
				gotFalse = true;
			}
			// Get the first true 
			if(DOC.containsValue(true) && !gotTrue){
				calActivityTrue.setTime((Date) ( DOC.get(TIMESTAMP)));
				gotTrue = true;
			}
			// if we get a true and false, get time difference 
			// and prepare for a new true false 
			if(gotFalse && gotTrue){
				duration  += timeDifference(calActivityFalse, calActivityTrue);
				gotTrue   = false;
				gotFalse  = false;
			}
		}
		if(DOC!=null){
			// if the activity is going at end date
			if(gotTrue && !gotFalse){
				duration += timeDifference(calActivityTrue, endDate);
			}
		}
		// if the activity is going the whole time
		if(DOC==null){
			temp = coll.find(gtStartDate).first();
			if(temp.containsValue(false)){
				duration = timeDifference(startDate, endDate);
				temp = null;
			}
		}
	}
			
	System.out.print("The user spent "+duration/60+" minutes doing the activity monitored by <"+ itemName);
	System.out.println("> between "+startDate.getTime()+" and "+endDate.getTime());

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

	
	
	
	
	
	/*
	
	
	
	//gets start date , end date , item name
	//Item type has to be Boolean
	//calculate the true time of the item
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
		Document doc = coll.find(new Document(ITEM,itemName)
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
	
	public long findActivityTime2(Calendar startDate, Calendar endDate, String itemName){
		
		long duration = 0;
		Calendar calActivityOn 	= Calendar.getInstance();
		Calendar calActivityOff = Calendar.getInstance();
		Calendar calActivityFirst = Calendar.getInstance();
		Calendar lookUpFrom		= Calendar.getInstance();
		Document docActivityOn;
		Document docActivityOff;
		
		//first calculation 
		//checks if the activity is On at start time , if so , add the time
		//gets the first activity's document after the startDate
		Document doc = coll.find(new Document("item",itemName)
									     .append("timestamp", new Document("$gt", startDate.getTime())))
									     .first();
		if(doc != null){calActivityFirst.setTime((Date) ( doc.get("timestamp")));}
		
		if(doc== null){
			System.out.println("The user didn't do the activity within the given time");
		} else if(calActivityFirst.after(endDate)){
			
			if(doc.containsValue(false)){duration = timeDifference(startDate, endDate);}
			else {System.out.println("The user didn't do the activity within the given time");}
		
		} else if(!calActivityFirst.after(endDate)){
			
			if(doc.containsValue(false)){duration = timeDifference(startDate, calActivityFirst);}
			//all the code should be here
			lookUpFrom = (Calendar) calActivityFirst.clone();
			//second calculation 
			//Finds the first true (activity ON) , Finds the first false (activity Off), gets the time difference
			//start again from the false time  (activity Off)
			
			while(true){
				//gets the moment where the user started the activity
				 docActivityOn = coll.find(new Document("item",itemName)
				 			.append("value",true)
				 			.append("timestamp", new Document("$gt", lookUpFrom.getTime())))
				 			.first();
				if(docActivityOn == null){break;}
				calActivityOn.setTime((Date) ( docActivityOn.get("timestamp")));
				if(calActivityOn.after(endDate)){break;}
				
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
*/
}
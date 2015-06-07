package se.ltu.m7016e.project;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongoDB {
	private String hostAddress = null;
	private int hostPort = 0; 
	private String database= null;
	private String collection = null; 
	private MongoClient mongoClient = null;
	private MongoDatabase db = null;
	private MongoCollection<Document> coll= null;
	
	public MongoDB(String database , String collection) {
		hostAddress = "localhost";
		hostPort = 27017;
		this.database = database;
		this.collection = collection;
	}
		
	public MongoDB(String hostAddress,int hostPort , String database , String collection) {
		this.hostAddress = hostAddress;
		this.hostPort = hostPort;
		this.database = database;
		this.collection = collection;
	}
	public MongoCollection<Document> getColl() {
		return coll;
	}
	public MongoDatabase getdb() {
		return db;
	}
		
	//connect to the database
	public void connect(){
		mongoClient = new MongoClient(hostAddress, hostPort);
		db = mongoClient.getDatabase(database);
		coll= db.getCollection(collection);
	}
		
	//close the connection to database
	public void disconnect(){
		mongoClient.close();
	}
		
	//find all the item matches a given key,value pair and print them
	public void findItem(String key, String value){
		
		FindIterable<Document> iterable = coll.find(new Document(key, value));
		iterable.forEach(new Block<Document>() {
			@Override
			public void apply(final Document document) {
					System.out.println(document);
					//System.out.println(document.toJson());
	    	}
		});
	}
		
	//print how many document in a collection
	public void getCount(){
		System.out.println("There is "+coll.count()+" documents in the collection");
	}			
}

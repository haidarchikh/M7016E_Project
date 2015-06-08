package se.ltu.m7016e.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;


public class PushbulletClient {
	
	private HttpClient client;
	private final static String noteUrl = "https://api.pushbullet.com/v2/pushes";
	private final static String token 	= "Bearer COg9mSrDPFKBQIxzK7twCbEOHgovuh14";
	private StringEntity stringEntity;
	
	public PushbulletClient(){}
	
	//gets note title and body and send a note to pushbullet user 
	public void sendNote(String noteTitle , String noteCotent) throws ClientProtocolException, IOException{
		client = HttpClients.custom().build();
		JSONObject JSONEntity = new JSONObject();
		try {
			JSONEntity.put("type" , "note")
			          .put("title", noteTitle)
			          .put("body" , noteCotent);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		System.out.println(JSONEntity);
		stringEntity = new StringEntity(JSONEntity.toString());
		HttpUriRequest post = RequestBuilder
				.post()
				.setUri(noteUrl)
				.setHeader(HttpHeaders.CONTENT_TYPE, "application/json")
				.setHeader("Authorization", token)
				.setEntity(stringEntity)
				.build();
		HttpResponse response = client.execute(post);
		BufferedReader rd = new BufferedReader(
				new InputStreamReader(response.getEntity().getContent()));
		 
		StringBuffer result = new StringBuffer();
		String line = "";
			while ((line = rd.readLine()) != null) {
				result.append(line);
		}
		System.out.println(result.toString());
		System.out.println("Pushbullet response code is "+response.getStatusLine().getStatusCode());
	}
}

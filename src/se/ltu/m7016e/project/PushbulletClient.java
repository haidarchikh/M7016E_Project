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


public class PushbulletClient {
	
	private HttpClient client;
	private final static String noteUrl = "https://api.pushbullet.com/v2/pushes";
	private final static String token 	= "Bearer COg9mSrDPFKBQIxzK7twCbEOHgovuh14";
	private StringBuilder note;
	private StringEntity stringEntity;
	
	public PushbulletClient(){}
	
	//gets note title and body and send a note to pushbullet user 
	public void sendNote(String noteTitle , String noteCotent) throws ClientProtocolException, IOException{
		client = HttpClients.custom().build();
		note = new StringBuilder();
		note.append("{\"type\": \"note\", \"title\":")
			.append("\""+noteTitle+"\"")
			.append(", \"body\":")
			.append("\""+noteCotent+"\"")
			.append("}");
		System.out.println(note.toString());
		stringEntity = new StringEntity(note.toString());
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
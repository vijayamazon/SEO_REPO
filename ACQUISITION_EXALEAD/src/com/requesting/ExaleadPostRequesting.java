package com.requesting;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ExaleadPostRequesting {

	public static void main(String[] args){

		//extract(query = "tablette", nb = 10, loc = "Ton/Repertoire/Local/")
        try{
		String url_string = "http://exasearchv6.gslb.cdweb.biz:10010/search-api/search";
		URL url = new URL(url_string);

		HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	    connection.setRequestMethod("GET");
		//connection.setRequestProperty("User-Agent",this.user_agent);
		//connection.setInstanceFollowRedirects(true);
		//connection.setConnectTimeout(30000);
		connection.connect();
		// getting the status from the connection
		System.out.println(connection.getResponseCode());
		// getting the content to parse
		InputStreamReader in = new InputStreamReader((InputStream) connection.getContent());
		BufferedReader buff = new BufferedReader(in);
		String content_line;
		StringBuilder builder=new StringBuilder();
		do {
			content_line = buff.readLine();
			builder.append(content_line);
		} while (content_line != null);
		String html = builder.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	
}
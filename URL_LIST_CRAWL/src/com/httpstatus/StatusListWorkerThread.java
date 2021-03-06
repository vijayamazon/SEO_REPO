package com.httpstatus;

import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class StatusListWorkerThread implements Runnable {
	private String user_agent;
	private String description;
	private List<Integer> thread_fetch_ids = new ArrayList<Integer>();
	private List<ULRId> my_urls_to_fetch = new ArrayList<ULRId>();
	private Connection con;

	public StatusListWorkerThread(Connection con ,List<Integer> to_fetch, String my_user_agent, String my_description) throws SQLException{
		this.user_agent=my_user_agent;
		this.description=my_description;
		this.thread_fetch_ids=to_fetch;
		this.con = con;
		PreparedStatement pst  = null;
		if ("all".equals(my_description)){
			String my_url="SELECT URL, ID FROM HTTPSTATUS_LIST WHERE TO_FETCH = TRUE and ID in "+to_fetch.toString();
			my_url=my_url.replace("[", "(");
			my_url=my_url.replace("]", ")");
			pst = con.prepareStatement(my_url);
		} else {
			String my_url="SELECT URL, ID FROM HTTPSTATUS_LIST WHERE TO_FETCH = TRUE and DESCRIPTION='"+description+"' and ID in "+to_fetch.toString();
			my_url=my_url.replace("[", "(");
			my_url=my_url.replace("]", ")");
			pst = con.prepareStatement(my_url);
		};
		ResultSet rs = null;
		rs = pst.executeQuery();
		while (rs.next()) {
			String loc_url = rs.getString(1);
			int id = rs.getInt(2);
			ULRId toadd = new ULRId();
			toadd.setId(id);
			toadd.setUrl(loc_url);
			my_urls_to_fetch.add(toadd); 
		}
	}

	public void run() {
		List<StatusInfo> status=processCommand();
		try {
			updateStatus(status);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread().getName()+" End");
	}

	private void updateStatus(List<StatusInfo> status) throws SQLException{
		Statement st = con.createStatement();       
		con.setAutoCommit(false);      
		for (int i=0;i<status.size();i++){
			String batch ="UPDATE HTTPSTATUS_LIST SET STATUS="+status.get(i).getStatus_info()+", TO_FETCH=FALSE WHERE ID="+status.get(i).getId();
			st.addBatch(batch);
		}      
		//int counts[] = st.executeBatch();
		st.executeBatch();
		con.commit();
		System.out.println("Inserting : " + status.size() + "ULRs into database");
	}

	private List<StatusInfo> processCommand() {
		List<StatusInfo> my_fetched_status = new ArrayList<StatusInfo>();
		for (int i=0;i<my_urls_to_fetch.size();i++){
			ULRId line_info=my_urls_to_fetch.get(i);
			StatusInfo info = new StatusInfo();
			info.setId(line_info.getId());
			// second method
			int status=-1;
			HttpURLConnection connection = null;
			try{
				//System.out.println(Thread.currentThread().getName()+" fetching URL : "+line_info.getUrl());
				URL url = new URL(line_info.getUrl());
				connection = (HttpURLConnection)url.openConnection();
				connection.setRequestMethod("HEAD");
				connection.setRequestProperty("User-Agent",this.user_agent);
				connection.setInstanceFollowRedirects(false);
				connection.setConnectTimeout(30000);
				connection.connect();
				// getting the status from the connection
				status=connection.getResponseCode();
				System.out.println(Thread.currentThread().getName()+" Status " +status+ " fetching URL : "+line_info.getUrl());
			  } catch (Exception e){
				System.out.println("Error with "+line_info);
				e.printStackTrace();
			}

			if (connection != null){
				connection.disconnect();
			}

			info.setStatus_info(status);

			my_fetched_status.add(info);
		}
		return my_fetched_status;
	}
	
	class StatusInfo{
		private int status_info;
		private int id;


		public int getStatus_info() {
			return status_info;
		}
		public void setStatus_info(int status_info) {
			this.status_info = status_info;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}

	class ULRId{
		private String url="";
		private int id;
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public int getId() {
			return id;
		}
		public void setId(int id) {
			this.id = id;
		}
	}
	

}

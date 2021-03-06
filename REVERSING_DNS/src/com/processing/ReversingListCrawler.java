package com.processing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ReversingListCrawler {
	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/reversing_dns.properties";
	// we here just want to get every URL from the input file and get if the SKU is sold by market place/cdiscount and so on
	private static String insert_statement="INSERT INTO IP_HOSTNAME(IP,HOSTNAME,COUNT)"
			+ " VALUES(?,?,?)";
	private static int nb_lines = 0;
	private static List<IPInfo> resultsList = new ArrayList<IPInfo>();
	private static Connection con;
	private static int batch_size = 10000;

	public static void main(String[] args)  {
		String fileName="/home/sduprey/My_Data/My_Logs_IPs/IP_23_11.csv";

		// Reading the property of our database
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(database_con_path);
			props.load(in);
		} catch (IOException ex) {
			System.out.println("Trouble getting the database parameters");
			ex.printStackTrace();
		} finally {

			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				System.out.println("Trouble getting the database parameters");
				ex.printStackTrace();
			}
		}

		// the following properties have been identified
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");

		try {  
			con = DriverManager.getConnection(url, user, passwd);
		}catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("Trouble instantiating the database connection");
		} 
		try {
			reverse_dns_the_list(fileName);
			update_database();
		} catch (IOException e) {
			System.out.println("Trouble saving result flat file ");
			e.printStackTrace();
		}finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static void reverse_dns_the_list(String fileName) throws IOException{
		System.out.println("Reading line number : "+nb_lines);
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String header = br.readLine();
		System.out.println(header);
		String line="";
		while ((line = br.readLine()) != null) {
			String[] pieces=line.split(",");
			String currentIP = pieces[0];
			String currentCount = pieces[1];
			String current_hostname = fetch_dns(currentIP);
			IPInfo ifInfo = new IPInfo();
			ifInfo.setCount(Integer.valueOf(currentCount));
			ifInfo.setHostname(current_hostname);
			ifInfo.setIp(currentIP);				
			System.out.println(" Reading lines number " + nb_lines);
			System.out.println(" Current IP : " + currentIP);
			System.out.println(" Current count : " + currentCount);
			System.out.println(" Current hostname : " + current_hostname);
			resultsList.add(ifInfo);
			nb_lines++;
		}
	}

	private static void update_database(){
		try{
			con.setAutoCommit(false);
			PreparedStatement st = con.prepareStatement(insert_statement);		
			int local_counter=0;
			for ( IPInfo ipinfo : resultsList){
				local_counter++;
				st.setString(1,ipinfo.getIp());
				st.setString(2,ipinfo.getHostname());
				st.setInt(3,ipinfo.getCount());
				st.addBatch();			
				if(local_counter == batch_size){
					System.out.println(" Inserting batch ");
					st.executeBatch();		 
					con.commit();
					st.close();
					st = con.prepareStatement(insert_statement);	
				}
			}
			st.executeBatch();		 
			con.commit();
			System.out.println(Thread.currentThread()+"Committed " + local_counter + " updates");
		} catch (SQLException e){
			//System.out.println("Line already inserted : "+nb_lines);
			e.printStackTrace();  
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex1) {
					ex1.printStackTrace();
					System.out.println("Trouble inserting batch");
				}
			}
		}	
	}

	private static String fetch_dns(String ip_adresse) throws UnknownHostException{
		long before = System.currentTimeMillis();
		InetAddress addr = InetAddress.getByName(ip_adresse);
		String hostname = addr.getHostName();
		long after = System.currentTimeMillis();
		System.out.println((after - before) + " ms");
		return hostname;
	}

	private static class IPInfo {
		private String ip;
		private String hostname;
		public String getIp() {
			return ip;
		}
		public void setIp(String ip) {
			this.ip = ip;
		}
		public String getHostname() {
			return hostname;
		}
		public void setHostname(String hostname) {
			this.hostname = hostname;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
		}
		private int count;
	}



}
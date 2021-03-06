package com.httpstatus;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;


public class StatusListThreadPool {
	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/url_list_status.properties";
	private static int fixed_pool_size = 50;
	private static int size_bucket = 20;
	private static List<Integer> tofetch_list = new ArrayList<Integer>();

	public static void main(String[] args) {
		String my_user_agent= "CdiscountBot-crawler";
		String my_description = "all";
		if (args.length>=1){
			my_user_agent= args[0];
		} else {
			System.out.println("You didn't specify any user agent, we'll use : "+my_user_agent);
		}
		if (args.length==2){
			my_description= args[1];
		} else {
			System.out.println("You didn't specify any description, none will be used");
		}

		if (args.length==3){
			fixed_pool_size= Integer.valueOf(args[2]);
			System.out.println("You specified "+fixed_pool_size + " threads");
		}

		if (args.length==4){
			fixed_pool_size= Integer.valueOf(args[3]);
			System.out.println("You specified a "+size_bucket + " bucket size");
		} else {
			System.out.println("You didn't specify a bucket size, default will be "+size_bucket);
		}

		System.out.println("User agent selected : "+my_user_agent);
		System.out.println("Description chosen : "+my_description);

		//		// Getting the database property
		Properties props = new Properties();
		FileInputStream in = null;      
		try {
			in = new FileInputStream(database_con_path);
			props.load(in);
		} catch (IOException ex) {
			System.out.println("Trouble fetching database configuration");
			ex.printStackTrace();

		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				System.out.println("Trouble fetching database configuration");
				ex.printStackTrace();
			}
		}
		//the following properties have been identified
		String url = props.getProperty("db.url");
		String user = props.getProperty("db.user");
		String passwd = props.getProperty("db.passwd");

		System.out.println("You are connected to the postgresql HTTPSTATUS_LIST database as "+user);
		// Instantiating the pool thread
		System.out.println("You'll be using "+fixed_pool_size+" threads ");
		ExecutorService executor = Executors.newFixedThreadPool(fixed_pool_size);

		// The database connection
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;

		try {  
			con = DriverManager.getConnection(url, user, passwd);
			// getting the number of URLs to fetch
			if ("all".equals(my_description)){
				pst = con.prepareStatement("SELECT ID FROM HTTPSTATUS_LIST WHERE TO_FETCH = TRUE");
			} else {
				pst = con.prepareStatement("SELECT ID FROM HTTPSTATUS_LIST WHERE TO_FETCH = TRUE and DESCRIPTION='"+my_description+"'");
			};
			rs = pst.executeQuery();
			while (rs.next()) {
				tofetch_list.add(rs.getInt(1));
			}
			int size=tofetch_list.size();
			System.out.println(size + " URL status to fetch according to the database \n");
			// we add one for the euclidean remainder
			int local_count=0;
			List<Integer> thread_list = new ArrayList<Integer>();
			for (int size_counter=0; size_counter<size;size_counter ++){
				if(local_count<size_bucket ){
					thread_list.add(tofetch_list.get(size_counter));
					local_count++;
				}
				if (local_count==size_bucket){
					Connection local_con = DriverManager.getConnection(url, user, passwd);
					System.out.println("Launching another thread with "+local_count+ " URLs to fetch");
					Runnable worker = new StatusListWorkerThread(local_con,thread_list,my_user_agent,my_description);
					executor.execute(worker);		
					local_count=0;
					thread_list = new ArrayList<Integer>();
				}
			}
			if (thread_list.size()>0){
				Connection local_con = DriverManager.getConnection(url, user, passwd);
				System.out.println("Launching another thread with "+local_count+ " URLs to fetch");
				Runnable worker = new StatusListWorkerThread(local_con,thread_list,my_user_agent,my_description);
				executor.execute(worker);
			}      
			//            
			//            
			//			// we add one for the euclidean remainder
			//			int nb_bucket = size/size_bucket+1;
			//			// we add one for the euclidean remainder
			//			int nbbucket_per_connection=nb_bucket/nb_connection+1;
			//			Connection local_con = DriverManager.getConnection(url, user, passwd);
			//			int connection_nb=1;
			//			// Dispatching all threads with their work to do
			//			int global_counter=0;
			//			for (int i = 0; i < nb_bucket; i++) {
			//				List<Integer> thread_list = new ArrayList<Integer>();
			//				int local_count=0;
			//				while (local_count<=size_bucket && global_counter<size){
			//					thread_list.add(tofetch_list.get(global_counter));
			//					local_count++;
			//					global_counter++;
			//				}
			//				if (connection_nb*nbbucket_per_connection <= i){
			//					local_con = DriverManager.getConnection(url, user, passwd);
			//					connection_nb++;
			//					System.out.println("Number of connections"+connection_nb);
			//				}
			//				System.out.println("Launching another thread with "+local_count+ " URLs to fetch");
			//				Runnable worker = new StatusListWorkerThread(local_con,thread_list,my_user_agent,my_description);
			//				executor.execute(worker);
			//			}
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(StatusListThreadPool.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}
				if (con != null) {
					con.close();
				}

			} catch (SQLException ex) {
				Logger lgr = Logger.getLogger(StatusListThreadPool.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}

		executor.shutdown();
		while (!executor.isTerminated()) {
		}

		System.out.println("Finished all threads");

	}


}

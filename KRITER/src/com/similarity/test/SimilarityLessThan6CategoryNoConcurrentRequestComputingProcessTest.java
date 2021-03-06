package com.similarity.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.similarity.computing.SimilarityComputingNoFetchWorkerThread;
import com.similarity.parameter.KriterParameter;
import com.statistics.processing.CatalogEntry;

public class SimilarityLessThan6CategoryNoConcurrentRequestComputingProcessTest {
	public static String kriter_conf_path = "/home/sduprey/My_Data/My_Kriter_Conf/kriter.conf";
	public static Properties properties;
	private static String database_con_path = "/home/sduprey/My_Data/My_Postgre_Conf/kriter.properties";
	private static List<String> less_than_six_categories = new ArrayList<String>();
	// 633 categories with less than 6 elements
	private static int list_fixed_pool_size = 1;
	private static int list_size_bucket = 633;
	private static boolean recreate_table = false;
	public static String less_than_size_string = "6";
	public static String select_less_than_six_category = "select categorie_niveau_4 from CATEGORY_FOLLOWING where count < "+less_than_size_string;
	private static String select_entry_from_category4 = " select SKU, CATEGORIE_NIVEAU_1, CATEGORIE_NIVEAU_2, CATEGORIE_NIVEAU_3, CATEGORIE_NIVEAU_4,  LIBELLE_PRODUIT, MARQUE, DESCRIPTION_LONGUEUR80, VENDEUR, ETAT, RAYON, TO_FETCH FROM CATALOG";

	private static String drop_CATEGORY_FOLLOWING_table = "DROP TABLE IF EXISTS CATEGORY_FOLLOWING";
	private static String create_CATEGORY_FOLLOWING_table = "select distinct categorie_niveau_4, count(*), true as to_fetch into CATEGORY_FOLLOWING from CATALOG group by categorie_niveau_4";

	public static void main(String[] args) {
		System.out.println("Reading the configuration files : "+kriter_conf_path);
		try{
			loadProperties();
			KriterParameter.database_con_path=properties.getProperty("kriter.database_con_path");
			KriterParameter.small_list_pool_size =Integer.valueOf(properties.getProperty("kriter.small_list_pool_size")); 
			KriterParameter.small_list_size_bucket =Integer.valueOf(properties.getProperty("kriter.small_list_size_bucket")); 
			KriterParameter.big_list_pool_size =Integer.valueOf(properties.getProperty("kriter.big_list_pool_size")); 
			KriterParameter.big_list_size_bucket =Integer.valueOf(properties.getProperty("kriter.big_list_size_bucket")); 
			KriterParameter.max_list_size_separator_string=properties.getProperty("kriter.max_list_size_separator_string");
			KriterParameter.recreate_table=Boolean.parseBoolean(properties.getProperty("kriter.recreate_table"));
			KriterParameter.compute_optimal_parameters=Boolean.parseBoolean(properties.getProperty("kriter.compute_optimal_parameters"));
			KriterParameter.kriter_threshold =Integer.valueOf(properties.getProperty("kriter.kriter_threshold")); 
			KriterParameter.computing_max_list_size =Integer.valueOf(properties.getProperty("kriter.computing_max_list_size"));
			KriterParameter.batch_size =Integer.valueOf(properties.getProperty("kriter.batch_size"));
			KriterParameter.displaying_threshold =Integer.valueOf(properties.getProperty("kriter.displaying_threshold"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Trouble getting the configuration : unable to launch the crawler");
			System.exit(0);
		}
		System.out.println("Number of threads for list crawler : "+list_fixed_pool_size);
		System.out.println("Bucket size for list crawler : "+list_size_bucket);
		// it would be best to use a property file to store MD5 password
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

		System.out.println("You'll connect to the postgresql KRITERDB database as "+user);
		// Instantiating the pool thread
		ExecutorService executor = Executors.newFixedThreadPool(list_fixed_pool_size);
		// The database connection
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {  
			con = DriverManager.getConnection(url, user, passwd);
			System.out.println("Cleaning up and building the category_following table");
			if (recreate_table){
				cleaning_category_scheduler_database(con);
			}
			// getting the too big categories to exclude
			System.out.println("Requesting all distinct too big categories");
			pst = con.prepareStatement(select_less_than_six_category);
			rs = pst.executeQuery();
			while (rs.next()) {
				// fetching all
				String category_level_4 = rs.getString(1);
				less_than_six_categories.add(category_level_4);
			}
			rs.close();
			pst.close();
			// getting the number of URLs to fetch
			System.out.println("Requesting all data from categories categories");
			pst = con.prepareStatement(select_entry_from_category4);
			rs = pst.executeQuery();
			Map<String, List<CatalogEntry>> my_entries = new HashMap<String, List<CatalogEntry>>();
			while (rs.next()) {
				// fetching all
				CatalogEntry entry = new CatalogEntry();
				String sku = rs.getString(1);
				entry.setSKU(sku);
				// category fetching
				String CATEGORIE_NIVEAU_1 = rs.getString(2);
				entry.setCATEGORIE_NIVEAU_1(CATEGORIE_NIVEAU_1);
				String CATEGORIE_NIVEAU_2 = rs.getString(3);
				entry.setCATEGORIE_NIVEAU_2(CATEGORIE_NIVEAU_2);
				String CATEGORIE_NIVEAU_3 = rs.getString(4);
				entry.setCATEGORIE_NIVEAU_3(CATEGORIE_NIVEAU_3);
				String CATEGORIE_NIVEAU_4 = rs.getString(5);
				entry.setCATEGORIE_NIVEAU_4(CATEGORIE_NIVEAU_4);
				// product libelle
				String  LIBELLE_PRODUIT = rs.getString(6);
				entry.setLIBELLE_PRODUIT(LIBELLE_PRODUIT);
				String MARQUE = rs.getString(7);
				entry.setMARQUE(MARQUE);
				// brand description
				String  DESCRIPTION_LONGUEUR80 = rs.getString(8);
				entry.setDESCRIPTION_LONGUEUR80(DESCRIPTION_LONGUEUR80);
				// vendor and state (available or not)
				String VENDEUR = rs.getString(9);
				entry.setVENDEUR(VENDEUR);
				String ETAT = rs.getString(10);
				entry.setETAT(ETAT);
				String RAYON = rs.getString(11);
				entry.setRAYON(RAYON);
				Boolean to_fetch = rs.getBoolean(12);
				entry.setTO_FETCH(to_fetch);
				// we here just keep the less than six categories
				if (less_than_six_categories.contains(CATEGORIE_NIVEAU_4)){
					List<CatalogEntry> toprocess = my_entries.get(CATEGORIE_NIVEAU_4);
					if (toprocess == null){
						toprocess = new ArrayList<CatalogEntry>();
						my_entries.put(CATEGORIE_NIVEAU_4, toprocess);
					}
					toprocess.add(entry);
				} else {
					System.out.println("Too big category (we just keep the less than 6), we drop it : "+CATEGORIE_NIVEAU_4);
				}
			}		
			rs.close();
			pst.close();

			// iterating over the categories map !!! 
			Map<String, List<CatalogEntry>> thread_list = new HashMap<String, List<CatalogEntry>>();
			Iterator<Entry<String, List<CatalogEntry>>> it = my_entries.entrySet().iterator();
			// dispatching to threads
			int local_count=0;
			int global_count=0;
			while (it.hasNext()){	
				Map.Entry<String, List<CatalogEntry>> pairs = (Map.Entry<String, List<CatalogEntry>>)it.next();
				String current_category=pairs.getKey();
				List<CatalogEntry> category_entries = pairs.getValue();
				if(local_count<list_size_bucket ){
					thread_list.put(current_category,category_entries);		
					local_count++;
				}
				if (local_count==list_size_bucket){
					// one new connection per task
					System.out.println("Launching another thread with "+local_count+" Categories to fetch");
					Connection local_con = DriverManager.getConnection(url, user, passwd);
					Runnable worker = new SimilarityComputingNoFetchWorkerThread(local_con,thread_list);
					//Runnable worker = new SimilarityComputingWorkerThread(con,thread_list);
					executor.execute(worker);		
					// we initialize everything for the next thread
					local_count=0;
					thread_list = new HashMap<String, List<CatalogEntry>>();
				}
				global_count++;
			}

			// we add one for the euclidean remainder
			// there might be a last task with the euclidean remainder
			if (thread_list.size()>0){
				// one new connection per task
				System.out.println("Launching another thread with "+local_count+ " Categories to fetch");
				Connection local_con = DriverManager.getConnection(url, user, passwd);
				Runnable worker = new SimilarityComputingNoFetchWorkerThread(local_con,thread_list);
				//Runnable worker = new SimilarityComputingWorkerThread(con,thread_list);
				executor.execute(worker);
			}
			System.gc();
			System.out.println("We have : " +global_count + " Categories status to fetch according to the Kriter database \n");
		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (pst != null) {
					pst.close();
				}

			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		System.out.println("Finished all threads");
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private static void cleaning_category_scheduler_database(Connection con) throws SQLException{
		PreparedStatement drop_category_table_st = con.prepareStatement(drop_CATEGORY_FOLLOWING_table);
		drop_category_table_st.executeUpdate();
		System.out.println("Dropping the old CATEGORY_FOLLOWING table");
		drop_category_table_st.close();
		PreparedStatement create_category_table_st = con.prepareStatement(create_CATEGORY_FOLLOWING_table);
		create_category_table_st.executeUpdate();
		System.out.println("Creating the new CATEGORY_FOLLOWING table");
		create_category_table_st.close();
	}

	private static void loadProperties(){
		properties = new Properties();
		try {
			properties.load(new FileReader(new File(kriter_conf_path)));
		} catch (Exception e) {
			System.out.println("Failed to load properties file!!");
			e.printStackTrace();
		}
	}
}


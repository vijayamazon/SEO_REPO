package com.similarity.noconcurrentrequest.random.computing;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import com.statistics.processing.CatalogEntry;
import com.statistics.processing.StatisticsUtility;

public class SimilarityRandomComputingNoFetchWorkerThread implements Runnable {
	private Connection con;
	private static Random my_rand = new Random();
	private Map<String, List<CatalogEntry>> my_categories_to_compute  = new HashMap<String, List<CatalogEntry>>();
	// beware static shared global cache for unfetched skus
	private Map<CatalogEntry, Set<String>> unfetched_skus_local_cache = new HashMap<CatalogEntry, Set<String>>();

	private static String select_entry_from_category1 = " select SKU, CATEGORIE_NIVEAU_1, CATEGORIE_NIVEAU_2, CATEGORIE_NIVEAU_3, CATEGORIE_NIVEAU_4,  LIBELLE_PRODUIT, MARQUE, DESCRIPTION_LONGUEUR80, VENDEUR, ETAT FROM CATALOG where CATEGORIE_NIVEAU_1=?";
	private static String select_entry_from_category3 = " select SKU, CATEGORIE_NIVEAU_1, CATEGORIE_NIVEAU_2, CATEGORIE_NIVEAU_3, CATEGORIE_NIVEAU_4,  LIBELLE_PRODUIT, MARQUE, DESCRIPTION_LONGUEUR80, VENDEUR, ETAT FROM CATALOG where CATEGORIE_NIVEAU_3=?";
	private static String select_entry_from_category2 = " select SKU, CATEGORIE_NIVEAU_1, CATEGORIE_NIVEAU_2, CATEGORIE_NIVEAU_3, CATEGORIE_NIVEAU_4,  LIBELLE_PRODUIT, MARQUE, DESCRIPTION_LONGUEUR80, VENDEUR, ETAT FROM CATALOG where CATEGORIE_NIVEAU_2=?";

	private static String update_category = "update CATEGORY_FOLLOWING set to_fetch = false where CATEGORIE_NIVEAU_4 = ?";

	//private static String insert_cds_statement = "INSERT INTO CDS_SIMILAR_PRODUCTS(SKU,SKU1,SKU2,SKU3,SKU4,SKU5,SKU6) VALUES(?,?,?,?,?,?,?)";
	private static String update_catalog_statement = "UPDATE CATALOG SET SKU1=?,SKU2=?,SKU3=?,SKU4=?,SKU5=?,SKU6=?,TO_FETCH=false where SKU=?";

	private Map<String,List<String>> matching_skus = new HashMap<String,List<String>>();
	private static int kriter_threshold =6;
	private static int batch_size = 100;

	public SimilarityRandomComputingNoFetchWorkerThread(Connection con, Map<String, List<CatalogEntry>>  to_fetch) throws SQLException{
		this.con = con;
		this.my_categories_to_compute = to_fetch;
	}

	public void run() {
		String category_to_debug="";
		try {  
			Iterator<Entry<String, List<CatalogEntry>>> it = my_categories_to_compute.entrySet().iterator();
			// dispatching to threads
			while (it.hasNext()){	
				Map.Entry<String, List<CatalogEntry>> pairs = (Map.Entry<String, List<CatalogEntry>>)it.next();
				String category=pairs.getKey();
				List<CatalogEntry> my_data = pairs.getValue();
				category_to_debug=category;
				System.out.println(Thread.currentThread()+" Dealing with category : "+category);
				randomComputeDataList(my_data,category);
				

				//				System.out.println(Thread.currentThread()+" Category skus all fetched for data : "+category);
				//				computeDataList(my_data);
				//				//saving_similar_step_by_step();
				//				saving_similar();
				// for debug
				System.out.println(Thread.currentThread()  + "Category "+category+" dealt with");
				System.out.println(Thread.currentThread()  + "Updating category "+category+" size : "+my_data.size());
				updateCategory(category_to_debug);			
				//				System.gc();
			}		
			// dealing with unfetched skus
			// we loop over each sku and get back to fomer category level to find matching offer
			//
//			if (unfetched_skus_local_cache.size()>0){
//				backup_category3();
//			}
//
//			if (unfetched_skus_local_cache.size()>0){
//				backup_category2();
//			}
//
//			if (unfetched_skus_local_cache.size()>0){
//				backup_category1();
//			}

			//saving_similar_step_by_step();
			saving_similar();

			close_connection();

		} catch (Exception ex) {
			System.out.println("Trouble with category : "+category_to_debug);
			ex.printStackTrace();
		} finally {
			try {
				if (con != null) {
					con.close();
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void updateCategory(String category){
		PreparedStatement st;
		try {
			con.setAutoCommit(true);
			st = con.prepareStatement(update_category);
			st.setString(1, category);
			st.executeUpdate();
			st.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Trouble updating category");
			e.printStackTrace();
		} 

		System.out.println("Category : "+ category+ " fetched !");
	}

	public boolean updateDataList(CatalogEntry current_entry, List<CatalogEntry> my_data){
		boolean done = false;
		if (my_data.size() >= kriter_threshold){
			// we do it the standard way
			done = find_similar_backup(current_entry,my_data);
		} else if (my_data.size() < kriter_threshold) {
			Set<String> current_similars = unfetched_skus_local_cache.get(current_entry);
			for (CatalogEntry to_add : my_data){
				current_similars.add(to_add.getSKU());
			}
			if (current_similars.size()>= kriter_threshold){
				matching_skus.put(current_entry.getSKU(),new ArrayList<String>(current_similars));
				done = true;
			} else {
				unfetched_skus_local_cache.put(current_entry,current_similars);
			}
		}
		return done;
	}

	public void randomComputeDataList(List<CatalogEntry> my_data,String category){
		int total_size=my_data.size();
		if (my_data.size() >= kriter_threshold){
			int nb_computation = 0;
			for (CatalogEntry currentEntry : my_data){
				nb_computation++;
				if (nb_computation%200 == 0){
					System.out.println(Thread.currentThread() +" Having computed distance matrix "+nb_computation+" from "+total_size);
				}
				List<CatalogEntry> filtered_entries = shrink_to_kriter_size(my_data);
				List<String> similars = new ArrayList<String>();
				for (CatalogEntry toAdd : filtered_entries){
					similars.add(toAdd.getSKU());
				}
				// adding the 6 first closest skus
				//System.out.println("Adding 6 similar SKUs for : "+currentEntry.getSKU());
				matching_skus.put(currentEntry.getSKU(),similars);
				if ((matching_skus.size() != 0) && matching_skus.size() % batch_size == 0 ){
					saving_similar();
					matching_skus.clear();
				}
			}
		} else if (my_data.size() < kriter_threshold) {
			Set<String> similars = new HashSet<String>();
			for (CatalogEntry to_add : my_data){
				similars.add(to_add.getSKU());
			}
			for (CatalogEntry to_process : my_data){
				unfetched_skus_local_cache.put(to_process,similars);
			}
			// we here have to fetch lower category
		}
	}

	public void backup_category3() throws SQLException{
		Iterator<Entry<CatalogEntry, Set<String>>> it = unfetched_skus_local_cache.entrySet().iterator();	
		List<CatalogEntry> to_remove = new ArrayList<CatalogEntry>();
		while (it.hasNext()){
			Map.Entry<CatalogEntry, Set<String>> pairs = (Map.Entry<CatalogEntry, Set<String>>)it.next();
			CatalogEntry current_entry=pairs.getKey();			
			List<CatalogEntry> newSet = fetch_category_data3(current_entry.getCATEGORIE_NIVEAU_3());
			if (updateDataList(current_entry,newSet)){
				to_remove.add(current_entry);	
			}
		}

		for (CatalogEntry torem : to_remove){
			unfetched_skus_local_cache.remove(torem);
		}
	}

	public void backup_category2() throws SQLException{
		Iterator<Entry<CatalogEntry, Set<String>>> it = unfetched_skus_local_cache.entrySet().iterator();	
		List<CatalogEntry> to_remove = new ArrayList<CatalogEntry>();
		while (it.hasNext()){
			Map.Entry<CatalogEntry, Set<String>> pairs = (Map.Entry<CatalogEntry, Set<String>>)it.next();
			CatalogEntry current_entry=pairs.getKey();			
			List<CatalogEntry> newSet = fetch_category_data2(current_entry.getCATEGORIE_NIVEAU_2());
			if (updateDataList(current_entry,newSet)){
				to_remove.add(current_entry);	
			}
		}

		for (CatalogEntry torem : to_remove){
			unfetched_skus_local_cache.remove(torem);
		}
	}

	public void backup_category1() throws SQLException{
		Iterator<Entry<CatalogEntry, Set<String>>> it = unfetched_skus_local_cache.entrySet().iterator();	
		List<CatalogEntry> to_remove = new ArrayList<CatalogEntry>();
		while (it.hasNext()){
			Map.Entry<CatalogEntry, Set<String>> pairs = (Map.Entry<CatalogEntry, Set<String>>)it.next();
			CatalogEntry current_entry=pairs.getKey();			
			List<CatalogEntry> newSet = fetch_category_data1(current_entry.getCATEGORIE_NIVEAU_1());
			if (updateDataList(current_entry,newSet)){
				to_remove.add(current_entry);	
			}
		}

		for (CatalogEntry torem : to_remove){
			unfetched_skus_local_cache.remove(torem);
		}
	}

	public List<CatalogEntry> shrink_to_kriter_size(List<CatalogEntry> my_list){
		Set<CatalogEntry> to_return = new HashSet<CatalogEntry>();

		// to_return is a set forbidding duplicated entries
		while (to_return.size() < kriter_threshold) {
			CatalogEntry candidate = my_list.get(my_rand.nextInt(my_list.size()));
			to_return.add(candidate);
		}
		return new ArrayList<CatalogEntry>(to_return);
		//		if (to_return.size() == kriter_threshold){
		//			return new ArrayList<CatalogEntry>(to_return);
		//		} else {
		//			while (to_return.size() < kriter_threshold) {
		//				CatalogEntry candidate = my_list.get(my_rand.nextInt(my_list.size()));
		//				to_return.add(candidate);
		//			}	
		//			return new ArrayList<CatalogEntry>(to_return);
		//		}
	}

	public void saving_similar_step_by_step(){
		System.out.println("Inserting the batch "+matching_skus.size());
		Iterator<Entry<String, List<String>>> it = matching_skus.entrySet().iterator();
		int local_counter = 0;
		PreparedStatement st = null;
		String current_sku = "";
		while (it.hasNext()){
			try{
				st = con.prepareStatement(update_catalog_statement);
				local_counter++;
				Map.Entry<String, List<String>> pairs = (Map.Entry<String, List<String>>)it.next();
				current_sku=pairs.getKey();
				List<String> similars =pairs.getValue();
				//System.out.println("Current Sku :" + current_sku + similars);
				// preparing the statement
				st.setString(1,similars.get(0));
				st.setString(2,similars.get(1));
				st.setString(3,similars.get(2));
				st.setString(4,similars.get(3));
				st.setString(5,similars.get(4));				
				st.setString(6,similars.get(5));
				st.setString(7,current_sku);
				st.executeUpdate();
				st.close();
			} catch (SQLException e){
				if(e.getMessage().contains("cds_similar_products_sku_key")){
					System.out.println("Already inserted : "+current_sku);
				} else {
					e.printStackTrace();  
				}

				if (st != null){
					try {
						st.close();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}	
		}
		System.out.println(Thread.currentThread()+"Committed " + local_counter + " updates");
	}

	public void saving_similar(){
		System.out.println("Inserting the batch "+matching_skus.size());
		try{
			Iterator<Entry<String, List<String>>> it = matching_skus.entrySet().iterator();
			int local_counter = 0;
			con.setAutoCommit(false);
			PreparedStatement st = con.prepareStatement(update_catalog_statement);
			while (it.hasNext()){
				local_counter++;
				Map.Entry<String, List<String>> pairs = (Map.Entry<String, List<String>>)it.next();
				String current_sku=pairs.getKey();
				List<String> similars =pairs.getValue();
				//System.out.println("Current Sku :" + current_sku + similars);
				// preparing the statement
				st.setString(1,similars.get(0));
				st.setString(2,similars.get(1));
				st.setString(3,similars.get(2));
				st.setString(4,similars.get(3));
				st.setString(5,similars.get(4));				
				st.setString(6,similars.get(5));
				st.setString(7,current_sku);
				st.addBatch();
			}
			st.executeBatch();
			con.commit();
			st.close();
			System.out.println(Thread.currentThread()+"Committed " + local_counter + " updates");
		} catch (SQLException e){
			//System.out.println("Line already inserted : "+nb_lines);
			e.printStackTrace();  
			if (con != null) {
				try {
					con.rollback();
				} catch (SQLException ex1) {
					ex1.printStackTrace();
				}
			}
			e.printStackTrace();
		}	
	}


	public boolean find_similar_backup(CatalogEntry current_entry, List<CatalogEntry> entries){

		boolean done = false;
		Set<String> current_similars = unfetched_skus_local_cache.get(current_entry);
		// sorting the array and keeping the indexes
		Double[] vector_list = computeDistanceVector(current_entry, entries);
		DescendingArrayIndexComparator comparator = new DescendingArrayIndexComparator(vector_list);
		Integer[] indexes = comparator.createIndexArray();
		Arrays.sort(indexes, comparator);
		int loc = 0;
		while (current_similars.size()<kriter_threshold){
			current_similars.add(entries.get(indexes[loc]).getSKU());
			loc++;
		}
		if (current_similars.size()>= kriter_threshold){
			matching_skus.put(current_entry.getSKU(),new ArrayList<String>(current_similars));
			done = true;
		} else {
			unfetched_skus_local_cache.put(current_entry,current_similars);
		}
		return done;
	}



	public List<CatalogEntry> fetch_category_data3(String category) throws SQLException{
		List<CatalogEntry> my_entries = new ArrayList<CatalogEntry>();
		PreparedStatement select_statement = con.prepareStatement(select_entry_from_category3);
		select_statement.setString(1, category);
		ResultSet rs = select_statement.executeQuery();
		while (rs.next()) {
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
			String ETAT = rs.getString(9);
			entry.setETAT(ETAT);
			my_entries.add(entry);
		}
		select_statement.close();
		return my_entries;
	}

	public List<CatalogEntry> fetch_category_data2(String category) throws SQLException{
		List<CatalogEntry> my_entries = new ArrayList<CatalogEntry>();
		PreparedStatement select_statement = con.prepareStatement(select_entry_from_category2);
		select_statement.setString(1, category);
		ResultSet rs = select_statement.executeQuery();
		while (rs.next()) {
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
			String ETAT = rs.getString(9);
			entry.setETAT(ETAT);
			my_entries.add(entry);
		}
		select_statement.close();
		return my_entries;
	}

	public List<CatalogEntry> fetch_category_data1(String category) throws SQLException{
		List<CatalogEntry> my_entries = new ArrayList<CatalogEntry>();
		PreparedStatement select_statement = con.prepareStatement(select_entry_from_category1);
		select_statement.setString(1, category);
		ResultSet rs = select_statement.executeQuery();
		while (rs.next()) {
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
			String ETAT = rs.getString(9);
			entry.setETAT(ETAT);
			my_entries.add(entry);
		}
		select_statement.close();
		return my_entries;
	}

	public Double[] computeDistanceVector(CatalogEntry currentEntry, List<CatalogEntry> entries){
		int size_list = entries.size();
		Double[] to_return = new Double[size_list];
		for (int j=0;j<size_list;j++){
			CatalogEntry entryj = entries.get(j);
			Double distone = StatisticsUtility.computeTFdistance(currentEntry.getLIBELLE_PRODUIT(), entryj.getLIBELLE_PRODUIT());
			Double disttwo = StatisticsUtility.computeTFdistance(currentEntry.getDESCRIPTION_LONGUEUR80(), entryj.getDESCRIPTION_LONGUEUR80());
			to_return[j] = distone + disttwo;
		}
		return to_return;
	}

	public Double[] computeVectorizedDistanceMatrix(List<CatalogEntry> entries){
		int size_list = entries.size();
		int vector_size_list = size_list*(size_list+1)/2;
		Double[] to_return = new Double[vector_size_list];
		for (int i=0;i<size_list;i++){
			if (i%1000 == 0){
				System.out.println(Thread.currentThread() +" Having computed distance matrix"+i+" from "+size_list);
			}
			CatalogEntry entryi = entries.get(i);
			for (int j=i;j<size_list;j++){
				CatalogEntry entryj = entries.get(j);
				Double distone = StatisticsUtility.computeTFdistance(entryi.getLIBELLE_PRODUIT(), entryj.getLIBELLE_PRODUIT());
				Double disttwo = StatisticsUtility.computeTFdistance(entryi.getDESCRIPTION_LONGUEUR80(), entryj.getDESCRIPTION_LONGUEUR80());
				to_return[fromMatrixToVector(i,j,size_list)] = distone + disttwo;
			}
		}
		return to_return;
	}

	public int fromMatrixToVector(int i, int j, int N)
	{
		int my_index;
		if (i <= j)
			my_index = i * N - (i - 1) * i / 2 + j - i;
		else
			my_index = j * N - (j - 1) * j / 2 + i - j;

		return my_index;
	}
	private void close_connection(){
		try {
			if (con != null) {
				con.close();
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
		}
	}
}
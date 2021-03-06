package com.crawlerrors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.webmasters.Webmasters;
import com.google.api.services.webmasters.model.UrlCrawlErrorCount;
import com.google.api.services.webmasters.model.UrlCrawlErrorCountsPerType;
import com.google.api.services.webmasters.model.UrlCrawlErrorsCountsQueryResponse;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSample;
import com.google.api.services.webmasters.model.UrlCrawlErrorsSamplesListResponse;

public class MagasinsCrawlErrorFetchingData {

	private static String[] magasin_urls = {
		"http://www.cdiscount.com/le-sport/",
		"http://www.cdiscount.com/electromenager/",
		"http://www.cdiscount.com/maison/bricolage-outillage/",
		"http://www.cdiscount.com/auto/",
		"http://www.cdiscount.com/maison/r-/",
		"http://www.cdiscount.com/juniors/jeux-et-jouets-par-type/",
		"http://www.cdiscount.com/high-tech/",
		"http://www.cdiscount.com/jardin-animalerie/",
		"http://www.cdiscount.com/telephonie/",
		"http://www.cdiscount.com/maison/",
		"http://www.cdiscount.com/bricolage-chauffage/",
		"http://www.cdiscount.com/vin-alimentaire/",
		"http://www.cdiscount.com/photo-numerique/",
		"http://www.cdiscount.com/pret-a-porter/",
		"http://www.cdiscount.com/informatique/",
		"http://www.cdiscount.com/bagages/",
		"http://www.cdiscount.com/culture-multimedia/",
	"http://www.lecomptoirsante.com/"};
	
	private static String[] magasin_column_names = {
		"le-sport",
		"electromenager",
		"bricolage_outillage",
		"auto",
		"maison_r_tiret",
		"jeux_et_jouets_par_type",
		"high_tech",
		"jardin_animalerie",
		"telephonie",
		"maison",
		"bricolage_chauffage",
		"vin_alimentaire",
		"photo_numerique",
		"pret_a_porter",
		"informatique",
		"bagages",
		"culture_multimedia",
	    "lecomptoirsante"
	};
	
	private static String CLIENT_ID = "******************************";
	private static String CLIENT_SECRET = "**************************";
	private static String REDIRECT_URI = "***************************";
	private static String OAUTH_SCOPE = "https://www.googleapis.com/auth/webmasters.readonly";

	public static void main(String[] args) throws IOException {
		HttpTransport httpTransport = new NetHttpTransport();
		JsonFactory jsonFactory = new JacksonFactory();

		GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
				httpTransport, jsonFactory, CLIENT_ID, CLIENT_SECRET, Arrays.asList(OAUTH_SCOPE))
		.setAccessType("online")
		.setApprovalPrompt("auto").build();

		String url = flow.newAuthorizationUrl().setRedirectUri(REDIRECT_URI).build();
		System.out.println("Please open the following URL in your browser then type the authorization code:");
		System.out.println("  " + url);

		System.out.println("Enter authorization code:");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String code = br.readLine();

		// we got the code from the specified URL !
		// String code = "4/LMxOcYk7kOc7Ry-giUimjbpONrWChXOSVrVXeAu7u0I.Mimqw4ebi3QWoiIBeO6P2m_uuubMkwI";

		GoogleTokenResponse response = flow.newTokenRequest(code).setRedirectUri(REDIRECT_URI).execute();
		GoogleCredential credential = new GoogleCredential().setFromTokenResponse(response);

		// Create a new authorized API client
		Webmasters service = new Webmasters.Builder(httpTransport, jsonFactory, credential)
		.setApplicationName("WebmastersCommandLine")
		.build();


		// counting error having happened that current day
		for (String current_magasin : magasin_urls){
			Webmasters.Urlcrawlerrorscounts.Query query =service.urlcrawlerrorscounts().query(current_magasin);
			Webmasters.Sites.Get get_request = service.sites().get(current_magasin);
			try {
				UrlCrawlErrorsCountsQueryResponse responseurl = query.execute();
				System.out.println(responseurl.getCountPerTypes());
				if (responseurl.getCountPerTypes() != null){
					System.out.println("Result for magasin : "+current_magasin);
					for (UrlCrawlErrorCountsPerType catitem : responseurl.getCountPerTypes()){
						System.out.println("Category : "+catitem.getCategory());			
						if (catitem.getEntries() != null) {
							for (UrlCrawlErrorCount item : catitem.getEntries()){
								System.out.println("Count : "+item.getCount()+" timestamp : "+item.getTimestamp());

							}
						}
					}
				}
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
			}
		}
		// counting error having happened that current day
		for (String current_magasin : magasin_urls){
			Webmasters.Urlcrawlerrorssamples.List sampleurlquery =service.urlcrawlerrorssamples().list(current_magasin,"notFound","web");
			try {
				UrlCrawlErrorsSamplesListResponse responsesampleurl = sampleurlquery.execute();
				if (responsesampleurl.getUrlCrawlErrorSample() != null){
					System.out.println("Result for magasin : "+current_magasin);
					for (UrlCrawlErrorsSample sampleitem : responsesampleurl.getUrlCrawlErrorSample()){
						System.out.println("First detected : "+sampleitem.getFirstDetected());
						System.out.println("Last crawled : "+sampleitem.getLastCrawled());	
						System.out.println("Page URL : "+sampleitem.getPageUrl());
						if (sampleitem.getUrlDetails() !=null){
							if (sampleitem.getUrlDetails().getContainingSitemaps() !=null){
								System.out.println("URL containing site maps : "+sampleitem.getUrlDetails().getContainingSitemaps());
							}
							if (sampleitem.getUrlDetails().getLinkedFromUrls() !=null){
								System.out.println("URL links : "+sampleitem.getUrlDetails().getLinkedFromUrls());	
							}
						}
					}

				}
			} catch (IOException e) {
				System.out.println("An error occurred: " + e);
			}
		}


	}

}

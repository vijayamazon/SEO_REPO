package com.urlutilities;

public class URLUtilitiesTest {
	
	// we here follow Julien's rule
//	Voici le principe de hiérarchie pour les navid (première séquence de chiffres indiquée après identifiant type de page dansl’url ( ex : /v- , /l-, /f-)…
//			+ 2 chiffres pour chaque sous-strate ( car 99 nœuds max pourune même strate)
//			 
//			Root/Home : 1
//			Root-1/Home magasin = 1XX
//			Root-2/Rayon : 1XXXX
//			Root-3/Sous-rayon : 1XXXXXX
//			Root-4 : Sous-sous rayon : 1XXXXXXXX
//			Root-5 : 1XXXXXXXXXX


	private static String[] totest = 
		{
		"http://www.cdiscount.com/bricolage-chauffage/v-117-1.html",
		"http://www.cdiscount.com/jardin-animalerie/v-163-1.html"};
//		" http://www.cdiscount.com/informatique/v-107-0.html",
//		" http://www.cdiscount.com/high-tech/v-106-0.html",
//		" http://www.cdiscount.com/telephonie/v-144-0.html",
//		" http://www.cdiscount.com/photo-numerique/v-112-0.html",
//		" http://www.cdiscount.com/auto/v-133-0.html",
//		" http://www.cdiscount.com/pret-a-porter/v-113-3.html",
//		"http://www.cdiscount.com/juniors/peluches/plush-company-15726-peluche-karola-vache/f-1206506-plu8029956157264.html",
//		"http://www.cdiscount.com/au-quotidien/alimentaire/whirlpool-eco306/f-12701-whirleco306.html",
//		"http://www.cdiscount.com/pret-a-porter/derniers-arrivages/waooh-tee-shirt-col-v-et-ecrit-bleu/f-11331-mp00857468.html?mpos=15%7Cmp",
//		"http://www.cdiscount.com/jardin/plantes/seaweed-xtract-alguamix-100-ml/f-16301-cul3700688517537.html?mpos=774%7Cmp",
//		"http://www.cdiscount.com/jeux-pc-video-console/ps4/nba-2k15-ps4/f-1030401-5026555417488.html?mpos=22%7Ccd",
//		"http://www.cdiscount.com/au-quotidien/droguerie/kit-de-nettoyage-pour-acier-inoxydable/f-127060302-wpr8015250283600.html",
//		"http://www.cdiscount.com/au-quotidien/alimentaire/tisane-bio-ange-gardien-en-sachet-anti-refroi/f-1270105-flo3560467790557.html",
//		"http://www.cdiscount.com/le-sport/soins-du-sportif/ceinture-lombaire-reglable-support-protection-9/f-1211401-auc6913280719026.html?mpos=11%7Cmp",
//		"http://www.cdiscount.com/electromenager/lavage-sechage/whirlpool-aws6213-lave-linge/f-11001040401-whiaws6213.html?mpos=21%7Ccd",
//		"http://www.cdiscount.com/juniors/r-cuisine+maya.html",
//		"http://www.cdiscount.com/pret-a-porter/vetements-femme/vetements-de-marque/little-marcel/l-113029560-4.html",
//		"http://www.cdiscount.com/au-quotidien/droguerie/balai/f-127060401-dom3466000044203.html",
//		"http://www.cdiscount.com/bijouterie/parure-en-calebasse-rhea/f-126-auc3663089010581.html?mpos=19%7Cmp",
//		"http://www.cdiscount.com/m-8089-vantage.html",
//		"http://www.cdiscount.com/lf-76901_6/produits-minceur_oenobiol.html"};


	public static void main(String[] args){	
		for (int i =0; i<totest.length;i++){
			String current = totest[i];
			System.out.println(current);
			String magasin = URL_Utilities.checkMagasin(current);
			System.out.println("Magasin " + magasin);
			String rayon = URL_Utilities.checkRayon(current);
			System.out.println("Rayon : "+rayon);
			String type = URL_Utilities.checkType(current);
			System.out.println("Type : "+type);			
			System.out.println("\n\n");	
		}
	}
}
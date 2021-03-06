CREATE TABLE IF NOT EXISTS ARBOCRAWL_RESULTS (
    # URL WITHOUT ? PARAMETERS
    URL TEXT,
    # PAGE ALL TEXT
    WHOLE_TEXT TEXT,
    # PAGE TITLE
    TITLE TEXT,
    # PAGE H1 
    H1 TEXT,
    # PAGE SHORT DESCRIPTION
    SHORT_DESCRIPTION TEXT,
    # PAGE STATUS CODE
    STATUS_CODE INT,
    # DEPTH OF THE PAGE AT THE SITE LEVEL
    DEPTH INT,
    # NUMBER OF OUTGOING LINKS
    OUTLINKS_SIZE INT,
    # NUMBER OF INCOMONG LINKS
    INLINKS_SIZE INT,
    # PAGE INCOMING LINKS ANCHOR SEMANTIC
    INLINKS_SEMANTIC TEXT,
    # PAGE INCOMING LINKS ANCHOR TERM FREQUENCY
    INLINKS_SEMANTIC_COUNT TEXT,
    # NUMBER OF ITEMTYPE http://data-vocabulary.org/Breadcrumb
    NB_BREADCRUMBS INT,
    # NUMBER OF ITEMPROP aggregateRating
    NB_AGGREGATED_RATINGS INT,
    # NUMBER OF ITEMPROP ratingValue
    NB_RATINGS_VALUES INT,
    # NUMBER OF ITEMPROP price
    NB_PRICES INT,
    # NUMBER OF ITEMPROP availability
    NB_AVAILABILITIES INT,
    # NUMBER OF ITEMPROP review
    NB_REVIEWS INT,
    # NUMBER OF ITEMPROP reviewCount
    NB_REVIEWS_COUNT INT,
    # NUMBER OF ITEMPROP image
    NB_IMAGES INT,
    # NUMBER OF OCCURENCES FOUND IN URL of search + recherche + Recherche + Search
    NB_SEARCH_IN_URL INT,
    # NUMBER OF OCCURENCES FOUND IN PAGE TEXT ajout + ajouter + Ajout + Ajouter
    NB_ADD_IN_TEXT INT,
    # NUMBER OF OCCURENCES FOUND IN PAGE TEXT filtre + facette + Filtre + Facette + filtré + filtrés
    NB_FILTER_IN_TEXT INT,
    # NUMBER OF OCCURENCES FOUND IN PAGE TEXT Ma recherche + Votre recherche + résultats pour + résultats associés   
    NB_SEARCH_IN_TEXT INT,
    # NUMBER OF OCCURENCES FOUND IN PAGE TEXT guide d'achat + Guide d'achat
    NB_GUIDE_ACHAT_IN_TEXT INT,
    # NUMBER OF OCCURENCES FOUND IN PAGE TEXT caractéristique + Caractéristique + descriptif + Descriptif +information + Information    
    NB_PRODUCT_INFO_IN_TEXT INT,
    # NUMBER OF OCCURENCES FOUND IN PAGE TEXT livraison + Livraison + frais de port + Frais de port
    NB_LIVRAISON_IN_TEXT INT,
    # NUMBER OF OCCURENCES FOUND IN PAGE TEXT garantie + Garantie +assurance + Assurance
    NB_GARANTIES_IN_TEXT INT,
    # NUMBER OF OCCURENCES FOUND IN PAGE TEXT Produits Similaires + produits similaires + Meilleures Ventes + meilleures ventes +Meilleures ventes + Nouveautés + nouveautés + Nouveauté + nouveauté   
    NB_PRODUITS_SIMILAIRES_IN_TEXT INT,
    # NUMBER OF HTML TAG img IN THE PAGE
    NB_IMAGES_TEXT INT,
    # AVERAGE WIDTH OF HTML TAG img IN THE PAGE
    WIDTH_AVERAGE REAL,
    # AVERAGE HEIGHT OF HTML TAG img IN THE PAGE
    HEIGHT_AVERAGE REAL,
    # PAGE INRANK TO BE COMPUTED BY AN EXTERNAL PROCESS
    PAGE_RANK REAL,
    # PAGE TYPE TO BE DETERMINED BY OUR CLASSIFYING ALGORITHM
    PAGE_TYPE TEXT,
    # TEN BEST TF/IDF HITS FOR THE PAGE
    SEMANTIC_HITS TEXT,
    # TITLE TF/IDF
    SEMANTIC_TITLE TEXT,
    # NAME OF THE CONCURRENT
    CONCURRENT_NAME VARCHAR(50),
    LAST_UPDATE DATE
) TABLESPACE mydbspace;

CREATE INDEX ON ARBOCRAWL_RESULTS (url);

INSERT INTO ARBOCRAWL_RESULTS
(URL, WHOLE_TEXT, TITLE, H1, SHORT_DESCRIPTION, STATUS_CODE, DEPTH, OUTLINKS_SIZE, INLINKS_SIZE, NB_BREADCRUMBS, NB_AGGREGATED_RATINGS, NB_RATINGS_VALUES, NB_PRICES, NB_AVAILABILITIES, NB_REVIEWS, NB_REVIEWS_COUNT, NB_IMAGES INT,SEMANTIC_HITS,CONCURRENT_NAME,LAST_UPDATE)"
VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)


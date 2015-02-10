CREATE TABLE IF NOT EXISTS CRAWL_RESULTS (
    URL TEXT,
    WHOLE_TEXT TEXT,
    TITLE TEXT,
    LINKS_SIZE INT,
    LINKS TEXT,
    H1 TEXT,
    FOOTER_EXTRACT TEXT,
    ZTD_EXTRACT TEXT,
    SHORT_DESCRIPTION TEXT,
    XPATH1 TEXT,
    XPATH2 TEXT,
    XPATH3 TEXT,
    XPATH4 TEXT,
    XPATH5 TEXT, 
    XPATH6 TEXT,
    XPATH7 TEXT,
    XPATH8 TEXT,
    XPATH9 TEXT,
    XPATH10 TEXT,
    CDISCOUNT_VENDOR BOOLEAN,
    YOUTUBE_REFERENCED BOOLEAN,
    ATTRIBUTES TEXT,
    NB_ATTRIBUTES INT, 
    STATUS_CODE INT,
    HEADERS TEXT,
    DEPTH INT,
    PAGE_TYPE VARCHAR(50),
    MAGASIN VARCHAR(100),
    RAYON VARCHAR(100),
    PRODUIT  VARCHAR(100),
    BRAND    VARCHAR(100),
    CATEGORY VARCHAR(100),
    PAGE_RANK REAL,
    BLOBOID OID,
    LAST_UPDATE DATE
) TABLESPACE mydbspace;
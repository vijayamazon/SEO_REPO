CREATE TABLE IF NOT EXISTS MAJESTIC_CISCOUNT_CSV_EXPORT (
    TARGET_URL TEXT, 
    SOURCE_URL TEXT,
    ANCHOR_TEXT TEXT,
    CRAWL_DATE DATE,
    FIRST_FOUND_DATE DATE,
    FLAG_NO_FOLLOW BOOLEAN,
    FLAG_IMAGE_LINK BOOLEAN,
    FLAG_REDIRECT BOOLEAN,
    FLAG_FRAME BOOLEAN,
    FLAG_OLD_CRAWL BOOLEAN,
    FLAG_ALT_TEXT BOOLEAN,
    FALG_MENTION BOOLEAN,
    SOURCE_CITATIONFLOW INT,
    SOURCE_TRUSTFLOW INT,
    TARGET_CITATIONFLOW INT,
    TARGET_TRUSTFLOW INT)

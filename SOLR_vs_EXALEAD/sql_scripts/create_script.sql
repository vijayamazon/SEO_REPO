CREATE TABLE IF NOT EXISTS SOLR_VS_EXALEAD (
    ID SERIAL PRIMARY KEY  NOT NULL,
    URL TEXT,
    STATUS INT,
    TO_FETCH BOOLEAN,
    H1_SOLR TEXT,
    TITLE_SOLR TEXT,
    XPATH1_SOLR TEXT,
    XPATH2_SOLR TEXT,
    XPATH3_SOLR TEXT,
    XPATH4_SOLR TEXT,
    XPATH5_SOLR TEXT,
    XPATH6_SOLR TEXT,
    XPATH7_SOLR TEXT,
    XPATH8_SOLR TEXT,
    XPATH9_SOLR TEXT,
    XPATH10_SOLR TEXT,
    H1_EXALEAD TEXT,
    TITLE_EXALEAD TEXT,
    XPATH1_EXALEAD TEXT,
    XPATH2_EXALEAD TEXT,
    XPATH3_EXALEAD TEXT,
    XPATH4_EXALEAD TEXT,
    XPATH5_EXALEAD TEXT, 
    XPATH6_EXALEAD TEXT,
    XPATH7_EXALEAD TEXT,
    XPATH8_EXALEAD TEXT,
    XPATH9_EXALEAD TEXT,
    XPATH10_EXALEAD TEXT,
    H1_COMPARISON INT,
    TITLE_COMPARISON INT,
    XPATH1_COMPARISON INT,
    XPATH2_COMPARISON INT,
    XPATH3_COMPARISON INT,
    XPATH4_COMPARISON INT,
    XPATH5_COMPARISON INT,
    XPATH6_COMPARISON INT,
    XPATH7_COMPARISON INT,
    XPATH8_COMPARISON INT,
    XPATH9_COMPARISON INT,
    XPATH10_COMPARISON INT
) TABLESPACE mydbspace;


#Depth by depth reading of URLs and creation of nodes and relationships each time
#Filling up NODES & EDGES depth by depth (by creating all nodes per depth and their links)
# does work but very slow
PostGresLinksByLevelDaemon

#Reading everything and updating by batch
#Filling up NODES & EDGES all at once (fast but too heayv on memory redhibitoire)
FastBatchPostGresLinksDaemon

#Filling up all nodes NODES  depth by depth and then all EDGES depth by depth 
# all nodes are created, then all relations are created
# all insertions are batched
# fast and not too heavy on memory
FastBatchByLevelPostGresLinksDaemon

## Tackle up the problem of memory going depth by depth and creating all required nodes
FastBatchPostGresLinksByLevelLowMemoryDaemon

#Computing the page rank on the whole NODES, EDGES database and updating CRAWL_RESULTS database
ComputePageRank

# compute the page rank inside each magasin (recreate edges and nodes table per magasin and update crawl_results)
BatchComputePageRankPerMagasin



#Attributes completion metrics for one magasin
ProcessOneMagasinQualityScore

#Attributes completion metrics for one magasin aggregated by category
ProcessMagasinAttributesCompletionPerCategoryMetrics

#Attributes completion metrics for one magasin aggregated by category and vendor
ProcessMagasinAttributesCompletionPerCategoryPerVendorMetrics

#Attributes completion metrics for one magasin aggregated by rayon and by category and by vendor
ProcessMagasinAttributesCompletionPerRayonPerCategoryPerVendorMetrics

# compute the duplicated titles from the continuous crawl
ProcessContinuousCrawl
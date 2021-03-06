
# methodology for an entity
select keyword, url, search_position, search_volume, magasin, rayon, produit into 'radical_for_base'_pricing from pricing_keywords where domain=''current_ent'';
CREATE INDEX ON 'radical_for_base'_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,' radical_for_base '_PRICING.magasin as magasin, ' radical_for_base '_PRICING.rayon as rayon, ' radical_for_base '_PRICING.produit as produit, ' radical_for_base '_PRICING.keyword as am_keyword, ' radical_for_base '_PRICING.url as am_url, ' radical_for_base '_PRICING.search_volume as am_search_volume, ' radical_for_base '_PRICING.search_position as am_search_position INTO ' radical_for_base '_VS_CDISCOUNT_PRICING from ' radical_for_base '_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( 'radical_for_base'_PRICING.keyword=CDISCOUNT_PRICING.keyword);

# we must first do Cdiscount 
# entity cdiscount.com
radical_for_base=cdiscount
current_ent=cdiscount.com
select keyword, url, search_position, search_volume, magasin, rayon, produit into cdiscount_pricing from pricing_keywords where domain='cdiscount.com';
CREATE INDEX ON cdiscount_pricing (keyword);
# we don't do cdiscount vs cdiscount 
# select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,amazon_PRICING.magasin as magasin, amazon_PRICING.rayon as rayon, amazon_PRICING.produit as produit, amazon_PRICING.keyword as am_keyword, amazon_PRICING.url as am_url, amazon_PRICING.search_volume as am_search_volume, amazon_PRICING.search_position as am_search_position INTO amazon_VS_CDISCOUNT_PRICING from amazon_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( amazon_PRICING.keyword=CDISCOUNT_PRICING.keyword);

# entity amazon.fr
radical_for_base=amazon
current_ent=amazon.fr
select keyword, url, search_position, search_volume, magasin, rayon, produit into amazon_pricing from pricing_keywords where domain='amazon.fr';
CREATE INDEX ON amazon_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,amazon_PRICING.magasin as magasin, amazon_PRICING.rayon as rayon, amazon_PRICING.produit as produit, amazon_PRICING.keyword as am_keyword, amazon_PRICING.url as am_url, amazon_PRICING.search_volume as am_search_volume, amazon_PRICING.search_position as am_search_position INTO amazon_VS_CDISCOUNT_PRICING from amazon_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( amazon_PRICING.keyword=CDISCOUNT_PRICING.keyword);


current_ent=aubert.com
radical_for_base=aubert
select keyword, url, search_position, search_volume, magasin, rayon, produit into aubert_pricing from pricing_keywords where domain='aubert.com';
CREATE INDEX ON aubert_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position, aubert_PRICING.magasin as magasin, aubert_PRICING.rayon as rayon, aubert_PRICING.produit as produit, aubert_PRICING.keyword as am_keyword, aubert_PRICING.url as am_url, aubert_PRICING.search_volume as am_search_volume, aubert_PRICING.search_position as am_search_position INTO aubert_VS_CDISCOUNT_PRICING from aubert_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( aubert_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=autourdebebe.com
radical_for_base=autourdebebe
select keyword, url, search_position, search_volume, magasin, rayon, produit into autourdebebe_pricing from pricing_keywords where domain='autourdebebe.com';
CREATE INDEX ON autourdebebe.com_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,autourdebebe_PRICING.magasin as magasin, autourdebebe_PRICING.rayon as rayon, autourdebebe_PRICING.produit as produit, autourdebebe_PRICING.keyword as am_keyword, autourdebebe_PRICING.url as am_url, autourdebebe_PRICING.search_volume as am_search_volume, autourdebebe_PRICING.search_position as am_search_position INTO autourdebebe_VS_CDISCOUNT_PRICING from autourdebebe_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( autourdebebe_PRICING.keyword=CDISCOUNT_PRICING.keyword);

#current_ent=king-jouet.com
#radical_for_base=king-jouet
#select keyword, url, search_position, search_volume, magasin, rayon, produit into 'radical_for_base'_pricing from pricing_keywords where domain=''current_ent'';
#CREATE INDEX ON 'radical_for_base'_pricing (keyword);
#select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,' radical_for_base '_PRICING.magasin as magasin, ' radical_for_base '_PRICING.rayon as rayon, ' radical_for_base '_PRICING.produit as produit, ' radical_for_base '_PRICING.keyword as am_keyword, ' radical_for_base '_PRICING.url as am_url, ' radical_for_base '_PRICING.search_volume as am_search_volume, ' radical_for_base '_PRICING.search_position as am_search_position INTO ' radical_for_base '_VS_CDISCOUNT_PRICING from ' radical_for_base '_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( 'radical_for_base'_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=toysrus.fr
radical_for_base=toysrus
select keyword, url, search_position, search_volume, magasin, rayon, produit into toysrus_pricing from pricing_keywords where domain='toysrus.fr';
CREATE INDEX ON toysrus_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,toysrus_PRICING.magasin as magasin, toysrus_PRICING.rayon as rayon, toysrus_PRICING.produit as produit, toysrus_PRICING.keyword as am_keyword, toysrus_PRICING.url as am_url, toysrus_PRICING.search_volume as am_search_volume, toysrus_PRICING.search_position as am_search_position INTO toysrus_VS_CDISCOUNT_PRICING from toysrus_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( toysrus_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=materiel.net
radical_for_base=materiel
select keyword, url, search_position, search_volume, magasin, rayon, produit into materiel_pricing from pricing_keywords where domain='materiel.net';
CREATE INDEX ON materiel_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,materiel_PRICING.magasin as magasin, materiel_PRICING.rayon as rayon, materiel_PRICING.produit as produit, materiel_PRICING.keyword as am_keyword, materiel_PRICING.url as am_url, materiel_PRICING.search_volume as am_search_volume, materiel_PRICING.search_position as am_search_position INTO materiel_VS_CDISCOUNT_PRICING from materiel_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( materiel_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=pearl.fr
radical_for_base=pearl
select keyword, url, search_position, search_volume, magasin, rayon, produit into pearl_pricing from pricing_keywords where domain='pearl.fr';
CREATE INDEX ON pearl_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,pearl_PRICING.magasin as magasin,  pearl_PRICING.rayon as rayon, pearl_PRICING.produit as produit, pearl_PRICING.keyword as am_keyword, pearl_PRICING.url as am_url, pearl_PRICING.search_volume as am_search_volume, pearl_PRICING.search_position as am_search_position INTO pearl_VS_CDISCOUNT_PRICING from pearl_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( pearl_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=ebay.fr
radical_for_base=ebay
select keyword, url, search_position, search_volume, magasin, rayon, produit into ebay_pricing from pricing_keywords where domain='ebay.fr';
CREATE INDEX ON ebay_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,ebay_PRICING.magasin as magasin, ebay_PRICING.rayon as rayon, ebay_PRICING.produit as produit, ebay_PRICING.keyword as am_keyword, ebay_PRICING.url as am_url, ebay_PRICING.search_volume as am_search_volume, ebay_PRICING.search_position as am_search_position INTO ebay_VS_CDISCOUNT_PRICING from ebay_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( ebay_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=rueducommerce.fr
radical_for_base=rueducommerce
select keyword, url, search_position, search_volume, magasin, rayon, produit into rueducommerce_pricing from pricing_keywords where domain='rueducommerce.fr';
CREATE INDEX ON rueducommerce_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,rueducommerce_PRICING.magasin as magasin, rueducommerce_PRICING.rayon as rayon, rueducommerce_PRICING.produit as produit, rueducommerce_PRICING.keyword as am_keyword, rueducommerce_PRICING.url as am_url, rueducommerce_PRICING.search_volume as am_search_volume, rueducommerce_PRICING.search_position as am_search_position INTO rueducommerce_VS_CDISCOUNT_PRICING from rueducommerce_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( rueducommerce_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=conforama.fr
radical_for_base=conforama
select keyword, url, search_position, search_volume, magasin, rayon, produit into conforama_pricing from pricing_keywords where domain='conforama.fr';
CREATE INDEX ON conforama_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,conforama_PRICING.magasin as magasin, conforama_PRICING.rayon as rayon, conforama_PRICING.produit as produit, conforama_PRICING.keyword as am_keyword, conforama_PRICING.url as am_url, conforama_PRICING.search_volume as am_search_volume, conforama_PRICING.search_position as am_search_position INTO conforama_VS_CDISCOUNT_PRICING from conforama_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( conforama_PRICING.keyword=CDISCOUNT_PRICING.keyword);
@@@@@ done until there
current_ent=darty.com
radical_for_base=darty
select keyword, url, search_position, search_volume, magasin, rayon, produit into darty_pricing from pricing_keywords where domain='darty.com';
CREATE INDEX ON darty_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,darty_PRICING.magasin as magasin, darty_PRICING.rayon as rayon, darty_PRICING.produit as produit, darty_PRICING.keyword as am_keyword, darty_PRICING.url as am_url, darty_PRICING.search_volume as am_search_volume, darty_PRICING.search_position as am_search_position INTO darty_VS_CDISCOUNT_PRICING from darty_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( darty_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=fnac.com
radical_for_base=fnac
select keyword, url, search_position, search_volume, magasin, rayon, produit into fnac_pricing from pricing_keywords where domain='fnac.com';
CREATE INDEX ON fnac_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,fnac_PRICING.magasin as magasin, fnac_PRICING.rayon as rayon, fnac_PRICING.produit as produit, fnac_PRICING.keyword as am_keyword, fnac_PRICING.url as am_url, fnac_PRICING.search_volume as am_search_volume, fnac_PRICING.search_position as am_search_position INTO fnac_VS_CDISCOUNT_PRICING from fnac_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( fnac_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=priceminister.com
radical_for_base=priceminister
select keyword, url, search_position, search_volume, magasin, rayon, produit into priceminister_pricing from pricing_keywords where domain='priceminister.com';
CREATE INDEX ON priceminister_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position, priceminister_PRICING.magasin as magasin, priceminister_PRICING.rayon as rayon, priceminister_PRICING.produit as produit, priceminister_PRICING.keyword as am_keyword, priceminister_PRICING.url as am_url, priceminister_PRICING.search_volume as am_search_volume, priceminister_PRICING.search_position as am_search_position INTO priceminister_VS_CDISCOUNT_PRICING from priceminister_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on(priceminister_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=zalando.fr
radical_for_base=zalando
select keyword, url, search_position, search_volume, magasin, rayon, produit into zalando_pricing from pricing_keywords where domain='zalando.fr';
CREATE INDEX ON zalando_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position, zalando_PRICING.magasin as magasin, zalando_PRICING.rayon as rayon, zalando_PRICING.produit as produit, zalando_PRICING.keyword as am_keyword, zalando_PRICING.url as am_url, zalando_PRICING.search_volume as am_search_volume, zalando_PRICING.search_position as am_search_position INTO zalando_VS_CDISCOUNT_PRICING from zalando_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( zalando_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=ldlc.com
radical_for_base=ldlc
select keyword, url, search_position, search_volume, magasin, rayon, produit into ldlc_pricing from pricing_keywords where domain='ldlc.com';
CREATE INDEX ON ldlc_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,ldlc_PRICING.magasin as magasin, ldlc_PRICING.rayon as rayon, ldlc_PRICING.produit as produit, ldlc_PRICING.keyword as am_keyword, ldlc_PRICING.url as am_url, ldlc_PRICING.search_volume as am_search_volume, ldlc_PRICING.search_position as am_search_position INTO ldlc_VS_CDISCOUNT_PRICING from ldlc_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on(ldlc_PRICING.keyword=CDISCOUNT_PRICING.keyword);
@@@@@
current_ent=boulanger.fr
radical_for_base=boulanger
select keyword, url, search_position, search_volume, magasin, rayon, produit into boulanger_pricing from pricing_keywords where domain='boulanger.fr';
CREATE INDEX ON boulanger_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,boulanger_PRICING.magasin as magasin, boulanger_PRICING.rayon as rayon, boulanger_PRICING.produit as produit, boulanger_PRICING.keyword as am_keyword, boulanger_PRICING.url as am_url, boulanger_PRICING.search_volume as am_search_volume, boulanger_PRICING.search_position as am_search_position INTO boulanger_VS_CDISCOUNT_PRICING from boulanger_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( boulanger_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=french.alibaba.com
radical_for_base=alibaba
select keyword, url, search_position, search_volume, magasin, rayon, produit into alibaba_pricing from pricing_keywords where domain='french.alibaba.com';
CREATE INDEX ON alibaba_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,alibaba_PRICING.magasin as magasin, alibaba_PRICING.rayon as rayon, alibaba_PRICING.produit as produit, alibaba_PRICING.keyword as am_keyword, alibaba_PRICING.url as am_url, alibaba_PRICING.search_volume as am_search_volume, alibaba_PRICING.search_position as am_search_position INTO alibaba_VS_CDISCOUNT_PRICING from alibaba_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( alibaba_PRICING.keyword=CDISCOUNT_PRICING.keyword);

current_ent=pixmania.fr
radical_for_base=pixmania
select keyword, url, search_position, search_volume, magasin, rayon, produit into pixmania_pricing from pricing_keywords where domain='pixmania.fr';
CREATE INDEX ON pixmania_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position, pixmania_PRICING.magasin as magasin, pixmania_PRICING.rayon as rayon, pixmania_PRICING.produit as produit, pixmania_PRICING.keyword as am_keyword, pixmania_PRICING.url as am_url, pixmania_PRICING.search_volume as am_search_volume, pixmania_PRICING.search_position as am_search_position INTO pixmania_VS_CDISCOUNT_PRICING from pixmania_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( pixmania_PRICING.keyword=CDISCOUNT_PRICING.keyword);

@@@@@@@@@@@@@@@@ epurated script



select keyword, url, search_position, search_volume, magasin, rayon, produit into cdiscount_pricing from pricing_keywords where domain='cdiscount.com';
CREATE INDEX ON cdiscount_pricing (keyword);

select keyword, url, search_position, search_volume, magasin, rayon, produit into amazon_pricing from pricing_keywords where domain='amazon.fr';
CREATE INDEX ON amazon_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,amazon_PRICING.magasin as magasin, amazon_PRICING.rayon as rayon, amazon_PRICING.produit as produit, amazon_PRICING.keyword as am_keyword, amazon_PRICING.url as am_url, amazon_PRICING.search_volume as am_search_volume, amazon_PRICING.search_position as am_search_position INTO amazon_VS_CDISCOUNT_PRICING from amazon_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( amazon_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into aubert_pricing from pricing_keywords where domain='aubert.com';
CREATE INDEX ON aubert_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position, aubert_PRICING.magasin as magasin, aubert_PRICING.rayon as rayon, aubert_PRICING.produit as produit, aubert_PRICING.keyword as am_keyword, aubert_PRICING.url as am_url, aubert_PRICING.search_volume as am_search_volume, aubert_PRICING.search_position as am_search_position INTO aubert_VS_CDISCOUNT_PRICING from aubert_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( aubert_PRICING.keyword=CDISCOUNT_PRICING.keyword);

select keyword, url, search_position, search_volume, magasin, rayon, produit into autourdebebe_pricing from pricing_keywords where domain='autourdebebe.com';
CREATE INDEX ON autourdebebe.com_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,autourdebebe_PRICING.magasin as magasin, autourdebebe_PRICING.rayon as rayon, autourdebebe_PRICING.produit as produit, autourdebebe_PRICING.keyword as am_keyword, autourdebebe_PRICING.url as am_url, autourdebebe_PRICING.search_volume as am_search_volume, autourdebebe_PRICING.search_position as am_search_position INTO autourdebebe_VS_CDISCOUNT_PRICING from autourdebebe_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( autourdebebe_PRICING.keyword=CDISCOUNT_PRICING.keyword);

select keyword, url, search_position, search_volume, magasin, rayon, produit into toysrus_pricing from pricing_keywords where domain='toysrus.fr';
CREATE INDEX ON toysrus_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,toysrus_PRICING.magasin as magasin, toysrus_PRICING.rayon as rayon, toysrus_PRICING.produit as produit, toysrus_PRICING.keyword as am_keyword, toysrus_PRICING.url as am_url, toysrus_PRICING.search_volume as am_search_volume, toysrus_PRICING.search_position as am_search_position INTO toysrus_VS_CDISCOUNT_PRICING from toysrus_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( toysrus_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into materiel_pricing from pricing_keywords where domain='materiel.net';
CREATE INDEX ON materiel_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,materiel_PRICING.magasin as magasin, materiel_PRICING.rayon as rayon, materiel_PRICING.produit as produit, materiel_PRICING.keyword as am_keyword, materiel_PRICING.url as am_url, materiel_PRICING.search_volume as am_search_volume, materiel_PRICING.search_position as am_search_position INTO materiel_VS_CDISCOUNT_PRICING from materiel_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( materiel_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into pearl_pricing from pricing_keywords where domain='pearl.fr';
CREATE INDEX ON pearl_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,pearl_PRICING.magasin as magasin,  pearl_PRICING.rayon as rayon, pearl_PRICING.produit as produit, pearl_PRICING.keyword as am_keyword, pearl_PRICING.url as am_url, pearl_PRICING.search_volume as am_search_volume, pearl_PRICING.search_position as am_search_position INTO pearl_VS_CDISCOUNT_PRICING from pearl_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( pearl_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into ebay_pricing from pricing_keywords where domain='ebay.fr';
CREATE INDEX ON ebay_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,ebay_PRICING.magasin as magasin, ebay_PRICING.rayon as rayon, ebay_PRICING.produit as produit, ebay_PRICING.keyword as am_keyword, ebay_PRICING.url as am_url, ebay_PRICING.search_volume as am_search_volume, ebay_PRICING.search_position as am_search_position INTO ebay_VS_CDISCOUNT_PRICING from ebay_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( ebay_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into rueducommerce_pricing from pricing_keywords where domain='rueducommerce.fr';
CREATE INDEX ON rueducommerce_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,rueducommerce_PRICING.magasin as magasin, rueducommerce_PRICING.rayon as rayon, rueducommerce_PRICING.produit as produit, rueducommerce_PRICING.keyword as am_keyword, rueducommerce_PRICING.url as am_url, rueducommerce_PRICING.search_volume as am_search_volume, rueducommerce_PRICING.search_position as am_search_position INTO rueducommerce_VS_CDISCOUNT_PRICING from rueducommerce_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( rueducommerce_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into conforama_pricing from pricing_keywords where domain='conforama.fr';
CREATE INDEX ON conforama_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,conforama_PRICING.magasin as magasin, conforama_PRICING.rayon as rayon, conforama_PRICING.produit as produit, conforama_PRICING.keyword as am_keyword, conforama_PRICING.url as am_url, conforama_PRICING.search_volume as am_search_volume, conforama_PRICING.search_position as am_search_position INTO conforama_VS_CDISCOUNT_PRICING from conforama_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( conforama_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into darty_pricing from pricing_keywords where domain='darty.com';
CREATE INDEX ON darty_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,darty_PRICING.magasin as magasin, darty_PRICING.rayon as rayon, darty_PRICING.produit as produit, darty_PRICING.keyword as am_keyword, darty_PRICING.url as am_url, darty_PRICING.search_volume as am_search_volume, darty_PRICING.search_position as am_search_position INTO darty_VS_CDISCOUNT_PRICING from darty_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( darty_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into fnac_pricing from pricing_keywords where domain='fnac.com';
CREATE INDEX ON fnac_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,fnac_PRICING.magasin as magasin, fnac_PRICING.rayon as rayon, fnac_PRICING.produit as produit, fnac_PRICING.keyword as am_keyword, fnac_PRICING.url as am_url, fnac_PRICING.search_volume as am_search_volume, fnac_PRICING.search_position as am_search_position INTO fnac_VS_CDISCOUNT_PRICING from fnac_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( fnac_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into priceminister_pricing from pricing_keywords where domain='priceminister.com';
CREATE INDEX ON priceminister_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position, priceminister_PRICING.magasin as magasin, priceminister_PRICING.rayon as rayon, priceminister_PRICING.produit as produit, priceminister_PRICING.keyword as am_keyword, priceminister_PRICING.url as am_url, priceminister_PRICING.search_volume as am_search_volume, priceminister_PRICING.search_position as am_search_position INTO priceminister_VS_CDISCOUNT_PRICING from priceminister_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on(priceminister_PRICING.keyword=CDISCOUNT_PRICING.keyword);


select keyword, url, search_position, search_volume, magasin, rayon, produit into zalando_pricing from pricing_keywords where domain='zalando.fr';
CREATE INDEX ON zalando_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position, zalando_PRICING.magasin as magasin, zalando_PRICING.rayon as rayon, zalando_PRICING.produit as produit, zalando_PRICING.keyword as am_keyword, zalando_PRICING.url as am_url, zalando_PRICING.search_volume as am_search_volume, zalando_PRICING.search_position as am_search_position INTO zalando_VS_CDISCOUNT_PRICING from zalando_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( zalando_PRICING.keyword=CDISCOUNT_PRICING.keyword);

select keyword, url, search_position, search_volume, magasin, rayon, produit into ldlc_pricing from pricing_keywords where domain='ldlc.com';
CREATE INDEX ON ldlc_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,ldlc_PRICING.magasin as magasin, ldlc_PRICING.rayon as rayon, ldlc_PRICING.produit as produit, ldlc_PRICING.keyword as am_keyword, ldlc_PRICING.url as am_url, ldlc_PRICING.search_volume as am_search_volume, ldlc_PRICING.search_position as am_search_position INTO ldlc_VS_CDISCOUNT_PRICING from ldlc_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on(ldlc_PRICING.keyword=CDISCOUNT_PRICING.keyword);

select keyword, url, search_position, search_volume, magasin, rayon, produit into boulanger_pricing from pricing_keywords where domain='boulanger.fr';
CREATE INDEX ON boulanger_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,boulanger_PRICING.magasin as magasin, boulanger_PRICING.rayon as rayon, boulanger_PRICING.produit as produit, boulanger_PRICING.keyword as am_keyword, boulanger_PRICING.url as am_url, boulanger_PRICING.search_volume as am_search_volume, boulanger_PRICING.search_position as am_search_position INTO boulanger_VS_CDISCOUNT_PRICING from boulanger_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( boulanger_PRICING.keyword=CDISCOUNT_PRICING.keyword);

select keyword, url, search_position, search_volume, magasin, rayon, produit into alibaba_pricing from pricing_keywords where domain='french.alibaba.com';
CREATE INDEX ON alibaba_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position,alibaba_PRICING.magasin as magasin, alibaba_PRICING.rayon as rayon, alibaba_PRICING.produit as produit, alibaba_PRICING.keyword as am_keyword, alibaba_PRICING.url as am_url, alibaba_PRICING.search_volume as am_search_volume, alibaba_PRICING.search_position as am_search_position INTO alibaba_VS_CDISCOUNT_PRICING from alibaba_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( alibaba_PRICING.keyword=CDISCOUNT_PRICING.keyword);

select keyword, url, search_position, search_volume, magasin, rayon, produit into pixmania_pricing from pricing_keywords where domain='pixmania.fr';
CREATE INDEX ON pixmania_pricing (keyword);
select CDISCOUNT_PRICING.keyword as cd_keyword, CDISCOUNT_PRICING.url as cd_url, CDISCOUNT_PRICING.search_volume as cd_search_volume, CDISCOUNT_PRICING.search_position as cd_search_position, pixmania_PRICING.magasin as magasin, pixmania_PRICING.rayon as rayon, pixmania_PRICING.produit as produit, pixmania_PRICING.keyword as am_keyword, pixmania_PRICING.url as am_url, pixmania_PRICING.search_volume as am_search_volume, pixmania_PRICING.search_position as am_search_position INTO pixmania_VS_CDISCOUNT_PRICING from pixmania_PRICING LEFT OUTER JOIN CDISCOUNT_PRICING  on( pixmania_PRICING.keyword=CDISCOUNT_PRICING.keyword);



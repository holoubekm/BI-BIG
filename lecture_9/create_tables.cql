CREATE KEYSPACE IF NOT EXISTS holoumar_solr_ks WITH REPLICATION = {'class': 'NetworkTopologyStrategy', 'Solr': 1, replication_factor': 3};
USE holoumar_solr_ks;

CREATE TABLE IF NOT EXISTS logs (
	id int,
	user text,
	date timestamp,
	page text,
	severity text,
PRIMARY KEY (id);

CREATE TABLE IF NOT EXISTS user_spec_alog (
	user text,
	date text,
	page text,
	severity text,
	count int,
PRIMARY KEY ((user, severity), date, page));

CREATE TABLE IF NOT EXISTS user_alog(
	user text,
	count int,
PRIMARY KEY (user));

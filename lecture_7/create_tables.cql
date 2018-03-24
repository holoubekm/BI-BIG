CREATE KEYSPACE IF NOT EXISTS holoumar_accesslog_ks WITH REPLICATION = {'class': 'SimpleStrategy', 'replication_factor': 3};
USE holoumar_accesslog_ks;

CREATE TABLE IF NOT EXISTS logs (
	user text,
	date timestamp,
	page text,
	severity text,
PRIMARY KEY (user, date));

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

SELECT *, COUNT(*) as count FROM 
	(
		SELECT user, SUBSTR(date, 1, 10) AS date, page, severity FROM logs
	) t
	GROUP BY t.user, t.page, t.date, t.severity

SELECT user, COUNT(user) as count FROM logs GROUP BY user



naplneni tabulky ->
	INSERT INTO TABLE user_spec_alog SELECT t.user, t.date, t.page, t.severity, t.count FROM 
	(
		SELECT u.user, u.date, u.page, u.severity, count(*) as count FROM 
		(
			SELECT user, SUBSTR(date, 1, 10) AS date, page, severity FROM logs
		) u
	) t;

	INSERT INTO TABLE user_alog SELECT t.user, t.count FROM 
	(
		SELECT user, COUNT(user) as count FROM logs GROUP BY user
	) t;



--staci zalozit pomoci cassandry

hive> CREATE DATABASE bigdata;
hive> USE bigdata;
hive> CREATE EXTERNAL TABLE user_spec_alog 
        ( user string, date timestamp, page string, severity string, count int ) 
        STORED BY 'org.apache.hadoop.hive.cassandra.cql3.CqlStorageHandler' 
        TBLPROPERTIES ( "cassandra.ks.name" = "holoumar_accesslog_ks" , 
          "cassandra.cf.name" = "user_spec_alog" , 
          "cassandra.ks.repfactor" = "2" , 
          "cassandra.ks.strategy" = 
            "org.apache.cassandra.locator.SimpleStrategy" );
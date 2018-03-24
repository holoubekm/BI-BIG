package cz.cvut.fit.big.hadoop;


/**
 * Static constants for the keyspace, table, etc.
 */
public class AccessLogtuff {

	// definition of keyspace
  public final static String KEYSPACE = "accesslog_ks";


  public final static String LOG_TABLE = "logs";

  public final static String LOG_COL_USER = "user";
  public final static String LOG_COL_PAGE = "webpage";
  public final static String LOG_COL_DATE = "date";
  public final static String LOG_COL_SEVERITY = "severity";




  /** Main table with characters and their counts. */
  public final static String REPORTS_TABLE = "reports";

  /** Column in character table for the character name. */
  public final static String REPORTS_COL_USER = "user";
  public final static String REPORTS_COL_PAGE = "webpage";
  public final static String REPORTS_COL_DATE = "date";
  public final static String REPORTS_COL_SEVERITY = "severity";
  public final static String REPORTS_COL_COUNT = "count";

  	// selection of Cassandra node
  public final static String CONTACT_POINT = "192.168.151.147";

 
}

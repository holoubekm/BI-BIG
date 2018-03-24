package cz.cvut.fit.big.hadoop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;

/**
 * Set up the Cassandra database to use for testing.
 *
 * Create the keyspace, create the database, initialize with some data!
 */
public final class Setup {
  private static final Logger LOG = LoggerFactory.getLogger(Setup.class);

  /** Create the keyspace for the application. */
  private void createKeyspace(Session session) {
    // Create a keyspace, if it does not exist yet.
    session.execute(String.format(
        "CREATE KEYSPACE IF NOT EXISTS %s WITH REPLICATION = " +
        "{'class': 'SimpleStrategy', 'replication_factor': 3};",
    AccessLogtuff.KEYSPACE
    ));
    
    LOG.info("Keyspace created!");
    
    ResultSet execute = session.execute("USE " + AccessLogtuff.KEYSPACE);
}

  /**
   * Set up the table for the input side of this application.
   * @param session C* session.
   * @param titlesToBooks Map from titles of books (file names) to their content.
   */
  private void setupInputTable(
      Session session) {
     
    session.execute(String.format(
        "CREATE TABLE IF NOT EXISTS %s ( " +
            "%s text," +
            "%s timestamp," +
            "%s text," +
            "%s text," +
        	"PRIMARY KEY (%s,%s)"
        	+ "); ",
        AccessLogtuff.LOG_TABLE,
        AccessLogtuff.LOG_COL_USER,
        AccessLogtuff.LOG_COL_DATE,
        AccessLogtuff.LOG_COL_PAGE,
        AccessLogtuff.LOG_COL_SEVERITY,
        AccessLogtuff.LOG_COL_USER,
        AccessLogtuff.LOG_COL_DATE        
    ));
    
    LOG.info("Created INPUT TABLE");
  }

  /**
   * Set up the table for the output side of this application.
   * @param session C* session.
   */
  private void setupOutputTable(Session session) {
    // Create the table, if it does not exist yet.
	  session.execute(String.format(
		        "CREATE TABLE IF NOT EXISTS %s ( " +
		            "%s text," +
		            "%s text," +
		            "%s timestamp," +
		            "%s text," +
		            "%s int," +
		        	"PRIMARY KEY ((%s,%s),%s,%s)"
		        	+ "); ",
		        AccessLogtuff.REPORTS_TABLE,
		        AccessLogtuff.REPORTS_COL_USER,
		        AccessLogtuff.REPORTS_COL_PAGE,
		        AccessLogtuff.REPORTS_COL_DATE,
		        AccessLogtuff.REPORTS_COL_SEVERITY,
		        AccessLogtuff.REPORTS_COL_COUNT,
		        AccessLogtuff.REPORTS_COL_USER,
		        AccessLogtuff.REPORTS_COL_SEVERITY,        
		        AccessLogtuff.REPORTS_COL_DATE,        
		        AccessLogtuff.REPORTS_COL_PAGE       
		    ));
		    
		    LOG.info("Created OUTPUT TABLE");
  }
  
  
  
  //
  // TODO: Vytvořte tabulku a job na součet všech requestů uživatelů.
  //
  private void setupBonusTable(Session session) {
	  
  }

  public void loadDataAndSetupTables(String bookFiles[]) {

    Cluster cluster = Cluster.builder().addContactPoint(AccessLogtuff.CONTACT_POINT).build();
    Session session = cluster.connect();
    createKeyspace(session);
    setupInputTable(session);
    setupOutputTable(session);
    cluster.close();
  }


  /**
   * Actually load in the data!
   * @param args Specify the files to load.
   */
  public static void main(String args[]) {
    new Setup().loadDataAndSetupTables(args);
  }
}

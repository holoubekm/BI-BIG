package cz.cvut.fit.big.hadoop;

import java.util.List;
import java.util.Map;

import org.apache.cassandra.hadoop.cql3.CqlConfigHelper;
import org.apache.cassandra.hadoop2.ConfigHelper;
import org.apache.cassandra.hadoop2.cql3.CqlOutputFormat;
import org.apache.cassandra.hadoop2.cql3.CqlPagingInputFormat;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Quick and dirty Hadoop application to get started!
 */
public class AccessLogReporter extends Configured implements Tool {
  private static final Logger LOG = LoggerFactory.getLogger(AccessLogReporter.class);

  public int run(String[] args) throws Exception {
	  
	 
    Job job = new Job(getConf());
    job.setJarByClass(getClass());
    job.setJobName("AccessLog Reporter");    
  
    
    job.setInputFormatClass(CqlPagingInputFormat.class);
    ConfigHelper.setInputRpcPort(job.getConfiguration(), "9160");
    ConfigHelper.setInputInitialAddress(job.getConfiguration(), AccessLogtuff.CONTACT_POINT);
    ConfigHelper.setInputColumnFamily(
        job.getConfiguration(),
        AccessLogtuff.KEYSPACE,
        AccessLogtuff.LOG_TABLE
    );
    ConfigHelper.setInputPartitioner(job.getConfiguration(), "Murmur3Partitioner");

    
   
    CqlConfigHelper.setInputCQLPageRowSize(job.getConfiguration(), "10000");
    ConfigHelper.setInputSplitSize(job.getConfiguration(), 16*1024*1024);

    job.setOutputFormatClass(CqlOutputFormat.class);
    ConfigHelper.setOutputColumnFamily(
        job.getConfiguration(),
        AccessLogtuff.KEYSPACE,
        AccessLogtuff.REPORTS_TABLE
    );

    String putQuery = String.format(
        "UPDATE %s.%s SET %s = ?",
        AccessLogtuff.KEYSPACE,
        AccessLogtuff.REPORTS_TABLE,
        AccessLogtuff.REPORTS_COL_COUNT
    );
    CqlConfigHelper.setOutputCql(job.getConfiguration(), putQuery);

    ConfigHelper.setOutputInitialAddress(job.getConfiguration(), AccessLogtuff.CONTACT_POINT);
    ConfigHelper.setOutputPartitioner(job.getConfiguration(), "Murmur3Partitioner");

    job.setMapperClass(LogReporterMapper.class);
    job.setReducerClass(LogReporterReducer.class);

    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(IntWritable.class);
    
    job.setOutputKeyClass(Map.class);
    job.setOutputValueClass(List.class);

    job.waitForCompletion(true);

    return 0;
  }

  public static void main(String[] args) throws Exception {
	  
    int exitCode = ToolRunner.run(new AccessLogReporter(), args);
    System.exit(exitCode);
  }

}

package cz.cvut.fit.big.hadoop;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogReporterMapper
    extends Mapper<Map<String, ByteBuffer>, Map<String, ByteBuffer>, Text, IntWritable> {
  private static final Logger LOG = LoggerFactory.getLogger(LogReporterMapper.class);
  private final static IntWritable ONE = new IntWritable(1);
  private Text word = new Text();

  @Override
  public void map(
      Map<String, ByteBuffer> key,
      Map<String, ByteBuffer> columns,

     Context context) throws IOException, InterruptedException {
	  
    String storyText = ByteBufferUtil.string(columns.get(AccessLogtuff.LOG_COL_PAGE));
    String user = ByteBufferUtil.string(key.get(AccessLogtuff.LOG_COL_USER));    
    long timestamp =  key.get(AccessLogtuff.LOG_COL_DATE).getLong();
    long msInDay = 1000 * 60 * 60 * 24; // Number of milliseconds in a day
    long msPortion = timestamp % msInDay;
    timestamp =  timestamp - msPortion;    
    String severity = ByteBufferUtil.string(columns.get(AccessLogtuff.LOG_COL_SEVERITY));  
  
    word.set(user+","+timestamp+","+severity+","+storyText);   
    
    context.write(word, ONE);      
    
  }
}

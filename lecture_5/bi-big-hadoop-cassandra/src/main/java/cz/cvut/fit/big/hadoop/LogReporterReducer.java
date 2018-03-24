package cz.cvut.fit.big.hadoop;

import org.apache.cassandra.utils.ByteBufferUtil;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Reducer class for counting words.
 */
public class LogReporterReducer
    extends Reducer<Text, IntWritable, Map<String, ByteBuffer>, List<ByteBuffer>> {

  // Create a map from primary key names to primary key values.
  private Map<String, ByteBuffer> primaryKeys;

  /**
   * Initialize the Cassandra / Hadoop reducer (create the primary key map).
   *
   * @param context
   * @throws IOException
   * @throws InterruptedException
   */
  protected void setup(Context context) throws IOException, InterruptedException {
    primaryKeys = new LinkedHashMap<String, ByteBuffer>();
  }

  	/**
	 * Main reduce function. Standard Hadoop word-count boilerplate, but
	 * populate a map of primary characterName characterNameCounts and a list of
	 * column characterNameCounts to write to the context.
	 * 
	 * @param characterName
	 *            Character name (the "word" in word count).
	 * @param characterNameCounts
	 *            Counts for the character ("count" in word count).
	 * @param context
	 *            Hadoop job context.
	 * @throws IOException
	 * @throws InterruptedException
	 */
  @Override
  public void reduce(
      Text characterName,
      Iterable<IntWritable> characterNameCounts,
      Context context) throws IOException, InterruptedException {
    int sum = 0;
    for (IntWritable val : characterNameCounts) {
      sum += val.get();
    }

    
    String[] split = characterName.toString().split(",");
    long date = Long.parseLong(split[1]);
    primaryKeys.put(AccessLogtuff.REPORTS_COL_USER, ByteBufferUtil.bytes(split[0]));
    primaryKeys.put(AccessLogtuff.REPORTS_COL_SEVERITY, ByteBufferUtil.bytes(split[2]));
    primaryKeys.put(AccessLogtuff.REPORTS_COL_DATE, ByteBufferUtil.bytes(date));
    primaryKeys.put(AccessLogtuff.REPORTS_COL_PAGE, ByteBufferUtil.bytes(split[3]));

    
    // Create a list of the bound variables for the CQL3 INSERT query.
    List<ByteBuffer> boundVariables = new ArrayList<ByteBuffer>();
    boundVariables.add(ByteBufferUtil.bytes(sum));
   // boundVariables.add(ByteBufferUtil.bytes(split[1]));
    context.write(primaryKeys, boundVariables);
  }

}

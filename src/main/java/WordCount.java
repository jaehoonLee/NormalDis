import java.io.IOException;
import java.util.*;
        
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.*;
import org.apache.hadoop.mapreduce.lib.output.*;
        
public class WordCount {
        
 public static class MyMapper extends Mapper<LongWritable, Text, Text, LongWritable> {
    private final static LongWritable one = new LongWritable(1);
    private LongWritable sumValue;
    private Text word = new Text();
    int index = 0;

    //read line by line
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
//        StringTokenizer tokenizer = new StringTokenizer(line, "\t\r\n\f |,.()<>");
        StringTokenizer tokenizer = new StringTokenizer(line, ",");
        int index = 0;
        int sum = 0;
        String dateKey ="";
        String sexKey ="";
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken().toLowerCase();
            if(index == 0)
            {
                dateKey = token;
            }
            else if(index == 1 || index == 3)
            {
//              selKey += (token + ",");
            }
            else if(index == 2)
            {
                sexKey = token;
            }
            else
            {
                sum +=  Integer.parseInt(token);
                if((index - 3)%5==0)
                {
                    word.set(dateKey + "," + (index - 3 - 5)+ "," + sexKey);
                    sumValue = new LongWritable(sum);
                    if(!sexKey.equals("0"))
                        context.write(word, sumValue);
                    sum = 0;
                }
            }
            index++;
        }
    }
 } 

 public static class MyReducer extends Reducer<Text, LongWritable, NullWritable, Text> {
    private Text sumWritable = new Text();

    public void reduce(Text key, Iterable<LongWritable> values, Context context) 
      throws IOException, InterruptedException {
        long sum = 0;
        for (LongWritable val : values) {
            sum += val.get();
        }
        sumWritable.set(key.toString() + "," +sum);
        context.write(NullWritable.get(), sumWritable);
    }
 }
        
 public static void main(String[] args) throws Exception {
    Configuration conf = new Configuration();
    Job job = new Job(conf, "WordCount");

    job.setJarByClass(WordCount.class);
    job.setMapperClass(MyMapper.class);
    job.setReducerClass(MyReducer.class);
    
    // if mapper outputs are different, call setMapOutputKeyClass and setMapOutputValueClass
    job.setMapOutputKeyClass(Text.class);
    job.setMapOutputValueClass(LongWritable.class);

    // reducer output class
    job.setOutputKeyClass(NullWritable.class);
    job.setOutputValueClass(Text.class);

    // An InputFormat for plain text files. Files are broken into lines. Either linefeed or carriage-return are used to signal end of line.
    // Keys are the position in the file, and values are the line of text..        
    job.setInputFormatClass(TextInputFormat.class);
    job.setOutputFormatClass(TextOutputFormat.class);
        
    FileInputFormat.addInputPath(job, new Path(args[0]));
    FileOutputFormat.setOutputPath(job, new Path(args[1]));
        
    job.waitForCompletion(true);
 }
        
}

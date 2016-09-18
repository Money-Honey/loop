package com.loopme.webapp.hbase;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.TaskInputOutputContext;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class AvgTemperatureDriver extends Configured implements Tool {

    @Override
    public int run(String[] args) throws Exception {
        Job job = new Job(getConf(), "Avg temperature");
        job.setJarByClass(getClass());
        job.setNumReduceTasks(0);

        Scan scan = new Scan();

        job.setJarByClass(AvgTemperatureDriver.class);
        job.setMapperClass(AvgTemperatureMapper.class);

        job.setOutputFormatClass(TableOutputFormat.class);
        job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, "weather");

//        TaskInputOutputContext<ImmutableBytesWritable,Result,ImmutableBytesWritable,Mutation>;

        return job.waitForCompletion(true) ? 0 : 1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new AvgTemperatureDriver(), args);
        System.exit(exitCode);
    }

}

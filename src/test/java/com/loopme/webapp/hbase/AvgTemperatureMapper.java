package com.loopme.webapp.hbase;

import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class AvgTemperatureMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Mutation> {

    public void map(LongWritable rowkey, Text result, Mapper.Context context) throws InterruptedException, IOException {
        Integer max = 2;
        Integer min = 1;

        Put put = new Put(new byte[] {1});
        put.addColumn(Bytes.toBytes("temperatures"), Bytes.toBytes("avg"), Bytes.toBytes((max + min) / 2.0));
        context.write(new ImmutableBytesWritable(new byte[] {1}), put);
    }
}
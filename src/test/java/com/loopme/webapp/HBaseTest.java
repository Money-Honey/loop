package com.loopme.webapp;

import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.io.Text;
import org.junit.Test;

import java.io.IOException;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class HBaseTest {

    @Test
    public void hbase() throws Exception {
        TableMapReduceUtil.initTableMapperJob(
                "Dig", null, DigTableMapper.class, ImmutableBytesWritable.class, null, null
        );
    }

    class DigTableMapper extends TableMapper<Text, Put> {
        @Override
        protected void setup(Context context) throws IOException, InterruptedException {

        }

        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

        }

        @Override
        protected void cleanup(Context context) throws IOException, InterruptedException {

        }
    }


}

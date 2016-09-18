package com.loopme.webapp.hbase;

import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author <a href="mailto:dema.luxoft@gmail.com">Volodymyr Dema</a>
 */
public class RecalculateHbaseMapper extends Mapper<ImmutableBytesWritable, Result, ImmutableBytesWritable, Put> {

    public void map(ImmutableBytesWritable rowkey, Result result, Context context) throws InterruptedException, IOException {
        Integer max = getInt(result, "temperatures", "max");
        Integer min = getInt(result, "temperatures", "min");

        Put put = new Put(rowkey.copyBytes());
        put.add(Bytes.toBytes("temperatures"), Bytes.toBytes("avg"), Bytes.toBytes((max + min) / 2.0));
        context.write(new ImmutableBytesWritable(rowkey), put);
    }

    private Integer getInt(Result result, String family, String qualifier) {
        Cell cell = result.getColumnLatestCell(Bytes.toBytes(family), Bytes.toBytes(qualifier));
        byte[] value = CellUtil.cloneValue(cell);
        return ByteBuffer.wrap(value).getInt();
    }
}
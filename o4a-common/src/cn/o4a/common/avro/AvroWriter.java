package cn.o4a.common.avro;

import org.apache.avro.Schema;
import org.apache.avro.file.DataFileWriter;

import java.io.IOException;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/7/29 9:35
 */
@FunctionalInterface
public interface AvroWriter<T> {
    /**
     * Avro文件写入
     *
     * @param dataWriter writer
     * @param schema     schema
     * @throws IOException 异常
     */
    void write(DataFileWriter<T> dataWriter, Schema schema) throws IOException;
}

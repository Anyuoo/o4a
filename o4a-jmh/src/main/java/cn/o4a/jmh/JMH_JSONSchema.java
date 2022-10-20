package cn.o4a.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.RunnerException;

import java.util.concurrent.TimeUnit;

/**
 * @author Anyu
 * @version 1.0.0
 * @since 2022/8/1 9:19
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@OperationsPerInvocation(100_000)
public class JMH_JSONSchema {

    public static void main(String[] args) throws RunnerException {

        JSONSchemaValidator.valid(JSONConst.JSON_SCHENA_1, JSONConst.JSON_1);

        //Options opt = new OptionsBuilder()
        //        .include(JMH_JSONSchema.class.getSimpleName())
        //        .forks(1)
        //        .build();
        //
        //new Runner(opt).run();
    }

    ///**
    // * json path
    // */
    //@Benchmark
    //public void measureJSONPathAvgTime() {
    //    //
    //
    //}

    /**
     * json schema
     */
    @Benchmark
    public void measureJSONSchemaAvgTime() {
        //
        JSONSchemaValidator.valid(JSONConst.JSON_SCHENA_1, JSONConst.JSON_1);
    }

}

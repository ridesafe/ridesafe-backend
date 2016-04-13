package io.ridesafe.backend

import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

/**
 * Created by evoxmusic on 13/04/16.
 */
@Configuration
open class MySparkConfiguration {

    @Autowired
    var environment: Environment? = null

    @Bean
    open fun sparkConf() = SparkConf().setAppName("Rider's activity recognition")
            .set("spark.cassandra.connection.host", environment?.getProperty("cassandra.contact-points"))
            .setMaster("local[*]")

    @Bean
    open fun getSparkContext() = JavaSparkContext(sparkConf())

}

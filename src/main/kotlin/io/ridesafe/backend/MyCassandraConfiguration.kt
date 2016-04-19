/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.ridesafe.backend

import com.datastax.driver.core.SocketOptions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean
import org.springframework.data.cassandra.config.SchemaAction
import org.springframework.data.cassandra.convert.CassandraConverter
import org.springframework.data.cassandra.convert.MappingCassandraConverter
import org.springframework.data.cassandra.core.CassandraOperations
import org.springframework.data.cassandra.core.CassandraTemplate
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext
import org.springframework.data.cassandra.mapping.CassandraMappingContext
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories

/**
 * Created by evoxmusic on 12/04/16.
 */
@Configuration
@EnableCassandraRepositories(basePackages = arrayOf("io.ridesafe.backend.repositories.cassandra"))
open class MyCassandraConfiguration {

    @Autowired
    private var env: Environment? = null

    @Bean
    open fun cluster(): CassandraClusterFactoryBean {
        val cluster = CassandraClusterFactoryBean()
        val contactPoints = env?.getProperty("cassandra.contact-points")
        cluster.setContactPoints(contactPoints)
        cluster.setPort(env?.getProperty("cassandra.port")?.toInt() ?: 9042)
        cluster.setSocketOptions(SocketOptions().setReadTimeoutMillis(0))
        return cluster
    }

    @Bean
    open fun mappingContext(): CassandraMappingContext = BasicCassandraMappingContext()

    @Bean
    open fun converter(): CassandraConverter = MappingCassandraConverter(mappingContext())

    @Bean
    open fun session(): CassandraSessionFactoryBean {
        val session = CassandraSessionFactoryBean()
        session.setCluster(cluster().`object`)
        session.setKeyspaceName(env?.getProperty("cassandra.keyspace"))
        session.converter = converter()
        session.schemaAction = SchemaAction.NONE
        return session
    }

    @Bean
    open fun cassandraTemplate(): CassandraOperations = CassandraTemplate(session().`object`)

    @Bean
    open fun myClusterConfiguration(): Any? {
        // set fetch size higher than the default (5000) non editable value
        cluster().`object`.configuration.queryOptions.fetchSize = 100000000
        return null
    }


}
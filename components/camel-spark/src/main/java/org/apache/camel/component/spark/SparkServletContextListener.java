/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.component.spark;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import spark.Access;

/**
 * A {@link javax.servlet.ServletContextListener} to ensure we initialize Spark in servlet mode.
 */
public class SparkServletContextListener implements ServletContextListener {

    private static final Logger LOG = LoggerFactory.getLogger(ServletSparkApplication.class);

    @Override
    public void contextInitialized(ServletContextEvent event) {
        LOG.info("contextInitialized");
        // force spark to be in Servlet mode
        Access.runFromServlet();
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        LOG.info("contextDestroyed");
        // noop
    }

}

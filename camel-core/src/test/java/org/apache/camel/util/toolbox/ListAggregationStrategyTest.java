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
package org.apache.camel.util.toolbox;

import java.util.Comparator;

import org.apache.camel.builder.RouteBuilder;

/*
 * Reuse of the existing test cases, because the toolbox just provides
 * another api. So just overwrite the RouteBuilder-definition.
 */
public class ListAggregationStrategyTest extends org.apache.camel.processor.aggregator.ListAggregationStrategyTest {

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("direct:start")
                    .aggregate(constant("AllInOne"), AggregationStrategies.list(String.class))
                        .completionTimeout(500)
                    .to("mock:result");
                Comparator<String> comparator = new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareTo(s2);
                    }
                };
                from("direct:startComparator")
                    .aggregate(constant("AllInOne"), AggregationStrategies.list(String.class, comparator))
                        .completionTimeout(500)
                    .to("mock:result");
            }
        };
    }
}

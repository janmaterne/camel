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
package org.apache.camel.processor.aggregator;

import java.util.Comparator;
import java.util.List;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Predicate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ValueBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.processor.aggregate.ListAggregationStrategy;

public class ListAggregationStrategyTest extends ContextTestSupport {

    public void testSingle() throws InterruptedException {
        testRun("direct:start", "One");
    }

    public void testList() throws InterruptedException {
        testRun("direct:start", "One", "Two", "Three");
    }
    
    @SuppressWarnings("unchecked")
    public void testSort() throws InterruptedException {
        testRun("direct:startComparator", "January", "February", "March", "April");
        List<String> list = getMockEndpoint("mock:result").getExchanges().get(0).getIn().getBody(List.class);
        assertEquals("April", list.get(0));
        assertEquals("February", list.get(1));
        assertEquals("January", list.get(2));
        assertEquals("March", list.get(3));
    }

    private void testRun(final String endpoint, final String... bodies) throws InterruptedException {
        MockEndpoint result = getMockEndpoint("mock:result");
        result.expectedMessageCount(1);
        result.expectedMessagesMatches(new Predicate() {
            @Override
            public boolean matches(Exchange exchange) {
                // 1st test: type is list
                @SuppressWarnings("unchecked")
                List<String> list = exchange.getIn().getBody(List.class);
                // 2st test: size
                return bodies.length == list.size();
            }
        });

        for (String body : bodies) {
            template.sendBody(endpoint, body);
        }

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                Comparator<String> comparator = new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareTo(s2);
                    }
                };
                ListAggregationStrategy<String> strategy = new ListAggregationStrategy<String>(null);
                ListAggregationStrategy<String> strategyComparator = new ListAggregationStrategy<String>(comparator);
                ValueBuilder correlationExpression = constant("AllInOne");
                
                from("direct:start")
                    .aggregate(correlationExpression, strategy)
                        .completionTimeout(500)
                    .to("mock:result");

                from("direct:startComparator")
                    .aggregate(correlationExpression, strategyComparator)
                        .completionTimeout(500)
                    .to("mock:result");
            }
        };
    }
}

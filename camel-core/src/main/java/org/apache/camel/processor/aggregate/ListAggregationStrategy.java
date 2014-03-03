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
package org.apache.camel.processor.aggregate;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.camel.Exchange;

/**
 * Aggregation strategy which collects all incoming bodies in a list.
 * 
 * @param <T> type of the list
 */
public class ListAggregationStrategy<T> implements AggregationStrategy {

    /** The comparator used to keep the list sorted if any. */
    final Comparator<T> comparator;

    /**
     * @param comparator The comparator used to keep the list sorted if any; if
     *            this is null then the list will not be kept sorted.
     */
    public ListAggregationStrategy(final Comparator<T> comparator) {
        this.comparator = comparator;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Exchange aggregate(final Exchange aggregatedExchange, final Exchange newExchange) {
        final T newBody = (T)newExchange.getIn().getBody();
        List<T> exchangeBodies = null;

        if (aggregatedExchange == null) {
            exchangeBodies = new LinkedList<T>();
            exchangeBodies.add(newBody);
            newExchange.getIn().setBody(exchangeBodies);
            return newExchange;
        }

        exchangeBodies = aggregatedExchange.getIn().getBody(List.class);
        exchangeBodies.add(newBody);
        if (this.comparator != null) {
            Collections.sort(exchangeBodies, this.comparator);
        }

        return aggregatedExchange;
    }

}

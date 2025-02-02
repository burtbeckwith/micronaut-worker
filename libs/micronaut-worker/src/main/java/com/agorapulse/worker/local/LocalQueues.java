/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2022 Agorapulse.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.agorapulse.worker.local;

import com.agorapulse.worker.queue.JobQueues;
import io.micronaut.context.annotation.Secondary;
import io.micronaut.core.convert.ConversionService;
import io.micronaut.core.type.Argument;

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

@Secondary
@Singleton
@Named("local")
public class LocalQueues implements JobQueues {

    private final ConcurrentMap<String, ConcurrentLinkedDeque<Object>> queues = new ConcurrentHashMap<>();
    private final ConversionService<?> conversionService;

    public LocalQueues(Optional<ConversionService<?>> conversionService) {
        this.conversionService = conversionService.orElse(ConversionService.SHARED);
    }

    @Override
    public <T> void readMessages(String queueName, int maxNumberOfMessages, Duration waitTime, Argument<T> argument, Consumer<T> action) {
        ConcurrentLinkedDeque<Object> objects = queues.get(queueName);
        if (objects == null) {
            return;
        }

        for (int i = 0; i < maxNumberOfMessages && !objects.isEmpty(); i++) {
            action.accept(conversionService.convertRequired(objects.removeFirst(), argument));
        }
    }

    @Override
    public void sendMessage(String queueName, Object result) {
        queues.computeIfAbsent(queueName, key -> new ConcurrentLinkedDeque<>()).addLast(result);
    }
}

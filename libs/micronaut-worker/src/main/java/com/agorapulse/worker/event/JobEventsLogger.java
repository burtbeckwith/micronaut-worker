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
package com.agorapulse.worker.event;

import com.agorapulse.worker.Job;
import io.micronaut.runtime.event.annotation.EventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Singleton
public class JobEventsLogger {

    private static final Logger LOGGER = LoggerFactory.getLogger(Job.class);

    @EventListener
    void onJobExecutionStarted(JobExecutionStartedEvent event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Starting job {}", event.getName());
        }
    }

    @EventListener
    void onJobExecutionResult(JobExecutionResultEvent event) {
        if (LOGGER.isInfoEnabled()) {
            Object result = event.getResult();
            if (result != null) {
                LOGGER.info("Job {} emitted result {}", event.getName(), result);
            } else if (LOGGER.isTraceEnabled()){
                LOGGER.trace("No results emitted from job {}", event.getName());
            }
        }
    }

    @EventListener
    void onJobExecutionFinished(JobExecutionFinishedEvent event) {
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("Finished executing job {} (some results can still be generated asynchronously later)", event.getName());
        }
    }

}

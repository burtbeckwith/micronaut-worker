/*
 * SPDX-License-Identifier: Apache-2.0
 *
 * Copyright 2021 Agorapulse.
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
package com.agorapulse.worker;

import com.agorapulse.worker.annotation.FixedRate;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ConsumerJob {

    public static final String ENVIRONMENT = "consumer-job-test";

    private final List<String> messages = new ArrayList<>();

    @FixedRate("100ms")
    void accept(String message) {
        this.messages.add(message);
    }

    public List<String> getMessages() {
        return messages;
    }

}

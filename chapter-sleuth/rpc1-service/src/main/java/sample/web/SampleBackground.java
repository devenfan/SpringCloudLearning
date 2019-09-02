/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package sample.web;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class SampleBackground {

    private static final Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    private Tracer              tracer;

    @Autowired
    private SpanAccessor        accessor;

    @Autowired
    private Random              random;

    @Async
    public void background() throws InterruptedException {
        int millis = this.random.nextInt(3000);
        Thread.sleep(millis);
        this.tracer.addTag("background-sleep-millis", String.valueOf(millis));
        logger.info("[RPC1.background] CurrentSpan: {}", this.accessor.getCurrentSpan());
    }

}

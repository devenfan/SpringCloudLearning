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
import java.util.concurrent.Callable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.cloud.sleuth.Span;
import org.springframework.cloud.sleuth.SpanAccessor;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class SampleController implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    private static final Log log    = LogFactory.getLog(SampleController.class);

    @Autowired
    private RestTemplate     restTemplate;
    @Autowired
    private Tracer           tracer;
    @Autowired
    private SpanAccessor     accessor;
    @Autowired
    private SampleBackground background;

    private final Random     random = new Random();
    private int              port;

    @RequestMapping("/hi")
    public String hi() throws InterruptedException {
        Thread.sleep(this.random.nextInt(1000));
        String s = this.callUserServiceApi("/user/hi", String.class);
        return "[RPC1] hi: \r\n " + s;
    }

    @RequestMapping("/h2")
    public String h2() throws InterruptedException {
        int millis = this.random.nextInt(1000);
        Thread.sleep(millis);
        this.tracer.addTag("random-sleep-millis", String.valueOf(millis));
        String s = this.callUserServiceApi("/user/hi", String.class);
        return "[RPC1] h2: \r\n " + s;
    }

    @RequestMapping("/call")
    public Callable<String> call() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                int millis = SampleController.this.random.nextInt(1000);
                Thread.sleep(millis);
                SampleController.this.tracer.addTag("callable-sleep-millis", String.valueOf(millis));
                Span currentSpan = SampleController.this.accessor.getCurrentSpan();
                return "[RPC1] async call: " + currentSpan;
            }
        };
    }

    @RequestMapping("/background")
    public String async() throws InterruptedException {
        this.background.background();
        return "[RPC1] background";
    }

    @RequestMapping("/info")
    public String info() throws InterruptedException {
        Span span = this.tracer.createSpan("http:customTraceEndpoint", new AlwaysSampler());
        int millis = this.random.nextInt(1000);
        log.info(String.format("Sleeping for [%d] millis", millis));
        Thread.sleep(millis);
        this.tracer.addTag("random-sleep-millis", String.valueOf(millis));
        String s = this.callSelfRestApi("/info", String.class);
        this.tracer.close(span);
        return "[RPC1] info: " + s;
    }

    @RequestMapping("/health")
    public String health() throws InterruptedException {
        int millis = this.random.nextInt(1000);
        log.info(String.format("Sleeping for [%d] millis", millis));
        Thread.sleep(millis);
        this.tracer.addTag("random-sleep-millis", String.valueOf(millis));

        String s = this.callSelfRestApi("/health", String.class);
        return "[RPC1] health: " + s;
    }

    @Override
    public void onApplicationEvent(EmbeddedServletContainerInitializedEvent event) {
        this.port = event.getEmbeddedServletContainer().getPort();
    }

    private <T> T callSelfRestApi(String apiUri, Class<T> respClazz) {
        return this.restTemplate.getForObject("http://localhost:" + this.port + apiUri, respClazz);
    }

    private <T> T callUserServiceApi(String apiUri, Class<T> respClazz) {
        return this.restTemplate.getForObject("http://localhost:8762" + apiUri, respClazz);
    }
}

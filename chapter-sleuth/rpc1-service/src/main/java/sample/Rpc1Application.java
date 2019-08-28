package sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.client.RestTemplate;
import sample.web.SampleController;

@EnableEurekaClient
@SpringBootApplication
@EnableAspectJAutoProxy(proxyTargetClass = true)
//@EnableAsync
public class Rpc1Application {

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public SampleController sampleController() {
		return new SampleController();
	}

	public static void main(String[] args) {
		SpringApplication.run(Rpc1Application.class, args);
	}

}

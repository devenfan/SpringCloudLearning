package sample.web;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * UserServiceFeignClient
 *
 * @author Deven
 * @version : UserServiceFeignClient, v 0.1 2019-08-27 19:35 Deven Exp$
 */
@FeignClient("user-service")
public interface UserServiceFeignClient {

    @RequestMapping(value = "/user/hi", method = RequestMethod.GET)
    String userHi();
}

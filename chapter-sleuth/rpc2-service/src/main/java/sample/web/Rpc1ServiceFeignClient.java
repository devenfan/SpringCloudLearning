package sample.web;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Rpc1ServiceFeignClient
 *
 * @author Deven
 * @version : Rpc1ServiceFeignClient, v 0.1 2019-08-27 19:35 Deven Exp$
 */
@FeignClient("rpc1-service")
public interface Rpc1ServiceFeignClient {

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    String rpcHi();

    @RequestMapping(value = "/h2", method = RequestMethod.GET)
    String rpcH2();
}

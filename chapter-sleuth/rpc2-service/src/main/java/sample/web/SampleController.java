package sample.web;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Spencer Gibb
 */
@RestController
public class SampleController {

    private static final Log         log    = LogFactory.getLog(SampleController.class);

    private UserServiceFeignClient   userServiceFeignClient;

    private Rpc1ServiceFeignClient   rpc1ServiceFeignClient;

    private final Random             random = new Random();


    @Autowired
    public void setUserServiceFeignClient(UserServiceFeignClient userServiceFeignClient) {
        this.userServiceFeignClient = userServiceFeignClient;
    }

    @Autowired
    public void setRpc1ServiceFeignClient(Rpc1ServiceFeignClient rpc1ServiceFeignClient) {
        this.rpc1ServiceFeignClient = rpc1ServiceFeignClient;
    }

    @RequestMapping("/hi")
    public String h1() throws InterruptedException {
        Thread.sleep(this.random.nextInt(1000));
        String s1 = this.rpc1ServiceFeignClient.rpcHi();
        String s2 = this.userServiceFeignClient.userHi();
        return "[RPC2] hi: \r\n " + s1 + " \r\n " + s2;
    }

    @RequestMapping("/h2")
    public String h2() {
        String s1 = this.userServiceFeignClient.userHi();
        String s2 = this.rpc1ServiceFeignClient.rpcH2();
        return "[RPC2] h2: \r\n " + s1 + " \r\n " + s2;
    }

}

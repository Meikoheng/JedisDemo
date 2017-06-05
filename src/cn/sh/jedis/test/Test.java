package cn.sh.jedis.test;

import cn.sh.jedis.service.RedisService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by sh44565 on 2017/6/5.
 */
public class Test {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("application.xml");
        RedisService redisService = (RedisService) applicationContext.getBean("redisService");
//        redisService.set("foo", "bar");
//        redisService.setValue("foo", "bar", 10L);
//        redisService.del("foo");
//        redisService.hSet("sh1", "b", "c");
        redisService.incr("increment");
    }
}

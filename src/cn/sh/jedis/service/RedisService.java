package cn.sh.jedis.service;

import com.sun.org.apache.regexp.internal.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

/**
 * Created by sh44565 on 2017/6/5.
 */
@Service
public class RedisService {

    private static Jedis jedis;

    @Autowired
    private RedisTemplate redisTemplate;

//    static {
//        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
//        jedis = (Jedis) context.getBean("jedis");
//    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, 10, TimeUnit.SECONDS);
    }

    public void setValue(String key, String value, Long seconds) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                byte[] bKey = redisSerializer.serialize(key);
                byte[] bValue = redisSerializer.serialize(value);
                redisConnection.set(bKey, bValue);
                redisConnection.expire(bKey, seconds);
                return null;
            }
        });
    }

    public void del(String key) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                redisConnection.del(redisTemplate.getStringSerializer().serialize(key));
                return null;
            }
        });
    }

    public void hSet(String key, String field, String value) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                byte[] serKey = redisSerializer.serialize(key);
                byte[] serField = redisSerializer.serialize(field);
                byte[] serValue = redisSerializer.serialize(value);
                redisConnection.hSet(serKey, serField, serValue);
                return null;
            }
        });
    }

    public void incr(String key) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer<String> redisSerializer = redisTemplate.getStringSerializer();
                redisConnection.incr(redisSerializer.serialize(key));
                return null;
            }
        });
    }

    public void leftPush(String key, String value) {
        redisTemplate.opsForList().leftPush(key, value);
    }

    public void hMSet(String key, Map<String, Object> map) {
        redisTemplate.execute(new RedisCallback() {
            @Override
            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
                RedisSerializer redisSerializer = redisTemplate.getStringSerializer();
                Map<byte[], byte[]> bMap = new HashMap<byte[], byte[]>();
                map.entrySet().forEach(m -> bMap.put(redisSerializer.serialize(m.getKey()), redisSerializer.serialize(m.getValue())));
                redisConnection.hMSet(redisSerializer.serialize(key), bMap);
                return null;
            }
        });
    }

//    public static void main(String[] args) {
////        jedis.set("foo", "bar");
////        jedis.expire("foo", 10);
//    }
}

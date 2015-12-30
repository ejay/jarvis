import redis.clients.jedis.Jedis;

import java.util.Arrays;

/**
 * Created by bas on 12-12-15.
 */
public class Main {
    public static void main(String[] args){
        System.out.println("args = " + Arrays.toString(args));
        test();
    }

    public static void test(){
        Jedis jedis = new Jedis("localhost");
        jedis.set("foo", "bar");
        String value = jedis.get("foo");
        System.out.println("value = " + value);
    }
}

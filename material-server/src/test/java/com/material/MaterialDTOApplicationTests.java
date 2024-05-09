package com.material;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;
import java.util.Set;


@SpringBootTest
class MaterialDTOApplicationTests {

	@Resource
	private RedisTemplate redisTemplate;

	@Test
	void contextLoads() {
		//hset hget hdel hkeys hvals
		HashOperations hashOperations = redisTemplate.opsForHash();

		hashOperations.put("100","name","tom");
		hashOperations.put("100","age","20");
		String name = (String) hashOperations.get("100", "name");
		System.out.println(name);

		Set keys = hashOperations.keys("100");
		System.out.println(keys);

		List values = hashOperations.values("100");
		System.out.println(values);

		hashOperations.delete("100","age");
	}

}

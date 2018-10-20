package com.nxt.test.deom;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


/**
 * @author Daily
 *
 * 2018年10月19日
 */
public class JedisLock {
	public static final String LOCALHOST_ID =  getIP();
	private static final String LOCK_SUCCESS = "OK";
	/*
	EX seconds -- Set the specified expire time, in seconds.
	PX milliseconds -- Set the specified expire time, in milliseconds.
	NX -- Only set the key if it does not already exist.
	XX -- Only set the key if it already exist.
	*/
	private static final String SET_IF_NOT_EXIST = "NX";
	private static final String SET_WITH_EXPIRE_TIME = "PX";
	private static final Long RELEASE_SUCCESS = 1L;

	/**
	 * 	尝试获取分布式锁
	 * 	
	 * @param jedis      Redis客户端
	 * @param lockKey    锁
	 * @param requestId  请求标识
	 * @param expireTime 超期时间
	 * @return 是否获取成功
	 */
	public static boolean tryGetDistributedLock(Jedis jedis, String lockKey, String requestId, int expireTime) {
		String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
		if (LOCK_SUCCESS.equals(result)) {
			return true;
		}
		return false;
	}

	/**
	 * 释放分布式锁
	 * 
	 * @param jedis     Redis客户端
	 * @param lockKey   锁
	 * @param requestId 请求标识
	 * @return 是否释放成功
	 */
	public static boolean releaseDistributedLock(Jedis jedis, String lockKey, String requestId) {

		String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
		Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));

		if (RELEASE_SUCCESS.equals(result)) {
			return true;
		}
		return false;

	}
	
	private static String getIP() {
		String ip = null;
		try {
			 ip = InetAddress.getLocalHost().getHostAddress().toString();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
        return ip;
	}
	
	public static void main(String[] args) {
		JedisLock jedisLock = new JedisLock();
		boolean b = false;
		int i = 0;
		while(!b) {
			b = JedisLock.tryGetDistributedLock(jedisLock.jedis, "test-lock", LOCALHOST_ID, 60 * 1000);
			System.out.println( i++ + " lock:" + b);
		}
		
//		boolean b = JedisLock.releaseDistributedLock(jedisLock.jedis, "test-lock", LOCALHOST_ID);
//		System.out.println("release:" + b);
	}
	
	 private Jedis jedis;//非切片额客户端连接
	    private JedisPool jedisPool;//非切片连接池
	    private ShardedJedis shardedJedis;//切片额客户端连接
	    private ShardedJedisPool shardedJedisPool;//切片连接池
	    
	    public JedisLock() 
	    { 
	        initialPool(); 
	        initialShardedPool(); 
	        shardedJedis = shardedJedisPool.getResource(); 
	        jedis = jedisPool.getResource(); 
	        
	        
	    } 
	 
	    /**
	     * 初始化非切片池
	     */
	    private void initialPool() 
	    { 
	        // 池基本配置 
	        JedisPoolConfig config = new JedisPoolConfig(); 
	        config.setMaxIdle(5); 
	        config.setTestOnBorrow(false); 
	        
	        jedisPool = new JedisPool(config,"127.0.0.1",6379, 100000, "123456");
	    }
	    
	    /** 
	     * 初始化切片池 
	     */ 
	    private void initialShardedPool() 
	    { 
	        // 池基本配置 
	        JedisPoolConfig config = new JedisPoolConfig(); 
	        config.setMaxIdle(5); 
	        config.setTestOnBorrow(false); 
	        // slave链接 
	        List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>(); 
	        shards.add(new JedisShardInfo("127.0.0.1", 6379, "master")); 

	        // 构造池 
	        shardedJedisPool = new ShardedJedisPool(config, shards); 
	    } 
}

package com.sp.framework.redis.manager;

import java.util.List;
import java.util.Map;
import java.util.Set;

	/**
	 * cahce接口,在底层封装了jedis的api
	 *
	 * 业务方法的缓存
	 * 	1.缓存读取步骤：
	 * 		*先通过key以及field查找缓存
	 * 		*如果查不到，则查询数据源(db,solr,file等) ；如果查到，直接返回缓存数据
	 * 		*将数据源的数据保存到缓存中，并返回
	 *
	 * 	2.无参数方法的缓存：
	 * 		直接使用key/value来保存缓存
	 *
	 * 	3.带参数方法的缓存：
	 * 		*使用redis的map结构来保存缓存数据(这边有专门的方法如setMapObject)
	 * 		*使用key定位方法
	 * 		*field为参数的组合，在方法内能唯一指向数据，根据需要自己将参数拼装为field,如简单类型：p1-v1-p2-v2;如列表：v1-v2-v3;对象f1-f2-f3
	 * 		*无参数时，field为默认值default-field;
	 *
	 *  4.缓存方法的嵌套：
	 *  	*原则上允许嵌套使用，但不建议嵌套及过多的层级嵌套
	 *  	*最内层的缓存方法表示为方法体内不会有其它读取缓存的方法，方法名称没有专门的标识
	 *  	*外层的缓存方法需要设置过期时间,默认为一小时
	 *
	 *  5.缓存的更新：
	 *  	*最内层的缓存方法,定义一个对应的更新缓存的方法,当数据发生改变时，调用此方法更新。并根据需要决定是否调用外层缓存方法对应的删除缓存方法
	 *  	*对于拥有复杂参数的方法，或是无法通过更新某个对象就能够指定方法参数的情况，不建议定义缓存的更新方法，应该直接删除
	 *  	*如：某方法参数是多个对象拼成的，但我只是更新了某个对象的数据，完全不清楚其它对象应该传哪些参数
	 *
	 *
	 *  6.缓存的删除:
	 *  	*外层的缓存方法，定义一个对应的删除缓存的方法,当数据发生改变时，可以根据需求来进行调用(实时性要求高，不推荐)
	 *  	*对于无参数的缓存方法，对应的删除缓存方法直接通过key来进行删除
	 *  	*对于带参数的缓存方法，对应的删除缓存方法需要两种情况：通过key来删除整个方法的缓存数据;传递通过key与参数组合来删除对应参数的缓存数据(一般不建议)
	 *
	 *  7.哪些数据可以被缓存：
	 *  	*原则上只读的业务方法都可以加上缓存
	 *  	*推荐是访问量大，但修改少的数据
	 *  	*绝对不允许将写的次数多于读的次数的数据加入缓存
	 *
	 *  8.方法命名规则
	 *  	*读取缓存的方法必须以fromCache结尾
	 *  	*更新缓存的方法以refreshCache结尾
	 *  	*删除缓存的方法以removeCache结尾
	 *  	*它们是成对的存在，除了后辍，应该一致(有时候不一定会有更新缓存的方法)
	 *
	 *  9.key的定义原则
	 *  	*key必须定义到CacheKeyConstant中,不允许写死在方法中
	 *  	*key的规则为MC_[package]_[className]_[methodName]
	 *  	*MC表示方法缓存
	 *  	*package为全包名
	 *  	*classname为接口的类名
	 *  	*methodName为方法名
	 *
	 *
	 * 临时存储的应用(待收集较齐全后再来完善)
	 * 	1.限制用户单位时间内的操作次数，如登录密码错十次，淘宝就暂时不让此人登录了
	 * 	2.暂时存储一些统计数据，如点击量，高并发时参与活动等
	 * 	3.最新上架商品
	 *
	 *
	 * 不同数据结构的应用场景：
	 * 	1.key/value：将查询出的单个对象或是列表，或是复杂map保存到对象中，这里的数据都是整存整取
	 *  2.队列：需要单个添加，列表查询的数据(零存整取的列表)，如最新上架商品top10,后台每修改一个商品加到队首，读取时则为最新10条等
	 *  3.map:一般是单个对象放到map中，有单个字段属性读取的需求(零取零取，或零存整取),带参的方法使用此结构来存储
	 *  4.set:和队列类似，但支持去重，却是无序的
	 *  5.Sorted set：与set比起来就是多了一个顺序
	 * @author alexlu
	 *
	 */
	public interface CacheManager {
	
	/**
	 * 按照默认的方式生成map的field
	 * 其中的obj最后都会调用toString()方法
	 * @param obj
	 * @return
	 */
	String generateMapFieldByDefault(Object... obj);

	/**
	 * 保存一个对象
	 * 方法中会序列化对象成字符串
	 * @param key
	 * @param obj
	 */
	<T> void setObject(String key,T obj);
	
	/**
	 * 保存一个对象
	 * 方法中会序列化对象成字符串
	 * @param key
	 * @param t
	 * @param expireSeconds 过期时间
	 */
	<T> void setObject(String key,T t,Integer expireSeconds);
	
	/**
	 * 获取一个对象
	 * 反序列化字符串
	 * @param key
	 * @return 对象
	 */
	<T> T getObject(String key);
	
	
	
	/**
	 * 保存字符串到缓存中
	 * @param key 
	 * @param value 字符串值
	 * @param expireSeconds 过期时间(秒)
	 */
	void setValue(String key,String value,Integer expireSeconds);
	
	/**
	 * 保存字符串到缓存中(没有过期时间)
	 * @param key
	 * @param value
	 */
	void setValue(String key,String value);
	
	
	/**
	 * 通过绝对的key路径保存字符串到缓存中，不会加上keyStart
	 * @param key
	 * @param value 字符串值
	 * @return
	 */
	void setValueByFullKey(String key,String value);
	
	/**
	 * 通过绝对的key路径保存字符串到缓存中，不会加上keyStart
	 * @param key
	 * @param value 字符串值
	 * @param expireSeconds 过期时间(秒)
	 * @return
	 */
	void setValueByFullKey(String key,String value,Integer expireSeconds);
	
	
	/**
	 * 数字递减1
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行
	 * @param key
	 * @param count
	 */
	long decrBy(String key,Integer count);
	
	
	/**
	 * 数字递减
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行
	 * @param key
	 */
	long decr(String key);

	/**
	 * 数字递增count数
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行
	 * @param key
	 * @param count 递增多少
	 */
	long incrBy(String key,Integer count);
	
	/**
	 * 数字递增1
	 * 如果 key 不存在，那么 key 的值会先被初始化为 0 ，然后再执行
	 * @param key
	 */
	long incr(String key);
	
	/**
	 * 通过key移除数据
	 * @param key
	 * @return
	 */
	Long remove(String key);
	
	/**
	 * 通过key获取数据
	 * @param key
	 * @return
	 */
	String getValue(String key);
	
	/**
	 * 通过绝对的key路径获取数据，不会加上keyStart
	 * @param key
	 * @return
	 */
	String getValueByFullKey(String key);
	
	
	/**
	 * 通过key以及字段名称，保存字符到一个map中
	 * 使用时通过key与field来进行获取
	 * @param key
	 * @param field
	 * @param value
	 */
	void setMapValue(String key,String field,String value,int seconds);
	
	
	/**
	 * 通过key以及map的field来获取具体的值
	 * @param key
	 * @param field
	 * @return
	 */
	String getMapValue(String key,String field);
	
	
	/**
	 * 删除某个key的map对应field的值
	 * @param key
	 * @param field
	 * @return
	 */
	void removeMapValue(String key,String field);
	
	
	/**
	 * 通过key以及字段名称，保存对象到一个map中
	 * 使用时通过key与field来进行获取
	 * @param key
	 * @param field
	 * @param seconds 有效期时间
	 */
	<T> void setMapObject(String key,String field,T t,int seconds);
	
	/**
	 * 通过key以及map的field来获取具体的对象值
	 * @param key
	 * @param field
	 * @return
	 */
	<T> T getMapObject(String key,String field);
	
	/**
	 * 通过key获取整个map
	 * 获取的对应key的value是一个序列化的字符串，需要调用 convertMapValue()方法来提取最终的值
	 * @param key
	 * @return
	 */
	Map<String,String> getAllMap(String key);
	
	/**
	 * 将当前字符串数组插入到队列的头部
	 * @param key
	 * @param values 字符串数组
	 */
	void pushToListHead(String key,String[] values);
	
	/**
	 * 将当前字符串插入到队列的头部
	 * @param key
	 * @param value
	 */
	void pushToListHead(String key,String value);
	
	/**
	 * 将当前字符串数组插入到队列的尾部
	 * @param key
	 * @param values
	 */
	void pushToListFooter(String key,String[] values);
	
	/**
	 * 将当前字符串插入到队列的尾部
	 * @param key
	 * @param value
	 */
	void pushToListFooter(String key,String value);
	
	/**
	 * 取出并返回队列头部的数据元素
	 * 会在队列中删除返回的元素
	 * @param key
	 * @return
	 */
	String popListHead(String key);
	
	/**
	 * 取出并返回队到尾部的数据元素
	 * 会在队列中删除返回的元素
	 * @param key
	 * @return
	 */
	String popListFooter(String key);
	
	/**
	 * 返回某个index的队列元素
	 * @param key
	 * @param index
	 * @return
	 */
	String findListItem(String key,long index);
	
	/**
	 * 获取队列数据，通过start以及end,如果想获取全部数据，start填写0,end填写maxLong的值
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	List<String> findLists(String key,long start,long end);
	
	/**
	 * 返回队列中元素数量
	 * @param key
	 * @return
	 */
	long listLen(String key);
	
	/**
	 * 添加字符串数组到set中
	 * @param key
	 * @param values
	 */
	void addSet(String key,String[] values);
	
	/**
	 * 从set中返回一个元素
	 * @param key
	 * @return
	 */
	String popSet(String key);
	
	/**
	 * set中是否已存在某个值
	 * @param key
	 * @param member
	 * @return
	 */
	boolean existsInSet(String key,String member);
	
	/**
	 * 获取整个set
	 * @param key
	 * @return
	 */
	Set<String> findSetAll(String key);
	
	/**
	 * 获取set的数量
	 * @param key
	 * @return
	 */
	long findSetCount(String key);
	
	/**
	 * 添加value到一个sortset中
	 * @param key
	 * @param value
	 * @param sortNo
	 */
	void addSortSet(String key,String value,long sortNo);
	
	/**
	 * 获取sortSet中sortNo为start到end的数据
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	Set<String> findSortSets(String key,long start,long end);
	
	/**
	 * 获取sortSet的数量
	 * @param key
	 * @return
	 */
	long findSortSetCount(String key);
	
	/**
	 * 获取sortSet数量，通过sortNo的min到max进行筛选定位
	 * @param key
	 * @param min
	 * @param max
	 * @return
	 */
	long findSortSetCount(String key,long min,long max);
	
	
	/**
	 * 获取keys的列表
	 * 只可以使用字符串*,不可以只有*，注意，这是防止返回过多KEY，影响性能
	 * @param keyPattern
	 * @return
	 */
	// remarkd by 林晓咏 on 2019/01/25
	// 这个指令严谨使用
//	List<String> Keys(String keyPattern);
	
	
	/**
	 * 批量删除(按匹配规则，但不允许传递 *，防止性能影响，必须字符加上通配符一起才可)
	 * @param keyPattern
	 * @return
	 */
	boolean removeKeys(String keyPattern);
	
	
	/**
	 * Map的对象使用了一个CacheExpiredCommand对象，并做了序列化
	 * 所以使用getAllMap时无法获取正确的内容
	 * @param mapValueStr
	 * @return
	 */
	String convertMapValue(String mapValueStr);
	
		
	/**
	 * 设置某个key的过期时间
	 * @param key
	 * @param expireSeconds
	 */
	void expire(String key,Integer expireSeconds);

	/**
	 * 尝试获取锁并持有等待超时时间
	 *
	 * @param key
	 * @param expire
	 * @param timeout
	 * @return
	 */
	boolean tryLock(String key, int expire, long timeout);

	/**
	 * 尝试获取锁但不持有等待超时时间
	 * 不论获取成功失败则即可返回结果
	 *
	 * @param key
	 * @param expire
	 * @return
	 */
	boolean tryLock(String key, int expire);

	/**
	 * @param key
	 * @return
	 */
	boolean releaseLock(String key);

	/**
	 *
	 * @param key
	 * @param members
	 * @return
	 */
	Long remSet(String key, String... members);

	/**
	 *
	 * @param key
	 * @return
	 */
	Long getExpire(String key);

	/**
	 * @param key
	 * @param value
	 * @param mills
	 */
	void setValue(String key, String value, Long mills);

	/**
	 * 根据多个key获取获取对应顺序的多个值
	 *
	 * @param keys
	 * @return
	 */
	List<String> getValues(String... keys);

	/**
	 * 相当于setNX操作
	 *
	 * @param key
	 * @param value
	 * @return
	 */
	boolean setAndLock(String key, String value);
}

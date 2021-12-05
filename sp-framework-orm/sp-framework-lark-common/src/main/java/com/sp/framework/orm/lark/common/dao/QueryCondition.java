package com.sp.framework.orm.lark.common.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用条件类
 * @Since:2016年2月18日 下午4:40:09
 * @Copyright:Copyright (c) 2016
 * @Version:1.0
 */
public class QueryCondition implements Serializable{
	private static final long serialVersionUID = -1817422219749819592L;

    /**
     * @Title likeEqualsMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:09:25
     * 含义 模糊等于
     */
    private Map<String, Object> likeEqualsMap;

    /**
     * @Title equalsMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:09:34
     * 含义 等于
     */
    private Map<String, Object> equalsMap;

    /**
     * @Title notEqualsMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:09:40
     * 含义 不等于
     */
    private Map<String, Object> notEqualsMap;

    /**
     * @Title greateMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:09:46
     * 含义 大于
     */
    private Map<String, Object> greateMap;

    /**
     * @Title greateEqualsMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:09:52
     * 含义 大于等于
     */
    private Map<String, Object> greateEqualsMap;

    /**
     * @Title lessMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:09:58
     * 含义 小于
     */
    private Map<String, Object> lessMap;

    /**
     * @Title lessEqualsMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:10:05
     * 含义 小于等于
     */
    private Map<String, Object> lessEqualsMap;

    /**
     * @Title nullMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:10:10
     * 含义 值为空
     */
    private Map<String, Object> nullMap;

    /**
     * @Title notNullMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:10:16
     * 含义 值非空
     */
    private Map<String, Object> notNullMap;

    /**
     * @Title inMap
     * @type Map<String,List<Object>>
     * @date 2013-8-6 上午10:10:22
     * 含义 等于列表中的某个值
     */
    private Map<String, List<Object>> inMap;

    /**
     * @Title notInMap
     * @type Map<String,List<Object>>
     * @date 2013-8-6 上午10:10:29
     * 含义 不等于列表中的任意一个值
     */
    private Map<String, List<Object>> notInMap;

    /**
     * @Title betweenInMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:10:35
     * 含义 大于等于某个值并且小于等于另外一个值
     */
    private Map<String, Object> betweenInMap;

    /**
     * @Title notBetweenInMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:10:44
     * 含义 小于开始值或者大于结束值
     */
    private Map<String, Object> notBetweenInMap;

    /**
     * @Title sortBy
     * @type List<Sort>
     * @date 2013-8-6 上午10:11:09
     * 含义 排序
     */
    private List<Sort> sortBy;
    
    /**
     * @Title batchUpdateMap
     * @type Map<String,Object>
     * @date 2013-8-6 上午10:11:32
     * 含义 批量编辑数据库
     */
    private Map<String, Object> batchUpdateMap;
    
	public QueryCondition(){
	}
    
    /**
     *likeEqualsMap的获取.
     * 
     * @return likeEqualsMap值返回.
     */
    public Map<String, Object> getLikeEqualsMap() {
        return likeEqualsMap;
    }

    /**
     *equalsMap的获取.
     * 
     * @return equalsMap值返回.
     */
    public Map<String, Object> getEqualsMap() {
        return equalsMap;
    }

    /**
     *notEqualsMap的获取.
     * 
     * @return notEqualsMap值返回.
     */
    public Map<String, Object> getNotEqualsMap() {
        return notEqualsMap;
    }

    /**
     *greateMap的获取.
     * 
     * @return greateMap值返回.
     */
    public Map<String, Object> getGreateMap() {
        return greateMap;
    }

    /**
     *greateEqualsMap的获取.
     * 
     * @return greateEqualsMap值返回.
     */
    public Map<String, Object> getGreateEqualsMap() {
        return greateEqualsMap;
    }

    /**
     *lessMap的获取.
     * 
     * @return lessMap值返回.
     */
    public Map<String, Object> getLessMap() {
        return lessMap;
    }

    /**
     *lessEqualsMap的获取.
     * 
     * @return lessEqualsMap值返回.
     */
    public Map<String, Object> getLessEqualsMap() {
        return lessEqualsMap;
    }

    /**
     *nullMap的获取.
     * 
     * @return nullMap值返回.
     */
    public Map<String, Object> getNullMap() {
        return nullMap;
    }

    /**
     *notNullMap的获取.
     * 
     * @return notNullMap值返回.
     */
    public Map<String, Object> getNotNullMap() {
        return notNullMap;
    }

    /**
     *inMap的获取.
     * 
     * @return inMap值返回.
     */
    public Map<String, List<Object>> getInMap() {
        return inMap;
    }

    /**
     *notInMap的获取.
     * 
     * @return notInMap值返回.
     */
    public Map<String, List<Object>> getNotInMap() {
        return notInMap;
    }

    /**
     *betweenInMap的获取.
     * 
     * @return betweenInMap值返回.
     */
    public Map<String, Object> getBetweenInMap() {
        return betweenInMap;
    }

    /**
     *notBetweenInMap的获取.
     * 
     * @return notBetweenInMap值返回.
     */
    public Map<String, Object> getNotBetweenInMap() {
        return notBetweenInMap;
    }

    /**
     *batchUpdateMap的获取.
     *@return batchUpdateMap值返回.
     */
    public Map<String, Object> getBatchUpdateMap() {
        return batchUpdateMap;
    }

    // ///////////////////////////////////////////////////////
    /**
     * @Method checkMap方法.<br>
     * @Description 判断Map对象是否已经实例化
     * @author 胡久洲
     * @date 2015年9月12日 下午12:59:42
     * @param map 被判断的Map对象
     * @return 已经实例化的Map对象
     * @return
     */
    private Map<String, Object> checkMap(Map<String, Object> map) {
        if(map == null) {
            return new HashMap<String, Object>(5);
        }
        return map;
    }
 

    // ////////////////////////////////////////////////
    /**
     * @Description 模糊等于
     * @author 胡久洲
     * @date 2013-8-6 上午10:12:15
     * @param key 	属性名称
     * @param value	值
     */
    public void put(String key, Object value) {
        likeEqualsMap = checkMap(likeEqualsMap);
        likeEqualsMap.put(key, value);
    }

    /**
     * @Description 等于
     * @author 胡久洲
     * @date 2013-8-6 上午10:12:41
     * @param key	属性名称
     * @param value	值
     */
    public void equals(String key, Object value) {
        equalsMap = checkMap(equalsMap);
        equalsMap.put(key, value);
    }

    /**
     * @Description 不等于
     * @author 胡久洲
     * @date 2013-8-6 上午10:12:58
     * @param key	属性名称
     * @param value	值
     */
    public void notEquals(String key, Object value) {
        notEqualsMap = checkMap(notEqualsMap);
        notEqualsMap.put(key, value);
    }

    /**
     * @Description 大于
     * @author 胡久洲
     * @date 2013-8-6 上午10:13:10
     * @param key	属性名称
     * @param value	值
     */
    public void greate(String key, Object value) {
        greateMap = checkMap(greateMap);
        greateMap.put(key, value);
    }

    /**
     * @Description 大于等于
     * @author 胡久洲
     * @date 2013-8-6 上午10:13:22
     * @param key	属性名称
     * @param value	值
     */
    public void greateEquals(String key, Object value) {
        greateEqualsMap = checkMap(greateEqualsMap);
        greateEqualsMap.put(key, value);
    }

    /**
     * @Description 小于
     * @author 胡久洲
     * @date 2013-8-6 上午10:13:34
     * @param key	属性名称
     * @param value	值
     */
    public void less(String key, Object value) {
        lessMap = checkMap(lessMap);
        lessMap.put(key, value);
    }

    /**
     * @Description 小于等于
     * @author 胡久洲
     * @date 2013-8-6 上午10:13:46
     * @param key	属性名称
     * @param value	值
     */
    public void lessEquals(String key, Object value) {
        lessEqualsMap = checkMap(lessEqualsMap);
        lessEqualsMap.put(key, value);
    }

    /**
     * @Description 值为空
     * @author 胡久洲
     * @date 2013-8-6 上午10:13:58
     * @param key	属性名称
     */
    public void isNull(String key) {
        nullMap = checkMap(nullMap);
        nullMap.put(key, key);
    }

    /**
     * @Description 值非空
     * @author 胡久洲
     * @date 2013-8-6 上午10:14:10
     * @param key	属性名称
     */
    public void isNotNull(String key) {
        notNullMap = checkMap(notNullMap);
        notNullMap.put(key, key);
    }

    /**
     * @Description 等于列表中的某个值
     * @author 胡久洲
     * @date 2013-8-6 上午10:14:21
     * @param key	属性名称
     * @param value	值
     */
    public void in(String key, List<Object> value) {
        if(null==inMap){
            inMap=new HashMap<String, List<Object>>(5);
        }
        inMap.put(key, value);
    }
    /**
     * @Description 等于列表中的某个值
     * @author 胡久洲
     * @date 2013-8-6 上午10:14:39
     * @param key	属性名称
     * @param value	值
     */
    public void in(String key, Object... value) {
        if(null==inMap){
            inMap=new HashMap<String, List<Object>>(5);
        }
        List<Object> list=new ArrayList<Object>();
        if(value!=null&&value.length>0){
            for(int i=0;i<value.length;i++){
                list.add(value[i]);
            }
        }
        inMap.put(key, list);
    }

    /**
     * @Description 不等于列表中的任意一个值
     * @author 胡久洲
     * @date 2013-8-6 上午10:15:10
     * @param key	属性名称
     * @param value	值
     */
    public void notIn(String key, List<Object> value) {
        if(null==notInMap){
            notInMap = new HashMap<String, List<Object>>(5);
        }
        notInMap.put(key, value);
    }
    
    /**
     * @Description 等于列表中的某个值
     * @author 胡久洲
     * @date 2013-8-6 上午10:14:39
     * @param key	属性名称
     * @param value	值
     */
    public void notIn(String key, Object... value) {
        if(null==notInMap){
        	notInMap = new HashMap<String, List<Object>>(5);
        }
        List<Object> list = new ArrayList<Object>();
        if(value!=null&&value.length>0){
            for(int i=0;i<value.length;i++){
                list.add(value[i]);
            }
        }
        notInMap.put(key, list);
    }

    /**
     * @Description 大于等于某个值并且小于等于另外一个值
     * @author 胡久洲
     * @date 2013-8-6 上午10:15:27
     * @param key		属性名称
     * @param beginValue
     * @param endValue
     */
    public void between(String key, Object beginValue,Object endValue) {
        betweenInMap = checkMap(betweenInMap);
        betweenInMap.put(key, beginValue+"_"+endValue);
    }

    /**
     * @Description 小于开始值或者大于结束值
     * @author 胡久洲
     * @date 2013-8-6 上午10:15:46
     * @param key
     * @param beginValue
     * @param endValue
     */
    public void notBetween(String key, Object beginValue,Object endValue) {
        notBetweenInMap = checkMap(notBetweenInMap);
        notBetweenInMap.put(key, beginValue+"_"+endValue);
    }

    /**
     * @Description 小于开始值或者大于结束值
     * @author 胡久洲
     * @date 2013-8-6 上午10:15:53
     * @param key
     * @param value
     */
    public void batchUpdate(String key, Object value) {
        batchUpdateMap = checkMap(batchUpdateMap);
        batchUpdateMap.put(key, value);
    }
    
    
    /**
     * 排序
     * @Title: order
     * @author hjz
     * @param 设定文件
     * @return 返回类型
     */
    private void order(String field, String type) {
        Sort oe = new Sort(field, type);
        if(sortBy == null) {
        	sortBy = new ArrayList<Sort>(5);
        }
        if(sortBy.contains(oe)) {
        	sortBy.remove(oe);
        }
        sortBy.add(sortBy.size(), oe);
    }

    /**
     * @Description 升序
     * @author 胡久洲
     * @date 2013-8-6 上午10:16:07
     * @param key
     */
    public void orderAsc(String key) {
        order(key, Sort.ASC);
    }

    /**
     * @Description 降序
     * @author 胡久洲
     * @date 2013-8-6 上午10:16:17
     * @param key
     */
    public void orderDesc(String key) {
        order(key, Sort.DESC);
    }

	/**
	 * 设定likeEqualsMap的值.
	 * @param Map<String,Object>
	 */
	public void setLikeEqualsMap(Map<String, Object> likeEqualsMap) {
		this.likeEqualsMap = likeEqualsMap;
	}

	/**
	 * 设定equalsMap的值.
	 * @param Map<String,Object>
	 */
	public void setEqualsMap(Map<String, Object> equalsMap) {
		this.equalsMap = equalsMap;
	}

	/**
	 * 设定notEqualsMap的值.
	 * @param Map<String,Object>
	 */
	public void setNotEqualsMap(Map<String, Object> notEqualsMap) {
		this.notEqualsMap = notEqualsMap;
	}

	/**
	 * 设定greateMap的值.
	 * @param Map<String,Object>
	 */
	public void setGreateMap(Map<String, Object> greateMap) {
		this.greateMap = greateMap;
	}

	/**
	 * 设定greateEqualsMap的值.
	 * @param Map<String,Object>
	 */
	public void setGreateEqualsMap(Map<String, Object> greateEqualsMap) {
		this.greateEqualsMap = greateEqualsMap;
	}

	/**
	 * 设定lessMap的值.
	 * @param Map<String,Object>
	 */
	public void setLessMap(Map<String, Object> lessMap) {
		this.lessMap = lessMap;
	}

	/**
	 * 设定lessEqualsMap的值.
	 * @param Map<String,Object>
	 */
	public void setLessEqualsMap(Map<String, Object> lessEqualsMap) {
		this.lessEqualsMap = lessEqualsMap;
	}

	/**
	 * 设定nullMap的值.
	 * @param Map<String,Object>
	 */
	public void setNullMap(Map<String, Object> nullMap) {
		this.nullMap = nullMap;
	}

	/**
	 * 设定notNullMap的值.
	 * @param Map<String,Object>
	 */
	public void setNotNullMap(Map<String, Object> notNullMap) {
		this.notNullMap = notNullMap;
	}

	/**
	 * 设定inMap的值.
	 * @param Map<String,List<Object>>
	 */
	public void setInMap(Map<String, List<Object>> inMap) {
		this.inMap = inMap;
	}

	/**
	 * 设定notInMap的值.
	 * @param Map<String,List<Object>>
	 */
	public void setNotInMap(Map<String, List<Object>> notInMap) {
		this.notInMap = notInMap;
	}

	/**
	 * 设定betweenInMap的值.
	 * @param Map<String,Object>
	 */
	public void setBetweenInMap(Map<String, Object> betweenInMap) {
		this.betweenInMap = betweenInMap;
	}

	/**
	 * 设定notBetweenInMap的值.
	 * @param Map<String,Object>
	 */
	public void setNotBetweenInMap(Map<String, Object> notBetweenInMap) {
		this.notBetweenInMap = notBetweenInMap;
	}
	/**
	 * 设定batchUpdateMap的值.
	 * @param Map<String,Object>
	 */
	public void setBatchUpdateMap(Map<String, Object> batchUpdateMap) {
		this.batchUpdateMap = batchUpdateMap;
	}

	/**
	 * sortBy的获取.
	 * @return List<Sort>
	 */
	public List<Sort> getSortBy() {
		return sortBy;
	}

	/**
	 * 设定sortBy的值.
	 * @param List<Sort>
	 */
	public void setSortBy(List<Sort> sortBy) {
		this.sortBy = sortBy;
	}
}

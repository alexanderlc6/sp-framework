/**
 * Copyright (c) 2012 Yonghui All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Yonghui.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Yonghui.
 *
 * YONGHUI MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. YONGHUI SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.sp.framework.utilities;

import java.util.ArrayList;
import java.util.List;

public class CollectionUtil {

	/**
	 * 判断数组中是否存在该元素
	 * @param arr
	 * @param obj
	 * @return
	 */
	public static boolean contains(Object arr, Object obj){
		//TODO
		return false;
	}
	
	/**
	 * 在数组中查找元素，找到则返回对应位置，没找到返回-1
	 * @param arr
	 * @param obj
	 * @return
	 */
	public static int indexOf(Object arr, Object obj){
		//TODO
		return -1;
	}
	/**
     * Discription:笛卡尔乘积算法
     * 把一个List{[1,2],[3,4],[a,b]}转化成List{[1,3,a],[1,3,b],[1,4,a],[1,4,b],[2,3,a],[2,3,b],[2,4,a],[2,4,b]}数组输出
     * 
     * @param dimvalue原List
     * @param result通过乘积转化后的数组
     * @param layer中间参数
     * @param curList中间参数
     */
	public static void descartes(List<List<String>> dimvalue, List<List<String>> result, int layer, List<String> curList) {
        if (layer < dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
            	CollectionUtil.descartes(dimvalue, result, layer + 1, curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    CollectionUtil.descartes(dimvalue, result, layer + 1, list);
                }
            }
        } else if (layer == dimvalue.size() - 1) {
            if (dimvalue.get(layer).size() == 0) {
                result.add(curList);
            } else {
                for (int i = 0; i < dimvalue.get(layer).size(); i++) {
                    List<String> list = new ArrayList<String>(curList);
                    list.add(dimvalue.get(layer).get(i));
                    result.add(list);
                }
            }
        }
    }
}

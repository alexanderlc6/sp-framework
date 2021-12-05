/**
 * Copyright (c) 2011-2014, hubin (243194995@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.sp.framework.common.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Ajax响应帮助类
 * <p>
 * @author   hubin
 * @date	 2015年3月25日
 * @version  1.0.0
 */
public class AjaxHelper {

	/**
	 * @Description 响应JSON内容字符串
	 * @param response
	 * @param object
	 * @throws IOException
	 */
	public static void jsonPrint(HttpServletResponse response, Object object, String charset ) throws IOException {
		//outPrint(response, JacksonUtil.useDefaultMapper().toJson(object),charset);
	}


	/**
	 * @Description 响应Ajax请求
	 * @param response
	 * @param content
	 *            响应内容
	 * @param charset
	 *            字符编码
	 * @throws IOException
	 */
	public static void outPrint(HttpServletResponse response, String content, String charset ) throws IOException {
		response.setContentType("application/json;charset=" + charset);
		PrintWriter out = response.getWriter();
		out.print(content);
		out.flush();
	}

}

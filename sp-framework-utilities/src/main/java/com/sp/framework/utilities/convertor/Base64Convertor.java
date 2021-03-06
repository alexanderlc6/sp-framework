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
package com.sp.framework.utilities.convertor;

import java.io.IOException;

import net.iharder.Base64;

public class Base64Convertor implements Convertor<byte[]> {

	@Override
	public byte[] parse(String strValue) {
		try {
			return Base64.decode(strValue);
		} catch (IOException e) {		
			e.printStackTrace();
			throw new RuntimeException("Decode with Base64 error.");
		}
	}

	@Override
	public String format(byte[] value) {
		return Base64.encodeBytes(value);
	}

}

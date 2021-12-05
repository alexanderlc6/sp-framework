/**
 * Copyright (c) 2010 Jumbomart All Rights Reserved.
 *
 * This software is the confidential and proprietary information of Jumbomart.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Jumbo.
 *
 * JUMBOMART MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. JUMBOMART SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 *
 */
package com.sp.framework.utilities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;

import javax.imageio.ImageIO;

public class SecurityUtil{

	/*
	 * public static Color[] background = new Color[]{new Color(245, 245, 245), new Color(220, 220, 220) , new Color(211, 211, 211)};
	 */
	public static Color[]	background	= new Color[] { new Color(132, 132, 132) };

	// public static Color[] words = new Color[]{new Color(148, 0, 211), new
	// Color(153, 50, 204)
	// , new Color(75, 0, 130), new Color(138, 43, 226), new Color(147, 112,
	// 219), new Color(123, 104, 238), new Color(106, 90, 205)};
	public static Color[]	words		= new Color[] { new Color(255, 255, 255) };

	public static Color[]	line		= words;

	public static Random	random		= new Random();

	public static void generateSecurityImage(OutputStream os,boolean needConfuse,String sRand,int numLength) throws IOException{
		// 在内存中创建图象
		int width = numLength * 20, height = 25;
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		// 获取图形上下文
		Graphics g = image.getGraphics();
		// 生成随机类
		// Random random = new Random();
		// 设定背景色
		g.setColor(getRandColor(background));
		g.fillRect(0, 0, width, height + 10);
		// 设定字体
		g.setFont(new Font("Arial", Font.PLAIN, 18));
		// 画边框
		g.setColor(Color.white);
		g.drawRect(0, 0, width - 1, height - 1);
		// 随机产生条干扰线，使图象中的认证码不易被其它程序探测到
		if (needConfuse){
			g.setColor(getRandColor(line));
			for (int i = 0; i < 2; i++){
				int x = random.nextInt(width);
				int y = random.nextInt(height);
				int xl = random.nextInt(12);
				int yl = random.nextInt(12);
				g.drawLine(x, y, xl, yl);
			}
		}
		char[] chars = sRand.toCharArray();
		for (int i = 0; i < chars.length; i++){
			// 将认证码显示到图象中
			int drawY = random.nextInt(6);
			g.setColor(getRandColor(words));
			g.drawString(String.valueOf(chars[i]), 20 * i + 5, 14 + drawY);
		}
		ImageIO.write(image, "JPEG", os);
	}

	private static Color getRandColor(Color[] colorRanges){
		int j = (int) (Math.random() * colorRanges.length);
		return colorRanges[j];
	}
}

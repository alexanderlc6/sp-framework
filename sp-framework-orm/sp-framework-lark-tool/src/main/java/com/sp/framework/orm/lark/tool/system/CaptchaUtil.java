package com.sp.framework.orm.lark.tool.system;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Random;

import javax.imageio.ImageIO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 验证码工具类
 * 
 * 可配置内容：
 * <ul>
 * <li>字体及背景颜色备选集</li>
 * <li>生成内容(纯数字、纯字母、数字+字母、公式、其它内容)</li>
 * <li>生成位数(公式时无效)</li>
 * </ul>
 * 
 * @author alexlu
 * 
 */
public class CaptchaUtil {

	private static final Logger		log							= LoggerFactory
																		.getLogger(CaptchaUtil.class);
	private static String			defaultFontColor			= "FFFFFF";

	private static String			defaultBackgroundColor		= "000000";

	/** 前景色(默认为白色) */
	private static String[]			fontColors					= new String[] { "BC2F1E" };

	/** 背景色(默认为黑色) */
	private static String[]			backgroundColors			= new String[] { "FFFFFF" };

//	/**
//	 * 类型 1:纯数字 2:纯字母 3:数字+字母 4:公式 5:其它内容
//	 * */
//	private static final Integer	TYPE_NUMBER					= 1;
//	/**
//	 * 类型2:纯字母
//	 */
//	private static final Integer	TYPE_LETTER					= 2;
//	/**
//	 * 类型 3:数字+字母
//	 */
//	private static final Integer	TYPE_NUMBER_LETTER			= 3;
//	/**
//	 * 类型4:公式
//	 */
//	private static final Integer	TYPE_FORMULA				= 4;

	//类型 1:纯数字 2:纯字母 3:数字+字母 4:公式 5:其它内容
	public enum Type{
		TYPE_NUMBER, TYPE_LETTER, TYPE_NUMBER_LETTER, TYPE_FORMULA
	}
	
	/**
	 * 位数，几位数
	 */
	private static Integer			digit						= 4;

	private static String[]			nums						= new String[] {
			"1", "2", "3", "4", "5", "6", "7", "8", "9" };

	private static String[]			letters						= new String[] {
			"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "m",
			"n", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y" };

	private static String[]			symbols						= new String[] {
			"+", "-", "*", "/"									};

	/**
	 * 字体大小
	 */
	private static final Float	FONTSIZE					= 42f;

	/**
	 * 字体
	 */
	private static final String		FONTFAMILY					= "Times New Roman";

	private static final Random		random						= new Random();

	private static Font font=null;
	
	static{
		InputStream in = null;
		
		
		try {
			in=ResourceUtil.getResourceAsStream(
					"timesbd.ttf", CaptchaUtil.class);
			//in = new FileInputStream(new File(Thread.currentThread().getContextClassLoader().getResource("").getPath()+"timesbd.ttf"));
			font = Font.createFont(Font.TRUETYPE_FONT, in).deriveFont(FONTSIZE).deriveFont(Font.BOLD);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			font=new Font(null,Font.BOLD,FONTSIZE.intValue());
		}
		
	}
	/**
	 * 获取字体颜色
	 * 
	 * @return
	 */
	private static Color getRandFontColor() {
		return getRandColor(fontColors, defaultFontColor);
	}

	/**
	 * 获取背景颜色
	 * 
	 * @return
	 */
	private static Color getRandBkColor() {
		return getRandColor(backgroundColors, defaultBackgroundColor);
	}

	private static Color getRandColor(String[] colorRanges, String defaultColor) {
		int j = 0;
		String colorStr = "";

		if (colorRanges.length > 0) {
			j = random.nextInt(colorRanges.length);
			;
			colorStr = colorRanges[j];
		} else {
			colorStr = defaultColor;
		}
		Integer r = Integer.parseInt(colorStr.substring(0, 2), 16);

		Integer g = Integer.parseInt(colorStr.substring(2, 4), 16);

		Integer b = Integer.parseInt(colorStr.substring(4), 16);

		return new Color(r, g, b);
	}

	/**
	 * 获得验证码字符串
	 * 
	 * @return
	 */
	private static String getWord(Type type) {
		switch (type) {
			case TYPE_NUMBER:
				// 数字
				return getPureNumVeliCode(random);
			case TYPE_LETTER:
				// 字母
				return getPureLetterVeliCode(random);
			case TYPE_NUMBER_LETTER:
				// 数字+字母
				return getNumAndLetterVeliCode(random);
			case TYPE_FORMULA:
				// 公式
				return getFormulaVeliCode(random);
			default:

				return "";
		}
	}

	/**
	 * 纯数字的验证码
	 * 
	 * @return
	 */
	private static String getPureNumVeliCode(Random random) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digit; i++) {
			int rv = random.nextInt(nums.length);
			sb.append(nums[rv]);
		}
		log.debug("validation code is {}", sb.toString());
		return sb.toString();
	}

	/**
	 * 纯字母的验证码
	 * 
	 * @return
	 */
	private static String getPureLetterVeliCode(Random random) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digit; i++) {
			int rv = random.nextInt(letters.length);
			sb.append(letters[rv]);
		}
		log.debug("validation code is {}", sb.toString());
		return sb.toString();
	}

	/**
	 * 数字和字母都有的验证码
	 * 
	 * @return
	 */
	private static String getNumAndLetterVeliCode(Random random) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < digit; i++) {
			int randomValue = random.nextInt(10);
			if (randomValue % 2 == 1) {
				int rv = random.nextInt(nums.length);
				sb.append(nums[rv]);
			} else {
				int rv = random.nextInt(letters.length);
				sb.append(letters[rv]);
			}
		}
		log.debug("validation code is {}", sb.toString());
		return sb.toString();
	}

	/**
	 * 公式验证码
	 * 
	 * @return
	 */
	private static String getFormulaVeliCode(Random random) {
		StringBuffer sb = new StringBuffer();
		// 符号(+,-,*,/)的下标
		Integer rv = random.nextInt(symbols.length);
		// 第一个数
		BigDecimal b1 = new BigDecimal(getPureNumVeliCode(random));
		// 第二个数
		BigDecimal b2 = new BigDecimal(getPureNumVeliCode(random));
		// 避免出现除数为0的情况
		if (rv == 3) {
			// 如果是除法,那除数必须不能为0，如果为0，再次生成b2
			while (b2.equals(new BigDecimal(0))) {
				b2 = new BigDecimal(getPureNumVeliCode(random));
			}
		} else if (rv == 1) {
			// 避免值为负数的情况
			while (!b1.max(b2).equals(b1)) {
				b1 = new BigDecimal(getPureNumVeliCode(random));
				b2 = new BigDecimal(getPureNumVeliCode(random));
			}
		}
		// 符号(+,-,*,/)
		String symbol = symbols[rv];
		// 计算出公式的值
		BigDecimal value = compute(b1, b2, rv);
		sb.append(b1).append(symbol).append(b2).append("=").append(value);
		log.debug("validation code is {}", sb.toString());
		return sb.toString();
	}

	/**
	 * 公式验证码时的计算
	 * 
	 * @return
	 */
	private static BigDecimal compute(BigDecimal s1, BigDecimal s2, Integer rv) {
		BigDecimal value = null;
		switch (rv) {
			case 1:
				value = s1.subtract(s2);
				return value;
			case 2:
				value = s1.multiply(s2);
				return value;
			case 3:
				value = s1.divide(s2, 0, BigDecimal.ROUND_DOWN);
				return value;
			default:
				value = s1.add(s2);
				return value;
		}
	}

	/**
	 * 存在干扰线的验证码
	 * 
	 * @param os
	 *            :OutputStream
	 * @param rows
	 *            :干扰线的条数(大于0的数字)
	 * @return String
	 * 			  :验证码
	 * @throws Exception
	 */
	public static String drawValidationCodeImage(OutputStream os, int rows, Type type) {

		return drawImage(os, true, rows, type);

	}

	/**
	 * 不存在干扰线的验证码
	 * 
	 * @param os
	 *            :OutputStream
	 * @return String
	 * 			  :验证码
	 * @throws Exception
	 */
	public static String drawValidationCodeImage(OutputStream os, Type type) {

		return drawImage(os, false, 0, type);
	}
	
	
	
	private static Font getFont(){
		//new Font(FONTFAMILY, Font.BOLD, FONTSIZE)
		//变形处理
		AffineTransform fontAT = new AffineTransform();
		Random rand = new Random(System.currentTimeMillis());
		int rotate = rand.nextInt(30);
		fontAT.rotate(rand.nextBoolean() ? Math.toRadians(rotate) : -Math.toRadians(rotate / 2));
	//	fontAT.scale(1, 1);
	//	fontAT.shear(0d,rand.nextBoolean() ? Math.toRadians(rotate) : Math.toRadians(rotate / 2));
		Font cfont=font.deriveFont(fontAT);
		return cfont;
	}

	/**
	 * 画验证码图片
	 * 
	 * @param os
	 * @param needConfuse
	 *            :是否产生条干扰线 (如果为false, rows的参数将无用)
	 * @param rows
	 *            :干扰线的条数
	 * @param sRand
	 *            :内容
	 * @param numLength
	 *            :字符个数 (如果type=4时, 应传入digit*2+3(多的+=?号的位置))
	 * @return String
	 * 			  :验证码
	 * @throws Exception
	 */
	private static String drawImage(OutputStream os, boolean needConfuse, int rows, Type type) {
		String value = "";
		try {
			String sRand = getWord(type);
			if (type.equals(Type.TYPE_FORMULA)) {
				int index = sRand.indexOf("=");
				value = sRand.substring(index + 1);
				sRand = sRand.substring(0, index);				
				sRand += "=?";
			} else {
				value = sRand;
			}

			// 在内存中创建图象
			int width = sRand.length() * 25, height = 50;
			BufferedImage image = new BufferedImage(width, height,
					BufferedImage.TYPE_USHORT_565_RGB);
			// 获取图形上下文
			Graphics g = image.getGraphics();
			// 生成随机类
			Random random = new Random();
			// 设定背景色
			g.setColor(getRandBkColor());
			g.fillRect(0, 0, width, height + 10);
			// 设定字体
			
			// 画边框
			// g.setColor(Color.white);
			// g.drawRect(0, 0, width - 1, hight - 1);
			// 随机产生条干扰线，使图象中的认证码不易被其它程序探测到
			if (needConfuse) {
				g.setColor(getRandFontColor());
				if (rows < 1) {
					throw new Exception("干扰线条数不可以小于1");
				}
				for (int i = 0; i < rows; i++) {
					int x = random.nextInt(width);
					int y = random.nextInt(height);
					int xl = random.nextInt(15);
					int yl = random.nextInt(15);
					g.drawLine(x, y, x+xl, y+yl);
				}
			}
			char[] chars = sRand.toCharArray();
			for (int i = 0; i < chars.length; i++) {
				// 将认证码显示到图象中
				int drawY = random.nextInt(6);
				g.setFont(getFont());
				g.setColor(getRandFontColor());
				g.drawString(String.valueOf(chars[i]), 20 * i + 6, 35 + drawY);
			}
			ImageIO.write(image, "JPEG", os);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return value;
	}
}

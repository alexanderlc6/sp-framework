package com.sp.framework.utilities.type;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * 功能：对java中常见的字符串使用的功能进行二次封装，<br>
 * 达到减少字符串使用出错的机会。
 * 创建者: luchao1@yonghui.cn
 * 修改者                   修改时间
 * 
 */
public class StringUtil extends StringUtils{
	
	/**
	 * 禁止实例化
	 */
	protected StringUtil() {
		
	}

    /**
     * 判别字符串是否为null或者没有内容
     * @param inStr 被判断的输入参数
     * @return  布尔值：true=表示输入字符串为null或者没有内容
     *                  false=表示输入字符串不为null或者有内容
     */
    public static boolean zero(String inStr) {
        return ((null == inStr) || (inStr.length() <= 0));
    }

    /**
     * 判断字符串是否为null或没有内容或全部为空格。
     * @param inStr 被判断的输入参数
     * @return  布尔值：true=表示输入字符串为null或没有内容或全部为空格
     *                  false=表示输入字符串不为null或有内容或全部不为空格
     */
    public static boolean empty(String inStr) {
        return (zero(inStr) || (inStr.trim().equals("")));
    }
    
    /**
     * 判断非空字符串
     * @Project pd-framework
     * @Package com.sp.framework.utilities.type
     * @Method isNotBlank方法.<br>
     * @Description TODO(用一句话描述该类做什么)
     * @author Alex Lu
     * @date 2014-3-24 下午2:28:43
     * @param str	源字符串
     * @return	true：非空，false：null或者""
     */
    public static Boolean isNotBlank(Object obj){
		Boolean flag=false;
		if(obj instanceof String){
			if(isNotBlank((String) obj)){
				flag=true;
			}
		}else{
			if(null!=obj){
				flag=true;
			}	
		}
		return flag;
	}
    
    /**
     * 判断空字符串
     * @Project pd-framework
     * @Package com.sp.framework.utilities.type
     * @Method isBlank方法.<br>
     * @Description TODO(用一句话描述该类做什么)
     * @author Alex Lu
     * @date 2014-3-24 下午2:28:43
     * @param str	源字符串
     * @return	true：null或者""，false：非空
     */
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 在str为null或者没有内容的情况下，返回空串；否则返回输入参数。
     * @param inStr 被判断的输入参数
     * @return 字符串="" 表示输入字符串为null或者没有内容
     *          字符串!="" 表示输入字符串有内容
     */
    public static String toZeroSafe(String inStr) {
        if (zero(inStr)) {
            return "";
        }
        return inStr;
    }

    /**
     * 在inStr为null或者没有内容的情况下，返回def；否则返回str
     * @param inStr 被判断的输入参数
     * @param def inStr为null或者没有内容的情况下，需要返回的字符串
     * @return 字符串=def 表示输入字符串为null或者没有内容
     *          字符串=inStr 表示输入字符串有内容
     */
    public static String toZeroSafe(String inStr, String def) {
        if (zero(inStr)) {
            return def;
        }
        return inStr;
    }

    /**
     * 在str为null或者没有内容，或者全部为空格的情况下，返回空串；否则返回str
     * @param inStr  被判断的输入参数
     * @return 字符串="" 表示输入字符串为null或者没有内容或者全部为空格
     *          字符串!="" 表示输入字符串有内容
     */
    public static String toEmptySafe(String inStr) {
        if (empty(inStr)) {
            return "";
        }
        return inStr;
    }

    /**
     * 在str为null或者没有内容，或者全部为空格的情况下，返回def；否则返回str
     * @param inStr 被判断的输入参数
     * @param def inStr为null或者没有内容或者全部为空格的情况下，需要返回的字符串
     * @return 字符串=def 表示输入字符串为null或者没有内容或者全部为空格
     *          字符串=inStr 表示输入字符串有内容
     */
    public static String toEmptySafe(String inStr, String def) {
        if (empty(inStr)) {
            return def;
        }
        return inStr;
    }

    /**
     * 去掉输入字符串首尾空格
     * @param inStr 输入字符串
     * @return 首尾无空格的字符串
     */
    public static String trim(String inStr) {
        if (empty(inStr)) {
            return inStr;
        }
        return inStr.trim();
    }

    /**
     * 判断字符串是否内容相同
     * @param s1  第1个输入字符串
     * @param s2  第2个输入字符串
     * @return 布尔值=true：两个字符串相等
     *                =false:两个字符串不相等
     */
    public static boolean equals(String s1, String s2) {
        if (null == s1) {
            return false;
        }
        return s1.equals(s2);
    }

    /**
     * 判断字符串是否内容相同，不区分大小写
     * @param s1  第1个输入字符串
     * @param s2  第2个输入字符串
     * @return 布尔值=true：两个字符串相等
     *                =false:两个字符串不相等
     */
    public static boolean equalsIgnoreCase(String s1, String s2) {
        if (null == s1) {
            return false;
        }
        return s1.equalsIgnoreCase(s2);
    }

    /**
     * 把字符数组转换成字符串
     * @param array 字符数组
     * @return 转换后的字符串
     */
    public static String toString(char[] array) {
        return String.valueOf(array);
    }
    
    /**
	 * 转换字符串
	 * 
	 * @param obj
	 * @return
	 */
	public static String toString(Object obj) {
		if (obj == null)
			return "";
		else
			return String.valueOf(obj);
	}
	
	/**
	 * 转换字符串数组
	 * 
	 * @param objs
	 * @return
	 */
	public static String[] changeObjectArray(Object objs[]) {
		String strs[] = new String[objs.length];
		System.arraycopy(((Object) (objs)), 0, strs, 0, objs.length);
		return strs;
	}

    /**
     * 在str字符串中，将所有token字符串，用delim字符串进行转义处理。
     * @param str 被替换的字符串
     * @param token 被替换的子字符串
     * @param delim 子字符串需要替换的内容
     * @return 已经替换好的字符串
     */
    public static String normalize(String str, String token, String delim) {
        //为空，直接返回
        if (empty(str)) {
            return "";
        }
        //查找并替换
        StringTokenizer tokenizer = new StringTokenizer(str, token);
        StringBuilder fixedBuilder = new StringBuilder();
        while (tokenizer.hasMoreTokens()) {
            if (fixedBuilder.length() == 0) {
                fixedBuilder.append(tokenizer.nextToken());
            } else {
                fixedBuilder.append(fixedBuilder);
                fixedBuilder.append(delim);
                fixedBuilder.append(token);
                fixedBuilder.append(tokenizer.nextToken());
            }
        }

        if (str.indexOf(delim) == 0) {
            fixedBuilder.append(delim).append(token).append(fixedBuilder);
        }

        if (str.lastIndexOf(delim) == (str.length() - 1)) {
            fixedBuilder.append(fixedBuilder).append(delim).append(token);
        }

        return fixedBuilder.toString();
    }

    /**
     * 在字符串中，用新的字符串替换指定的字符
     * @param src 需要替换的字符串
     * @param charOld 被替换的字符
     * @param strNew  用于替换的字符串
     * @return 已经替换好的字符串
     */
    public static String replace(String src, char charOld, String strNew) {
        if (null == src) {
            return src;
        }
        if (null == strNew) {
            return src;
        }

        StringBuilder buf = new StringBuilder();
        for (int i = 0, n = src.length(); i < n; i++) {
            char c = src.charAt(i);
            if (c == charOld) {
                buf.append(strNew);
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * 在字符对象中，用新的字符串替换指定的字符串
     * @param src 需要替换的字符对象
     * @param strOld 被替换的字符串
     * @param strNew  用于替换的字符串 
     */
    public static void replace(StringBuilder src, String strOld, String strNew) {
        if ((null == src) || (src.length() <= 0)) {
            return;
        }
        String s = replace(src.toString(), strOld, strNew);
        src.setLength(0);
        src.append(s);
    }

    /**
     * 在字符串中，用新的字符串替换指定的字符串
     * @param src 需要替换的字符对象
     * @param strOld 被替换的字符串
     * @param strNew  用于替换的字符串
     * @return 已经被替换的字符串
     */
    public static String replace(String src, String strOld, String strNew) {
        if (null == src) {
            return src;
        }
        if (zero(strOld)) {
            return src;
        }
        if (null == strNew) {
            return src;
        }
        if (equals(strOld, strNew)) {
            return src;
        }
        
        return src.replaceAll(strOld, strNew);
    }

    /**
     * 把字符串的第一个字符变为大写
     * @param s 输入字符串
     * @return 返回第一个字符是大写的字符串
     */
    public static String upperFirst(String s) {
        String str = null;
        if (null != s) {
            if (s.length() == 1) {
                str = s.toUpperCase();
            } else {
                str = s.substring(0, 1).toUpperCase() + s.substring(1);
            }
        }
        return str;
    }

    /**
     * 把字符对象第一个字符变为大写
     * @param sb 字符对象
     */
    public static void upperFirst(StringBuilder sb) {
        if ((null != sb) && (sb.length() > 0)) {
            sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        }
    }

    /**
     * 把字符串的第一个字符变为小写
     * @param s 输入的字符串
     * @return 返回的第一个字符是小写的字符串
     */
    public static String lowerFirst(String s) {
        String str = null;
        if (null != s) {
            if (s.length() == 1) {
                str = s.toLowerCase();
            } else {
                str = s.substring(0, 1).toLowerCase() + s.substring(1);
            }
        }
        return str;
    }

    /**
     * 把字符对象的第一个字符变为小写
     * @param sb 输入的字符对象
     */
    public static void lowerFirst(StringBuilder sb) {
        if ((null != sb) && (sb.length() > 0)) {
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
        }
    }

    /**
     * 根据指定的delima标志获取输入字符串的后缀
     * @param str 输入字符串
     * @param delima 指定的标志,一般是一个字符，如“.”
     * @return 输入字符串子的后缀
     */
    public static String getLastSuffix(String str, String delima) {
        if (zero(delima)) {
            return str;
        }

        String suffix = "";
        if (!zero(str)) {
            int index = str.lastIndexOf(delima);
            if (index >= 0) {
                suffix = str.substring(index + delima.length());
            } else {
                suffix = str;
            }
        }
        return suffix;
    }

    /**
     * 根据指定的delima标志获取输入字符串的前缀
     * @param src 输入字符串
     * @param delima 指定的标志,一般是一个字符，如“.”
     * @return 输入字符串的前缀
     */
    public static String getLastPrefix(String src, String delima) {
        if (zero(delima)) {
            return src;
        }

        String prefix = "";
        if (!zero(src)) {
            int index = src.lastIndexOf(delima);
            if (index >= 0) {
                prefix = src.substring(0, index);
            }
        }
        return prefix;
    }

    /**
     * 将str字符串按照其中出现的delim分割成字符串数组
     * @param str 输入的字符串
     * @param delim 分割标志
     * @return 分割好的数组
     */
    public static String[] split(String str, String delim) {
        if (zero(str) || zero(delim)) {
            return new String[0];
        }
        return str.split(delim);
    }

    /**
     * 将str字符串按照其中出现的delim分割成字符串数组,并能去掉前后空格
     * @param str 输入的字符串
     * @param delim 分割标志
     * @param trim =true 去掉前后空格 =false 不去掉前后空格
     * @return 分割好的数组
     */
    public static String[] split(String str, String delim, boolean trim) {
        String[] set = split(str, delim);
        if (trim) {
            for (int i = 0; i < set.length; i++) {
                set[i] = set[i].trim();
            }
        }
        return set;
    }

    /**
     * 从str字符串的起始位置中，按照words数组中各个元素字符串出现的顺序，去除所有这些元素字符串。
     * 不去分大小写，不考虑whitespace符号。
     * 如果str中不存在这些元素字符串，或者没有按照顺序出现，抛出异常。
     * @param str
     * @param words
     * @return
     * @throws Exception
     */
    public static String removeSequenceHeadingWordsIgnoreCase( String str, String[] words, String delim )
        throws Exception
    {
        if ( empty( str ) || ArrayUtil.empty( words ) )
            return "";

        String[] set = split( str, delim );
        int setIndex = 0;
        for ( int wordIndex = 0; ( setIndex < set.length ) && ( wordIndex < words.length ) ; wordIndex++ ) {
            String s = set[setIndex];
            String w = words[wordIndex];
            if ( empty( w ) )
                continue;

            if ( ! s.trim().equalsIgnoreCase( w.trim() ) )
                throw new Exception( "no word '" + w + "' in the string '" + str + "' of index " + setIndex );

            setIndex ++;
        }
        return join(delim, setIndex, set );
    }

    /**
     * 将set字符串数组从fromIndex开始以后的元素合并成以delim为分割符的字符串
     * @param set
     * @param delim
     * @param fromIndex 以0开始
     * @return
     */
    public static String join(String delim, int fromIndex, String... set)
    {
        if ( ( null == set ) || ( set.length <= 0 ) || ( fromIndex >= set.length ) )
            return "";

        if ( fromIndex < 0 )
            fromIndex = 0;

        StringBuffer sb = new StringBuffer();
        sb.append( set[fromIndex] );
        for( int i = fromIndex+1; i < set.length; i++ ) {
            sb.append( delim );
            sb.append( set[i] );
        }
        return sb.toString();
    }
    
    public static String join(String separator, Object... items) {
		String tmpKey = StringUtil.join(items, separator);
		tmpKey = StringUtil.subStrEndDiffStr(tmpKey, separator);
		tmpKey = StringUtil.subStrStartDiffStr(tmpKey, separator);
		tmpKey = StringUtil.replace(tmpKey, separator + "+", separator);
		return tmpKey;
	}
    
    
    /**
     * 把占位符号进行替换.<br>
     *工程名:cctccati<br>
     *方法名:replaceSpecialChar方法.<br>
     * 
     *@author:luchao1@yonghui.cn
     *@since :1.0:2009-8-8
     *@param replaceContentSrc:被替换的原字符串
     *@param inputPrifx：input输入框的名称前缀
     *@return 替换好的字符串
     */
    public static StringBuilder replaceSpecialChar(String replaceContentSrc,
            String inputPrifx) {
        String oldReplaceContent = replaceContentSrc;
        StringBuilder builder = new StringBuilder();
        if(StringUtil.empty(oldReplaceContent)){
            return builder;    
        }
        String splitChar = new String("_");
        String replaceStrBegin = "<input type=\"text\" class=\"inputUnderLine2\" name=\"" + inputPrifx;
        String replaceStrMiddle="\" id=\""+inputPrifx+"Id";
        String replaceStrend = "\">&nbsp;&nbsp;&nbsp;";
        // 首先判断开始有没有
        String beginChar = oldReplaceContent.substring(0, splitChar.length());
        if(StringUtil.equals(beginChar, splitChar)) {
            builder.append(replaceStrBegin + 0 +replaceStrMiddle+0+ replaceStrend);
            oldReplaceContent = oldReplaceContent.substring(splitChar.length());
        }
        // 把中间的替换掉
        boolean flagReplace = false;
        String endChar = oldReplaceContent.substring(
                oldReplaceContent.length() - splitChar.length(),
                oldReplaceContent.length());
        if(StringUtil.equals(endChar, splitChar)) {
            oldReplaceContent = oldReplaceContent.substring(0,
                    oldReplaceContent.length() - splitChar.length());
            flagReplace = true;
        }
        // 把中间的去掉
        String[] splitStrs = StringUtil.split(oldReplaceContent, splitChar);
        if(flagReplace) {
            for(int i = 0; i < splitStrs.length; i++) {
                String q = splitStrs[i];
                builder.append(q);
                builder.append(replaceStrBegin + (i + 1) +replaceStrMiddle+(i+1)+ replaceStrend);

            }
        } else {
            for(int i = 0; i < splitStrs.length; i++) {
                String q = splitStrs[i];
                builder.append(q);
                if(i != splitStrs.length - 1) {
                    builder.append(replaceStrBegin + (i + 1) +replaceStrMiddle+(i+1)+ replaceStrend);
                }
            }
        }
        return builder;
    }
    
    
    /**
     * 指定字符串出现的次数.<br>
    *工程名:cctccati<br>
    *包名:com.soft.sb.util.type<br>
    *方法名:countStringNumber方法.<br>
    *
    *@author:luchao1@yonghui.cn
    *@since :1.0:2009-8-10
    *@param srcStr：查找的字符串
    *@param countStr：指定要查找的字符串
    *@return
     */
    public static int countStringNumber(String srcStr,String countStr){
        int indexCount = 0;
        int index = 0;
        int count=0;
        for(;;) {
            index = srcStr.indexOf(countStr, indexCount);
            if(index == -1){
                break;
            }
            count++;
            indexCount = (index += countStr.length());
        }
        return count;
    } 
    
    /**
	 * 比较第二个字符串数组是否被第一个字符串数组包含
	* @author 作者的名字
	* @version 创建时间：Sep 10, 2010  10:31:55 AM
	* @param arr1 原字符串数组
	* @param arr2 被追加的字符串数组
	* @return 第二个字符串追加到第一个字符串后的String
	 */
	public static String compareAddDiffArr(String[] arr1,String[] arr2){
		String arr2Str = "";
		for(int i=0;i<arr2.length;i++){
			if(i == 0){
				arr2Str = arr2[i];
			}else{
				arr2Str += (","+arr2[i]);
			}
		}
		arr2Str += ",";
		for(int j=0;j<arr1.length;j++)
			arr2Str = arr2Str.replace(arr1[j]+",",""); 
		arr2Str = (arr2Str.endsWith(",")) ? arr2Str.substring(0,arr2Str.length()-1) : arr2Str;
		return arr2Str;
	}
	/**
	 * 比较第二个字符串是否被第一个字符串包含，分割字符串用","
	* @author 作者的名字
	* @version 创建时间：Sep 10, 2010  10:31:55 AM
	* @param arr1 原字符串
	* @param arr2 被追加的字符串
	* @return 第二个字符串追加到第一个字符串后的String
	 */
	public static String addStrDiffStr(String arr1,String arr2) {
		arr1 = arr1.replaceAll(" ", "");
		arr2 = arr2.replaceAll(" ", "");
		String[] arr1s = {};
		String[] arr2s = {};
		String str = "";
		if(arr1 != null && !arr1.equals(""))
			arr1s = arr1.split(",");
		else{
			arr1 = "";
		}
		if(arr2 != null && !arr2.equals(""))
			arr2s = arr2.split(",");
		else{
			arr2 = "";
		}
		String temp[]=new String[arr1s.length+arr2s.length];
		System.arraycopy(arr1s,0,temp,0,arr1s.length);//将数组arr1s的元素复制到temp中
		System.arraycopy(arr2s,0,temp,arr1s.length,arr2s.length);//将数组arr2s的元素复制到temp中
		
		//连接数组完成,开始清除重复元素
		for(int i=0;i<temp.length;i++){
			if(temp[i]!="-1"){
				for(int j=i+1;j<temp.length;j++){
					if(temp[i].equals(temp[j])){
						temp[j]="-1";//将发生重复的元素赋值为"-1"
					}	
				}
			}
		}
		for(int i=0,j=0;j<temp.length && i<temp.length;i++,j++){
			if(temp[i].equals("-1")){
				j--;
		    }
		    else{
		    	str += temp[i];
		    	str += ", ";
		    }
		}
		if(!str.equals("")){
			str = str.replace("-1, ",""); 
			str = str.replace(", -1",""); 
			str = (str.endsWith(", ")) ? str.substring(0,str.length()-2) : str;
		}
		return str;
	}
	
	/**
	 * 
	 * @Title: subStrEndDiffStr
	 * @Description: TODO 判断第一个字符串是否为第二个字符串结尾，并且去掉
	 * @param str
	 * @param ch
	 * @return 设定文件
	 * @author hujiuzhou
	 * @return String 返回类型
	 * @throws
	 */
	public static String subStrEndDiffStr(String str,String ch){
		str = toEmptySafe(str);
		ch = toEmptySafe(ch);
		return (str.endsWith(ch)) ? str.substring(0, str.length() - ch.length()) : str;
	}
	
	/**
	 * 
	 * @Title: subStrStartDiffStr
	 * @Description: TODO 判断第一个字符串是否为第二个字符串开始，并且去掉
	 * @param str
	 * @param ch
	 * @return 设定文件
	 * @author hujiuzhou
	 * @return String 返回类型
	 * @throws
	 */
	public static String subStrStartDiffStr(String str,String ch){
		str = str.trim();
		ch = ch.trim();
		return (str.startsWith(ch)) ? str.substring(ch.length(), str.length()) : str;
	}
	
	/**
	 * 生成UUID
	 * 
	 * @return
	 */
	public static String createUUID() {
		UUID uuid = UUID.randomUUID();
		return uuid.toString();
	}

	/**
	 * 生成无“-”的UUID
	 * 
	 * @return
	 */
	public static String createSystemDataPrimaryKey() {
		return StringUtil.replace(createUUID(), "-", "");
	}

	/**
	 * 替换单引号为字符串形式
	 * 
	 * @param source
	 * @return
	 */
	public static String invertedCommaFilter(String source) {
		if (source == null)
			return null;
		else
			return StringUtil.replace(source, "'", "\\'");
	}
	
	/**
	 * 替换含{?}字符串
	 * 
	 * @param source
	 *            源字符串
	 * @param values
	 *            替换数组
	 * @param level
	 *            开始位
	 * @return
	 */
	public static String fillSpacing(String source, String values[], int level) {
		if (source.indexOf("{?}") != -1) {
			String source1 = source.substring(0, source.indexOf("{?}") + 3);
			source = StringUtil.replace(source1, "{?}", values[level]) + source.substring(source.indexOf("{?}") + 3, source.length());
			level++;
			source = fillSpacing(source, values, level);

		}
		return source;
	}

	/**
	 * 获取中文字符串占用长度（像素）
	 * 
	 * @param str
	 * @return
	 */
	public static int getVarcharSpace(String str) {
		int space = 0;
		if (str != null && !str.equals("")) {
			for (int i = 0; i < str.length(); i++)
				if (str.substring(i, i + 1).matches("[\u4E00-\u9FA5]"))
					space += 12;
				else
					space += 6;

		}
		return space;
	}
	
	/**
	 * 格式化xml中value值里面的非法字符
	 * @Project SC
	 * @Package com.sp.framework.utilities.type
	 * @Method replaceXml方法.<br>
	 * @Description TODO(用一句话描述该类做什么)
	 * @author hjz
	 * @date 2014-12-17 下午2:38:20
	 * @param xml
	 * @return
	 */
	public static String replaceXml(String xml) {
		String pat = "[\"\"](.*?)[\"\"]";
		String[] str = xml.split(pat);
		
		Pattern p = Pattern.compile(pat);
        Matcher m = p.matcher(xml);
        List<String> values = new ArrayList<String>();
        while(m.find()){
        	values.add(m.group().replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
        }
        StringBuffer sf = new StringBuffer();
        for(int i = 0; i < str.length; i++) {
        	sf.append(str[i]);
        	if(values.size() > i) {
        		sf.append(values.get(i));
        	}
		}
        
        return sf.toString();
	}
	
	/**
	 * 处理url
	 * 
	 * url为null返回null，url为空串或以http://或https://开头，则加上http://，其他情况返回原参数。
	 * 
	 * @param url
	 * @return
	 */
	public static String handelUrl(String url) {
		if (url == null) {
			return null;
		}
		url = url.trim();
		if (url.equals("") || url.startsWith("http://")
				|| url.startsWith("https://")) {
			return url;
		} else {
			return "http://" + url.trim();
		}
	}

	/**
	 * 分割并且去除空格
	 * 
	 * @param str
	 *            待分割字符串
	 * @param sep
	 *            分割符
	 * @param sep2
	 *            第二个分隔符
	 * @return 如果str为空，则返回null。
	 */
	public static String[] splitAndTrim(String str, String sep, String sep2) {
		if (StringUtils.isBlank(str)) {
			return null;
		}
		if (!StringUtils.isBlank(sep2)) {
			str = StringUtils.replace(str, sep2, sep);
		}
		String[] arr = StringUtils.split(str, sep);
		// trim
		for (int i = 0, len = arr.length; i < len; i++) {
			arr[i] = arr[i].trim();
		}
		return arr;
	}

	/**
	 * 文本转html
	 * 
	 * @param txt
	 * @return
	 */
	public static String txt2htm(String txt) {
		if (StringUtils.isBlank(txt)) {
			return txt;
		}
		StringBuilder sb = new StringBuilder((int) (txt.length() * 1.2));
		char c;
		boolean doub = false;
		for (int i = 0; i < txt.length(); i++) {
			c = txt.charAt(i);
			if (c == ' ') {
				if (doub) {
					sb.append(' ');
					doub = false;
				} else {
					sb.append("&nbsp;");
					doub = true;
				}
			} else {
				doub = false;
				switch (c) {
				case '&':
					sb.append("&amp;");
					break;
				case '<':
					sb.append("&lt;");
					break;
				case '>':
					sb.append("&gt;");
					break;
				case '"':
					sb.append("&quot;");
					break;
				case '\n':
					sb.append("<br/>");
					break;
				default:
					sb.append(c);
					break;
				}
			}
		}
		return sb.toString();
	}

	/**
	 * 剪切文本。如果进行了剪切，则在文本后加上"..."
	 * 
	 * @param s
	 *            剪切对象。
	 * @param len
	 *            编码小于256的作为一个字符，大于256的作为两个字符。
	 * @return
	 */
	public static String textCut(String s, int len, String append) {
		if (s == null) {
			return null;
		}
		int slen = s.length();
		if (slen <= len) {
			return s;
		}
		// 最大计数（如果全是英文）
		int maxCount = len * 2;
		int count = 0;
		int i = 0;
		for (; count < maxCount && i < slen; i++) {
			if (s.codePointAt(i) < 256) {
				count++;
			} else {
				count += 2;
			}
		}
		if (i < slen) {
			if (count > maxCount) {
				i--;
			}
			if (!StringUtils.isBlank(append)) {
				if (s.codePointAt(i - 1) < 256) {
					i -= 2;
				} else {
					i--;
				}
				return s.substring(0, i) + append;
			} else {
				return s.substring(0, i);
			}
		} else {
			return s;
		}
	}
	
	/**
	 * p换行
	 * @param inputString
	 * @return
	 */
	public static String removeHtmlTagP(String inputString) {  
	    if (inputString == null)  
	        return null;  
	    String htmlStr = inputString; // 含html标签的字符串  
	    String textStr = "";  
	    Pattern p_script;
	    Matcher m_script;
	    Pattern p_style;
	    Matcher m_style;
	    Pattern p_html;
	    Matcher m_html;
	    try {  
	        //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>  
	        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";   
	        //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>  
	        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";   
	        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
	        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
	        m_script = p_script.matcher(htmlStr);  
	        htmlStr = m_script.replaceAll(""); // 过滤script标签  
	        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
	        m_style = p_style.matcher(htmlStr);  
	        htmlStr = m_style.replaceAll(""); // 过滤style标签  
	        htmlStr.replace("</p>", "\n");
	        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
	        m_html = p_html.matcher(htmlStr);  
	        htmlStr = m_html.replaceAll(""); // 过滤html标签  
	        textStr = htmlStr;  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return textStr;// 返回文本字符串  
	}  
	
	public static String removeHtmlTag(String inputString) {  
	    if (inputString == null)  
	        return null;  
	    String htmlStr = inputString; // 含html标签的字符串  
	    String textStr = "";  
	    Pattern p_script;
	    Matcher m_script;
	    Pattern p_style;
	    Matcher m_style;
	    Pattern p_html;
	    Matcher m_html;
	    try {  
	        //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script>  
	        String regEx_script = "<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";   
	        //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style>  
	        String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>";   
	        String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式  
	        p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);  
	        m_script = p_script.matcher(htmlStr);  
	        htmlStr = m_script.replaceAll(""); // 过滤script标签  
	        p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);  
	        m_style = p_style.matcher(htmlStr);  
	        htmlStr = m_style.replaceAll(""); // 过滤style标签  
	        p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);  
	        m_html = p_html.matcher(htmlStr);  
	        htmlStr = m_html.replaceAll(""); // 过滤html标签  
	        textStr = htmlStr;  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return textStr;// 返回文本字符串  
	}  

	/**
	 * 检查字符串中是否包含被搜索的字符串。被搜索的字符串可以使用通配符'*'。
	 * 
	 * @param str
	 * @param search
	 * @return
	 */
	public static boolean contains(String str, String search) {
		if (StringUtils.isBlank(str) || StringUtils.isBlank(search)) {
			return false;
		}
		String reg = StringUtils.replace(search, "*", ".*");
		Pattern p = Pattern.compile(reg);
		return p.matcher(str).matches();
	}
	
	 /**
     * 判断输入字符串是否包含指定的字符串
     * @param str 输入字符串
     * @param searchStr 指定是否包含的字符串
     * @return  =true:包含指定的字符串
     *           =false:不包含指定的字符串
     */
   /* public static boolean contains(String str, String searchStr) {
        if (str == null || searchStr == null) {
            return false;
        }
        if (searchStr.length() == 0) // ""空串不认为被包含。String.indexOf()认为空串被包含
        {
            return false;
        } else {
            return str.indexOf(searchStr) >= 0;
        }
    }*/

	public static boolean containsKeyString(String str) {
		if (StringUtils.isBlank(str)) {
			return false;
		}
		if (str.contains("'") || str.contains("\"") || str.contains("\r")
				|| str.contains("\n") || str.contains("\t")
				|| str.contains("\b") || str.contains("\f")) {
			return true;
		}
		return false;
	}
	
	
	public static String addCharForString(String str, int strLength,char c,int position) {
		  int strLen = str.length();
		  if (strLen < strLength) {
			  while (strLen < strLength) {
			  StringBuffer sb = new StringBuffer();
			  if(position==1){
				  //右補充字符c
				  sb.append(c).append(str);
			  }else{
				//左補充字符c
				  sb.append(str).append(c);
			  }
			  str = sb.toString();
			  strLen = str.length();
			  }
			}
		  return str;
	 }

	// 将""和'转义
	public static String replaceKeyString(String str) {
		if (containsKeyString(str)) {
			return str.replace("'", "\\'").replace("\"", "\\\"").replace("\r",
					"\\r").replace("\n", "\\n").replace("\t", "\\t").replace(
					"\b", "\\b").replace("\f", "\\f");
		} else {
			return str;
		}
	}
	
	//单引号转化成双引号
	public static String replaceString(String str) {
		if (containsKeyString(str)) {
			return str.replace("'", "\"").replace("\"", "\\\"").replace("\r",
					"\\r").replace("\n", "\\n").replace("\t", "\\t").replace(
					"\b", "\\b").replace("\f", "\\f");
		} else {
			return str;
		}
	}
	
	public static String getSuffix(String str) {
		int splitIndex = str.lastIndexOf(".");
		return str.substring(splitIndex + 1);
	}
	
	/**
	 * 字符串补齐
	 * @param source  源字符串
	 * @param fillLength 补齐长度
	 * @param fillChar 补齐的字符
	 * @param isLeftFill true为左补齐，false为右补齐
	 * @return
	 */
	public static String stringFill(String source, int fillLength, char fillChar, boolean isLeftFill) {
		if (source == null || source.length() >= fillLength)
			return source;

		StringBuilder result = new StringBuilder(fillLength);
		int len = fillLength - source.length();
		if (isLeftFill) {
			for (; len > 0; len--) {
				result.append(fillChar);
			}
			result.append(source);
		} else {
			result.append(source);
			for (; len > 0; len--) {
				result.append(fillChar);
			}
		}
		return result.toString();
	}

	public static void main(String args[]) {
		System.out.println(replaceKeyString("&nbsp;\r" + "</p>"));
		System.out.println(join("-","1","","3","4","5","6","7","9"));
	}
}

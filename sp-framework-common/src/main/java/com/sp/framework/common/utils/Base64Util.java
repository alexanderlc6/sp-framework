package com.sp.framework.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;


/**
 * 
  * @ClassName: Base64Util
  * @Description: base64加密
  * @author alexlu
  * @date 2017年5月11日
  *
 */
public class Base64Util {
    
    private static final Logger LOG = LoggerFactory.getLogger(Base64Util.class);
    private static final char[] base64Map =  //base64 character table

            { 'A','B','C','D','E','F','G','H','I','J','K','L','M','N',
                    'O','P','Q','R','S','T','U','V','W','X','Y','Z','a','b',
                    'c','d','e','f','g','h','i','j','k','l','m','n','o','p',
                    'q','r','s','t','u','v','w','x','y','z','0','1','2','3',
                    '4','5','6','7','8','9','+','/' } ;
    
    /**
     * 对给定的字符串进行base64压码操作
     * 
     * @param input 待压码的字符串
     * @return 压码后的字符串
     */    
    public static String encode(String input) {
        try {
            return new String(Base64.encodeBase64(input.getBytes("utf-8")),"utf-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(input, e);
        }
        
        return null;
    }

    public static String encode(byte buf[])
    {
        StringBuffer sb = new StringBuffer();
        String padder = "" ;

        if( buf.length == 0 ) {
            return "";
        }

        //cope with less than 3 bytes conditions at the end of buf

        switch( buf.length%3)
        {
            case 1 :
            {
                padder += base64Map[(( buf[buf.length-1] >>> 2 ) & 63) ] ;
                padder += base64Map[ (( buf[buf.length-1] << 4 ) & 63) ] ;
                padder += "==" ;
                break ;
            }
            case 2 :
            {
                padder += base64Map[ ( buf[buf.length-2] >>> 2 ) & 63 ] ;
                padder += base64Map[ (((buf[buf.length-2] << 4 )&63)) | ((( buf[buf.length-1] >>>4 ) & 63)) ] ;
                padder += base64Map[ ( buf[buf.length-1] << 2 ) & 63 ] ;
                padder += "=" ;
                break ;
            }
            default :    break ;
        }

        int temp =0 ;
        int index = 0 ;

        //encode buf.length-buf.length%3 bytes which must be a multiply of 3

        for( int i=0 ; i < ( buf.length-(buf.length % 3) ) ; )
        {
            //get three bytes and encode them to four base64 characters
            temp = ((buf[i++] << 16)&0xFF0000)|((buf[i++] << 8)&0xFF00)|(buf[i++]&0xFF) ;
            index = (temp >> 18) & 63 ;
            sb.append(base64Map[ index ]);
            if(sb.length()%76==0)//a Base64 encoded line is no longer than 76 characters
            {
                sb.append('\n');
            }

            index = (temp >> 12 ) & 63 ;
            sb.append(base64Map[ index ]);
            if(sb.length()%76==0) {
                sb.append('\n');
            }

            index = (temp >> 6 ) & 63 ;
            sb.append(base64Map[ index ]);
            if(sb.length()%76==0) {
                sb.append('\n');
            }

            index = temp & 63 ;
            sb.append(base64Map[ index ]);
            if(sb.length()%76==0) {
                sb.append('\n');
            }
        }

        sb.append(padder);  //add the remaining one or two bytes
        return sb.toString();
    }

    /**
     * 对给定的字符串进行base64解码操作
     *
     * @param input : 待解码的字符串
     * @return 解码后的字符串
     */
    public static String decode(String input) {
        try {
            return new String(Base64.decodeBase64(input.getBytes("utf-8")),"utf-8");
        } catch (UnsupportedEncodingException e) {
            LOG.error(input, e);
        }

        return null;
    }

    /**
     * 将二进制数据编码为BASE64字符串
     * @param binaryData
     * @return
     */
    public static String encodeByteToStr(byte[] binaryData) {
        try {
            return new String(Base64.encodeBase64(binaryData), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
     
    /**
     * 将BASE64字符串恢复为二进制数据
     * @param base64String
     * @return
     */
    public static byte[] decodeStrToByte(String base64String) {
        try {
            return Base64.decodeBase64(base64String.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }
}

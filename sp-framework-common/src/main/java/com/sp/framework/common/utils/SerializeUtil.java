package com.sp.framework.common.utils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 
 * @author zhenming.zhao
 *
 */
public class SerializeUtil {
	/**
	 * 序列化
	 * @param object
	 * @return
	 */
	public static byte[] serialize(Object object) {
		if(null==object){
			return null;
		}
        ObjectOutputStream oos = null;
         ByteArrayOutputStream baos = null;
         try {
              
             baos = new ByteArrayOutputStream();
             oos = new ObjectOutputStream(baos);
             oos.writeObject(object);
              byte[] bytes = baos.toByteArray();
              return bytes;
        } catch (Exception e) {
        	e.printStackTrace();
        }
         return null;
  }
	/**
	 *  反序列化
	 * @param bytes
	 * @return
	 */
   public static Object unserialize( byte[] bytes) {
	   
	   if(null==bytes) {
		   return null;
	   }
        ByteArrayInputStream bais = null;
         try {
             
             bais = new ByteArrayInputStream(bytes);
             ObjectInputStream ois = new ObjectInputStream(bais);
              return ois.readObject();
        } catch (Exception e) {

        }
         return null;
  }
}

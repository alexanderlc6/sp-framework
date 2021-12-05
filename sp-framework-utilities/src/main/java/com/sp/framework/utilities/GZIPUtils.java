package com.sp.framework.utilities;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * @description:
 * @author: luchao
 * @date: Created in 4/7/21 6:18 PM
 */
public class GZIPUtils {
    public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

    public GZIPUtils() {
    }

    public static byte[] compress(String str, String encoding) {
        if (str != null && str.length() != 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = null;

            Object var5;
            try {
                gzip = new GZIPOutputStream(out);
                gzip.write(str.getBytes(encoding));
                gzip.finish();
                gzip.close();
                byte[] var4 = out.toByteArray();
                return var4;
            } catch (Exception var15) {
                var15.printStackTrace();
                var5 = null;
            } finally {
                try {
                    out.close();
                } catch (IOException var14) {
                }

            }

            return (byte[])var5;
        } else {
            return null;
        }
    }

    public static byte[] compress(String str) throws IOException {
        return compress(str, "UTF-8");
    }

    public static byte[] uncompress(byte[] bytes) {
        if (bytes != null && bytes.length != 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);

            Object var4;
            try {
                GZIPInputStream ungzip = new GZIPInputStream(in);
                byte[] buffer = new byte[1024];

                int n;
                while((n = ungzip.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }

                byte[] var6 = out.toByteArray();
                return var6;
            } catch (Exception var16) {
                var16.printStackTrace();
                var4 = null;
            } finally {
                try {
                    out.close();
                } catch (IOException var15) {
                }

            }

            return (byte[])var4;
        } else {
            return null;
        }
    }

    public static String uncompressToString(byte[] bytes, String encoding) {
        if (bytes != null && bytes.length != 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);

            Object var5;
            try {
                GZIPInputStream ungzip = new GZIPInputStream(in);
                byte[] buffer = new byte[1024];

                int n;
                while((n = ungzip.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }

                String var7 = out.toString(encoding);
                return var7;
            } catch (Exception var17) {
                var17.printStackTrace();
                var5 = null;
            } finally {
                try {
                    out.close();
                } catch (IOException var16) {
                }

            }

            return (String)var5;
        } else {
            return null;
        }
    }

    public static String uncompressToString(byte[] bytes) {
        return uncompressToString(bytes, "UTF-8");
    }

    public static void main(String[] args) throws IOException {
        String s = "aaaaaaaaaaaaa中文aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        System.out.println("字符串长度：" + s.length());
        byte[] zipped = compress(s);
        System.out.println("压缩后：" + zipped.length);
        String str = uncompressToString(zipped);
        System.out.println("解压后：" + str);
    }
}

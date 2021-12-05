package com.sp.framework.utilities.sftp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;



public class FileUtil {

    public static String SEGM_NEWLINE = "\n";
    public static String SEGM_COLUMN = ";";
    public static String SEGM_HOLDER = "---";

    /**
     * 拷贝文件或目录 使用方法为 
     * <pre> option1:FileUtil.copyFile(file,newFilePath,newFileName) 
     * option2:FileUtil.copyFile(file,newFilePath,null) 
     * option3:FileUtil.copyFile(directory,newFilePath,newFileName)---->此处会输出警告，因为拷贝的是目录的话，新的文件名不起任何效果
     * option4:FileUtil.copyFile(directory,newFilePath,null)
     * </pre>
     * @param file 等拷贝文件---可为文件目录
     * @param newFilePath 新文件路径
     * @param newFileName 新文件名----如果为空，文件名同待拷贝的文件名
     * @return
     * @Description:
     */
    public static boolean copyFile(File file, String newFilePath, String newFileName) {
        boolean result = true;
        if (file.isDirectory()) {
            if (StringUtils.isEmpty(newFileName)) {
                System.out.println("### WARN!!! The file is directory ! So the newFileName is unavailable!!!");
            }
            File[] childFileArray = file.listFiles();
            for (File childFile : childFileArray) {
                result = copyFile(childFile, newFilePath + "/" + file.getName(), null);
                if (false == result) {
                    break;
                }
            }
            return result;
        } else {
            InputStream is = null;
            // 如果新文件名为空的话，将原始文件名赋值给新文件名，
            if (StringUtils.isEmpty(newFileName)) {
                newFileName = file.getName();
            }
            try {
                // 写入字节流
                is = new FileInputStream(file);
                // 调用方法将字节流写进新文件中
                result = copyFile(is, newFilePath, newFileName, null);
            } catch (FileNotFoundException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                result = false;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                result = false;
            } finally {
                if (null != is) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            return result;
        }
    }

    /**
     * 得到目录下的所有文件
     * 
     * @param path
     * @return
     * @Description:
     */
    public static File[] getFileArray(String path) {
        File file = new File(path);
        File[] fileArray = null;
        if (file.exists()) {
            if (file.isDirectory()) {
                fileArray = file.listFiles();
            }
        }
        return fileArray;
    }

    /**
     * 前三个条件都不能为空，buffer为空的话有默认值
     * 
     * @param is 输入流
     * @param newFilePath 新文件路径
     * @param newFileName 新文件名
     * @param buffer 缓存空间大小，如果为空给为4kb大小
     * @throws IOException
     * @Description:
     */
    public static boolean copyFile(InputStream is, String newFilePath, String newFileName, Integer buffer) throws IOException {
        boolean result = false;
        if (null == is || StringUtils.isEmpty(newFilePath) || StringUtils.isEmpty(newFileName)) {
            throw new IOException("### the InputStream or new file's path or new file's name is null!!!!!!!! - -!see my eyes, if i know you, you will be dead!" + "\nInputStream:" + is + "\nnewFilePath:" + newFilePath + "\nnewFileName:" + newFileName);
        }

        if (null != buffer) {
            if (buffer <= 0) {
                throw new IOException("### The stream buffer's size not greater than 0,please theck this param!");
            }
        } else {
            // 为空的话默认1024
            buffer = 4 * 1024;
        }
        File newDirectary = new File(newFilePath);
        if (!newDirectary.exists()) {
            newDirectary.mkdirs();
        }
        File newFile = new File(newFilePath + "/" + newFileName);
        OutputStream os = new FileOutputStream(newFile);

        byte[] size = new byte[buffer];
        int index = 0;
        while ((index = is.read(size)) > 0) {
            os.write(size, 0, index);
        }
        // 关闭输入流和输出流
        if (null != is) {
            is.close();
        }
        if (null != os) {
            os.flush();
            os.close();
        }
        result = true;
        return result;
    }



    /**
     * 移动文件
     * 
     * @param sourcePath 具体的路径
     * @param sourceFile 具体的文件名
     * @param dscPath 目的的路径（具体的，非配置形式）
     * @param doneFileSuffix操作文件的后缀
     * @Description:
     */
    public static void moveFile(String sourcePath, String sourceFile, String dscPath, String doneFileSuffix) {

        dscPath = dscPath.endsWith("/") ? dscPath : dscPath + "/";
        sourcePath = sourcePath.endsWith("/") ? sourcePath : sourcePath + "/";
        // 文件原地址
        File oldFile = new File(sourcePath + sourceFile);
        // 创建目录
        try {
            dynamicCreateDir(dscPath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 文件新（目标）地址
        String newFile = dscPath + sourceFile;
        // new一个新文件夹
        File fnewpath = new File(dscPath);
        // 判断文件夹是否存在
        if (!fnewpath.exists()) {
            fnewpath.mkdirs();
        }
        // 将文件移到新文件里
        File fnew = new File(newFile);
        oldFile.renameTo(fnew);
    }

    /**
     * nike的类中用到了此方法
     * 
     * @param sourceFile
     * @Description:
     */
    public static void moveFile(File sourceFile) {
        // 文件原地址
        File oldFile = sourceFile;
        // 文件新（目标）地址
        String newPath = oldFile.getParent() + "/.done/" + DateUtil.day() + "/";
        // new一个新文件夹
        File fnewpath = new File(newPath);
        // 判断文件夹是否存在
        if (!fnewpath.exists()) fnewpath.mkdirs();
        // 将文件移到新文件里
        File fnew = new File(newPath + oldFile.getName());
        oldFile.renameTo(fnew);
    }


    /**
     * 获取文件的InputStream
     * 
     * @param 文件名
     * @return InputStream
     * @throws ParseException
     */
    public static InputStream getInputStream(String path) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
    }

    /**
     * 获取文件的OutputStream
     * 
     * @param 文件名
     * @param 是否追加
     * @return InputStream
     * @throws ParseException
     */
    public static OutputStream getFileOutputStream(String path, boolean isAppend) throws FileNotFoundException {
        try {
            // URL is = Thread.currentThread().getContextClassLoader().getResource(FileName);
            return new FileOutputStream(path, isAppend);
        } catch (FileNotFoundException e) {
            throw e;
        }
    }

    /**
     * 创建文件
     * 
     * @param 文件路径
     * @param 文件名
     * @return file
     * @throws ParseException
     */
    public static File createFileAsFile(String filepath, String FileName) throws Exception {
        File file = null;
        try {
            if (null != filepath && "".equals(filepath.trim())) {
                String ch = filepath.substring(filepath.length() - 1, filepath.length());
                if (!ch.equals("/") && !ch.equals("\\")) {
                    filepath = filepath + "/";
                }
                if (!new File(filepath).isDirectory()) {
                    file = new File(filepath);
                    file.mkdirs();
                }
            }
            if (null != FileName && "".equals(FileName.trim())) {
                file = new File(filepath + FileName);
                file.createNewFile();
            }
        } catch (Exception e) {
            throw e;
        }
        return file;
    }

    /**
     * 根据目录和文件名生成文件
     * 
     * @param path
     * @param fileName
     * @param content
     * @throws IOException
     * @Description
     * @Author xiaozhou.zhou
     * @Modifier xiaozhou.zhou
     */
    public static void createFile(String path, String fileName, String content) throws IOException {
        /***** 目录生成b ******************************/
        File directory = new File(path);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        /***** 文件生成 *****************************/
        File file = new File(path + "/" + fileName);

        FileOutputStream fos = new FileOutputStream(file);
        byte[] byteArray = content.getBytes();
        ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
        byte[] buffer = new byte[1024];
        int byteread = 0;
        while ((byteread = in.read(buffer)) != -1) {
            fos.write(buffer, 0, byteread); // 文件写操作
        }
        fos.flush();
        fos.close();
    }

    /**
     * 创建文件
     * 
     * @param 文件路径
     * @param 文件名
     * @return boolean
     * @throws ParseException
     */
    public static boolean CreateFile(String filepath, String FileName) throws Exception {
        try {
            if (null != filepath && "".equals(filepath.trim())) {
                String ch = filepath.substring(filepath.length() - 1, filepath.length());
                if (!ch.equals("/") && !ch.equals("\\")) {
                    filepath = filepath + "/";
                }
            }
            if (!new File(filepath).isDirectory()) {
                new File(filepath).mkdirs();
            }
            if (null != FileName && "".equals(FileName.trim()) && !new File(filepath + FileName).isFile()) {
                return new File(filepath + FileName).createNewFile();
            }
        } catch (Exception e) {
            throw e;
        }
        return true;
    }

    /**
     * 将字符串写入文件
     * 
     * @param 文件名
     * @param 写入的字符串
     * @param 是否追加
     * @throws ParseException
     */
    public static void writeFile(String path, String source, boolean isAppend) throws Exception {
        byte[] bytes = source.getBytes();
        OutputStream os = getFileOutputStream(path, isAppend);
        os.write(bytes);
        os.close();
    }

    /**
     * 将字符串写入文件
     * 
     * @param 文件名
     * @param 写入的字符串
     * @throws ParseException
     */
    public static void writeFile(String path, String source) throws Exception {
        byte[] bytes = source.getBytes();
        OutputStream os = getFileOutputStream(path, false);
        os.write(bytes);
        os.close();
    }

    /**
     * 将输入流利用输出流写入文件
     * 
     * @param 文件路径
     * @param 文件名
     * @param 输入流
     * @throws ParseException
     */
    public static void writeFile(String filePath, String fileName, InputStream in) throws Exception {
        BufferedOutputStream outStream = null;
        BufferedInputStream inputStream = null;
        try {
            outStream = new BufferedOutputStream(new FileOutputStream(createFileAsFile(filePath, fileName)));
            inputStream = new BufferedInputStream(in);
            int c;
            while ((c = inputStream.read()) != -1) {
                outStream.write(c);
                outStream.flush();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        } finally {
            if (outStream != null) {
                outStream.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

    /**
     * 读取文件
     * 
     * @param 文件名
     * @return String
     * @author jindezhi
     */
    public static String readFile(String path) throws Exception {
        String output = "";
        File file = new File(path);
        if (file.exists()) {
            if (file.isFile()) {
                BufferedReader input = new BufferedReader(new FileReader(file));
                StringBuffer buffer = new StringBuffer();
                String text = "";
                while ((text = input.readLine()) != null) {
                    buffer.append(text).append("\n");
                }
                output = buffer.toString();
                if (input != null) {
                    input.close();
                }
            } else if (file.isDirectory()) {
                String[] dir = file.list();
                output += "Directory contents:\n";
                for (int i = 0; i < dir.length; i++) {
                    output += dir[i] + "\n";
                }
            }
        } else {
            throw new Exception("file:" + path + " Does not exist!");
        }
        return output;
    }

    /**
     * 读取文件
     * 
     * @return String
     * @author jindezhi
     */
    public static String readFile(InputStream stream) throws Exception {
        byte b[] = new byte[1024];
        int len = 0;
        int temp = 0; // 所有读取的内容都使用temp接收
        while ((temp = stream.read()) != -1) { // 当没有读取完时，继续读取
            b[len] = (byte) temp;
            len++;
        }
        stream.close();
        String result = new String(b, 0, len);
        return result;
    }

    /**
     * 删除文件
     * 
     * @param 文件名
     * @return bollean
     * @throws ParseException
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);

        if (file.exists()) {
            if (file.isFile()) {
                return file.delete();
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 遍历文件夹下所有匹配文件
     * 
     * @param file File 起始文件夹
     * @param p Pattern 匹配类型
     * @return ArrayList 其文件夹下的文件夹
     */
    private static ArrayList<File> filePattern(File file, Pattern p) {
        if (file == null) {
            return null;
        } else if (file.isFile()) {
            Matcher fMatcher = p.matcher(file.getName());
            if (fMatcher.matches()) {
                ArrayList<File> list = new ArrayList<File>();
                list.add(file);
                return list;
            }
        } else if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                ArrayList<File> list = new ArrayList<File>();
                for (int i = 0; i < files.length; i++) {
                    ArrayList<File> rlist = filePattern(files[i], p);
                    if (rlist != null) {
                        list.addAll(rlist);
                    }
                }
                return list;
            }
        }
        return null;
    }

    /**
     * 查找文件
     * */
    public static File[] refreshFileList(String... para) {
        String strPath = "";
        String s = "";
        if (para.length > 1) {
            strPath = para[0];
            s = para[1];
        } else if (para.length == 1) {
            strPath = para[0];
            s = "*";
        } else if (para.length == 0) {
            return null;
        }

        File dir = new File(strPath);

        s = s.replace('.', '#');
        s = s.replaceAll("#", "\\\\.");
        s = s.replace('*', '#');
        s = s.replaceAll("#", ".*");
        s = s.replace('?', '#');
        s = s.replaceAll("#", ".?");
        s = "^" + s + "$";

        Pattern p = Pattern.compile(s);
        ArrayList<?> list = filePattern(dir, p);
        int filesize = 0;
        if (list != null) {
            filesize = list.size();

            File[] files = new File[filesize];
            list.toArray(files);

            if (files == null || files.length == 0) {
                return null;
            }
            return files;
        } else {
            return null;
        }
    }

    /**
     * 目录创建
     * 
     * @param path
     * @return
     * @throws IOException
     * @Description:
     */
    public static File dynamicCreateDir(String path) throws IOException {
        File dir = new File(path);
        if (dir.exists()) {
            if (dir.isDirectory()) {
                return dir;
            } else {
                throw new IOException("File exists of that name");
            }
        } else {
            dir.mkdirs();
            return dir;
        }
    }

    /**
     * 
     * @param fromPath
     * @param file
     * @param readLock
     * @Description:
     */
    public static boolean canRead(String fromPath, File file, String doneFileName, String readLock) {
        boolean canRead = true;
        String fileName = file.getName();
        // 判断.done文件是否存在
        File doneFile = new File(fromPath + "/" + fileName + doneFileName);
        if (!doneFile.exists()) {
            // .done文件不存在，那么将会被排除
            canRead = false;
        }
        if (null != readLock && "".equals(readLock.trim())) {
            if (!readLock.startsWith(".")) {
                readLock = "." + readLock;
            }
            // 要进行锁配置判断
            File lockFile = new File(fromPath + "/" + fileName + readLock);
            if (lockFile.exists()) {
                canRead = false;
            }
        }
        return canRead;
    }

    /**
     * 得到file:/xxxxxx中的文件目录
     * 
     * @param first
     * @return
     * @Description:
     */
    public static String getFilePath(String first) {
        return first.substring(first.indexOf("file:") + 5, first.indexOf("?") > -1 ? first.indexOf("?") : first.length());
    }

    /**
     * 获取done文件名的后缀
     * 
     * @param doneFileName
     * @return
     * @Description:
     */
    public static String getDoneFileSuffix(String doneFileName) {
        String doneSuffix = null;
        if (null != doneFileName && "".equals(doneFileName.trim())) {
            if (doneFileName.indexOf("}") > -1) {
                // 该处判断的是${file.name}.done
                doneSuffix = doneFileName.substring(doneFileName.lastIndexOf("}") + 1);
            } else {
                // 该处判断的是.done
                doneSuffix = doneFileName;
            }
            // 该处进行健壮性补充
            if (doneSuffix.indexOf(".") != 0) {
                doneSuffix = "." + doneSuffix;
            }
        }
        return doneSuffix;
    }
    

//    /**
//     * 解析move中的配置，生成目录地址 move = .done/${date:now:yyyyMMdd}/${file:name}
//     * 
//     * move= .done/20150505/DELIVERY_NOTE_20150505110213.txt
//     * 
//     * @param move
//     * @param fileName
//     * @return
//     * @Description:
//     */
//    public static String parseMove(String move, String fileName) {
//        // 解析move中的内容
//        String timestampMove = move.substring(move.indexOf("date:now:"));
//        Map<String, Object> data = new HashMap<String, Object>();
//        String pattern = timestampMove.substring(timestampMove.indexOf("date:now:") + 9, timestampMove.indexOf("}"));
//        data.put("date:now:" + pattern, DateUtil.formatDate(DateUtil.nowDate(), pattern));
//        data.put("file:name", fileName);
//        try {
//            move = StringUtil.composeMessage(move, data);
//        } catch (Exception e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
//        return move;
//    }
    
    public static byte[] getByte(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                // log.error("helper:the file is null!");
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            // log.error("helper:get bytes from file process error!");
            e.printStackTrace();
        }
        return ret;
    }
}

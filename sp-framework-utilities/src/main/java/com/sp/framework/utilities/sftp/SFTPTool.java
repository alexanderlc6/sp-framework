package com.sp.framework.utilities.sftp;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.Vector;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

/**
 * SFTP公共处理类<br>
 * 获取指定目录下文件集合、上传文件、下载文件、删除文件、创建目录等
 * @author wenjin.gao
 * @Date 2015年7月6日  上午11:03:55
 * @Version 
 * @Description 
 *
 */
public class SFTPTool {
	
	private Logger logger = LoggerFactory.getLogger(SFTPTool.class);
	
	//sftp客户端对象
	private ChannelSftp sftp = null;
	
	private Session sshSession = null;
	
	//默认构造器
	SFTPTool(){}
	
	/**
	 * 连接sftp服务器
	 * @author wenjin.gao
	 * @param sftpip 
	 * @param sftpport
	 * @param sftpusername
	 * @param sftppassword
	 * @return
	 * @throws JSchException 
	 */
	public SFTPTool(String sftpip, int sftpport, String sftpusername, String sftppassword) throws JSchException{
		sftp = new ChannelSftp(); 
        JSch jsch = new JSch(); 
        jsch.getSession(sftpusername, sftpip, sftpport); 
        sshSession = jsch.getSession(sftpusername, sftpip, sftpport); 
        logger.info("Session created"); 
        sshSession.setPassword(sftppassword); 
        Properties sshConfig = new Properties(); 
        sshConfig.put("StrictHostKeyChecking", "no"); 
        sshSession.setConfig(sshConfig); 
        // 设置超时时间为30秒 
        sshSession.setTimeout(30000); 
        sshSession.connect(); 
        Channel channel = sshSession.openChannel("sftp"); 
        channel.connect(); 
        sftp = (ChannelSftp) channel; 

        
	}
	
	/**
	 * 关闭连接
	 * @author wenjin.gao
	 */
	public void disconnect() {
		if(this.sftp != null){  
            if(this.sftp.isConnected()){  
                this.sftp.disconnect();  
            }else if(this.sftp.isClosed()){  
                System.out.println("sftp is closed already");  
            }  
        }
		if(this.sshSession != null){
			this.sshSession.disconnect();
		}
	}

	/**
	 * 列出目录下的文件
	 * @author wenjin.gao
	 * @param directory 要列出文件列表的目录
	 * @param sftp 
	 * @return Vector
	 * @throws SftpException
	 */
	@SuppressWarnings("rawtypes")
	public Vector listFiles(String directory)throws SftpException{
		return sftp.ls(directory);
	}
	
	/**
	 * 上传文件
	 * @author wenjin.gao
	 * @param directory 上传的目录
	 * @param uploadFile 要上传的文件
	 * @param sftp 
	 * @return 0：文件下载成功，1：文件不存在
	 * @throws SftpException 文件路径不存在
	 */
	public int upload(String directory, String uploadFile) throws SftpException{
		sftp.cd(directory);
		File file = new File(uploadFile);
		//文件是否存在，若不存在，直接返回
		if(!file.exists()){
			logger.info("文件 {} 不存在", uploadFile);
			return 1;
		}
		try {
			sftp.put(new FileInputStream(file), file.getName());
		} catch (FileNotFoundException e) {
			logger.error("文件不存在", e);
			return 1;
		}
		return 0;
	}
	
	/**
	 * 下载文件
	 * @author wenjin.gao
	 * @param directory 下载目录
	 * @param downloadFile 下载的文件
	 * @param saveFile 本地下载路径
	 * @param sftp
	 * @return 0：文件下载成功，1：文件不存在
	 * @throws SftpException 文件路径不存在
	 */
	public int download(String directory, String downloadFile, String saveFile) throws SftpException{
		sftp.cd(directory);
		File file = new File(downloadFile);
		//文件是否存在，若不存在，直接返回
		if(!file.exists()){
			logger.info("文件 {} 不存在", downloadFile);
			return 1;
		}
		try {
			sftp.get(downloadFile, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			logger.error("文件不存在", e);
			return 1;
		}
		return 0;
	}
	
	/**
	 * 删除指定文件
	 * @author wenjin.gao
	 * @param directory 需删除文件所在目录
	 * @param deleteFile 需删除的文件
	 * @param sftp
	 * @throws SftpException
	 */
	public void delete(String directory, String deleteFile) throws SftpException{
		//进入到指定目录
		sftp.cd(directory);
		//删除指定文件
		sftp.rm(deleteFile);
	}
	
	/**
	 * 创建指定目录
	 * @author wenjin.gao
	 * @param filepath 需创建的目录
	 * @param sftp
	 */
	public void createDir(String filepath){
		//父目录存在标识
		boolean bcreated = false;
		//创建目录标识
        boolean bparent = false;
        File file = new File(filepath);
        String ppath = file.getParent().replace("\\", "//");
        try {
            sftp.cd(ppath);
            bparent = true;
        } catch (SftpException e1) {
            bparent = false;
        }
        try {
            if (bparent) {
                try {
                    sftp.cd(filepath);
                    bcreated = true;
                } catch (Exception e) {
                    bcreated = false;
                }
                if (!bcreated) {
                    sftp.mkdir(filepath);
                    sftp.cd(filepath);
                    bcreated = true;
                }
                return;
            } else {
                createDir(ppath);
                sftp.cd(ppath);
                sftp.mkdir(filepath);
            }
        } catch (SftpException e) {
            logger.error("mkdir failed : {}", filepath);
            e.printStackTrace();
        }

        try {
            sftp.cd(filepath);
        } catch (SftpException e) {
            e.printStackTrace();
            logger.error("can not cd into :", filepath);
        }
	}
	
	/**
	 * 向FTP服务器上传文件
	 * @author wenjin.gao
	 * @param remotePath FTP服务器保存目录 
	 * @param localFileName 上传到FTP服务器上的文件名
	 * @param sftp 
	 * @return
	 * @throws SftpException 
	 */
	public boolean sendFile(String remotePath, String localFileName) throws SftpException {
		// 得到文件名称
        if (localFileName == null || "".equals(localFileName)) {
            return false;
        }
        // 如果路径是\\转化成/
        if (localFileName.indexOf("/") == -1) {
            localFileName = localFileName.replace("\\\\", "\\");
            localFileName = localFileName.replace("\\", "/");
        }
        logger.info("remote path : {} : {} :" , remotePath, localFileName);
        String toFileName = localFileName.substring(localFileName.lastIndexOf("/") == -1 ? 0 : localFileName.lastIndexOf("/") + 1, localFileName.length());
        logger.info("remote path : {} : {} :" , remotePath,toFileName);
        FileInputStream fs = null;
        
        createDir(remotePath);
        File file = new File(localFileName);
        if (!file.exists()) {
            return false;
        }
        try {
			fs = new FileInputStream(file);
			sftp.put(fs, toFileName);
        } catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			if (fs != null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
		}
        
        return true;
	}
	
	/**
     * 下载文件
     * 
     * @param serverName 服务地址
     * @param port 端口
     * @param userName 用户名称
     * @param password 密码
     * @param remotePath --服务器上目录
     * @param localPath --本地文件目录
     * @param isDel --是否删除原文件
     * @return 0：下载成功，1：下载失败 2： 文件正在上传
     */
	@SuppressWarnings("unchecked")
	public int readFile(String remotePath, String localPath, String remoteBakPath, boolean isDel) throws JSchException {
        int readIsSuccess = 0;
        if (remotePath == null || "".equals(remotePath)) {
            readIsSuccess = 1;
            return readIsSuccess;
        }
        
        // 首先判断文件是否正在上传
        try {
            Vector<LsEntry> directorys = listFiles(remotePath);
            if (directorys.size() > 0) {
                for (int k = 0; k < directorys.size(); k++) {
                    LsEntry lsEntry = directorys.get(k);
                    if (null != lsEntry) {
                        boolean isRead = true;
                        // 先判断文件名称的合法性 如果不合法就不继续下面的工作
                        isRead = judgeFilesName(lsEntry.getFilename());

                        // 如果条件匹配就进行文件的下载
                        if (isRead) {
                            try {
                                String filename = lsEntry.getFilename();
                                if (".".equals(filename) || "..".equals(filename)) {
                                    continue;
                                }
                                localPath = localPath.lastIndexOf("/") > -1 ? localPath : localPath + "/";
                                FileUtil.dynamicCreateDir(localPath);
                                int flag = download(remotePath, filename, localPath + "/" + filename);
                                if (flag == 0) {
                                    readIsSuccess = 1;
                                } else {
                                    // 复制文件
                                    if (!StringUtils.isEmpty(remoteBakPath)) {
                                        upload(remoteBakPath, localPath + "/" + filename);
                                    }
                                    // deleteFile
                                    if (isDel) {
                                        delete(remotePath, filename);
                                    }
                                }
                            } catch (Exception e) {
                                readIsSuccess = 3;
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e1) {
            readIsSuccess = 2;
            e1.printStackTrace();
        } finally {
        	sftp.disconnect();
        }
        return readIsSuccess;
    }
	
	/**
	 * 将文件内容存入文件，并将文件放到FTP服务器指定目录。按照GBK编码进行转换<br>
	 * 若FTP服务器目录下存在该文件，则覆盖该文件
	 * @author wenjin.gao
	 * @param directory 存放文件目录
	 * @param fileName 文件名
	 * @param fileContent 文件内容
	 * @param sftp
	 * @return 0：操作成功，1：操作失败
	 * @throws SftpException 文件目录不存在
	 */
	public int uploadStringToFTP(String directory, String fileName, String fileContent) throws SftpException{
		return uploadStringToFTP(directory, fileName, fileContent, "GBK");
	}
	
	/**
	 * 将文件内容存入文件，并将文件放到FTP服务器指定目录。按照字符编码进行转换<br>
	 * 若FTP服务器目录下存在该文件，则覆盖该文件
	 * @author wenjin.gao
	 * @param directory 存放文件目录
	 * @param fileName 文件名
	 * @param fileContent 文件内容
	 * @param charset 字符串编码
	 * @return 0：操作成功，1：操作失败
	 * @throws SftpException
	 */
	public int uploadStringToFTP(String directory, String fileName, String fileContent, String charset) throws SftpException{
		try {
			sftp.cd(directory);
			sftp.put(new ByteArrayInputStream(fileContent.getBytes(charset)), fileName);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return 1;
		}
		return 0;
	}
	
	/**
	 * 读取指定的文件内容，按照GBK编码进行转换
	 * @author wenjin.gao
	 * @param directory 读取文件的目录
	 * @param fileName 读取文件名 
	 * @return
	 * @throws SftpException
	 */
	public String readFTPToString(String directory, String fileName) throws SftpException{
		return readFTPToString(directory, fileName, "GBK");
	}
	
	/**
	 * 读取指定的文件内容，按照字符编码进行转换
	 * @author wenjin.gao
	 * @param directory 读取文件的目录
	 * @param fileName 读取文件名 
	 * @param charset 字符串编码
	 * @return
	 * @throws SftpException
	 */
	public String readFTPToString(String directory, String fileName, String charset) throws SftpException{
		sftp.cd(directory);
		InputStream input = sftp.get(fileName);
		StringBuffer sb = new StringBuffer();
		String s = "";
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(input, charset));
			while((s = in.readLine()) != null){
				sb.append(s);
				sb.append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}
	
	/**
     * 判断文件名称是否符合匹配字符
     * 
     * @param fileName 文件名称
     * @param splitStr 匹配符
     * @return
     */
    private static boolean judgeFilesName(String fileName) {
        boolean flag = true;
        if ("".equals(fileName) && fileName == null) {
            flag = false;
            return flag;
        }

        String[] name = fileName.split("\\.");
        if (name.length == 0) {
            flag = false;
            return flag;
        }
        for (int i = 0; i < name.length; i++) {
            if (name[i] == null || name[i].equals("")) {
                flag = false;
                return flag;
            }
        }

        return flag;
    }
	
	public ChannelSftp getSftp() {
		return sftp;
	}

	public void setSftp(ChannelSftp sftp) {
		this.sftp = sftp;
	}

}

package com.fir.gateway.uttls;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * @author fir
 */
public class MD5Utils {
    private static MessageDigest md;
    static {
        try {
            //初始化摘要对象
            md = MessageDigest.getInstance("md5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    /**
     * 获得字符串的md5值
     *
     * @param str 字符串
     * @return MD5值
     */
    public static String generateMd5ForString(String str){
        //更新摘要数据
        md.update(str.getBytes());
        //生成摘要数组
        byte[] digest = md.digest();
        //清空摘要数据，以便下次使用
        md.reset();
        return formatByteArrayToString(digest);
    }


    /**
     * 获得文件的md5值
     *
     * @param file 文件对象
     * @return 文件MD5值
     * @throws IOException 文件流读取异常
     */
    public static String generateMd5ForFile(File file) throws IOException {
        //创建文件输入流
        FileInputStream fis = new FileInputStream(file);
        //将文件中的数据写入md对象
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fis.read(buffer)) != -1) {
            md.update(buffer, 0, len);
        }
        fis.close();
        //生成摘要数组
        byte[] digest = md.digest();
        //清空摘要数据，以便下次使用
        md.reset();
        return formatByteArrayToString(digest);
    }


    /**
     * 将摘要字节数组转换为md5值
     *
     * @param digest 字节数组
     * @return MD5数值
     */
    public static String formatByteArrayToString(byte[] digest) {
        //创建sb用于保存md5值
        StringBuilder sb = new StringBuilder();
        int temp;
        for (byte b : digest) {
            //将数据转化为0到255之间的数据
            temp = b & 0xff;
            if (temp < 16) {
                sb.append(0);
            }
            //Integer.toHexString(temp)将10进制数字转换为16进制
            sb.append(Integer.toHexString(temp));
        }
        return sb.toString();
    }
}
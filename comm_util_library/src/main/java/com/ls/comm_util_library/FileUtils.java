package com.ls.comm_util_library;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;

/**
 * 文件帮助类（包括流帮助）
 */
public class FileUtils {

    /**
     * 创建一个目录
     * @param path 需要创建目录路径
     * @return 还是目录路径
     */
    public static String createDir(String path){
        File file = new File(path);
        file.mkdirs();
        return path;
    }

    /**
     * 获取文件输入流（装饰了BufferedInputStream）
     * @param file 需要读取的文件
     * @return 文件输入流
     * @throws FileNotFoundException 读文件会产生异常，调用方自行处理
     */
    public static InputStream getInputStreamByFile(File file) throws FileNotFoundException {
        if(!file.exists()){
            return null;
        }
        return new BufferedInputStream(new FileInputStream(file));
    }

    /**
     * 获取文件输入流（装饰了BufferedInputStream）
     * @param path 需要读取的文件路径
     * @return 文件输入流
     * @throws FileNotFoundException 获取输入流会产生异常，调用方自行处理
     */
    public static InputStream getInputStreamByFile(String path) throws FileNotFoundException {
        return getInputStreamByFile(new File(path));
    }

    /**
     * 获取文件输出流（装饰了BufferedOutputStream）
     * @param file 文件
     * @return 输入流
     * @throws FileNotFoundException 获取输出流会产生异常，调用方自行处理
     */
    public static OutputStream getOutputStreamByFile(File file) throws FileNotFoundException {
        if(!file.exists()){
            return null;
        }
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    /**
     * 获取文件输出流（装饰了BufferedOutputStream）
     * @param path 文件路径
     * @return 输入流
     * @throws FileNotFoundException 获取输出流会产生异常，调用方自行处理
     */
    public static OutputStream getOutputStreamByFile(String path) throws FileNotFoundException {
        return getOutputStreamByFile(new File(path));
    }

    /**
     * 读文件并转换成字符串，当然要保证文件中存的是字符串
     * @param path 需要读取的文件路径
     * @return 转换好的字符串
     */
    public static String readFileText(String path){
        return readFileText(new File(path));
    }

    /**
     * 读文件并转换成字符串，当然要保证文件中存的是字符串
     * @param path 需要读取的文件路径
     * @param isOnceRead 是否一次性读完流的内容（如果内容比较多设置为false）
     * @return 转换好的字符串
     */
    public static String readFileText(String path,boolean isOnceRead){
        return readFileText(new File(path),isOnceRead);
    }

    /**
     * 读文件并转换成字符串，当然要保证文件中存的是字符串
     * @param file 需要读取的文件
     * @return 转换好的字符串
     */
    public static String readFileText(File file){
        return readFileText(file,false);
    }

    /**
     * 读文件并转换成字符串，当然要保证文件中存的是字符串
     * @param file 需要读取的文件
     * @param isOnceRead 是否一次性读完流的内容（如果内容比较多设置为false）
     * @return 转换好的字符串
     */
    public static String readFileText(File file,boolean isOnceRead){
        String result = "";
        try {
            result = inputStream2String(getInputStreamByFile(file),true,isOnceRead);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 读文件，返回字节数组
     * @param file 需要读取的文件
     * @return 字节数组
     */
    public static byte[] readFile(File file){
        try {
            return inputStream2Bytes(getInputStreamByFile(file),true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 读文件返回字节数组
     * @param path 需要读取的文件路径
     * @return 字节数组
     */
    public static byte[] readFile(String path){
        return readFile(new File(path));
    }

    /**
     * 流转字节数组
     * @param is 输入流
     * @param isCloseStream 是否在方法内关闭流
     * @return 字节数组
     */
    public static byte[] inputStream2Bytes(InputStream is,boolean isCloseStream){
        if(is == null) return null;
        if(!(is instanceof BufferedInputStream)){
            is = new BufferedInputStream(is);
        }
        int read;
        byte[] bs = new byte[2 * 1024];
        byte[] bytes = null;
        byte[] lastBytes = null;
        try {
            while((read = is.read(bs)) != -1){
                if(bytes == null){
                    bytes = Arrays.copyOfRange(bs,0,read);
                }
                else{
                    lastBytes = bytes;
                    bytes = new byte[lastBytes.length + read];
                    System.arraycopy(lastBytes,0,bytes,0,lastBytes.length);
                    System.arraycopy(bs,0,bytes,lastBytes.length,read);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(isCloseStream){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bytes;
    }

    /**
     * 流转字符串，当然要确保流传输的是字符
     * @param is 输入流
     * @param isCloseStream 是否在方法内关闭流
     * @param isOnceRead 是否一次性读完流的内容（如果内容比较多设置为false）
     * @return 转换好的字符串
     */
    public static String inputStream2String(InputStream is,boolean isCloseStream,boolean isOnceRead){
        byte[] bytes;
        String result = "";
        if(is == null) return result;
        if(!(is instanceof BufferedInputStream)){
            is = new BufferedInputStream(is);
        }
        int read;
        try {
            if(isOnceRead) {
                bytes = new byte[is.available()];
                read = is.read(bytes);
                result = new String(bytes);
            }
            else{
                bytes = new byte[2 * 1024];
                StringBuilder sb = new StringBuilder();
                while((read = is.read(bytes)) != -1){
                    sb.append(new String(bytes,0,read));
                }
                result = sb.toString();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(isCloseStream){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    /**
     * 写入文件，将字符串
     * @param text 需要写入的字符串
     * @param file 目标文件
     */
    public static void writeFile(String text, File file){
        writeFile(text.getBytes(),file);
    }

    /**
     * 写入文件，将字符串
     * @param text 需要写入的字符串
     * @param path 目标文件路径
     */
    public static void writeFile(String text, String path){
        writeFile(text,new File(path));
    }

    /**
     * 写入文件，将字节数组
     * @param bytes 需要写入的字节数组
     * @param path 目标文件路径
     */
    public static void writeFile(byte[] bytes,String path){
        writeFile(bytes,new File(path));
    }

    /**
     * 写入文件，将字节数组
     * @param bytes 需要写入的字节数组
     * @param file 目标文件
     */
    public static void writeFile(byte[] bytes,File file){
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        OutputStream os = null;
        try {
            os = getOutputStreamByFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        write(bytes,os,true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param path 目标文件路径
     */
    public static void writeFile(InputStream is,String path){
        writeFile(is,new File(path),true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param path 目标文件路径
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeFile(InputStream is,String path,boolean isCloseStream){
        writeFile(is,new File(path),isCloseStream);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param file 目标文件
     */
    public static void writeFile(InputStream is,File file){
        writeFile(is, file,true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param file 目标文件
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeFile(InputStream is,File file,boolean isCloseStream){
        if(is == null) return;
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        OutputStream os = null;
        try {
            os = getOutputStreamByFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!(is instanceof BufferedInputStream)){
            is = new BufferedInputStream(is);
        }
        write(is,os,isCloseStream);
    }

    /**
     * 字节数组写输出流
     * @param bytes 字节数组
     * @param out 输入流
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void write(byte[] bytes,OutputStream out,boolean isCloseStream){
        try{
            if(out == null){
                return;
            }
            if(!(out instanceof  BufferedOutputStream)){
                out = new BufferedOutputStream(out);
            }
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(isCloseStream && out != null){
                try {
                    out.flush();
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 输入流写输出流
     * @param in 输入流
     * @param out 输入流
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void write(InputStream in,OutputStream out,boolean isCloseStream){
        try {
            if(in == null || out == null){
                return;
            }
            if(!(in instanceof BufferedInputStream)){
                in = new BufferedInputStream(in);
            }
            if(!(out instanceof BufferedOutputStream)){
                out = new BufferedOutputStream(out);
            }
            byte[] bytes = new byte[2 * 1024];
            int read;
            while((read = in.read(bytes)) != -1){
                out.write(bytes,0,read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(isCloseStream) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (out != null) {
                    try {
                        out.flush();
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 删除文件（包括目录）
     * @param file 目标文件
     */
    public static void deleteFile(File file) {
        if (file.exists()) {
            if (file.isDirectory()) {
                deleteDir(file);
            } else {
                file.delete();
            }
        }
    }

    /**
     * 删除文件（包括目录）
     * @param path 目标文件路径
     */
    public static void deleteFile(String path) {
        deleteFile(new File(path));
    }

    private static void deleteDir(File file){
        File[] files = file.listFiles();
        for (File item: files){
            if(item.isDirectory()){
                deleteDir(item);
                item.delete();
            }
            else{
                item.delete();
            }
        }
    }

    /**
     * 计算文件大小（包括目录）
     * @param file 目标文件
     * @return 返回数字，需要自己转换
     */
    public static long calcFileSize(File file) {
        long size = 0;
        if (file.exists()) {
            try {
                if (file.isDirectory()) {
                    size = calcDirSize(file);
                } else {
                    size = file.length();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 计算文件大小（包括目录）
     * @param path 目标文件路径
     * @return 返回数字，需要自己转换
     */
    public static long calcFileSize(String path) {
        return calcFileSize(new File(path));
    }

    private static long calcDirSize(File file){
        long size = 0;
        File[] files = file.listFiles();
        for (File item: files){
            if(item.isDirectory()){
                size += calcDirSize(item);
            }
            else{
                size += item.length();
            }
        }
        return size;
    }

}

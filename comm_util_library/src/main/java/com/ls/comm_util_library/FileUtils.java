package com.ls.comm_util_library;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Arrays;

/**
 * 文件帮助类（包括流帮助）
 */
public class FileUtils {

    private static int COPY_ONE_SIZE = 4 * 1024;

    public static void setCopyOneSize(int copyOneSize){
        COPY_ONE_SIZE = copyOneSize;
    }

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
     * @param path 文件路径
     * @return 输入流
     * @throws FileNotFoundException 获取输出流会产生异常，调用方自行处理
     */
    public static OutputStream getOutputStreamByFile(String path) throws FileNotFoundException {
        return getOutputStreamByFile(path,false);
    }

    /**
     * 获取文件输出流（装饰了BufferedOutputStream）
     * @param path 文件路径
     * @return 输入流
     * @throws FileNotFoundException 获取输出流会产生异常，调用方自行处理
     */
    public static OutputStream getOutputStreamByFile(String path, boolean append) throws FileNotFoundException {
        return getOutputStreamByFile(new File(path),append);
    }

    /**
     * 获取文件输出流（装饰了BufferedOutputStream）
     * @param file 文件
     * @return 输入流
     * @throws FileNotFoundException 获取输出流会产生异常，调用方自行处理
     */
    public static OutputStream getOutputStreamByFile(File file) throws FileNotFoundException {
        return getOutputStreamByFile(file,false);
    }

    /**
     * 获取文件输出流（装饰了BufferedOutputStream）
     * @param file 文件
     * @param append 内容是否追于与文件尾部
     * @return 输入流
     * @throws FileNotFoundException 获取输出流会产生异常，调用方自行处理
     */
    public static OutputStream getOutputStreamByFile(File file, boolean append) throws FileNotFoundException {
        return new BufferedOutputStream(new FileOutputStream(file,append));
    }

    /**
     * 读文件并转换成字符串，当然要保证文件中存的是字符串
     * @param path 需要读取的文件路径
     * @return 转换好的字符串
     */
    public static String readFileText(String path){
        return readFileText(path,true);
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
     * @return 字节数组
     */
    public static byte[] inputStream2Bytes(InputStream is){
        return inputStream2Bytes(is,true);
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
        byte[] bs = new byte[COPY_ONE_SIZE];
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
     * @return 转换好的字符串
     */
    public static String inputStream2String(InputStream is){
        return inputStream2String(is,true);
    }

    /**
     * 流转字符串，当然要确保流传输的是字符
     * @param is 输入流
     * @param isCloseStream 是否在方法内关闭流
     * @return 转换好的字符串
     */
    public static String inputStream2String(InputStream is,boolean isCloseStream){
        return inputStream2String(is,isCloseStream,true);
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
                bytes = new byte[COPY_ONE_SIZE];
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
     * @param path 目标文件路径
     */
    public static void writeFile(String text, String path){
        writeFile(text,path,false);
    }

    /**
     * 写入文件，将字符串
     * @param text 需要写入的字符串
     * @param path 目标文件路径
     * @param append 是否追加写入文件
     */
    public static void writeFile(String text, String path,boolean append){
        writeFile(text,new File(path),append);
    }

    /**
     * 写入文件，将字符串
     * @param text 需要写入的字符串
     * @param file 目标文件
     */
    public static void writeFile(String text, File file){
        writeFile(text,file,false);
    }

    /**
     * 写入文件，将字符串
     * @param text 需要写入的字符串
     * @param file 目标文件
     * @param append 是否追加写入文件
     */
    public static void writeFile(String text, File file,boolean append){
        writeFile(text.getBytes(),file,append);
    }

    /**
     * 写入文件，将字节数组
     * @param bytes 需要写入的字节数组
     * @param path 目标文件路径
     */
    public static void writeFile(byte[] bytes,String path){
        writeFile(bytes,path,false);
    }

    /**
     * 写入文件，将字节数组
     * @param bytes 需要写入的字节数组
     * @param path 目标文件路径
     * @param append 是否追加写入文件
     */
    public static void writeFile(byte[] bytes,String path,boolean append){
        writeFile(bytes,new File(path),append);
    }

    /**
     * 写入文件，将字节数组
     * @param bytes 需要写入的字节数组
     * @param file 目标文件
     */
    public static void writeFile(byte[] bytes,File file){
        writeFile(bytes, file,false);
    }

    /**
     * 写入文件，将字节数组
     * @param bytes 需要写入的字节数组
     * @param file 目标文件
     * @param append 是否追加写入文件
     */
    public static void writeFile(byte[] bytes,File file,boolean append){
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        OutputStream os = null;
        try {
            os = getOutputStreamByFile(file,append);
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
        writeFile(is,path,true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param path 目标文件路径
     */
    public static void writeFile(InputStream is,String path,boolean append){
        writeFile(is,path,append,true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param path 目标文件路径
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeFile(InputStream is,String path,boolean append,boolean isCloseStream){
        writeFile(is,path,null,append,isCloseStream);
    }

    /**
     * 使用流复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     */
    public static void copy(String src,String dest){
        copy(src, dest,false);
    }

    /**
     * 使用流复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @param append 是否追加写入文件
     */
    public static void copy(String src,String dest,boolean append){
        copy(src,dest,append,null);
    }

    /**
     * 使用流复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @param listener 写入监听
     */
    public static void copy(String src,String dest,IWriteListener listener){
        copy(src,dest,false,listener);
    }

    /**
     * 使用流复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @param append 是否追加写入文件
     * @param listener 写入监听
     */
    public static void copy(String src,String dest,boolean append,IWriteListener listener){
        copy(new File(src),new File(dest),append,listener);
    }

    /**
     * 使用流复制文件
     * @param src 源文件
     * @param dest 目标文件
     */
    public static void copy(File src,File dest){
        copy(src, dest,false);
    }

    /**
     * 使用流复制文件
     * @param src 源文件
     * @param dest 目标文件
     * @param append 是否追加写入文件
     */
    public static void copy(File src,File dest,boolean append){
        copy(src,dest,append,null);
    }

    /**
     * 使用流复制文件
     * @param src 源文件
     * @param dest 目标文件
     * @param listener 写入监听
     */
    public static void copy(File src,File dest,IWriteListener listener){
        copy(src,dest,false,listener);
    }

    /**
     * 使用流复制文件
     * @param src 源文件
     * @param dest 目标文件
     * @param append 是否追加写入文件
     * @param listener 写入监听
     */
    public static void copy(File src,File dest,boolean append,IWriteListener listener){
        try {
            write(getInputStreamByFile(src),getOutputStreamByFile(dest,append),listener);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用nio 将流内容内存映射写文件，适用于大文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param path 目标文件路径
     * @param length 文件长度
     */
    public static void mappedWriteFile(InputStream is, String path,long position, long length){
        mappedWriteFile(is, path,position, length,null);
    }

    /**
     * 使用nio 将流内容内存映射写文件，适用于大文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param path 目标文件路径
     * @param length 文件长度
     * @param listener 写入监听
     */
    public static void mappedWriteFile(InputStream is, String path,long position, long length, IWriteListener listener){
        mappedWriteFile(is, path,position, length,listener, true);
    }

    /**
     * 使用nio 将流内容内存映射写文件，适用于大文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param path 目标文件路径
     * @param length 文件长度
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void mappedWriteFile(InputStream is, String path,long position, long length, boolean isCloseStream){
        mappedWriteFile(is, path,position, length,null, isCloseStream);
    }

    /**
     * 使用nio 将流内容内存映射写文件，适用于大文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param path 目标文件路径
     * @param length 文件长度
     * @param listener 写入监听
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void mappedWriteFile(InputStream is, String path,long position, long length, IWriteListener listener, boolean isCloseStream){
        mappedWriteFile(is, new File(path),position, length, listener, isCloseStream);
    }

    /**
     * 使用nio 将流内容内存映射写文件，适用于大文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param file 目标文件
     * @param length 文件长度
     */
    public static void mappedWriteFile(InputStream is, File file,long position, long length){
        mappedWriteFile(is, file,position, length,null);
    }

    /**
     * 使用nio 将流内容内存映射写文件，适用于大文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param file 目标文件
     * @param length 文件长度
     * @param listener 写入监听
     */
    public static void mappedWriteFile(InputStream is, File file,long position, long length, IWriteListener listener){
        mappedWriteFile(is, file,position, length,listener, true);
    }

    /**
     * 使用nio 将流内容内存映射写文件，适用于大文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param file 目标文件
     * @param length 文件长度
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void mappedWriteFile(InputStream is, File file,long position, long length, boolean isCloseStream){
        mappedWriteFile(is, file,position, length,null, isCloseStream);
    }

    /**
     * 使用nio 将流内容内存映射写文件，适用于大文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param file 目标文件
     * @param length 文件长度
     * @param listener 写入监听
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void mappedWriteFile(InputStream is, File file,long position, long length, IWriteListener listener, boolean isCloseStream){
        if(position >= length) return;
        if(is == null) return;
        if(!(is instanceof BufferedInputStream)){
            is = new BufferedInputStream(is);
        }
        RandomAccessFile randomAccessFile = null;
        FileChannel channel = null;
        try {
            randomAccessFile = new RandomAccessFile(file,"rw");
            randomAccessFile.setLength(length);
            randomAccessFile.seek(position);
            channel = randomAccessFile.getChannel();
            MappedByteBuffer mbb = channel.map(FileChannel.MapMode.READ_WRITE,0,length);
            byte[] buffer = new byte[COPY_ONE_SIZE];
            int read = 0;
            long alreadyRead = 0;
            while((read = is.read(buffer)) != -1){
                mbb.put(buffer,0,read);
                alreadyRead += read;
                onWrite(listener,alreadyRead);
            }
            onSuccess(listener);
        } catch (IOException e) {
            e.printStackTrace();
            onError(listener,e);
        }
        finally {
            if(channel != null) {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isCloseStream){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用nio 内存映射复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     */
    public static void mappedCopy(String src, String dest){
        mappedCopy(src, dest,null);
    }

    /**
     * 使用nio 内存映射复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @param listener 写入监听
     */
    public static void mappedCopy(String src, String dest, IWriteListener listener){
        mappedCopy(new File(src), new File(dest),listener);
    }

    /**
     * 使用nio 内存映射复制文件
     * @param src 源文件
     * @param dest 目标文件
     */
    public static void mappedCopy(File src, File dest){
        mappedCopy(src, dest,null);
    }

    /**
     * 使用nio 内存映射复制文件
     * @param src 源文件
     * @param dest 目标文件
     * @param listener 写入监听
     */
    public static void mappedCopy(File src, File dest, IWriteListener listener){
        RandomAccessFile srcFile = null;
        RandomAccessFile destFile = null;
        FileChannel srcChannel = null;
        FileChannel destChannel = null;
        try {
            srcFile = new RandomAccessFile(src, "r");
            destFile = new RandomAccessFile(dest, "rw");
            srcChannel = srcFile.getChannel();
            destChannel = destFile.getChannel();
            long fileSize = srcChannel.size();
            MappedByteBuffer srcMbb = srcChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileSize);
            MappedByteBuffer destMbb = destChannel.map(FileChannel.MapMode.READ_WRITE, 0, fileSize);
            int bufferSize = COPY_ONE_SIZE;
            long pages = 1;
            int tailSize = 0;
            if(fileSize < bufferSize){
                pages = 1;
                bufferSize = (int) fileSize;
            }
            else{
                tailSize = (int) (fileSize % bufferSize);
                if(fileSize % bufferSize == 0) {
                    pages = (int) (fileSize / bufferSize);
                }
                else{
                    pages = (int) (fileSize / bufferSize) + 1;
                }
            }
            byte[] buffer = new byte[bufferSize];
            long alreadyRead = 0;
            for (long i = 0; i < pages; i++){
                if(i == pages - 1){
                    bufferSize = tailSize;
                }
                srcMbb.get(buffer,0,bufferSize);
                destMbb.put(buffer,0,bufferSize);
                alreadyRead += bufferSize;
                onWrite(listener,alreadyRead);
            }
            onSuccess(listener);
        } catch (IOException e) {
            e.printStackTrace();
            onError(listener,e);
        }
        finally {
            if(srcChannel != null){
                try {
                    srcChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(destChannel != null){
                try {
                    destChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(srcFile != null){
                try {
                    srcFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(destFile != null){
                try {
                    destFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用RandomAccessFile 将流内容写入文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param path 目标文件路径
     * @param length 文件长度
     */
    public static void writeRandomAccessFile(InputStream is, String path, long length){
        writeRandomAccessFile(is,path,length,null);
    }

    /**
     * 使用RandomAccessFile 将流内容写入文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param path 目标文件路径
     * @param length 文件长度
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeRandomAccessFile(InputStream is, String path, long length, boolean isCloseStream){
        writeRandomAccessFile(is,path,length,null,isCloseStream);
    }

    /**
     * 使用RandomAccessFile 将流内容写入文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param path 目标文件路径
     * @param length 文件长度
     * @param listener 写入监听
     */
    public static void writeRandomAccessFile(InputStream is, String path, long length, IWriteListener listener){
        writeRandomAccessFile(is,path,length,listener,true);
    }

    /**
     * 使用RandomAccessFile 将流内容写入文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param path 目标文件路径
     * @param length 文件长度
     * @param listener 写入监听
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeRandomAccessFile(InputStream is, String path, long length, IWriteListener listener, boolean isCloseStream){
        writeRandomAccessFile(is,new File(path),length,listener,isCloseStream);
    }

    /**
     * 使用RandomAccessFile 将流内容写入文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param file 目标文件
     * @param length 文件长度
     */
    public static void writeRandomAccessFile(InputStream is, File file, long length){
        writeRandomAccessFile(is, file, length, null);
    }

    /**
     * 使用RandomAccessFile 将流内容写入文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param file 目标文件
     * @param length 文件长度
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeRandomAccessFile(InputStream is, File file, long length ,boolean isCloseStream){
        writeRandomAccessFile(is, file, length, null,isCloseStream);
    }

    /**
     * 使用RandomAccessFile 将流内容写入文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param file 目标文件
     * @param length 文件长度
     * @param listener 写入监听
     */
    public static void writeRandomAccessFile(InputStream is, File file, long length, IWriteListener listener){
        writeRandomAccessFile(is, file, length, listener,true);
    }

    /**
     * 使用RandomAccessFile 将流内容写入文件
     * （不知晓文件长度的情况下不能使用这个方法）
     * @param is 输入流
     * @param file 目标文件
     * @param length 文件长度
     * @param listener 写入监听
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeRandomAccessFile(InputStream is, File file, long length, IWriteListener listener, boolean isCloseStream){
        if(is == null) return;
        if(!(is instanceof BufferedInputStream)){
            is = new BufferedInputStream(is);
        }
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file,"rw");
            randomAccessFile.setLength(length);
            byte[] buffer = new byte[COPY_ONE_SIZE];
            int read = 0;
            long alreadyRead = 0;
            while((read = is.read(buffer)) > 0){
                randomAccessFile.write(buffer,0,read);
                alreadyRead += read;
                onWrite(listener,alreadyRead);
            }
            onSuccess(listener);
        } catch (IOException e) {
            e.printStackTrace();
            onError(listener,e);
        }
        finally {
            if(randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isCloseStream) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 插入文件，将字符串
     * @param text 字符串
     * @param path 目标文件路径
     * @param position 插入文件的索引
     */
    public static void insertFile(String text,String path,long position){
        insertFile(text, new File(path), position);
    }

    /**
     * 插入文件，将字符串
     * @param text 字符串
     * @param file 目标文件
     * @param position 插入文件的索引
     */
    public static void insertFile(String text,File file,long position){
        insertFile(text.getBytes(), file, position);
    }

    /**
     * 插入文件，将字节数组
     * @param bs 字节数组
     * @param path 目标文件路径
     * @param position 插入文件的索引
     */
    public static void insertFile(byte[] bs,String path,long position){
        insertFile(bs, new File(path), position);
    }

    /**
     * 插入文件，将字节数组
     * @param bs 字节数组
     * @param file 目标文件
     * @param position 插入文件的索引
     */
    public static void insertFile(byte[] bs,File file,long position){
        if(file == null) return;
        RandomAccessFile accessFile = null;
        File tempFile = null;
        InputStream inTemp = null;
        OutputStream outTemp = null;
        try {
            accessFile = new RandomAccessFile(file, "rw");
            //创建一个临时文件
            tempFile = File.createTempFile("temp", null);
            inTemp = getInputStreamByFile(tempFile);
            outTemp = getOutputStreamByFile(tempFile);
            //设置插入点
            accessFile.seek(position);
            //开始保存插入点后的数据
            byte[] bytes = new byte[COPY_ONE_SIZE];
            int read = 0;
            while((read = accessFile.read(bytes)) > 0){
                outTemp.write(bytes,0,read);
            }
            outTemp.flush();
            //开始进行数据插入
            accessFile.seek(position);
            accessFile.write(bs);
            while ((read = inTemp.read(bytes)) > 0){
                accessFile.write(bytes,0,read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(inTemp != null){
                try {
                    inTemp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outTemp != null){
                try {
                    outTemp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(accessFile != null){
                try {
                    accessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(tempFile != null){
                tempFile.delete();
            }
        }
    }

    /**
     * 插入文件，将文件流
     * @param is 文件流
     * @param path 目标文件路径
     * @param position 插入文件的索引
     */
    public static void insertFile(InputStream is,String path,long position){
        insertFile(is, new File(path), position,true);
    }

    /**
     * 插入文件，将文件流
     * @param is 文件流
     * @param path 目标文件路径
     * @param position 插入文件的索引
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void insertFile(InputStream is,String path,long position,boolean isCloseStream){
        insertFile(is,new File(path),position,isCloseStream);
    }

    /**
     * 插入文件，将文件流
     * @param is 文件流
     * @param file 目标文件
     * @param position 插入文件的索引
     */
    public static void insertFile(InputStream is,File file,long position){
        insertFile(is, file, position,true);
    }

    /**
     * 插入文件，将文件流
     * @param is 文件流
     * @param file 目标文件
     * @param position 插入文件的索引
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void insertFile(InputStream is,File file,long position,boolean isCloseStream){
        if(is == null || file == null) return;
        RandomAccessFile accessFile = null;
        File tempFile = null;
        InputStream inTemp = null;
        OutputStream outTemp = null;
        try {
            accessFile = new RandomAccessFile(file, "rw");
            //创建一个临时文件
            tempFile = File.createTempFile("temp", null);
            inTemp = getInputStreamByFile(tempFile);
            outTemp = getOutputStreamByFile(tempFile);
            //设置插入点
            accessFile.seek(position);
            //开始保存插入点后的数据
            byte[] bytes = new byte[COPY_ONE_SIZE];
            int read = 0;
            while((read = accessFile.read(bytes)) > 0){
                outTemp.write(bytes,0,read);
            }
            //开始进行数据插入
            accessFile.seek(position);
            while ((read = is.read(bytes)) > 0){
                accessFile.write(bytes,0,read);
            }
            while ((read = inTemp.read(bytes)) > 0){
                accessFile.write(bytes,0,read);
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
            if(inTemp != null){
                try {
                    inTemp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outTemp != null){
                try {
                    outTemp.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(accessFile != null){
                try {
                    accessFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(tempFile != null){
                tempFile.delete();
            }
        }
    }

    /**
     * 使用RandomAccessFile 复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     */
    public static void randomAccessCopy(String src, String dest){
        randomAccessCopy(src, dest,null);
    }

    /**
     * 使用RandomAccessFile 复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @param listener 写入监听
     */
    public static void randomAccessCopy(String src, String dest, IWriteListener listener){
        randomAccessCopy(new File(src), new File(dest),listener);
    }

    /**
     * 使用RandomAccessFile 复制文件
     * @param src 源文件
     * @param dest 目标文件
     */
    public static void randomAccessCopy(File src, File dest){
        randomAccessCopy(src, dest,null);
    }

    /**
     * 使用RandomAccessFile 复制文件
     * 该方法无法追加写入
     * @param src 源文件
     * @param dest 目标文件
     * @param listener 写入监听
     */
    public static void randomAccessCopy(File src, File dest, IWriteListener listener){
        RandomAccessFile srcFile = null;
        RandomAccessFile destFile = null;
        try {
            srcFile = new RandomAccessFile(src, "r");
            destFile = new RandomAccessFile(dest, "rw");
            byte[] buffer = new byte[COPY_ONE_SIZE];
            int read = 0;
            long alreadyRead = 0;
            while((read = srcFile.read(buffer)) > 0){
                destFile.write(buffer,0,read);
                alreadyRead += read;
                onWrite(listener,alreadyRead);
            }
            onSuccess(listener);
        } catch (IOException e) {
            e.printStackTrace();
            onError(listener,e);
        }
        finally {
            if(srcFile != null){
                try {
                    srcFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(destFile != null){
                try {
                    destFile.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param path 目标文件路径
     * @param listener 写入监听
     */
    public static void writeFile(InputStream is, String path, IWriteListener listener){
        writeFile(is,path,listener,true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param path 目标文件路径
     * @param listener 写入监听
     */
    public static void writeFile(InputStream is, String path, IWriteListener listener,boolean append){
        writeFile(is,path,listener,append,true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param path 目标文件路径
     * @param listener 写入监听
     * @param append 是否追加写入文件
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeFile(InputStream is, String path, IWriteListener listener,boolean append, boolean isCloseStream){
        writeFile(is,new File(path),listener,append,isCloseStream);
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
     * @param append 是否追加写入文件
     */
    public static void writeFile(InputStream is,File file,boolean append){
        writeFile(is, file,append, true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param file 目标文件
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeFile(InputStream is,File file,boolean append,boolean isCloseStream){
        writeFile(is, file,null,append, isCloseStream);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param file 目标文件
     * @param listener 写入监听
     */
    public static void writeFile(InputStream is, File file, IWriteListener listener){
        writeFile(is, file,listener,false);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param file 目标文件
     * @param listener 写入监听
     */
    public static void writeFile(InputStream is, File file, IWriteListener listener,boolean append){
        writeFile(is, file,listener,append,true);
    }

    /**
     * 写入文件，将输入流
     * @param is 输入流
     * @param file 目标文件
     * @param listener 写入监听
     * @param append 是否追加写入
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void writeFile(InputStream is, File file, IWriteListener listener,boolean append, boolean isCloseStream){
        if(is == null) return;
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        OutputStream os = null;
        try {
            os = getOutputStreamByFile(file,append);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(!(is instanceof BufferedInputStream)){
            is = new BufferedInputStream(is);
        }
        write(is,os,listener,isCloseStream);
    }

    /**
     * 字节数组写输出流
     * @param text 字符串
     * @param out 输入流
     */
    public static void write(String text,OutputStream out){
        write(text.getBytes(), out, true);
    }

    /**
     * 字节数组写输出流
     * @param text 字符串
     * @param out 输入流
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void write(String text,OutputStream out,boolean isCloseStream){
        write(text.getBytes(), out, isCloseStream);
    }

    /**
     * 字节数组写输出流
     * @param bytes 字节数组
     * @param out 输入流
     */
    public static void write(byte[] bytes,OutputStream out){
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
            if(out != null){
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
     * @param out 输出流
     */
    public static void write(InputStream in,OutputStream out){
        write(in, out, null);
    }

    /**
     * 输入流写输出流
     * @param in 输入流
     * @param out 输出流
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void write(InputStream in,OutputStream out,boolean isCloseStream){
        write(in, out, null, isCloseStream);
    }

    /**
     * 输入流写输出流
     * @param in 输入流
     * @param out 输出流
     * @param listener 写入监听
     */
    public static void write(InputStream in,OutputStream out,IWriteListener listener){
        write(in, out, listener, true);
    }

    /**
     * 输入流写输出流
     * @param in 输入流
     * @param out 输出流
     * @param listener 写入监听
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void write(InputStream in, OutputStream out, IWriteListener listener, boolean isCloseStream){
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
            byte[] bytes = new byte[COPY_ONE_SIZE];
            long alreadyRead = 0;
            int read;
            while((read = in.read(bytes)) != -1){
                out.write(bytes,0,read);
                alreadyRead += read;
                onWrite(listener,alreadyRead);
            }
            onSuccess(listener);
        } catch (IOException e) {
            e.printStackTrace();
            onError(listener,e);
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
     * 使用nio 将字符串写入文件
     * @param text 字符串
     * @param path 目标文件路径
     */
    public static void nioWrite(String text,String path){
        nioWrite(text,path,false);
    }

    /**
     * 使用nio 将字符串写入文件
     * @param text 字符串
     * @param path 目标文件路径
     * @param append 是否追加写入文件
     */
    public static void nioWrite(String text,String path,boolean append){
        nioWrite(text,new File(path),append);
    }

    /**
     * 使用nio 将字符串写入文件
     * @param text 字符串
     * @param file 目标文件
     */
    public static void nioWrite(String text,File file){
        nioWrite(text,file,false);
    }

    /**
     * 使用nio 将字符串写入文件
     * @param text 字符串
     * @param file 目标文件
     * @param append 是否追加写入文件
     */
    public static void nioWrite(String text,File file,boolean append){
        nioWrite(text.getBytes(),file,append);
    }

    /**
     * 使用nio 将字节数组写入文件
     * @param bytes 字节数组
     * @param path 目标文件路径
     */
    public static void nioWrite(byte[] bytes,String path){
        nioWrite(bytes,path,false);
    }

    /**
     * 使用nio 将字节数组写入文件
     * @param bytes 字节数组
     * @param path 目标文件路径
     * @param append 是否追加写入文件
     */
    public static void nioWrite(byte[] bytes,String path,boolean append){
        nioWrite(bytes,new File(path),append);
    }

    /**
     * 使用nio 将字节数组写入文件
     * @param bytes 字节数组
     * @param file 目标文件
     */
    public static void nioWrite(byte[] bytes,File file){
        nioWrite(bytes,file,false);
    }

    /**
     * 使用nio 将字节数组写入文件
     * @param bytes 字节数组
     * @param file 目标文件
     * @param append 是否追加写入文件
     */
    public static void nioWrite(byte[] bytes,File file,boolean append){
        if(file == null) return;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file,append);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fos == null) return;
        FileChannel outChannel = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        try {
            buffer.flip();
            outChannel.write(buffer);
            buffer.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param path 目标文件路径
     */
    public static void nioWrite(InputStream is,String path){
        nioWrite(is,path,false,true);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param path 目标文件路径
     * @param append 是否追加写入文件
     */
    public static void nioWrite(InputStream is,String path, boolean append){
        nioWrite(is,path,append,true);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param path 目标文件路径
     * @param append 是否追加写入文件
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void nioWrite(InputStream is,String path, boolean append,boolean isCloseStream){
        nioWrite(is,path,null,append,isCloseStream);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param path 目标文件路径
     * @param listener 写入监听
     */
    public static void nioWrite(InputStream is,String path, IWriteListener listener){
        nioWrite(is,path,listener,false);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param path 目标文件路径
     * @param listener 写入监听
     * @param append 是否追加写入文件
     */
    public static void nioWrite(InputStream is,String path, IWriteListener listener, boolean append){
        nioWrite(is,path,listener,append,true);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param path 目标文件路径
     * @param listener 写入监听
     * @param append 是否追加写入文件
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void nioWrite(InputStream is,String path, IWriteListener listener, boolean append,boolean isCloseStream){
        nioWrite(is,new File(path),listener,append,isCloseStream);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param file 目标文件
     */
    public static void nioWrite(InputStream is,File file){
        nioWrite(is,file,false);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param file 目标文件
     * @param append 是否追加写入文件
     */
    public static void nioWrite(InputStream is,File file,boolean append){
        nioWrite(is,file,append,true);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param file 目标文件
     * @param append 是否追加写入文件
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void nioWrite(InputStream is,File file,boolean append, boolean isCloseStream){
        nioWrite(is,file,null,append,isCloseStream);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param file 目标文件
     * @param listener 写入监听
     */
    public static void nioWrite(InputStream is,File file, IWriteListener listener){
        nioWrite(is,file,listener,false,true);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param file 目标文件
     * @param listener 写入监听
     * @param append 是否追加写入文件
     */
    public static void nioWrite(InputStream is,File file, IWriteListener listener,boolean append){
        nioWrite(is,file,listener,append,true);
    }

    /**
     * 使用nio 将流写入文件
     * @param is 输入流
     * @param file 目标文件
     * @param listener 写入监听
     * @param append 是否追加写入文件
     * @param isCloseStream 是否在方法内关闭流
     */
    public static void nioWrite(InputStream is,File file, IWriteListener listener,boolean append, boolean isCloseStream){
        if(is == null || file == null) return;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file,append);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(fos == null) return;
        if(is instanceof FileInputStream){
            nioWrite((FileInputStream)is,fos,listener,isCloseStream);
            if(!isCloseStream){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        FileChannel outChannel = fos.getChannel();
        if(!(is instanceof BufferedInputStream)){
            is = new BufferedInputStream(is);
        }
        byte[] bs = new byte[COPY_ONE_SIZE];
        ByteBuffer buffer = ByteBuffer.wrap(bs);
        int read = 0;
        long alreadyWrite = 0;
        try {
            while ((read = is.read(bs)) > 0) {
                buffer.clear();
                buffer.put(bs,0,read);
                buffer.flip();
                alreadyWrite += outChannel.write(buffer);
                onWrite(listener,alreadyWrite);
            }
            onSuccess(listener);
        } catch (IOException e) {
            e.printStackTrace();
            onError(listener,e);
        }
        finally {
            if(outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(isCloseStream){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用nio 复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     */
    public static void nioCopy(String src,String dest){
        nioCopy(src, dest,false);
    }

    /**
     * 使用nio 复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @param append 是否追加写入
     */
    public static void nioCopy(String src,String dest,boolean append){
        nioCopy(new File(src), new File(dest),append);
    }

    /**
     * 使用nio 复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @param listener 写入监听
     */
    public static void nioCopy(String src,String dest, IWriteListener listener){
        nioCopy(src, dest,listener,false);
    }

    /**
     * 使用nio 复制文件
     * @param src 源文件路径
     * @param dest 目标文件路径
     * @param listener 写入监听
     * @param append 是否追加写入
     */
    public static void nioCopy(String src,String dest, IWriteListener listener,boolean append){
        nioCopy(new File(src), new File(dest),listener,append);
    }

    /**
     * 使用nio 复制文件
     * @param src 源文件
     * @param dest 目标文件
     */
    public static void nioCopy(File src,File dest){
        nioCopy(src, dest,false);
    }

    /**
     * 使用nio 复制文件
     * @param src 源文件
     * @param dest 目标文件
     * @param append 是否追加写入
     */
    public static void nioCopy(File src,File dest,boolean append){
        try {
            nioMappedWrite(new FileInputStream(src),new FileOutputStream(dest,append),true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用nio 复制文件
     * @param src 源文件
     * @param dest 目标文件
     * @param listener 写入监听
     */
    public static void nioCopy(File src,File dest, IWriteListener listener){
        nioCopy(src, dest,listener,false);
    }

    /**
     * 使用nio 复制文件
     * @param src 源文件
     * @param dest 目标文件
     * @param listener 写入监听
     * @param append 是否追加写入
     */
    public static void nioCopy(File src,File dest, IWriteListener listener,boolean append){
        try {
            nioWrite(new FileInputStream(src),new FileOutputStream(dest,append),listener,true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * nio方式将流写入流
     * @param fis 文件输入流
     * @param fos 文件输入流
     * @param listener 写入监听
     * @param isCloseStream 是否在方法内部关闭流
     */
    public static void nioWrite(FileInputStream fis, FileOutputStream fos, IWriteListener listener, boolean isCloseStream){
        if(fis == null || fos == null) return;
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(COPY_ONE_SIZE);
        try {
            long alreadyWrite = 0;
            while (inChannel.read(buffer) > 0){
                // 读取完毕，复位指针，切除空余空间，用于将内容写入
                buffer.flip();
                alreadyWrite += outChannel.write(buffer);
                // 写入完毕，复位指针，膨胀空间为最大容量，用于读取新数据
                buffer.clear();
                onWrite(listener,alreadyWrite);
            }
            onSuccess(listener);
        } catch (IOException e) {
            e.printStackTrace();
            onError(listener,e);
        }
        finally {
            if(inChannel != null){
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isCloseStream){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 使用nio 内存映射的方式将流写入流
     * @param fis 文件输入流
     * @param fos 文件输入流
     * @param isCloseStream 是否在方法内部关闭流
     */
    public static void nioMappedWrite(FileInputStream fis,FileOutputStream fos,boolean isCloseStream){
        if(fis == null || fos == null) return;
        FileChannel inChannel = fis.getChannel();
        FileChannel outChannel = fos.getChannel();
        try {
            // 使用Java提供的接口处理写入，内部使用的是内存映射
            // 内部处理没有写入监听
            outChannel.transferFrom(inChannel,0,inChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(inChannel != null){
                try {
                    inChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(outChannel != null){
                try {
                    outChannel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(isCloseStream){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
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

    /**
     * 计算并格式化文件大小
     * @param path 文件路径
     * @return 格式化之后的字符串
     */
    public static String formatFileSize(String path){
        return formatFileSize(new File(path));
    }

    /**
     * 计算并格式化文件大小
     * @param file 文件
     * @return 格式化之后的字符串
     */
    public static String formatFileSize(File file){
        return formatSize(calcFileSize(file));
    }

    /**
     * 格式化大小为计算机格式
     * T、G、M、K、B
     * @param size
     * @return
     */
    public static String formatSize(long size){
        DecimalFormat df = new DecimalFormat("#.00");
        long KB = COPY_ONE_SIZE;
        long MB = KB*KB;
        long GB = MB*KB;
        long TB = GB*KB;
        if(size / TB > 0){
            return df.format((double)size / TB) + "T";
        }
        else if(size / GB > 0){
            return df.format((double)size / GB) + "G";
        }
        else if(size / MB > 0){
            return df.format((double)size / MB) + "M";
        }
        else if(size / KB > 0){
            return df.format((double)size / KB) + "K";
        }
        else{
            return size + "B";
        }
    }

    private static void onWrite(IWriteListener listener,long length){
        if(null != listener){
            listener.onWrite(length);
        }
    }

    private static void onSuccess(IWriteListener listener){
        if(null != listener){
            listener.onSuccess();
        }
    }

    private static void onError(IWriteListener listener,Exception e){
        if(null != listener){
            listener.onError(e);
        }
    }

    /**
     * 文件写入监听
     */
    public interface IWriteListener {
        void onSuccess();
        void onError(Exception e);
        void onWrite(long length);
    }

    public class WriteControl {
        private InputStream mIs;

        public WriteControl(InputStream is){
            mIs = is;
        }

        public void stop(){
            try {
                mIs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}

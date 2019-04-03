package com.cyb.jbpm;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;

import javax.servlet.ServletException;

import org.apache.log4j.Logger;

public class FileUtils
{
  public static Logger log = Logger.getLogger(FileUtils.class);
  static File tmpDir = null;
  static File saveDir = null;

  public static void readFileByBytes(String fileName)
  {
    File file = new File(fileName);
    InputStream in = null;
    try {
      System.out.println("以字节为单位读取文件内容，一次读�?个字节：");

      in = new FileInputStream(file);
      int tempbyte;
      while ((tempbyte = in.read()) != -1)
      {
        System.out.write(tempbyte);
      }
      in.close();
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    try {
      System.out.println("以字节为单位读取文件内容，一次读多个字节�?");

      byte[] tempbytes = new byte[100];
      int byteread = 0;
      in = new FileInputStream(fileName);
      showAvailableBytes(in);

      while ((byteread = in.read(tempbytes)) != -1)
        System.out.write(tempbytes, 0, byteread);
    }
    catch (Exception e1) {
      e1.printStackTrace();

      if (in != null)
        try {
          in.close();
        }
        catch (IOException localIOException1)
        {
        }
    }
    finally
    {
      if (in != null)
        try {
          in.close();
        }
        catch (IOException localIOException2)
        {
        }
    }
  }

  public static void readFileByChars(String fileName)
  {
    File file = new File(fileName);
    Reader reader = null;
    try {
      System.out.println("以字符为单位读取文件内容，一次读�?个字节：");

      reader = new InputStreamReader(new FileInputStream(file));
      int tempchar;
      while ((tempchar = reader.read()) != -1)
      {
        if ((char)tempchar != '\r') {
          System.out.print((char)tempchar);
        }
      }
      reader.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      System.out.println("以字符为单位读取文件内容，一次读多个字节�?");

      char[] tempchars = new char[30];
      int charread = 0;
      reader = new InputStreamReader(new FileInputStream(fileName));

      while ((charread = reader.read(tempchars)) != -1)
      {
        if ((charread == tempchars.length) && 
          (tempchars[(tempchars.length - 1)] != '\r'))
          System.out.print(tempchars);
        else {
          for (int i = 0; i < charread; i++) {
            if (tempchars[i] == '\r') {
              continue;
            }
            System.out.print(tempchars[i]);
          }
        }
      }
    }
    catch (Exception e1)
    {
      e1.printStackTrace();

      if (reader != null)
        try {
          reader.close();
        }
        catch (IOException localIOException)
        {
        }
    }
    finally
    {
      if (reader != null)
        try {
          reader.close();
        }
        catch (IOException localIOException1)
        {
        }
    }
  }

  public static StringBuffer readFileByLines(String fileName)
  {
    StringBuffer content = new StringBuffer("");
    File file = new File(fileName);
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "gbk"));

      String tempString = null;
      int line = 1;

      while ((tempString = reader.readLine()) != null)
      {
        content.append(tempString.trim() + ",");
      }

      reader.close();
    } catch (IOException e) {
      e.printStackTrace();

      if (reader != null)
        try {
          reader.close();
        }
        catch (IOException localIOException1)
        {
        }
    }
    finally
    {
      if (reader != null)
        try {
          reader.close();
        }
        catch (IOException localIOException2) {
        }
    }
    return content;
  }

  public static void readFileByRandomAccess(String fileName)
  {
    RandomAccessFile randomFile = null;
    try {
      System.out.println("随机读取�?段文件内容：");

      randomFile = new RandomAccessFile(fileName, "r");

      long fileLength = randomFile.length();

      int beginIndex = fileLength > 4L ? 4 : 0;

      randomFile.seek(beginIndex);
      byte[] bytes = new byte[10];
      int byteread = 0;

      while ((byteread = randomFile.read(bytes)) != -1)
        System.out.write(bytes, 0, byteread);
    }
    catch (IOException e) {
      e.printStackTrace();

      if (randomFile != null)
        try {
          randomFile.close();
        }
        catch (IOException localIOException1)
        {
        }
    }
    finally
    {
      if (randomFile != null)
        try {
          randomFile.close();
        }
        catch (IOException localIOException2)
        {
        }
    }
  }

  private static void showAvailableBytes(InputStream in)
  {
    try
    {
      System.out.println("当前字节输入流中的字节数�?:" + in.available());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void copyFile(String src, String dest) throws IOException {
    try {
      FileInputStream in = new FileInputStream(src);
      File file = new File(dest);
      if (!file.exists()) {
        file.createNewFile();
      }
      FileOutputStream out = new FileOutputStream(file);

      byte[] buffer = new byte[1024];
      int c;
      while ((c = in.read(buffer)) != -1)
      {
        for (int i = 0; i < c; i++)
          out.write(buffer[i]);
      }
      in.close();
      out.close();
    } catch (Exception e) {
      log.info(e.toString());
    }
  }

  public static void copyFile(File src, String dest) throws IOException {
    try {
      FileInputStream in = new FileInputStream(src);
      File file = new File(dest);
      if (!file.exists())
        file.createNewFile();
      FileOutputStream out = new FileOutputStream(file);

      byte[] buffer = new byte[1024];
      int c;
      while ((c = in.read(buffer)) != -1)
      {
        for (int i = 0; i < c; i++)
          out.write(buffer[i]);
      }
      in.close();
      out.close();
    } catch (Exception e) {
      log.info(e.toString());
    }
  }

  public static void copyFileByStream(InputStream src, String dest) {
    try {
      File file = new File(dest);
      if (!file.exists())
        file.createNewFile();
      FileOutputStream out = new FileOutputStream(dest);

      byte[] buffer = new byte[1024];
      int c;
      while ((c = src.read(buffer)) != -1)
      {
        for (int i = 0; i < c; i++)
          out.write(buffer[i]);
      }
      src.close();
      out.close();
    } catch (FileNotFoundException e) {
      log.info(e.toString());
    } catch (IOException e) {
      log.info(e.toString());
    }
  }

//  public static String MutilUpload(HttpServletRequest request) {}
  public static void initByProperties() throws ServletException {}

  public static void init() throws ServletException {
    String tmpPath = "d://tmpdir";
    String savePath = "d://updir";
    tmpDir = new File(tmpPath);
    saveDir = new File(savePath);
    if (!tmpDir.isDirectory())
      tmpDir.mkdir();
    if (!saveDir.isDirectory())
      saveDir.mkdir(); 
  }

  public static void main(String[] args) {
    String fileName = "d:\\file\\industry.txt";
    System.out.println(readFileByLines(fileName));
  }
}
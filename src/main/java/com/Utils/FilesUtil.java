package com.Utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FilesUtil extends FileUtils {
    private static final int BUFFER_SIZE = 2097152;

    public FilesUtil() {
    }

    public static String fileMd5(File file) {
        String fileMd5 = null;
        BufferedInputStream fileStream = null;

        try {
            fileStream = new BufferedInputStream(new FileInputStream(file));
            fileMd5 = fileMd5((InputStream)fileStream);
        } catch (Exception var12) {
        } finally {
            if (fileStream != null) {
                try {
                    fileStream.close();
                } catch (IOException var11) {
                }
            }

        }

        return fileMd5;
    }

    public static String fileMd5(InputStream fileStream) throws IOException {
        return DigestUtils.md5Hex(fileStream);
    }

    public static void unzip(InputStream input, String path) throws IOException {
        ZipInputStream zip = new ZipInputStream(input);
        String dir = path;
        if (!path.endsWith("/")) {
            dir = path + "/";
        }

        while(true) {
            ZipEntry entry;
            while((entry = zip.getNextEntry()) != null) {
                File targetFile;
                if (entry.isDirectory()) {
                    targetFile = new File(dir + entry.getName());
                    targetFile.mkdirs();
                } else {
                    targetFile = new File(dir + entry.getName());
                    if (!targetFile.getParentFile().exists()) {
                        targetFile.getParentFile().mkdirs();
                    }

                    FileOutputStream fos = new FileOutputStream(targetFile);
                    byte[] buf = new byte[BUFFER_SIZE];

                    int len;
                    while((len = zip.read(buf, 0, BUFFER_SIZE)) != -1) {
                        fos.write(buf, 0, len);
                    }

                    fos.close();
                }
            }

            zip.close();
            return;
        }
    }

    public static List<File> getAllFiles(String path) {
        File baseFile = new File(path);
        List<File> list = new ArrayList();
        if (!baseFile.isFile() && baseFile.exists()) {
            File[] files = baseFile.listFiles();
            File[] var4 = files;
            int var5 = files.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                File file = var4[var6];
                if (file.isDirectory()) {
                    list.addAll(getAllFiles(file.getAbsolutePath()));
                } else {
                    list.add(file);
                }
            }

            return list;
        } else {
            return list;
        }
    }

    public static byte[] readBytes(String path) throws IOException {
        File file = new File(path);
        return readFileToByteArray(file);
    }

    public static String readString(String path) throws IOException {
        File file = new File(path);
        return readFileToString(file, "UTF-8");
    }

    public static List<String> readLines(String path) throws IOException {
        File file = new File(path);
        return readLines(file, "UTF-8");
    }

    public static void deleteDir(String dir) throws IOException {
        deleteDirectory(new File(dir));
    }

    public static void writeByteToFile(String path, byte[] bytes) throws IOException {
        writeByteToFile(path, bytes, false);
    }

    public static void writeByteToFile(String path, byte[] bytes, boolean append) throws IOException {
        File file = new File(path);
        writeByteArrayToFile(file, bytes, append);
    }

    public static void writeStringToFile(String path, String str) throws IOException {
        writeStringToFile(path, str, false);
    }

    public static void writeStringToFile(String path, String str, boolean append) throws IOException {
        File file = new File(path);
        writeStringToFile(file, str, "UTF-8", append);
    }

    public static void writeLinesToFile(String path, Collection<String> collection) throws IOException {
        writeLinesToFile(path, collection, (String)null);
    }

    public static void writeLinesToFile(String path, Collection<String> collection, String lineEnding) throws IOException {
        writeLinesToFile(path, collection, lineEnding, false);
    }

    public static void writeLinesToFile(String path, Collection<String> collection, String lineEnding, boolean append) throws IOException {
        File file = new File(path);
        FileUtils.writeLines(file, collection, lineEnding, append);
    }

    /**
     *文件上传
     * @param input 输入的文件流
     * @param filePath  文件要保存的路径
     * @throws IOException
     */
    public static void upload(InputStream input, File filePath) throws IOException {
        FileOutputStream output = new FileOutputStream(filePath);

        byte[] bytes = new byte[2048];
        int n ;
        while (-1  != (n = input.read(bytes)) ) {
            output.write(bytes,0,n);
        }
    }

    /**
     * 将文件下载到客户端
     *
     * @param filePath
     * @param fileName
     * @param response
     * @param isOnLine 是否在线打开
     * @throws Exception
     */
    public static void download(String filePath, String fileName, HttpServletRequest request, HttpServletResponse response, boolean isOnLine) throws Exception {
        File f = new File(filePath);

        BufferedInputStream br = new BufferedInputStream(new FileInputStream(f));
        byte[] buf = new byte[1024];
        int len = 0;

        response.reset(); // 非常重要
        // 解决文件名中文乱码问题
        String fileNameDisplay = URLEncoder.encode(fileName, "UTF-8");
        // 针对火狐浏览器需要特殊处理
        if ("FF".equals(getBrowser(request))) {
            fileNameDisplay = new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1);
        }

        // 在线打开方式
        if (isOnLine) {
            response.setHeader("Content-Disposition", "inline; filename=" + fileNameDisplay);
        }
        // 纯下载方式
        else {
            response.setContentType("application/x-msdownload");
            response.setHeader("Content-Disposition", "attachment; filename=" + fileNameDisplay);
        }

        BufferedOutputStream bf=new BufferedOutputStream(response.getOutputStream());

        while ((len = br.read(buf)) > 0) {
            bf.write(buf,0,len);
        }
        bf.flush();
        bf.close();
        br.close();

    }

    /**
     * 判断客户端浏览器类型
     *
     * @param request
     * @return
     */
    private static String getBrowser(HttpServletRequest request) {
        String UserAgent = request.getHeader("USER-AGENT").toLowerCase();
        if (UserAgent != null) {
            if (UserAgent.contains("msie")) {
                return "IE";
            }
            if (UserAgent.contains("firefox")) {
                return "FF";
            }
            if (UserAgent.contains("chrome")) {
                return "Chrome";
            }
            if (UserAgent.contains("safari")) {
                return "SF";
            }

        }
        return null;
    }
}

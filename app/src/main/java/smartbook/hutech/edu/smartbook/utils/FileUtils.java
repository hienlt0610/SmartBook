package smartbook.hutech.edu.smartbook.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by hienl on 6/26/2017.
 */

public class FileUtils {

    public static File getExternalFilesDir(Context context) {
        return context.getExternalFilesDir(null);
    }

    /**
     * Check the SD card
     *
     * @return Whether there is a SDCard
     */
    public static boolean hasSDCard() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * Get file from full path
     *
     * @param path Path
     * @return File of path
     */
    public static File getFileByPath(String path) {
        if (StringUtils.isNotEmpty(path)) {
            return new File(path);
        }
        return null;
    }

    /**
     * Check is file exists
     *
     * @param file File
     * @return return true if file is exists
     */
    public static boolean isFileExists(File file) {
        return file != null && file.exists();
    }

    /**
     * Check if file exists
     *
     * @param path Path
     * @return true if file is exists
     */
    public static boolean isFileExists(String path) {
        return isFileExists(getFileByPath(path));
    }

    /**
     * Check if directory
     *
     * @param file File
     * @return true if is directory
     */
    public static boolean isDir(File file) {
        return isFileExists(file) && file.isDirectory();
    }

    /**
     * Check if directory
     *
     * @param path Path
     * @return true if is directory
     */
    public static boolean isDir(String path) {
        return isDir(getFileByPath(path));
    }

    /**
     * Check if it is file
     *
     * @param file File
     * @return true if it is file
     */
    public static boolean isFile(File file) {
        return file != null && file.isFile();
    }

    /**
     * Check if it is file
     *
     * @param path Path
     * @return true if it is file
     */
    public static boolean isFile(String path) {
        return isFile(getFileByPath(path));
    }

    /**
     * To determine whether the existence of the directory, there is no judgment to determine whether to create a success
     *
     * @param dirPath Directory path
     * @return {@code true}: Exist or create success<br>{@code false}: Does not exist or fails to create
     */
    public static boolean createOrExistsDir(String dirPath) {
        return createOrExistsDir(getFileByPath(dirPath));
    }

    /**
     * To determine whether the existence of the directory, there is no judgment to determine whether to create a success
     *
     * @param file File
     * @return {@code true}: Exist or create success<br>{@code false}: Does not exist or fails to create
     */
    public static boolean createOrExistsDir(File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * Delete the directory
     *
     * @param dirPath Directory path
     * @return {@code true}: Delete Success <br> {@code false}: Delete failed
     */
    public static boolean deleteDir(String dirPath) {
        return deleteDir(getFileByPath(dirPath));
    }

    /**
     * Delete the directory
     *
     * @param dir Directory
     * @return {@code true}: Delete Success <br> {@code false}: Delete failed
     */
    public static boolean deleteDir(File dir) {
        if (dir == null) return false;
        // The directory does not exist to return true
        if (!dir.exists()) return true;
        // not the directory returns false
        if (!dir.isDirectory()) return false;
        // Now the file exists and is the folder
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return dir.delete();
    }

    /**
     * Delete Files
     *
     * @param srcFilePath file path
     * @return {@code true}: Delete Success <br> {@code false}: Delete failed
     */
    public static boolean deleteFile(String srcFilePath) {
        return deleteFile(getFileByPath(srcFilePath));
    }

    /**
     * Delete Files
     *
     * @param file File
     * @return {@code true}: Delete Success <br> {@code false}: Delete failed
     */
    public static boolean deleteFile(File file) {
        return file != null && (!file.exists() || file.isFile() && file.delete());
    }

    /**
     * Delete all files in the directory
     *
     * @param dirPath Directory path
     * @return {@code true}: Delete Success <br> {@code false}: Delete failed
     */
    public static boolean deleteFilesInDir(String dirPath) {
        return deleteFilesInDir(getFileByPath(dirPath));
    }

    /**
     * Delete all files in the directory
     *
     * @param dir File
     * @return {@code true}: Delete Success <br> {@code false}: Delete failed
     */
    public static boolean deleteFilesInDir(File dir) {
        if (dir == null) return false;
        if (!dir.exists()) return true;
        if (!dir.isDirectory()) return false;
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                if (file.isFile()) {
                    if (!deleteFile(file)) return false;
                } else if (file.isDirectory()) {
                    if (!deleteDir(file)) return false;
                }
            }
        }
        return true;
    }

    /**
     * Get all files in the directory
     *
     * @param dirPath     Directory path
     * @param isRecursive Whether recursive into the subdirectory
     * @return File list
     */
    public static List<File> listFilesInDir(String dirPath, boolean isRecursive) {
        return listFilesInDir(getFileByPath(dirPath), isRecursive);
    }

    /**
     * Get all files in the directory
     *
     * @param dir         Directory file
     * @param isRecursive Whether recursive into the subdirectory
     * @return File list
     */
    public static List<File> listFilesInDir(File dir, boolean isRecursive) {
        if (!isDir(dir)) return null;
        if (isRecursive) return listFilesInDir(dir);
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            Collections.addAll(list, files);
        }
        return list;
    }

    /**
     * Get all files in the directory
     *
     * @param dirPath Directory path
     * @return File list
     */
    public static List<File> listFilesInDir(String dirPath) {
        return listFilesInDir(getFileByPath(dirPath));
    }

    /**
     * Get all files in the directory
     *
     * @param dir File dir
     * @return File list
     */
    public static List<File> listFilesInDir(File dir) {
        if (!isDir(dir)) return null;
        List<File> list = new ArrayList<>();
        File[] files = dir.listFiles();
        if (files != null && files.length != 0) {
            for (File file : files) {
                list.add(file);
                if (file.isDirectory()) {
                    list.addAll(listFilesInDir(file));
                }
            }
        }
        return list;
    }

    /**
     * Writes the input stream to a file
     *
     * @param filePath path
     * @param is       the input stream
     * @param append   is appended at the end of the file
     * @return {@code true}: write successful <br> {@code false}: write failed
     */
    public static boolean writeFileFromIS(String filePath, InputStream is, boolean append) {
        return writeFileFromIS(getFileByPath(filePath), is, append);
    }


    public static boolean writeFileFromIS(File file, InputStream is, boolean append) {
        if (file == null || is == null) return false;
        if (!isFileExists(file)) return false;
        OutputStream os = null;
        try {
            os = new BufferedOutputStream(new FileOutputStream(file, append));
            byte data[] = new byte[1024];
            int len;
            while ((len = is.read(data, 0, 1024)) != -1) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (os != null) {
                try {
                    os.close();
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Write a string to a file
     *
     * @param file    File
     * @param content String content
     * @param append  Whether it is added at the end of the document
     * @return {@code true}: write successful <br> {@code false}: write failed
     */
    public static boolean writeFileFromString(File file, String content, boolean append) {
        if (file == null || content == null) return false;
        if (!isFileExists(file)) return false;
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(file, append));
            bw.write(content);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Specifies that the code reads the file into a string by row
     *
     * @param file        File
     * @param charsetName Encoding format
     * @return String
     */
    public static String readFileToString(File file, String charsetName) {
        if (file == null) return null;
        BufferedReader reader = null;
        try {
            StringBuilder sb = new StringBuilder();
            if (StringUtils.isEmpty(charsetName)) {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            } else {
                reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName));
            }
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\r\n");// windows system for \r\n, Linux \n
            }
            // To remove the last line break
            return sb.delete(sb.length() - 2, sb.length()).toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Specifies that the code reads the file into a string by row
     *
     * @param filePath    file path
     * @param charsetName Encoding format
     * @return String
     */
    public static String readFileToString(String filePath, String charsetName) {
        return readFileToString(getFileByPath(filePath), charsetName);
    }

    /**
     * convert inputstream to outputstream
     *
     * @param is InputStream
     * @return outputStream
     */
    public static ByteArrayOutputStream input2OutputStream(InputStream is) {
        if (is == null) return null;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int len;
            while ((len = is.read(b, 0, 1024)) != -1) {
                os.write(b, 0, len);
            }
            return os;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Convert InputStream to byte array
     *
     * @param is InputStream
     * @return byte array
     */
    public static byte[] inputStream2Bytes(InputStream is) {
        if (is == null) return null;
        return input2OutputStream(is).toByteArray();
    }

    public static File separatorWith(File file, String name) {
        if (file == null) return null;
        if (StringUtils.isEmpty(name)) return file;
        String newPath = file.getAbsolutePath() + File.separator + name;
        return new File(newPath);
    }

}

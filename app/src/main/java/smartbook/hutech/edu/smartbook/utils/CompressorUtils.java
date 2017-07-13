package smartbook.hutech.edu.smartbook.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by hienl on 7/13/2017.
 */

public class CompressorUtils {

    public static List<File> unzipFileByKeyword(final String pathZipFile, final String pathDestDir, final String keyword) throws IOException {
        File zipFile = FileUtils.getFileByPath(pathZipFile);
        File zipDestDir = FileUtils.getFileByPath(pathDestDir);
        return unzipFileByKeyword(zipFile, zipDestDir, keyword);
    }

    public static List<File> unzipFileByKeyword(final File zipFile, final File destDir, final String keyword)
            throws IOException {
        if (zipFile == null || destDir == null) return null;
        List<File> files = new ArrayList<>();
        ZipFile zf = new ZipFile(zipFile);
        Enumeration<?> entries = zf.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            String entryName = entry.getName();
            if (StringUtils.isEmpty(keyword) || FileUtils.getFileName(entryName).toLowerCase().contains(keyword.toLowerCase())) {
                String filePath = destDir + File.separator + entryName;
                File file = new File(filePath);
                files.add(file);
                if (entry.isDirectory()) {
                    if (!FileUtils.createOrExistsDir(file)) return null;
                } else {
                    if (!FileUtils.createOrExistsFile(file)) return null;
                    InputStream in = null;
                    OutputStream out = null;
                    try {
                        in = new BufferedInputStream(zf.getInputStream(entry));
                        out = new BufferedOutputStream(new FileOutputStream(file));
                        byte buffer[] = new byte[1024];
                        int len;
                        while ((len = in.read(buffer)) != -1) {
                            out.write(buffer, 0, len);
                        }
                    } finally {
                        in.close();
                        out.close();
                    }
                }
            }
        }
        return files;
    }

    public static boolean unzipFiles(final Collection<File> zipFiles, final String destDirPath)
            throws IOException {
        return unzipFiles(zipFiles, FileUtils.getFileByPath(destDirPath));
    }

    public static boolean unzipFiles(final Collection<File> zipFiles, final File destDir)
            throws IOException {
        if (zipFiles == null || destDir == null) return false;
        for (File zipFile : zipFiles) {
            if (!unzipFile(zipFile, destDir)) return false;
        }
        return true;
    }

    public static boolean unzipFile(final String zipFilePath, final String destDirPath)
            throws IOException {
        return unzipFile(FileUtils.getFileByPath(zipFilePath), FileUtils.getFileByPath(destDirPath));
    }

    public static boolean unzipFile(final File zipFile, final File destDir)
            throws IOException {
        return unzipFileByKeyword(zipFile, destDir, null) != null;
    }

}

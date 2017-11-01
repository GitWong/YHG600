package com.galaxy.safe.utils;

import java.io.File;

/**
 * Created by Dell on 2016/3/25.
 */
public class DeleteFileUtils {

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void DeleteFile(File file) {

        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
//                file.delete();
            }
        }
    }

}

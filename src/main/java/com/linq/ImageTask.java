package com.linq;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 图片下载任务
 *
 * @author LinQ
 * @version 2014-06-17
 */
public class ImageTask implements Runnable {
    private String imageUrl;
    private String path;

    public ImageTask(String imageUrl, String path) {
        this.imageUrl = imageUrl;
        this.path = path;
    }

    @Override
    public void run() {
        try {
            downloadImage();
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void downloadImage() throws IOException {
        String name = FilenameUtils.getName(imageUrl);
        String filename = String.format("%s/%s", path, name);
        if (!new File(filename).exists()) {
            URL url = new URL(imageUrl);
            URLConnection connection = url.openConnection();
            FileOutputStream output = new FileOutputStream(filename);
            IOUtils.copy(connection.getInputStream(), output);
            output.close();
        }
    }
}

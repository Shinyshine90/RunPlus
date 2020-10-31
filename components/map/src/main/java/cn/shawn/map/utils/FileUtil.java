package cn.shawn.map.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static String assetToFilePath(Context context, String assetPath) {
        return context.getFilesDir().getPath() +  File.separator + assetPath;
    }

    public static File copyAssetsToData(Context context, String assetsPath, String path) throws IOException {
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = assetManager.open(assetsPath);
        File file = new File(path);
        if (!file.getParentFile().exists()) {
           file.getParentFile().mkdirs();
        }
        FileOutputStream outputStream = new FileOutputStream(file);
        streamCopy(inputStream, outputStream);
        return file;
    }

    public static void streamCopy(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            byte[] buffers = new byte[4092];
            int length;
            while ((length = inputStream.read(buffers)) > 0) {
                outputStream.write(buffers, 0 , length);
            }
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) { }
        }
    }
}

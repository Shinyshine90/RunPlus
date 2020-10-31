package cn.shawn.map.base.config;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.Serializable;

public class AMapStyle implements Serializable {

    public static AMapStyle createCamarnoStyle(Context context) {
        String sdcardDir = context.getFilesDir().getPath();
        String dataAsset = "map/style/macarno/style.data";
        String extraAsset = "map/style/macarno/style_extra.data";
        return new AMapStyle(sdcardDir, dataAsset, extraAsset);
    }

    public String sdcardDir;

    public String styleDataAsset;

    public String styleExtraAsset;

    public AMapStyle(String sdcardDir, String styleDataAsset, String styleExtraAsset) {
        this.sdcardDir = sdcardDir;
        this.styleDataAsset = styleDataAsset;
        this.styleExtraAsset = styleExtraAsset;
    }

    public boolean hasStyleData() {
        return !TextUtils.isEmpty(styleDataAsset) && !TextUtils.isEmpty(styleExtraAsset);
    }

    public boolean checkStyleDataValid() {
        String[] stylePaths = getStylePath();
        if (TextUtils.isEmpty(stylePaths[0]) || TextUtils.isEmpty(stylePaths[1])) {
            return false;
        }
        File styleDataFile = new File(stylePaths[0]);
        File styleExtraFile = new File(stylePaths[1]);
        return styleDataFile.isFile() && styleDataFile.length() > 0
                && styleExtraFile.isFile() && styleExtraFile.length() > 0;
    }

    public String[] getStylePath() {
        String dataPath = assetToFilePath(styleDataAsset);
        String extraPath = assetToFilePath(styleExtraAsset);
        return new String[]{dataPath, extraPath};
    }

    private String assetToFilePath(String assetPath) {
        return sdcardDir +  File.separator + assetPath;
    }
}
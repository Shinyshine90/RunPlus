package cn.shawn.runplus.utils;

import android.content.Context;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;

public class SpUtil {

    private static final String TABLE = "RunPlus";

    private static final String KEY_MOCK_LOCATION = "mock_location";

    private static final Gson sGSon = new Gson();

    public static void saveMockLocation(Context context, LatLng latLng) {
        context.getSharedPreferences(TABLE, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_MOCK_LOCATION, sGSon.toJson(latLng))
                .apply();
    }

    public static LatLng getMockLocation(Context context) {
        String json = context.getSharedPreferences(TABLE, Context.MODE_PRIVATE)
                .getString(KEY_MOCK_LOCATION, "{}");
        return sGSon.fromJson(json, LatLng.class);
    }
}

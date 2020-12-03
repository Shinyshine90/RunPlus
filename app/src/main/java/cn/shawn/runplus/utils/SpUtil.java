package cn.shawn.runplus.utils;

import android.content.Context;
import com.amap.api.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;

public class SpUtil {

    private static final String TABLE = "RunPlus";

    private static final String KEY_MOCK_LOCATION = "mock_location";

    public static void saveMockLocation(Context context, LatLng latLng) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("latitude", latLng.latitude);
            jsonObject.put("longitude", latLng.longitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        context.getSharedPreferences(TABLE, Context.MODE_PRIVATE)
                .edit()
                .putString(KEY_MOCK_LOCATION, jsonObject.toString())
                .apply();

    }

    public static LatLng getMockLocation(Context context) {
        String json = context.getSharedPreferences(TABLE, Context.MODE_PRIVATE)
                .getString(KEY_MOCK_LOCATION, "");
        try {
            JSONObject jsonObject = new JSONObject(json);
            return new LatLng(jsonObject.getDouble("latitude"),
                    jsonObject.getDouble("longitude"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}

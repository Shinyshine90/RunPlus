package cn.shawn.mock;

import android.content.Context;
import android.util.Log;

import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkPath;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;
import java.util.ArrayList;
import java.util.List;

/**
 * 高德 坐标拾取
 * https://lbs.amap.com/console/show/picker
 */
public class RouteLineFetcher {

    public static final String TAG = "RouteSearcher";

    private static final LatLonPoint HF_STATION = new LatLonPoint(31.883368, 117.315456);

    private static final LatLonPoint NJN_STATION = new LatLonPoint(31.968592,118.798128);

    private static final LatLonPoint HOME_VILLAGE = new LatLonPoint(31.961874, 118.758402);

    private static final LatLonPoint WNS_VILLAGE = new LatLonPoint(32.144383,118.752411);

    private RouteSearch mRouteSearch;

    public RouteLineFetcher(Context context) {
        mRouteSearch = new RouteSearch(context);
    }

    public List<LatLonPoint> fetch() throws AMapException {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(NJN_STATION, WNS_VILLAGE);
        RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo);
        WalkRouteResult result = mRouteSearch.calculateWalkRoute(query);
        WalkPath path = result.getPaths().get(0);
        List<LatLonPoint> points = new ArrayList<>();
        for (int k = 0; k < path.getSteps().size(); k++) {
            WalkStep step = path.getSteps().get(k);
            points.addAll(step.getPolyline());
        }
        for (int i = 0; i < 10; i++) {
            Log.i(TAG, "fetch: "+points.get(i).toString());
        }
        return points;
    }

}

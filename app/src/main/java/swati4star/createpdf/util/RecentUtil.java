package swati4star.createpdf.util;

import android.content.SharedPreferences;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class RecentUtil {

    private static final String RECENT_PREF = "Recent";

    public static LinkedHashMap<String, Map<String, String>> getList(SharedPreferences preferences)
        throws JSONException {

        //creating the empty list.
        LinkedHashMap<String, Map<String, String>> recentList = new LinkedHashMap<>();

        //check if recent exists.
        if (preferences.contains(RECENT_PREF)) {

            JSONObject jsonObject = new JSONObject(preferences
                    .getString(RECENT_PREF, ""));


            Iterator<String> keys = jsonObject.keys();

            while (keys.hasNext()) {
                String keyVal = keys.next();
                JSONObject item = jsonObject.getJSONObject(keyVal);

                //item object
                Map<String, String> itemMap = new HashMap<>();
                String key = item.keys().next();
                itemMap.put(key, item.getString(key));

                recentList.put(keyVal, itemMap);
            }
        }
        return recentList;
    }

    public static void addFeatureInRecentList(SharedPreferences preferences,
            int resId, Map<String, String> itemClicked) throws JSONException {

        LinkedHashMap<String, Map<String, String>> recentList = getList(preferences);


        //remove the first item from the recent list.
        if (recentList.size() == 3) {
            //now if the list contains the particular key
            if (recentList.remove(String.valueOf(resId)) == null) {
                //bucket is full.
                recentList.remove(recentList.keySet().iterator().next()); //removes the first.
            }
        }

        recentList.put(String.valueOf(resId), itemClicked);

        //update the preferences.
        preferences.edit().putString(RECENT_PREF, new JSONObject(recentList).toString()).apply();
    }
}
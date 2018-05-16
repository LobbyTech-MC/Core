package me.dablakbandit.core.json;

import java.util.Iterator;

import me.dablakbandit.core.json.Cookie;
import me.dablakbandit.core.json.JSONException;
import me.dablakbandit.core.json.JSONObject;
import me.dablakbandit.core.json.JSONTokener;

public class CookieList {

    public static JSONObject toJSONObject(String string) throws JSONException {
        JSONObject jo = new JSONObject();
        JSONTokener x = new JSONTokener(string);
        while (x.more()) {
            String name = Cookie.unescape(x.nextTo('='));
            x.next('=');
            jo.put(name, Cookie.unescape(x.nextTo(';')));
            x.next();
        }
        return jo;
    }

    @SuppressWarnings("rawtypes")
	public static String toString(JSONObject jo) throws JSONException {
        boolean      b = false;
        Iterator     keys = jo.keys();
        String       string;
        StringBuffer sb = new StringBuffer();
        while (keys.hasNext()) {
            string = keys.next().toString();
            if (!jo.isNull(string)) {
                if (b) {
                    sb.append(';');
                }
                sb.append(Cookie.escape(string));
                sb.append("=");
                sb.append(Cookie.escape(jo.getString(string)));
                b = true;
            }
        }
        return sb.toString();
    }
}

package ua.kazo.dentalacademy.util;

import java.util.Iterator;
import java.util.Map;

public class LogUtils {

    public static String getParamsAsString(Map<String, String> params) {
        StringBuilder builder = new StringBuilder("{");
        Iterator<Map.Entry<String, String>> it = params.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> pair = it.next();
            builder.append(pair.getKey()).append("=").append(pair.getValue());
            if (it.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("}");
        return builder.toString();
    }

}

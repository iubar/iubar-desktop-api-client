package it.iubar.desktop.api.utils;

import java.net.URI;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class QueryStringParser {

    public static Map<String, String> getQueryParams(String url) throws Exception {
        URI uri = new URI(url);
        String query = uri.getQuery();  // Es: "q=java&page=2"
        Map<String, String> result = new LinkedHashMap<>();

        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = pair.length > 1 ? URLDecoder.decode(pair[1], "UTF-8") : "";
                result.put(key, value);
            }
        }

        return result;
    }
    
    public static String removeQueryString(String url) throws Exception {
        URI uri = new URI(url);
        URI cleanUri = new URI(
            uri.getScheme(),
            uri.getAuthority(),
            uri.getPath(),
            null,     // <-- Rimuove la query
            uri.getFragment()
        );
        return cleanUri.toString();
    }
    
}
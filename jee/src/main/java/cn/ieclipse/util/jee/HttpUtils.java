package cn.ieclipse.util.jee;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.ieclipse.util.StringUtils;

public final class HttpUtils {
    private HttpUtils() {
    
    }
    
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    public static List<String> getParams(String value, boolean check,
            String name, String... valids) {
        List<String> array = new ArrayList<>();
        if (StringUtils.isEmpty(value)) {
            if (check) {
                throw new IllegalArgumentException("参数[" + name + "]错误，不能为空");
            }
        }
        String[] temp = value.replaceAll("\\s+", "").split(",");
        if (valids != null && check && valids.length > 0) {
            List<String> validList = Arrays.asList(valids);
            for (String t : temp) {
                if (validList.contains(t)) {
                    array.add(t);
                }
                else {
                    throw new IllegalArgumentException(
                            "参数[" + name + "]错误，无效的参数值：" + t);
                }
            }
        }
        else {
            array = Arrays.asList(temp);
        }
        
        return array;
    }
    
    public static String buildPath(String path, String defaultValue,
            Map<String, Object> params) {
        StringBuilder uri = new StringBuilder();
        if (StringUtils.isEmpty(path)) {
            uri.append(defaultValue);
        }
        else {
            uri.append(path);
        }
        if (params != null && !params.isEmpty()) {
            uri.append("?");
            int i = 0;
            for (String key : params.keySet()) {
                if (i > 0) {
                    uri.append('&');
                }
                uri.append(key);
                uri.append('=');
                uri.append('{');
                uri.append(key);
                uri.append('}');
                i++;
            }
        }
        return uri.toString();
    }
}

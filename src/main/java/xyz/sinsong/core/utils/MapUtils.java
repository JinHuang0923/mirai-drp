package xyz.sinsong.core.utils;

import java.util.HashMap;

/**
 * @author Jin Huang
 * @date 2021/10/26 14:43
 */
public class MapUtils {
    public static HashMap<String, String> getParamMap(String[] strings){
        HashMap<String, String> map = new HashMap<>();
        for (int i = 0; i < strings.length; i++) {
            map.put(String.valueOf(i),strings[i]); //key 下标  v数据
        }
        return map;
    }
}

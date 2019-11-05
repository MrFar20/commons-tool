package pers.mrwangx.commons.tool.display;

import pers.mrwangx.commons.tool.display.annotation.Display;
import pers.mrwangx.commons.tool.display.entity.DisplayInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/3
 * \* Time: 13:32
 * \* Description:
 **/
public class DisplayProcessor {

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        //获取class
        Class clazz = o.getClass();
        //默认分隔符为换行
        String lineSeparator = System.lineSeparator();
        //如果类上有Separtor注解, 切换分割符
        if (clazz.isAnnotationPresent(Display.Separator.class)) {
            Display.Separator s = (Display.Separator) clazz.getAnnotation(Display.Separator.class);
            lineSeparator = s.value();
        }
        List<DisplayInfo> displayInfos = getDisplayInfos(clazz);
        int len = displayInfos.size();
        //按照order排序
        Collections.sort(displayInfos);
        for (int i = 0; i < len; i++) {
            lineSeparator = i == len - 1 ? "" : lineSeparator;
            DisplayInfo info = displayInfos.get(i);
            Display dis = info.getDisplayAnnotation();
            Object typeObject = info.getTypeObject();
            if (dis.display()) {
                if (typeObject instanceof Method) { //如果为方法上的注解
                    Method m = (Method) typeObject;
                    String name = dis.value().equals("") ? m.getName() : dis.value();
                    try {
                        builder.append(name + ":" + m.invoke(o) + lineSeparator);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                } else if (typeObject instanceof Field) { //field上面的注解
                    Field f = (Field) typeObject;
                    String name = dis.value().equals("") ? f.getName() : dis.value();
                    f.setAccessible(true);
                    try {
                        builder.append(name + ":" + f.get(o) + lineSeparator);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return builder.toString();
    }

    /**
     * 获取类上的Display注解
     * @param clazz
     * @return
     */
    public static List<DisplayInfo> getDisplayInfos(Class clazz) {
        List<DisplayInfo> displayInfos = new ArrayList<>();
        Field[] fields = clazz.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            if (f.isAnnotationPresent(Display.class)) {
                displayInfos.add(new DisplayInfo(f, f.getAnnotation(Display.class)));
            }
        }
        Method[] methods = clazz.getDeclaredMethods();
        for (int i = 0; i < methods.length; i++) {
            Method m = methods[i];
            if (m.isAnnotationPresent(Display.class)) {
                displayInfos.add(new DisplayInfo(m, m.getAnnotation(Display.class)));
            }
        }
        return displayInfos;
    }

}

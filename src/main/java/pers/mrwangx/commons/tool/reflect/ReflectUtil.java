package pers.mrwangx.commons.tool.reflect;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/3
 * \* Time: 13:37
 * \* Description:
 **/
public class ReflectUtil {
    /**
     * 扫描某个包下的类文件
     *
     * @param packageName
     * @return
     */
    public static Set<Class> scanClasses(String packageName) {
        Set<Class> classes = new LinkedHashSet<>();
        Set<String> names = scanClassesNames(packageName);
        names.forEach(name -> {
            try {
                Class clazz = Class.forName(name);
                classes.add(clazz);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return;
        });
        return classes;
    }

    /**
     * 扫描某个包下的类名
     *
     * @param packageName 包名
     * @return
     */
    public static Set<String> scanClassesNames(String packageName) {
        Set<String> names = new LinkedHashSet<>();
        String pkgName = packageName;
        String name = pkgName.replaceAll("\\.", "/");
        URL url = Thread.currentThread().getContextClassLoader().getResource(name);
        String protocol = url.getProtocol();
        if (protocol.equals("file")) {
            File file = new File(url.getFile());
            Set<String> classSimpleNames = new LinkedHashSet<>();
            scanClassesNamesOfFile(classSimpleNames, packageName, file);
            classSimpleNames.forEach(n -> {
                names.add(n);
            });
        } else if (protocol.equals("jar")) {
            try {
                JarURLConnection jarURLConnection = (JarURLConnection) url.openConnection();
                JarFile jarFile = jarURLConnection.getJarFile();
                Enumeration<JarEntry> jarEntries = jarFile.entries();
                while (jarEntries.hasMoreElements()) {
                    JarEntry jarEntry = jarEntries.nextElement();
                    if (!jarEntry.isDirectory() && jarEntry.getName().startsWith(name)) {
                        names.add(jarEntry.getName().replaceAll("\\..*", "").replaceAll("/", "."));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return names;
    }

    /**
     * 扫描指定包名文件下的class文件 类名
     * @param classSimpleNames
     * @param packageName 包名
     * @param file 包名对应的文件
     */
    public static void scanClassesNamesOfFile(Set<String> classSimpleNames, String packageName, File file) {
        if (file.isDirectory()) {
            for (File ft : file.listFiles()) {
                String packageNameTemp = packageName;
                if (ft.isDirectory()) {
                    packageNameTemp += "." + ft.getName();
                } else {
                    packageNameTemp += "." + ft.getName().replaceAll("\\..*", "");
                }
                scanClassesNamesOfFile(classSimpleNames, packageNameTemp, ft);
            }
        } else {
            if (file.getName().endsWith(".class")) {
                classSimpleNames.add(packageName);
            }
        }
    }

    /**
     * 获取所有父类，包括本类
     *
     * @param clazz
     * @param endSuper
     * @return
     */
    public static Set<Class> getSuperClasses(Class clazz, Class endSuper) {
        Set<Class> classes = new LinkedHashSet<>();
        Class superClazz = clazz;
        while (superClazz != null && superClazz != endSuper) {
            classes.add(superClazz);
            superClazz = superClazz.getSuperclass();
        }
        return classes;
    }


}

package com.fx.client.utils;


import com.fx.client.bean.ImageInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PathUtils {

	// 其中 ImageInfo 是我简单的封装的音乐信息的实体类
    public static List<ImageInfo> getImageInfo(String relativePath) throws IOException {
        if (isJar()) {
            return getImageInfoFromJar(relativePath);
        }else {
            return getImageInfoFromLocal(relativePath);
        }
    }

    /**
     * 获取开发时存放的音乐文件
     *
     * @param relativePath 开发时音乐文件的目录
     * @return 获取到的音乐文件包装后的 {@link ImageInfo}
     */
    private static List<ImageInfo> getImageInfoFromLocal(String relativePath) {
        List<ImageInfo> musicList = new ArrayList<>();
        File files = new File(PathUtils.class.getResource(relativePath).getPath());
        for (File file : files.listFiles()) {
            String musicName = file.getName();
            String musicPath = file.getAbsolutePath();
            musicPath = musicPath.replaceAll("\\\\", "/");
            musicPath = musicPath.substring(musicPath.indexOf("/image"));

            ImageInfo musicInfo = new ImageInfo(musicName, musicPath);
            musicList.add(musicInfo);
        }
        return musicList;
    }


	// 主要说这个方法
    // Media 是可以支持 jar文件路径的，这个下面再说，先说代码
    /**
     *  从 Jar 包获取文件的方法
     * @param relativePath 音乐文件的目录
     * @return
     * @throws IOException
     */
    private static List<ImageInfo> getImageInfoFromJar(String relativePath) throws IOException {
        List<ImageInfo> musicList = new ArrayList<>();

		// 为了方便看，我把下面三个方法都放上来了
		// 这是拼接的 jar文件路径
        // 拼接后大概是 jar:file:D:/code/demo.jar!/image
        String jarPath = PathUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(jarPath);
        // 得到一个 JarFile，然后根据此可以获取到其下面的所有目录文件
        // 再通过遍历和过滤得到 /image 目录以及下面的文件
        JarFile jarFile = new JarFile(file);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();
            String innerPath = entry.getName();

			// 过滤只要 'image/' 下的文件，并且过滤掉 'image/' 这个空目录
            if (innerPath.startsWith("image/") && !innerPath.endsWith("image/")) {
                String musicName = innerPath.substring(innerPath.indexOf("/") + 1);
                String musicPath = "/image/" + musicName;
                // 其中歌曲名字只储存文件名
                // 歌曲路径为 /image/name.mp3 这种
                ImageInfo musicInfo = new ImageInfo(musicName, musicPath);
                musicList.add(musicInfo);
            }
        }
        return musicList;
    }

    private static Enumeration<JarEntry> getJarEntries() throws IOException {
        String jarPath = getJarPath();
        File file = new File(jarPath);
        JarFile jarFile = new JarFile(file);
        return jarFile.entries();
    }

    /**
     * 如果不是 jar包，会获取到 /target/classes 目录
     * 因此可以通过此判断是否是 jar 还是开发
     * @return
     */
    public static String getJarPath() {
        return PathUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    /**
     * 判断是不是从 Jar 包读取的情况
     * 如果是，则该路径下的不是文件夹
     * @return
     */
    public static boolean isJar() {
        return !(new File(getJarPath()).isDirectory());
    }
}



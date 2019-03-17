package com.cttic.liugw.ordinary.javaio;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PathTest {

    public static void getPathInfo(String strPath) throws IOException {
        Path listing = Paths.get(strPath);
        System.out.println("FileName=[" + listing.getFileName() + "]");
        System.out.println("Number of Elements=[" + listing.getNameCount() + "]");// 目录的层次深度
        System.out.println("Parent Path=[" + listing.getParent() + "]");
        System.out.println("Root Path=[" + listing.getRoot() + "]");
        System.out.println("SubPath from ROOT, 2 elements deep=[" + listing.subpath(0, 2) + "]");

        System.out.println(Files.getLastModifiedTime(listing));
        System.out.println(Files.size(listing));
        System.out.println(Files.isSymbolicLink(listing));
        System.out.println(Files.isDirectory(listing));
        System.out.println(Files.readAttributes(listing, "*"));// 获取所有文件属性信息

        Path path2 = Paths.get("C:/windows/lib");
        Path pathToListing = path2.relativize(listing);
        System.out.println("pathToListing=" + pathToListing.toString());
    }

    /**
     * 从符号链接获取真实连接，两种方法： 1：normalize()  2:
     * @throws IOException 
     */
    public static void getRealPath() throws IOException {
        Path normalPath = Paths.get("D:/tmp").normalize();
        System.out.println("normalPath=[" + normalPath.toString() + "]");

        Path realPath = Paths.get("D:/tmp").toRealPath();
        System.out.println("realPath=[" + realPath.toString() + "]");
    }

    /**
     * 使用glob模式匹配 过滤流，查找匹配的文件
     * @param path
     * @param strPattern
     */
    public static void searchFileInPath(String path, String strPattern) {
        Path dir = Paths.get(path);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(dir, strPattern)) {
            for (Path entry : stream) {
                System.out.println(entry.getFileName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        getPathInfo("C:\\Users\\liugaowei\\Documents\\Tencent Files\\187346454");
        getRealPath();
        searchFileInPath("C:\\Users\\liugaowei\\Documents\\Tencent Files\\187346454\\FileRecv", "*.pdf");
    }

}

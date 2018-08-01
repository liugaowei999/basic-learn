package com.cttic.liugw.ordinary.javaio;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class ListAllFiles {

    private static class FindJavaVistior extends SimpleFileVisitor<Path> {

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (file.toString().endsWith(".java")) {
                System.out.println(file);
            }
            return FileVisitResult.CONTINUE;
        }

    }

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("D:/");
        Files.walkFileTree(path, new FindJavaVistior());

    }

}

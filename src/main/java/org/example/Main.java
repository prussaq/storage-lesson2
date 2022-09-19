package org.example;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Main {

    public static void main(String[] args) throws IOException {
        Path path = Paths.get("C:\\Users\\file.txt");
        Path path1 = Path.of("Users\\file.txt");
        Path path2 = Path.of("./tmp/file");

        try {
            Files.createDirectories(Path.of("dir"));
        } catch (FileAlreadyExistsException e) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Files.copy(path, path1, StandardCopyOption.REPLACE_EXISTING);

        Files.walkFileTree(Path.of("dir"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                if (dir.endsWith("d")) {
                    return FileVisitResult.CONTINUE;
                } else {
                    return FileVisitResult.TERMINATE;
                }
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                return null;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                return null;
            }
        });

        ByteBuffer buffer = ByteBuffer.allocate(1024 * 1024 * 10);
        FileChannel fileChannel = FileChannel.open(Path.of("file.txt"));
        fileChannel.read(buffer);

        byte[] bytes = Files.readAllBytes(Path.of("file.txt"));

//        java -XX:+PrintFlagsFinal -version | grep (findStr) HeapSize
    }
}
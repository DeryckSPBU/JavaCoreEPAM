package javacore.commandline;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.zip.*;

class CommandLine {
    private static String currentPath = System.getProperty("user.dir");

    static String getCurrentPath() {
        return currentPath;
    }

    static void help() {
        System.out.println("createFile <name> -- creates new file");
        System.out.println("renameFile <old name> <new name> -- sets new name to file");
        System.out.println("deleteFile <name> -- removes file");
        System.out.println("createDir <name> -- creates new directory");
        System.out.println("renameDir <old name> <new name> -- sets new name to directory");
        System.out.println("deleteDir <name> -- removes directory");
        System.out.println("cd <name or ..> -- changes current working directory");
        System.out.println("zip <name> -- zip directory or file");
        System.out.println("unzip <name> -- unzip directory or file");
        System.out.println("getEntities -- shows list of files and directories in current working folder");
        System.out.println("getFiles -- shows list of files in current working folder");
        System.out.println("getSorted -- shows sorted list of files in current working folder");
        System.out.println("countFiles -- shows count of files in current working folder");
        System.out.println("exit -- ends program");
    }

    static void createFile(String name) {
        Path path = Paths.get(name);
        try {
            if (!path.isAbsolute()) {
                path = Paths.get(currentPath + File.separator + name);
            }
            Files.createFile(path);
        } catch (IOException e) {
            System.out.println("Couldn't not create file");
        }
    }

    static void renameFile(String oldName, String newName) {
        Path path = Paths.get(oldName);
        Path target = Paths.get(newName);
        if (!path.isAbsolute()) {
            path = Paths.get(currentPath + File.separator + oldName);
        }
        if (!target.isAbsolute()) {
            target = Paths.get(currentPath + File.separator + newName);
        }
        if (Files.isRegularFile(path)) {
            try {
                Files.move(path, target);
            } catch (IOException e) {
                System.out.println("Couldn't rename file");
            }
        } else {
            System.out.println("Not a file");
        }
    }

    static void deleteFile(String name) {
        Path path = Paths.get(name);
        if (!path.isAbsolute()) {
            path = Paths.get(currentPath + File.separator + name);
        }
        try {
            if (Files.isRegularFile(path)) {
                Files.delete(path);
            } else {
                System.out.println("Not a file");
            }
        } catch (IOException e) {
            System.out.println("Couldn't delete file");
        }
    }

    static void createDir(String name) {
        Path path = Paths.get(name);
        try {
            if (path.isAbsolute()) {
                path = Paths.get(currentPath + File.separator + name);
            }
            Files.createDirectory(path);
        } catch (IOException e) {
            System.out.println("Couldn't create file");
        }
    }

    static void renameDir(String oldName, String newName) {
        Path path = Paths.get(oldName);
        Path target = Paths.get(newName);
        if (!path.isAbsolute()) {
            path = Paths.get(currentPath + File.separator + oldName);
        }
        if (!target.isAbsolute()) {
            target = Paths.get(currentPath + File.separator + newName);
        }
        if (Files.isDirectory(path)) {
            path.toFile().renameTo(target.toFile());
        } else {
            System.out.println("Couldn't rename file");
        }
    }

    static void deleteDir(String name) {
        Path path = Paths.get(name);
        if (!path.isAbsolute()) {
            path = Paths.get(currentPath + File.separator + name);
        }
        try {
            if (Files.isDirectory(path)) {
                File[] files = path.toFile().listFiles();
                if (files != null) {
                    for (File f : files) {
                        if (f.isDirectory()) {
                            deleteDir(f.getCanonicalPath());
                        } else {
                            f.delete();
                        }
                    }
                }
                Files.delete(path);
            } else {
                System.out.println("Not a directory");
            }
        } catch (IOException e) {
            System.out.println("Couldn't delete file");
        }
    }

    static void exit() {
        System.exit(0);
    }

    static void cd(String path) {
        if (path.equals("..")) {
            int index = currentPath.length();
            for (int i = currentPath.length() - 1; i > 0; i--) {
                if (File.separator.equals("" + currentPath.charAt(i))) {
                    index = i;
                    break;
                }
            }
            currentPath = currentPath.substring(0, index);
        } else {
            File[] files = new File(currentPath).listFiles();
            if (files != null) {
                for (File file : files) {
                    if (Paths.get(currentPath).relativize(file.toPath()).toString().toLowerCase()
                            .equals(path.toLowerCase())) {
                        currentPath = currentPath + File.separator + Paths.get(currentPath).relativize(file.toPath()).toString();
                    }
                }
            }
        }
    }

    static void zip(String name) {
        Path path;
        if (!Paths.get(name).isAbsolute()) {
            path = Paths.get(currentPath + File.separator + name);
        } else {
            path = Paths.get(name);
        }
        String zipFileName = name + ".zip";
        try {
            ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(zipFileName));
            Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) {
                    try {
                        Path targetFile = path.relativize(file);
                        outputStream.putNextEntry(new ZipEntry(targetFile.toString()));
                        byte[] bytes = Files.readAllBytes(file);
                        outputStream.write(bytes, 0, bytes.length);
                        outputStream.closeEntry();
                    } catch (IOException e) {
                        System.out.println("Zip failed");
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
            outputStream.close();
        } catch (IOException e) {
            System.out.println("Couldn't zip");
        }
    }

    static void unzip(String name) {
        Path path = Paths.get(name + ".zip");
        if (!path.isAbsolute()) {
            path = Paths.get(currentPath + File.separator + name + ".zip");
        }
        try {
            ZipInputStream inputStream = new ZipInputStream(new FileInputStream(path.toString()));
            File folder = new File(name);
            folder.mkdir();
            ZipEntry zipEntry = inputStream.getNextEntry();
            while (zipEntry != null) {
                String fileName = zipEntry.getName();
                File newFile = new File(name + File.separator + fileName);
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                byte[] buffer = new byte[1024];
                while ((len = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = inputStream.getNextEntry();
            }
            inputStream.close();
        } catch (IOException e) {
            System.out.println("Couldn't unzip");
        }
    }

    static void getEntities() {
        File[] files = new File(currentPath).listFiles();
        if (files !=  null) {
            for (File file : files) {
                System.out.println(Paths.get(currentPath).relativize(file.toPath()));
            }
        }
    }

    static void getFiles() {
        File[] files = new File(currentPath).listFiles();
        if (files != null) {
            for (File file : files) {
                if (Files.isRegularFile(file.toPath())) {
                    System.out.println(Paths.get(currentPath).relativize(file.toPath()));
                }
            }
        }
    }

    static void getSorted() {
        File[] files = new File(currentPath).listFiles();
        if (files != null) {
            Arrays.sort(files);
            for (File file : files) {
                System.out.println(Paths.get(currentPath).relativize(file.toPath()));
            }
        }
    }

    static int countFiles() {
        File[] files = new File(currentPath).listFiles();
        int count = 0;
        if (files != null) {
            for (File file : files) {
                if (Files.isRegularFile(file.toPath())) {
                    count++;
                }
            }
        }
        return count;
    }
}

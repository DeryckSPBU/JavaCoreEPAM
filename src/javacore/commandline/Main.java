package javacore.commandline;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println(CommandLine.getCurrentPath());
            switch (sc.next().toLowerCase()) {
                case "help":
                    CommandLine.help();
                    break;
                case "cd":
                    CommandLine.cd(sc.next());
                    break;
                case "createfile":
                    CommandLine.createFile(sc.next());
                    break;
                case "deletefile":
                    CommandLine.deleteFile(sc.next());
                    break;
                case "renamefile":
                    CommandLine.renameFile(sc.next(), sc.next());
                    break;
                case "createdir":
                    CommandLine.createDir(sc.next());
                    break;
                case "deletedir":
                    CommandLine.deleteDir(sc.next());
                    break;
                case "renamedir":
                    CommandLine.renameDir(sc.next(), sc.next());
                    break;
                case "zip":
                    CommandLine.zip(sc.next());
                    break;
                case "unzip":
                    CommandLine.unzip(sc.next());
                    break;
                case "getentities":
                    CommandLine.getEntities();
                    break;
                case "getfiles":
                    CommandLine.getFiles();
                    break;
                case "getsorted":
                    CommandLine.getSorted();
                    break;
                case "countfiles":
                    System.out.println(CommandLine.countFiles());
                    break;
                case "exit":
                    System.out.println("Closing");
                    CommandLine.exit();
                    break;
                default:
                    System.out.println("Wrong method name or passed arguments");
                    break;
            }
        }
    }
}

package cn.abridge.springframework.test.util;

import java.io.File;

/**
 * @Author: lyd
 * @Description: 打印项目树形结构
 * @Date: 2023/10/8
 */
public class TreePrinter {
    public static void main(String[] args) {
        // 指定文件夹的路径
        String folderPath = "D:\\Code\\Work\\lms\\nladmin-ui\\src\\i18n";

        // 调用递归方法获取文件树并打印
        File folder = new File(folderPath);
        if (folder.exists() && folder.isDirectory()) {
            System.out.println(folder.getAbsolutePath());
            printFileTree(folder, "", true);
        } else {
            System.out.println("指定路径不是一个有效的文件夹。");
        }
    }

    public static void printFileTree(File folder, String prefix, boolean isLast) {
        File[] files = folder.listFiles();
        if (files != null) {
            int count = 0;
            for (File file : files) {
                count++;
                boolean isSubfolder = file.isDirectory();
                boolean isLastItem = count == files.length;

                System.out.print(prefix);
                System.out.print(isLastItem ? "└── " : "├── ");
                System.out.println(file.getName());

                if (isSubfolder) {
                    String newPrefix = prefix + (isLastItem ? "    " : "│   ");
                    printFileTree(file, newPrefix, isLastItem);
                }
            }
        }
    }
}

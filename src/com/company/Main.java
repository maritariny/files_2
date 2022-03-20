package com.company;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Main {

    public static void main(String[] args) {

        // 1. Создать три экземпляра класса GameProgress
        GameProgress gp1 = new GameProgress(100, 10, 0, 0);
        GameProgress gp2 = new GameProgress(120, 66, 1, 10);
        GameProgress gp3 = new GameProgress(130, 100, 2, 20);

        // 2. Сохранить сериализованные объекты GameProgress в папку savegames из предыдущей задачи
        File dirSavegames = new File("D://Games//savegames");
        saveGame(dirSavegames.getPath()+ "\\save1.dat", gp1);
        saveGame(dirSavegames.getPath() + "\\save2.dat", gp2);
        saveGame(dirSavegames.getPath() + "\\save3.dat", gp3);

        // 3. Созданные файлы сохранений из папки savegames запаковать в архив zip
        List<String> filesForZip= new ArrayList<>();
        for (File item : dirSavegames.listFiles()) {
            if (item.isDirectory()) {
                continue;
            }
            filesForZip.add(item.getPath());
        }
        zipFiles(dirSavegames.getPath() + "\\output.zip", filesForZip);

        // 4. Удалить файлы сохранений, лежащие вне архива
        for (File item : dirSavegames.listFiles()) {
            if (item.isDirectory()) {
                continue;
            }
            if (item.getName().endsWith(".zip")) {
                continue;
            }
            if (item.delete()) {
                System.out.println("Удален файл " + item.getPath());
            }
        }
    }

    public static boolean saveGame(String fileFullName, GameProgress gp) {

        try (FileOutputStream fos = new FileOutputStream(fileFullName);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gp);
            System.out.println("Записан файл " + fileFullName);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static boolean zipFiles(String zipFileFullName, List<String> objectsForZip) {

        if (objectsForZip.size() == 0) {
            System.out.println("Не обнаружены файлы для архивирования");
            return false;
        }
        try (ZipOutputStream zout = new ZipOutputStream(new FileOutputStream(zipFileFullName))) {

            int i = 0;
            for (String fileName : objectsForZip) {
                try (FileInputStream fis = new FileInputStream(fileName)) {
                    ZipEntry entry = new ZipEntry(new File(fileName).getName());
                    zout.putNextEntry(entry);
                    byte[] buffer = new byte[fis.available()];
                    fis.read(buffer); // Считывание упаковываемого файла
                    zout.write(buffer); // Запись упаковываемого файла
                    zout.closeEntry();
                    i++;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        System.out.println("Создан zip-архив " + zipFileFullName);
        return true;
    }
}

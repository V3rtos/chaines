package me.moonways.bridgenet.assembly.util;

import lombok.experimental.UtilityClass;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@UtilityClass
public class FilesZipCompressionUtils {

    public File compressToZip(Path parentDirectory, String archiveName, File... filesToCompress) throws IOException {
        File gzipFile = parentDirectory.resolve(archiveName + ".gz").toFile();

        // Создание нового ZIP архива
        FileOutputStream fos = new FileOutputStream(gzipFile);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        // Цикл по всем файлам, которые нужно компрессировать
        for (File file : filesToCompress) {
            // Чтение исходного файла
            FileInputStream fis = new FileInputStream(file);

            // Добавление новой записи в ZIP архив
            ZipEntry zipEntry = new ZipEntry(file.getName());
            zipOut.putNextEntry(zipEntry);

            // Копирование данных из исходного файла в ZIP архив
            byte[] bytes = new byte[fis.available()];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }

            // Закрытие текущей записи
            zipOut.closeEntry();
            fis.close();

            // Удаление исходного файла после компрессии
            file.delete();
        }

        // Закрытие ZIP архива
        zipOut.close();
        fos.close();

        return gzipFile;
    }

}

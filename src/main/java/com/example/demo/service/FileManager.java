package com.example.demo.service;

import com.example.demo.exception.KeyLoadException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileManager {
    FileManager fileService() {
        return new FileManager();
    }

    public static boolean writeObjectToFile(String fileName, Object obj) {
        try(FileOutputStream fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(obj);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static Object readObjectFromFile(String fileName) throws KeyLoadException {
        try(FileInputStream fis = new FileInputStream(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis)){
            return ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new KeyLoadException();
        }
    }

    // 유효한 사용자 확인용 (key파일 존재 유무로)
    public static boolean readFile(String fileName) throws IOException {
        Path path = (new File(fileName)).toPath();
        byte[] rslt = Files.readAllBytes(path);

        return true;
    }

}

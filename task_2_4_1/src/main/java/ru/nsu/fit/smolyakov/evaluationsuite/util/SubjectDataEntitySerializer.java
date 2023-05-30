package ru.nsu.fit.smolyakov.evaluationsuite.util;

import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SubjectDataEntitySerializer {
    public static SubjectData deserialize(String filename) throws IOException {
        FileInputStream fileInputStream
            = new FileInputStream(filename);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
            try {
                return (SubjectData) objectInputStream.readObject();
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void serialize(SubjectData subjectData, String filename) throws IOException {
        FileOutputStream fileOutputStream
            = new FileOutputStream(filename);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(subjectData);
            objectOutputStream.flush();
        }
    }
}

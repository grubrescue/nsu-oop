package ru.nsu.fit.smolyakov.evaluationsuite.util;

import ru.nsu.fit.smolyakov.evaluationsuite.entity.SubjectData;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.ZonedDateTime;

/**
 * Utility class for serializing and deserializing {@link SubjectData} entities.
 * Serialization is done using Java serialization.
 *
 * @see SubjectData
 * @see java.io.Serializable
 */
public class SubjectDataEntitySerializer {
    /**
     * Deserializes a {@link SubjectData} entity from the given file.
     *
     * @param filename file to deserialize from
     * @return deserialized entity
     * @throws IOException if an I/O error occurs
     */
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

    /**
     * Serializes the given {@link SubjectData} entity to the given file.
     *
     * @param subjectData entity to serialize
     * @param filename    file to serialize to
     * @throws IOException if an I/O error occurs
     */
    public static void serialize(SubjectData subjectData, String filename) throws IOException {
        FileOutputStream fileOutputStream
            = new FileOutputStream(filename);
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            subjectData.setLastUpdate(ZonedDateTime.now());
            objectOutputStream.writeObject(subjectData);
            objectOutputStream.flush();
        }
    }
}

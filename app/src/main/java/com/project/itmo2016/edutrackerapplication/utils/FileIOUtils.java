package com.project.itmo2016.edutrackerapplication.utils;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

/**
 * Class with utils for working with Files
 */
public final class FileIOUtils {

    /**
     * Method saves Serializable object to file.
     * If not able to save object to file, object is not saved to file, no Exception thrown.
     *
     * @param obj     is a Serializable object, that will be written to file
     * @param path    is a fileName, where object will be saved
     */
    public static void saveObjectToFile(Serializable obj, String path, Context context) {
        File f = new File(context.getFilesDir(), path);

        ByteArrayOutputStream bos = null;
        ObjectOutput out = null;
        FileOutputStream fout = null;

        try {
            //getting byte array from serializable localSchedule, then saving byte arr to file f
            bos = new ByteArrayOutputStream();

            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();

            fout = new FileOutputStream(f);
            fout.write(bos.toByteArray());

        } catch (Exception e) {
            e.printStackTrace();
            if(!f.delete()) Log.d("FileIOUtils", "unable to delete file");
        } finally {
            try {
                if (bos != null) bos.close();
                if (out != null) out.close();
                if (fout != null) fout.close();
            } catch (IOException ignore) {
            }
        }
    }

    /**
     * Method reads an object of type T from file and returns it. Correct T must be provided. Else null is returned.
     * Warnings about unchecked cast to type T are suppressed
     *
     * @param path    is a path to file, from where to read
     * @param <T>     is a type of object in file
     * @return read object of type T is returned. If unable to read object from file, null is returned.
     */
    @SuppressWarnings("unchecked")
    public static <T> T loadSerializableFromFile(String path, Context context) {
        FileInputStream streamIn = null;
        ObjectInputStream objectInputStream = null;
        T ret = null;

        try {
            streamIn = new FileInputStream(context.getFilesDir() + "/" + path);
            objectInputStream = new ObjectInputStream(streamIn);
            ret = (T) objectInputStream.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (streamIn != null) streamIn.close();
                if (objectInputStream != null) streamIn.close();
            } catch (IOException ignore) {
            }
        }
        return ret;
    }
}

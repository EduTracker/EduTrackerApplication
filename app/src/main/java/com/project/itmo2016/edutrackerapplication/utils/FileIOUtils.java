package com.project.itmo2016.edutrackerapplication.utils;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Created by Aleksandr Tukallo on 07.12.16.
 */

public class FileIOUtils {

    public static void saveObjectToFile(Serializable obj, String path, Context context) {
        File f = new File(context.getFilesDir(), path);

        try {
            ByteArrayOutputStream bos = null;
            ObjectOutput out = null;

            //getting byte array from serializable localSchedule, then saving byte arr to file f
            bos = new ByteArrayOutputStream();

            out = new ObjectOutputStream(bos);
            out.writeObject(obj);
            out.flush();

            FileOutputStream fout = new FileOutputStream(f);
            fout.write(bos.toByteArray());

            //TODO is closing streams ok? Mb transfer to finally?
            bos.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            f.delete();
        }
    }

    public static <T> T loadSerializableFromFile(String path, Context context) {
        try {
            FileInputStream streamIn = new FileInputStream(context.getFilesDir() + "/" + path);
            ObjectInputStream objectInputStream = new ObjectInputStream(streamIn);
            T ret;
            ret = (T) objectInputStream.readObject();
            //TODO is closing streams ok? Mb in finally?
            streamIn.close();
            objectInputStream.close();
            return ret;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

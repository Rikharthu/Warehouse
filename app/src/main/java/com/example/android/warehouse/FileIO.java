package com.example.android.warehouse;


import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

// TODO make it cool like in mememaker
/** helper class for managing file input-output*/
public class FileIO {
    public static final String TAG=FileIO.class.getSimpleName();

    private static FileIO mInstance;
    private  Context mContext;
    private FileIO(Context context){
        mContext = context;
    }

    // TODO save from assets

    /** get file directory, depending on the chosen storage method */
//    public static File getFileDirectory(Context context) {
//        // retrieve storage type from shared preferences by using our helper class
//        WarehouseApplicationSettings settings = new MemeMakerApplicationSettings(context);
//        String storageType = settings.getStoragePreference();
//
//        // depending on selected storage type return according file directory
//        if(storageType.equals(StorageType.INTERNAL)) {
//            // internal storage
//            // data/data/com.teamtreehouse.mememaker/files
//            return context.getFilesDir();
//        } else {
//            // external storage
//            // check for availability
//            if(isExternalStorageAvailable()) {
//                if(storageType.equals(StorageType.PRIVATE_EXTERNAL)) {
//                    return context.getExternalFilesDir(null);
//                } else {
//                    return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
//                }
//            } else {
//                return context.getFilesDir();
//            }
//        }
//    }

    public boolean saveToCache(String text,boolean isExternal){
        File cacheDir;
        if(isExternal){
            if(isExternalStorageReadable()){
                cacheDir= mContext.getExternalCacheDir();
            }else{
                return false;
            }
        }else{
            cacheDir=mContext.getCacheDir();
        }
        File cacheFile = new File(cacheDir,"cache.dat");
        try {
//            FileOutputStream out=mContext.openFileOutput(cacheFile.getAbsolutePath(),Context.MODE_PRIVATE);
            FileOutputStream out=new FileOutputStream(cacheFile);

            out.write(text.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public String readFromCache(boolean isExternal){
        File cacheDir;
        StringBuffer textBuffer=new StringBuffer();
        if(isExternal){
            if(isExternalStorageReadable()){
                cacheDir= mContext.getExternalCacheDir();
            }else{
                return null;
            }
        }else{
            cacheDir=mContext.getCacheDir();
        }
        File cacheFile = new File(cacheDir,"cache.dat");
        try {
            FileInputStream in=new FileInputStream(cacheFile);
            BufferedReader bis=new BufferedReader(new InputStreamReader(in));
            String line;
            while((line=bis.readLine())!=null){
                textBuffer.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.d(TAG,"readFromCache finished()");
        return textBuffer.toString();
    }

    public static FileIO getInstance(Context context){
        if(mInstance==null){
            // let thread enter this block one at a time
            synchronized (FileIO.class){
                // check if it is still null
                if(mInstance==null){
                    mInstance=new FileIO(context);
                }
            }
        }
        return mInstance;
    }

    /** Check external storage for availability */
    public static boolean isExternalStorageAvailable() {
        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /** Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /** Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public double getAvailableExternalMemory(){
        StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
        long sdAvailSize = (long)stat.getAvailableBlocks()
                * (long)stat.getBlockSize();
        //One binary gigabyte equals 1,073,741,824 bytes.
        double gigaAvailable = sdAvailSize / 1073741824.0;
        return gigaAvailable;
    }

    public boolean writeInternal(String filename,String text){
        FileOutputStream outputStream;
        File file =new File(mContext.getFilesDir(),filename);
        Log.d(TAG,"writing to: "+file.getAbsolutePath());

        try {
            // outputStream=new FileOutputStream(file);
            outputStream = mContext.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(text.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        Log.d(TAG,"succesfully written to: "+file.getAbsolutePath());
        // sucessfully written
        return true;
    }


    public boolean writeCache(String filename, String text){

        return false;
    }

    public String readInternal(String filename){
        FileInputStream inputStream;
        File file =new File(mContext.getFilesDir(),filename);
        Log.d(TAG,"writing to: "+file.getAbsolutePath());

        StringBuffer stringBuffer=new StringBuffer();

        try {
            // outputStream=new FileInputStream(file);
            inputStream = mContext.openFileInput(filename);
            int c;
            while((c=inputStream.read())!=-1){
                stringBuffer.append((char)c);
            }

            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Log.d(TAG,"succesfully written to: "+file.getAbsolutePath());
        // sucessfully written
        return stringBuffer.toString();
    }

    public String[] getInternalFiles(){
        return mContext.fileList();
    }

    public String[] getAlbumFiles(){
        // Get the directory for the user's public pictures directory.
        File file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//        if (!file.mkdirs()) {
//            Log.e(LOG_TAG, "Directory not created");
//        }
        return file.list();
    }

}

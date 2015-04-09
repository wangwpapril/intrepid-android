package com.swishlabs.intrepid_android.util;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by ryanracioppo on 2015-04-08.
 */
public class SaveImage {
  public static String saveImageLocally(String imageUrl, String fileName, Context context){
    try {
        // this is the file you want to download from the remote server
        String path = URLEncoder.encode(imageUrl, "utf-8");

        URL u = new URL(path);
        HttpsURLConnection c = (HttpsURLConnection) u.openConnection();
        c.setRequestMethod("GET");
        c.setDoOutput(true);
        c.connect();


        String PATH =
                Environment.getExternalStorageDirectory()
                + "/intrepid/"
                + "/images/";

        Log.v("log_tag", "PATH: " + PATH);
        File file = new File(PATH);
        file.mkdirs();
        File outputFile = new File(file, fileName+".jpg");

        FileOutputStream f = new FileOutputStream(outputFile);
        InputStream in;
        if (c.getResponseCode() > 400) {
            in = c.getErrorStream();
        }else{
            in = c.getInputStream();
        }
        Log.v("log_tag"," InputStream consist of : "+in.read());
        byte[] buffer = new byte[1024];
        int len1 = 0;
        while ((len1 = in.read(buffer)) > 0) {
            //System.out.println("Reading byte : "+in.read(buffer));
            f.write(buffer, 0, len1);
        }
        Toast.makeText(context, "Download Completed Successfully @ "+PATH, Toast.LENGTH_LONG).show();
        f.close();
        return PATH;

    } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Toast.makeText(context, "MalformedURLException", Toast.LENGTH_LONG).show();
        return null;
    } catch (ProtocolException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Toast.makeText(context, "ProtocolException", Toast.LENGTH_LONG).show();
        return null;
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Toast.makeText(context, "FileNotFoundException", Toast.LENGTH_LONG).show();
        return null;
    }catch (UnknownHostException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Toast.makeText(context, "UnknownHostException", Toast.LENGTH_LONG).show();
        return null;
    }
    catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        Toast.makeText(context, "IOException", Toast.LENGTH_LONG).show();
        return null;
    }

}
}
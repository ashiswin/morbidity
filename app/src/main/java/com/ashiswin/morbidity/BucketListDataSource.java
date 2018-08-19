package com.ashiswin.morbidity;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashis on 8/19/2018.
 */

public class BucketListDataSource {
    private static final String FILENAME = "bucketlist.txt";

    private List<String> bucketList;
    private Context context;

    public BucketListDataSource(Context context) {
        this.context = context;
        this.bucketList = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;
            while((line = br.readLine()) != null) {
                Log.d("BLDS-LOOP", line);
                if(line.equals("")) continue;
                bucketList.add(line);
            }
            Log.d("BLDS", "SUCCESS " + bucketList.toString());
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("BLDS", "FAIL");
        }
    }

    public List<String> getBucketList() {
        return bucketList;
    }

    public void saveBucketList(List<String> bucketList) {
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for(String l : bucketList) {
                bw.write(l + "\n");
                Log.d("BLDS-LOOP 2", l);
            }

            this.bucketList = bucketList;
            bw.flush();
            Log.d("BLDS", "SUCCESS " + bucketList.toString());
        } catch(IOException e) {
            e.printStackTrace();
            Log.d("BLDS", "Fail");
        }
    }
}

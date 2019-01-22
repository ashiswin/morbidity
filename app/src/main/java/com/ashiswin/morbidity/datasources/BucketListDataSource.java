package com.ashiswin.morbidity.datasources;

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
import java.util.Base64;
import java.util.List;

/**
 * Created by ashis on 8/19/2018.
 */

public class BucketListDataSource {
    private static final String FILENAME = "bucketlist.txt";

    private List<String> bucketList;
    private List<Boolean> checked;

    private Context context;

    public BucketListDataSource(Context context) {
        this.context = context;
        this.bucketList = new ArrayList<>();
        this.checked = new ArrayList<>();

        try {
            FileInputStream fis = context.openFileInput(FILENAME);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));

            String line;
            while((line = br.readLine()) != null) {
                if(line.equals("")) continue;
                Log.d("BLDS", line);
                String[] arr = line.split(" ");
                bucketList.add(new String(Base64.getDecoder().decode(arr[0])));
                checked.add(Boolean.parseBoolean(arr[1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getBucketList() {
        return bucketList;
    }

    public List<Boolean> getChecked() {
        return checked;
    }

    public void saveBucketList(List<String> bucketList, List<Boolean> checked) {
        Log.d("BLDS", checked.toString());
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, Context.MODE_PRIVATE);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));

            for(int i = 0; i < bucketList.size(); i++) {
                String item = Base64.getEncoder().encodeToString(bucketList.get(i).getBytes());
                Boolean check = checked.get(i);
                bw.write(item + " " + check.toString() + "\n");
                Log.d("BLDS", item + " " + check.toString());
            }

            this.bucketList = bucketList;
            this.checked = checked;

            bw.flush();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}

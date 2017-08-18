/*
 * Copyright (c) 2016. iElettronica.it
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package it.ielettronica.TVFS;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;


/**
 * Created by gnardelli on 11/22/15.
 * used to download the remote xml file from dropbox to local. everytime that internet is not available
 * the local file is used
 */

public class Downloader {

    //Tag for Log statements
    private static String myTag = "StackSites";

    //Handler msg that represents we are posting a progress update.
    static final int POST_PROGRESS = 1;

    /************************************************
     * Download a file from the Internet and store it locally
     *
     * @param URL - the url of the file to download
     * @param fos - a FileOutputStream to save the downloaded file to.
     ************************************************/
    public static void DownloadFromUrl(String URL, FileOutputStream fos) {  //this is the downloader method
        try {

            URL url = new URL(URL); //URL of the file

            //keep the start time so we can display how long it took to the Log.
            long startTime = System.currentTimeMillis();
            Log.d(myTag, "download begining");

			/* Open a connection to that URL. */
            URLConnection ucon = url.openConnection();

            // this will be useful so that you can show a tipical 0-100% progress bar
            //int lenghtOfFile = ucon.getContentLength();

            Log.i(myTag, "Opened Connection");

            /************************************************
             * Define InputStreams to read from the URLConnection.
             ************************************************/
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            Log.i(myTag, "Got InputStream and BufferedInputStream");

            /************************************************
             * Define OutputStreams to write to our file.
             ************************************************/

            BufferedOutputStream bos = new BufferedOutputStream(fos);
            Log.i(myTag, "Got FileOutputStream and BufferedOutputStream");

            /************************************************
             * Start reading the and writing our file.
             ************************************************/
            byte data[] = new byte[1024];
            //long total = 0;
            int count;
            //loop and read the current chunk
            while ((count = bis.read(data)) != -1) {
                //keep track of size for progress.
                //total += count;

                //write this chunk
                bos.write(data, 0, count);
            }
            //Have to call flush or the  file can get corrupted.
            bos.flush();
            bos.close();

            Log.d(myTag, "download ready in "
                    + ((System.currentTimeMillis() - startTime))
                    + " milisec");
        } catch (IOException e) {
            Log.d(myTag, "Error: " + e);
        }
    }
}
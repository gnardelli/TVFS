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

package it.ielettronica.TVFS;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class RemoteCommunications {

    private List<StackSite> stackSites;
    private StackSite stackSite;
    public static final String SERVER_ADDRESS = "http://www.ielettronica.it/";



    public void getStackSites(List<StackSite> sSites, String GroupName, GetSiteCallback callback) {
        new fetchStackSites(sSites, GroupName, callback).execute();
    }

    public void getGroups(List<StackGroup> groups,GetGroupCallback callback) {
        new fetchGroups(groups, callback).execute();
        //new fetchData(groups,"fetchGroups.php","nameGroup","", callback);
    }

    public void getGroupsFromChannel(List<StackGroup> groups,String NameChannel, GetGroupCallback callback) {
        new fetchGroupsFromChannel(groups,NameChannel, callback).execute();
        //new fetchData(links,"fetchLinks.php","link","foreignVideoStream="+NameChannel, callback);
    }

    public void getLinks(List<StackLink> links,String NameChannel, GetLinkCallback callback) {
        new fetchLinks(links,NameChannel, callback).execute();
        //new fetchData(links,"fetchLinks.php","link","foreignVideoStream="+NameChannel, callback);
    }

    public void storeDataInBackground(StackSite sSite,String NameGroup, String AddMod, GetSiteCallback callback) {
        new StoreDataAsyncTask(sSite, NameGroup, AddMod, callback).execute();
    }

    public void deleteLinkInBackground(StackLink lLink,  String NameChannel, GetLinkCallback callback) {
        new DeleteLinkAsyncTask(lLink,  NameChannel, callback).execute();
    }

    public void deleteGroupInBackground(StackGroup NameGroup,  String NameChannel, GetGroupCallback callback) {
        new DeleteGroupAsyncTask(NameGroup,  NameChannel, callback).execute();
    }




    public void deleteSiteInBackground(StackSite sSite, String GroupName, GetSiteCallback callback) {
        new DeleteSiteAsyncTask(sSite,  GroupName, callback).execute();
    }


    public void approveLinkInBackground(StackLink lLink,  String NameChannel, GetIntegerCallback callback) {
        new ApproveLinkAsyncTask(lLink,  NameChannel, callback).execute();
    }

    public void approveChannelInBackground(StackSite sSite, String GroupName, boolean Result, GetSiteCallback callback) {
        new ApproveChannelAsyncTask(sSite,  GroupName, Result, callback).execute();
    }

    public void storeGroupInBackground(StackGroup gGroup,String NameChannel, GetGroupCallback callback) {
        new StoreGroupAsyncTask(gGroup, NameChannel, callback).execute();
    }

    public void storeLinkInBackground(StackLink lLink,String NameChannel, String AddMod, GetLinkCallback callback) {
        new StoreLinkAsyncTask(lLink, NameChannel, AddMod, callback).execute();
    }



    private class DeleteSiteAsyncTask extends  AsyncTask<Void, Void, List<StackSite>> {

        private StackSite sSite;
        private GetSiteCallback sCallBack;
        private HttpURLConnection urlConnection = null;
        private String GroupName;

        public DeleteSiteAsyncTask(StackSite sSite, String GroupName, GetSiteCallback sCallBack) {
            this.sSite = sSite;
            this.sCallBack = sCallBack;
            this.GroupName=GroupName;
        }

        @Override
        protected void onPostExecute(List<StackSite> stackSitesReturned) {
            sCallBack.done(stackSitesReturned, Boolean.TRUE);
            super.onPostExecute(stackSitesReturned);
        }

        private List<StackSite> getStackListFromGSON(String GSONString) {


            List<StackSite> stackSites = new ArrayList<StackSite>();
            StackSite stackSiteL = new StackSite();
            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    JSONObject jObject=jArr.getJSONObject(i);
                    String Name = jObject.getString("name");
                    String Description = jObject.getString("description");
                    String Image = jObject.getString("image");
                    if (MainActivity.isAmministrator()) {
                        int isAccepted = jObject.getInt("isAccepted");
                        stackSiteL = new StackSite(Name, Description, Image, isAccepted);
                    } else {
                        stackSiteL = new StackSite(Name, Description, Image);
                    }
                    stackSites.add(stackSiteL);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return stackSites;
        }

        @Override
        protected List<StackSite> doInBackground(Void... params) {


            List<StackSite> stackSites = new ArrayList<StackSite>();

            try {

                OutputStreamWriter request = null;

                String response = null;
                String parameters = "name="+sSite.getName()+"&GroupFilter="+GroupName;

                try
                {

                    URL url = new URL(SERVER_ADDRESS + "deleteSite.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    //Response from server after login process will be stored in response variable.
                    response = sb.toString();
                    //Toast.makeText(MainActivity.getAppContext(), "Message from Server: \n" + response, Toast.LENGTH_SHORT).show();
                    //You can perform UI operations here


                    stackSites = getStackListFromGSON(sb.toString());

                    isr.close();
                    reader.close();

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return stackSites;
        }
    }



    private class DeleteGroupAsyncTask extends  AsyncTask<Void, Void, List<StackGroup>> {

        private StackGroup NameGroup;
        private GetGroupCallback sCallBack;
        private HttpURLConnection urlConnection = null;
        private String NameChannel;

        public DeleteGroupAsyncTask(StackGroup NameGroup, String NameChannel, GetGroupCallback sCallBack) {
            this.NameGroup = NameGroup;
            this.sCallBack = sCallBack;
            this.NameChannel=NameChannel;
        }

        @Override
        protected void onPostExecute(List<StackGroup> stackSitesReturned) {
            sCallBack.done(stackSitesReturned);
            super.onPostExecute(stackSitesReturned);
        }

        private List<StackGroup> getGroupsFromGSON(String GSONString) {

            List<StackGroup> Groups = new ArrayList<StackGroup>();

            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    int ii = i+1;
                    StackGroup gv = new StackGroup();
                    JSONObject jObject=jArr.getJSONObject(i);
                    String groupName = jObject.getString("group");
                    int groupLevel = jObject.getInt("level");

                    gv.setGroupName(groupName);
                    gv.setGroupLevel(groupLevel);
                    Groups.add(gv);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Groups;
        }

        @Override
        protected List<StackGroup> doInBackground(Void... params) {


            List<StackGroup> Groups = new ArrayList<StackGroup>();

            try {

                OutputStreamWriter request = null;

                String response = null;
                String parameters = "NameGroup="+NameGroup.getGroupName()+"&NameChannel="+NameChannel;

                try
                {

                    URL url = new URL(SERVER_ADDRESS + "deleteGroupFromChannel.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    //Response from server after login process will be stored in response variable.
                    response = sb.toString();
                    //Toast.makeText(MainActivity.getAppContext(), "Message from Server: \n" + response, Toast.LENGTH_SHORT).show();
                    //You can perform UI operations here
                    Groups = getGroupsFromGSON(sb.toString());

                    isr.close();
                    reader.close();

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return Groups;
        }
    }



    private class DeleteLinkAsyncTask extends  AsyncTask<Void, Void, List<StackLink>> {

        private StackLink lLink;
        private GetLinkCallback sCallBack;
        private HttpURLConnection urlConnection = null;
        private String NameChannel;

        public DeleteLinkAsyncTask(StackLink lLink, String NameChannel, GetLinkCallback sCallBack) {
            this.lLink = lLink;
            this.sCallBack = sCallBack;
            this.NameChannel=NameChannel;
        }

        @Override
        protected void onPostExecute(List<StackLink> stackSitesReturned) {
            sCallBack.done(stackSitesReturned);
            super.onPostExecute(stackSitesReturned);
        }

        private List<StackLink> getLinksFromGSON(String GSONString) {

            List<StackLink> Links = new ArrayList<StackLink>();

            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    int ii = i+1;
                    StackLink lv = new StackLink();
                    JSONObject jObject=jArr.getJSONObject(i);
                    String Link = jObject.getString("link");
                    int isAccepted = jObject.getInt("accepted");
                    int linkID = jObject.getInt("linkID");
                    String linkName = jObject.getString("linkName");
                    lv.setAccepted(isAccepted);
                    lv.setLinkValue(Link);
                    lv.setLinkTxt(linkName);
                    lv.setLinkID(linkID);
                    Links.add(lv);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Links;
        }

        @Override
        protected List<StackLink> doInBackground(Void... params) {


            List<StackLink> Links = new ArrayList<StackLink>();

            try {

                OutputStreamWriter request = null;

                String response = null;
                String parameters = "linkID="+lLink.getLinkID()+"&NameChannel="+NameChannel;

                try
                {

                    URL url = new URL(SERVER_ADDRESS + "deleteLink.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    //Response from server after login process will be stored in response variable.
                    response = sb.toString();
                    //Toast.makeText(MainActivity.getAppContext(), "Message from Server: \n" + response, Toast.LENGTH_SHORT).show();
                    //You can perform UI operations here
                    Links = getLinksFromGSON(sb.toString());

                    isr.close();
                    reader.close();

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return Links;
        }
    }



    private class ApproveChannelAsyncTask extends  AsyncTask<Void, Void, List<StackSite>> {

        private StackSite sSite;
        private GetSiteCallback sCallBack;
        private HttpURLConnection urlConnection = null;
        private String GroupName;
        private boolean Result;

        public ApproveChannelAsyncTask(StackSite sSite, String GroupName, boolean Result,  GetSiteCallback sCallBack) {
            this.sSite = sSite;
            this.sCallBack = sCallBack;
            this.GroupName=GroupName;
            this.Result = Result;
            this.Result = Boolean.TRUE;
        }


        @Override
        protected void onPostExecute(List<StackSite> stackSitesReturned) {
            sCallBack.done(stackSitesReturned, Result);
            super.onPostExecute(stackSitesReturned);
        }



        private List<StackSite> getStackListFromGSON(String GSONString) {

            int isAcceptedBefore = sSite.getAccepted();
            String NameChannel = sSite.getName();

            List<StackSite> stackSites = new ArrayList<StackSite>();
            StackSite stackSiteL = new StackSite();
            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    JSONObject jObject=jArr.getJSONObject(i);
                    String Name = jObject.getString("name");
                    String Description = jObject.getString("description");
                    String Image = jObject.getString("image");
                    if (MainActivity.isAmministrator()) {
                        int isAccepted = jObject.getInt("isAccepted");
                        if (Name.equals(NameChannel)) {
                            if (isAccepted == isAcceptedBefore) {
                                Result = Boolean.FALSE;
                            }
                        }
                        stackSiteL = new StackSite(Name, Description, Image, isAccepted);
                    } else {
                        stackSiteL = new StackSite(Name, Description, Image);
                    }
                    stackSites.add(stackSiteL);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return stackSites;
        }


        @Override
        protected List<StackSite> doInBackground(Void... params) {

            List<StackSite> stackSites = new ArrayList<StackSite>();

            try {

                OutputStreamWriter request = null;

                String response = null;
                String parameters = "name="+sSite.getName()+"&isAccepted="+sSite.getAccepted()+"&GroupFilter="+GroupName;

                try
                {

                    URL url = new URL(SERVER_ADDRESS + "approveSite.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    //Response from server after login process will be stored in response variable.
                    response = sb.toString();
                    //Toast.makeText(MainActivity.getAppContext(), "Message from Server: \n" + response, Toast.LENGTH_SHORT).show();
                    //You can perform UI operations here

                    stackSites = getStackListFromGSON(sb.toString());

                    isr.close();
                    reader.close();

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return stackSites;
        }
    }




    private class ApproveLinkAsyncTask extends  AsyncTask<Void, Void, Integer> {

        private StackLink lLink;
        private GetIntegerCallback sCallBack;
        private HttpURLConnection urlConnection = null;
        private String NameChannel;

        public ApproveLinkAsyncTask(StackLink lLink, String NameChannel, GetIntegerCallback sCallBack) {
            this.lLink = lLink;
            this.sCallBack = sCallBack;
            this.NameChannel=NameChannel;
        }

        @Override
        protected void onPostExecute(Integer disappAlsoChannel) {
            sCallBack.done(disappAlsoChannel);
            super.onPostExecute(disappAlsoChannel);
        }

        @Override
        protected Integer doInBackground(Void... params) {

            try {

                OutputStreamWriter request = null;

                String response = null;
                String parameters = "linkID="+lLink.getLinkID()+"&name="+NameChannel+"&isAccepted="+lLink.getAccepted();

                try
                {

                    URL url = new URL(SERVER_ADDRESS + "approveLink.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    response = sb.toString();


                    int ResultInt;
                    if (response.equals("0\n")) {
                        ResultInt = 0;
                    } else {
                        ResultInt = 1;
                    }


                    isr.close();
                    reader.close();

                    return ResultInt;

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return 0;
        }
    }


    private class StoreGroupAsyncTask extends  AsyncTask<Void, Void, List<StackGroup>> {

        private StackGroup gGroup;
        private String nNameChannel;
        private GetGroupCallback sCallBack;
        private HttpURLConnection urlConnection = null;

        public StoreGroupAsyncTask(StackGroup gGroup, String NameChannel,GetGroupCallback sCallBack) {
            this.gGroup = gGroup;
            this.nNameChannel=NameChannel;
            this.sCallBack = sCallBack;
        }

        @Override
        protected void onPostExecute(List<StackGroup> stackSitesReturned) {
            sCallBack.done(stackSitesReturned);
            super.onPostExecute(stackSitesReturned);
        }


        private List<StackGroup> getLinksFromGSON(String GSONString) {

            List<StackGroup> Groups = new ArrayList<StackGroup>();

            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    int ii = i+1;
                    StackGroup gv = new StackGroup();
                    JSONObject jObject=jArr.getJSONObject(i);
                    String groupName = jObject.getString("group");
                    int groupLevel = jObject.getInt("level");

                    gv.setGroupName(groupName);
                    gv.setGroupLevel(groupLevel);
                    Groups.add(gv);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Groups;
        }

        @Override
        protected List<StackGroup> doInBackground(Void... params) {

            List<StackGroup> Links = new ArrayList<StackGroup>();

            try {

                OutputStreamWriter request = null;

                String response = null;
                String parameters = "groupName="+gGroup.getGroupName()+"&NameChannel="+nNameChannel;

                try
                {

                    URL url = new URL(SERVER_ADDRESS + "storeGroup.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    //Response from server after login process will be stored in response variable.
                    response = sb.toString();
                    //Toast.makeText(MainActivity.getAppContext(), "Message from Server: \n" + response, Toast.LENGTH_SHORT).show();
                    //You can perform UI operations here
                    Links = getLinksFromGSON(sb.toString());

                    isr.close();
                    reader.close();

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return Links;
        }
    }


    private class StoreLinkAsyncTask extends  AsyncTask<Void, Void, List<StackLink>> {

        private StackLink lLink;
        private String nNameChannel;
        private GetLinkCallback sCallBack;
        private HttpURLConnection urlConnection = null;
        private String AddMod;

        public StoreLinkAsyncTask(StackLink lLink, String NameChannel, String AddMod, GetLinkCallback sCallBack) {
            this.lLink = lLink;
            this.nNameChannel=NameChannel;
            this.sCallBack = sCallBack;
            this.AddMod = AddMod;
        }

        @Override
        protected void onPostExecute(List<StackLink> stackSitesReturned) {
            sCallBack.done(stackSitesReturned);
            super.onPostExecute(stackSitesReturned);
        }


        private List<StackLink> getLinksFromGSON(String GSONString) {

            List<StackLink> Links = new ArrayList<StackLink>();

            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    int ii = i+1;
                    StackLink lv = new StackLink();
                    JSONObject jObject=jArr.getJSONObject(i);
                    String Link = jObject.getString("link");
                    int isAccepted = jObject.getInt("accepted");
                    int linkID = jObject.getInt("linkID");
                    String linkName = jObject.getString("linkName");
                    lv.setAccepted(isAccepted);
                    lv.setLinkValue(Link);
                    lv.setLinkTxt(linkName);
                    lv.setLinkID(linkID);
                    Links.add(lv);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Links;
        }

        @Override
        protected List<StackLink> doInBackground(Void... params) {

            List<StackLink> Links = new ArrayList<StackLink>();

            try {

                OutputStreamWriter request = null;

                String response = null;
                String parameters = "linkTxt="+lLink.getLinkTxt()+"&NameChannel="+nNameChannel+"&linkID="+lLink.getLinkID()+"&AddMod="+AddMod+"&link="+lLink.getLinkValue()+"&isAccepted="+lLink.getAccepted();

                try
                {

                    URL url = new URL(SERVER_ADDRESS + "storeLink.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    //Response from server after login process will be stored in response variable.
                    response = sb.toString();
                    //Toast.makeText(MainActivity.getAppContext(), "Message from Server: \n" + response, Toast.LENGTH_SHORT).show();
                    //You can perform UI operations here
                    Links = getLinksFromGSON(sb.toString());

                    isr.close();
                    reader.close();

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return Links;
        }
    }

    private class StoreDataAsyncTask extends  AsyncTask<Void, Void, List<StackSite>> {

        private StackSite sSite;
        private GetSiteCallback sCallBack;
        private String NameGroup;
        private HttpURLConnection urlConnection = null;
        private String AddMod;

        public StoreDataAsyncTask(StackSite sSite, String NameGroup, String AddMod, GetSiteCallback sCallBack) {
            this.sSite = sSite;
            this.NameGroup = NameGroup;
            this.sCallBack = sCallBack;
            this.AddMod = AddMod;
        }



        @Override
        protected void onPostExecute(List<StackSite> stackSitesReturned) {
            sCallBack.done(stackSitesReturned, Boolean.TRUE);
            super.onPostExecute(stackSitesReturned);
        }

        private List<StackSite> getStackListFromGSON(String GSONString) {


            List<StackSite> stackSites = new ArrayList<StackSite>();
            StackSite stackSiteL = new StackSite();
            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    JSONObject jObject=jArr.getJSONObject(i);
                    String Name = jObject.getString("name");
                    String Description = jObject.getString("description");
                    String Image = jObject.getString("image");
                    if (MainActivity.isAmministrator()) {
                        int isAccepted = jObject.getInt("isAccepted");
                        stackSiteL = new StackSite(Name,Description, Image, isAccepted);
                    } else {
                        stackSiteL = new StackSite(Name,Description, Image);
                    }
                    stackSites.add(stackSiteL);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return stackSites;
        }


        @Override
        protected List<StackSite> doInBackground(Void... params) {

            List<StackSite> stackSites = new ArrayList<StackSite>();
            try {



                OutputStreamWriter request = null;

                String response = null;
                String parameters = "name="+sSite.getName()+"&oldname="+sSite.getStaticName()+"&linkVal="+sSite.getLink()+"&AddMod="+AddMod+"&description="+sSite.getAbout()+"&NameGroup="+NameGroup+"&image="+sSite.getImgUrl();

                try
                {

                    URL url = new URL(SERVER_ADDRESS + "storeVideoStream.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.write(parameters);
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                     while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                     }
                     //Response from server after login process will be stored in response variable.
                    response = sb.toString();
                    //Toast.makeText(MainActivity.getAppContext(), "Message from Server: \n" + response, Toast.LENGTH_SHORT).show();
                    //You can perform UI operations here


                    stackSites = getStackListFromGSON(sb.toString());

                    isr.close();
                    reader.close();

                }
                catch(IOException e)
                {
                   e.printStackTrace();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return stackSites;
        }
    }

    private class fetchStackSites extends AsyncTask<Void, Void, List<StackSite>> {


        private List<StackSite> mItems;
        StackSite stackSite;
        String mGroupName;
        GetSiteCallback siteCallback;
        HttpURLConnection urlConnection = null;

        public fetchStackSites(List<StackSite> stackSites, String GroupName, GetSiteCallback siteCallback) {
            this.mItems = stackSites;
            this.mGroupName = GroupName;
            this.siteCallback = siteCallback;
        }

        @Override
        protected void onPostExecute(List<StackSite> stackSitesReturned) {
            siteCallback.done(stackSitesReturned, Boolean.TRUE);
            super.onPostExecute(stackSitesReturned);
        }


        private List<StackSite> getStackListFromGSON(String GSONString) {


            List<StackSite> stackSites = new ArrayList<StackSite>();
            StackSite stackSiteL = new StackSite();
            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    JSONObject jObject=jArr.getJSONObject(i);
                    String Name = jObject.getString("name");
                    String Description = jObject.getString("description");
                    String Image = jObject.getString("image");
                    if (MainActivity.isAmministrator()) {
                        int isAccepted = jObject.getInt("isAccepted");
                        stackSiteL = new StackSite(Name, Description, Image, isAccepted);
                    } else {
                        stackSiteL = new StackSite(Name, Description, Image);
                    }
                    stackSites.add(stackSiteL);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return stackSites;
        }

        @Override
        protected List<StackSite> doInBackground(Void... params) {
            List<StackSite> stackSites = new ArrayList<StackSite>();
            StackSite stackSiteL = new StackSite();
            try {

                OutputStreamWriter request = null;
                String response = null;
                String parameters = "GroupFilter="+mGroupName;
                URL url;
                if (MainActivity.isAmministrator()) {
                    url = new URL(SERVER_ADDRESS + "fetchVideoStreamAdmin.php");
                } else {
                    url = new URL(SERVER_ADDRESS + "fetchVideoStream.php");
                }
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestMethod("POST");

                request = new OutputStreamWriter(urlConnection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();
                String line = "";
                InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                //Response from server after login process will be stored in response variable.

                stackSites = getStackListFromGSON(sb.toString());

                isr.close();
                reader.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return stackSites;
        }
    }





    private class fetchString extends AsyncTask<Void, Void, List<String>> {


        private List<String> mLinks;
        GetStringCallback mDataCallback;
        String mParams;
        String mFilephp;
        String mValueGetFromPhp;
        HttpURLConnection urlConnection = null;

        public fetchString(List<String> links, String filephp, String valueGetFromPhp, String params, GetStringCallback dataCallback) {
            this.mLinks = links;
            this.mFilephp = filephp;
            this.mValueGetFromPhp = valueGetFromPhp;
            this.mParams = params;
            this.mDataCallback = dataCallback;
        }

        @Override
        protected void onPostExecute(List<String> dataReturned) {
            mDataCallback.done(dataReturned);
            super.onPostExecute(dataReturned);
        }


        private List<String> getValuessFromGSON(String GSONString) {

            List<String> Links = new ArrayList<String>();
            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    JSONObject jObject=jArr.getJSONObject(i);
                    String Link = jObject.getString(mValueGetFromPhp);
                    Links.add(Link);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Links;
        }


        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> ValuesFromPhp = new ArrayList<String>();


            try
            {

                OutputStreamWriter request = null;
                String response = null;
                String parameters = mParams;
                URL url = new URL(SERVER_ADDRESS + mFilephp);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestMethod("POST");


                request = new OutputStreamWriter(urlConnection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();
                String line = "";
                InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                //Response from server after login process will be stored in response variable.
                ValuesFromPhp = getValuessFromGSON(sb.toString());

                isr.close();
                reader.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }


            return ValuesFromPhp;
        }
    }




    private class fetchGroupsFromChannel extends AsyncTask<Void, Void, List<StackGroup>> {


        private List<StackGroup> mGroups;
        private GetGroupCallback groupsCallback;
        private String NameChannel;
        private HttpURLConnection urlConnection = null;

        public fetchGroupsFromChannel(List<StackGroup> mGroups, String NameChannel, GetGroupCallback groupsCallback) {
            this.mGroups = mGroups;
            this.NameChannel = NameChannel;
            this.groupsCallback = groupsCallback;
        }

        @Override
        protected void onPostExecute(List<StackGroup> groupsReturned) {
            groupsCallback.done(groupsReturned);
            super.onPostExecute(groupsReturned);
        }


        private List<StackGroup> getGroupsFromGSON(String GSONString) {

            List<StackGroup> Groups = new ArrayList<StackGroup>();

            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    int ii = i+1;
                    StackGroup gv = new StackGroup();
                    JSONObject jObject=jArr.getJSONObject(i);
                    String groupName = jObject.getString("nameGroup");
                    int groupLevel = jObject.getInt("level");

                    gv.setGroupName(groupName);
                    gv.setGroupLevel(groupLevel);
                    Groups.add(gv);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Groups;
        }

        @Override
        protected List<StackGroup> doInBackground(Void... params) {


            List<StackGroup> Groups = new ArrayList<StackGroup>();



            try
            {

                OutputStreamWriter request = null;
                String response = null;
                String parameters = "nameChannel="+NameChannel;
                URL url = new URL(SERVER_ADDRESS + "fetchGroupsFromChannel.php");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                urlConnection.setRequestMethod("POST");


                request = new OutputStreamWriter(urlConnection.getOutputStream());
                request.write(parameters);
                request.flush();
                request.close();
                String line = "";
                InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                BufferedReader reader = new BufferedReader(isr);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                //Response from server after login process will be stored in response variable.
                Groups = getGroupsFromGSON(sb.toString());

                isr.close();
                reader.close();

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Groups;
        }
    }



    private class fetchLinks extends AsyncTask<Void, Void, List<StackLink>> {


        private List<StackLink> mLinks;
        private GetLinkCallback linksCallback;
        private String NameChannel;
        private HttpURLConnection urlConnection = null;

        public fetchLinks(List<StackLink> links, String NameChannel, GetLinkCallback linksCallback) {
            this.mLinks = links;
            this.NameChannel = NameChannel;
            this.linksCallback = linksCallback;
        }

        @Override
        protected void onPostExecute(List<StackLink> linksReturned) {
            linksCallback.done(linksReturned);
            super.onPostExecute(linksReturned);
        }


        private List<StackLink> getLinksFromGSON(String GSONString) {

            List<StackLink> Links = new ArrayList<StackLink>();

            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    int ii = i+1;
                    StackLink lv = new StackLink();
                    JSONObject jObject=jArr.getJSONObject(i);
                    String Link = jObject.getString("link");
                    int isAccepted = jObject.getInt("accepted");
                    int linkID = jObject.getInt("linkID");
                    String linkName = jObject.getString("linkName");
                    lv.setAccepted(isAccepted);
                    lv.setLinkValue(Link);
                    lv.setLinkTxt(linkName);
                    lv.setLinkID(linkID);
                    Links.add(lv);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Links;
        }

        @Override
        protected List<StackLink> doInBackground(Void... params) {


            List<StackLink> Links = new ArrayList<StackLink>();



                    try
                    {

                        OutputStreamWriter request = null;
                        String response = null;
                        String parameters = "foreignVideoStream="+NameChannel;
                        URL url;
                        if (MainActivity.isAmministrator()) {
                            url = new URL(SERVER_ADDRESS + "fetchLinksAdmin.php");
                        } else {
                            url = new URL(SERVER_ADDRESS + "fetchLinks.php");
                        }
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setDoOutput(true);
                        urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                        urlConnection.setRequestMethod("POST");


                        request = new OutputStreamWriter(urlConnection.getOutputStream());
                        request.write(parameters);
                        request.flush();
                        request.close();
                        String line = "";
                        InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                        BufferedReader reader = new BufferedReader(isr);
                        StringBuilder sb = new StringBuilder();
                        while ((line = reader.readLine()) != null)
                        {
                            sb.append(line + "\n");
                        }
                        //Response from server after login process will be stored in response variable.
                        Links = getLinksFromGSON(sb.toString());

                        isr.close();
                        reader.close();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }

            return Links;
        }
    }



    private class fetchGroups extends AsyncTask<Void, Void, List<StackGroup>> {


        private List<StackGroup> mGroups;
        GetGroupCallback groupsCallback;
        HttpURLConnection urlConnection = null;

        public fetchGroups(List<StackGroup> groups, GetGroupCallback groupsCallback) {
            this.mGroups = groups;
            this.groupsCallback = groupsCallback;
        }

        @Override
        protected void onPostExecute(List<StackGroup> groupsReturned) {
            groupsCallback.done(groupsReturned);
            super.onPostExecute(groupsReturned);
        }


        private List<StackGroup> getgroupsFromGSON(String GSONString) {

            List<StackGroup> Groups = new ArrayList<StackGroup>();
            try {
                JSONArray jArr=new JSONArray(GSONString);

                for(int i=0;i<jArr.length();i++) {
                    StackGroup sg = new StackGroup();

                    JSONObject jObject=jArr.getJSONObject(i);
                    sg.setGroupName(jObject.getString("nameGroup"));
                    sg.setGroupLevel(jObject.getInt("level"));
                    sg.setGroupType(jObject.getInt("type"));
                    Groups.add(sg);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return Groups;
        }

        @Override
        protected List<StackGroup> doInBackground(Void... params) {
            List<StackGroup> groups = new ArrayList<StackGroup>();

            try {

                OutputStreamWriter request = null;

                try
                {
                    URL url = new URL(SERVER_ADDRESS + "fetchGroups.php");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(true);
                    urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    urlConnection.setRequestMethod("POST");


                    request = new OutputStreamWriter(urlConnection.getOutputStream());
                    request.flush();
                    request.close();
                    String line = "";
                    InputStreamReader isr = new InputStreamReader(urlConnection.getInputStream());
                    BufferedReader reader = new BufferedReader(isr);
                    StringBuilder sb = new StringBuilder();
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    //Response from server after login process will be stored in response variable.
                    groups = getgroupsFromGSON(sb.toString());

                    isr.close();
                    reader.close();

                }
                catch(IOException e)
                {
                    e.printStackTrace();
                }



            } catch (Exception ex) {
                ex.printStackTrace();
            }

            return groups;
        }
    }


}

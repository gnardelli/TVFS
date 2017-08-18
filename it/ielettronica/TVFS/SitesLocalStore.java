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

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SitesLocalStore {

    public static final String SP_NAME = "spStackListArray";
    SharedPreferences sitesLocalDatabase;

    public SitesLocalStore(Context context) {
        sitesLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }




    public void deleteGroupListArray() {


        SharedPreferences.Editor spEditor = sitesLocalDatabase.edit();
        spEditor.remove("JSONGroupString");
        spEditor.apply();

    }


    public void storeStackListArray(List<StackSite> StackSites) {

        Gson gson = new Gson();
        String jsArray = gson.toJson(StackSites);


        SharedPreferences.Editor spEditor = sitesLocalDatabase.edit();
        spEditor.putString("JSONString", jsArray);
        spEditor.apply();

    }




    public void storeGroupListArray(List<StackGroup> Groups) {

        Gson gson = new Gson();
        String jsArray = gson.toJson(Groups);

        SharedPreferences.Editor spEditor = sitesLocalDatabase.edit();
        spEditor.putString("JSONGroupString", jsArray);
        spEditor.apply();

    }


    public List<StackGroup> getGroupListArray() {

        String JSONString = sitesLocalDatabase.getString("JSONGroupString", "");
        List<StackGroup> groups = new ArrayList<StackGroup>();

        try {
            JSONArray jArr=new JSONArray(JSONString);

            for(int i=0;i<jArr.length();i++) {
                JSONObject jObject=jArr.getJSONObject(i);
                String Name = jObject.getString("NameGroup");
                StackGroup groupsV = new StackGroup();
                groupsV.setGroupName(Name);
                groups.add(groupsV);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return groups;
    }


    public List<StackSite> getStackListArray() {

        String JSONString = sitesLocalDatabase.getString("JSONString", "");

        List<StackSite> stackSites = new ArrayList<StackSite>();

        try {
            JSONArray jArr=new JSONArray(JSONString);

            for(int i=0;i<jArr.length();i++) {
                JSONObject jObject=jArr.getJSONObject(i);
                String Name = jObject.getString("name");
                String Description = jObject.getString("about");
                String Image = jObject.getString("imgUrl");
                int isAccepted = jObject.getInt("accepted");
                StackSite stackSiteL = new StackSite(Name,Description,Image,isAccepted);
                stackSites.add(stackSiteL);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return stackSites;
    }



}

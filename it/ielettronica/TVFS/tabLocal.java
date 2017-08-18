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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;


public class tabLocal extends Fragment  {


    private Context mContext;
    private  List<StackSite> itemsLocal;
    public static  DynamicListView sitesLocal;
    //private MyListAdapter adapterLocal;
    //private StableArrayAdapter adapterLocal;
    private static View viewLocal;
    private Button btnAddNewItem;
    public static TextView editEmptyLocalList;
    MyDBHandler dbHandler;
    public static Fragment Fa;

    public void getArgument(Context mContext) {
        //this.mConnection=mConnection;
        this.mContext = mContext;
    }

    @Override
    public void onResume() {
        super.onResume();


            try {
                itemsLocal = dbHandler.getStackSites();
                if (itemsLocal.size()==0) {
                    editEmptyLocalList.setVisibility(View.VISIBLE);
                } else {
                    editEmptyLocalList.setVisibility(View.INVISIBLE);
                }
                sitesLocal.setCheeseList(itemsLocal);
                sitesLocal.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                StableArrayAdapter adapterLocal = new StableArrayAdapter(getContext(), R.layout.row_site_local, itemsLocal);
                sitesLocal.setAdapter(adapterLocal);
                tabLocal.sitesLocal.getChildAt(0);
                tabLocal.sitesLocal.setAdapter(adapterLocal);
                tabLocal.sitesLocal.setSelection(MainActivity.posLocalListBeforeExecuted);
            } catch (Exception ex) {
                System.out.print("This is case the display is out");
            }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewLocal =inflater.inflate(R.layout.tablocal,container,false);
        Fa = this;
        dbHandler = new MyDBHandler(tabLocal.Fa.getContext(), null, null,1);

        sitesLocal = (DynamicListView) viewLocal.findViewById(R.id.sitesLocal);
        editEmptyLocalList = (TextView) viewLocal.findViewById(R.id.editEmptyLocalList);

        btnAddNewItem = (Button) viewLocal.findViewById(R.id.btnAdd);
        btnAddNewItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), AddNewChannel.class);
                intent.putExtra("TitleChannel", "");
                startActivity(intent);

            }
        });


        try {
            itemsLocal = dbHandler.getStackSites();

            if (itemsLocal.size()==0) {
                editEmptyLocalList.setVisibility(View.VISIBLE);
            } else {
                editEmptyLocalList.setVisibility(View.INVISIBLE);
            }

            sitesLocal.setCheeseList(itemsLocal);
            sitesLocal.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            StableArrayAdapter adapterLocal = new StableArrayAdapter(mContext, R.layout.row_site_local, itemsLocal);
            sitesLocal.setAdapter(adapterLocal);

        } catch (Exception ex) {
            System.out.print("This is case the display is out");
        }
            return viewLocal;

        }




}

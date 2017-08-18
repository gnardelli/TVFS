
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

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class tabFromDB extends Fragment  {

    protected Context mContext;
    private static String GroupSeletced;
    private static StackGroup GroupVSeletced;
    public static Fragment fa;
    public static boolean isExecutedOutSite;
    private  List<StackSite> itemsSS;
    public static ListView sitesMusic;
    private static Spinner sGroup;
    private MyListAdapterExt adapterBtn2;
    private MyListAdapterExt adapterFiltered;
    private  EditText searchText;
    protected static View view;
    private static List<String> groupsS;
    private static List<StackGroup> groupsV;



    //public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADDRESS = "http://www.ielettronica.it/";
    StackSite stackSite;
    private AlertDialog OptionDialog;
    private int posSpinner;
    private int posSpinnerLink;
    List<StackLink> retLinks;
    List<String> retLinksString;
    Spinner listLinks;
    Button btnAdd;

    @Override
    public void onResume() {

        super.onResume();
        if (isAdded()) {

            sitesMusic = (ListView) view.findViewById(R.id.sitesMusic);
            sGroup = (Spinner) view.findViewById(R.id.spGroup);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, groupsS);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sGroup.setAdapter(adapter);



            //if (isExecutedOutSite == Boolean.FALSE) {


                int spinnerPosition = adapter.getPosition(GroupSeletced);
                if (spinnerPosition != -1) {
                    sGroup.setSelection(spinnerPosition);
                    GroupVSeletced = groupsV.get(spinnerPosition);
                    sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);
                } else {


                    GroupSeletced = "English Channels";
                    GroupVSeletced = new StackGroup();
                    GroupVSeletced.setGroupName(GroupSeletced);
                    GroupVSeletced.setGroupLevel(0);
                    GroupVSeletced.setGroupType(0);

                    RemoteCommunications rm = new RemoteCommunications();
                    List<StackSite> returnedSite = new ArrayList<StackSite>();
                    rm.getStackSites(returnedSite, GroupSeletced, new GetSiteCallback() {
                        @Override
                        public void done(List<StackSite> returnedSite, boolean Result) {
                            itemsSS = returnedSite;
                            List<StackSite> itemsSSF = getFilterStackSite(itemsSS, searchText.getText().toString());
                            adapterBtn2 = new MyListAdapterExt(getContext(), itemsSSF, sitesMusic, GroupVSeletced);
                            sitesMusic.setAdapter(adapterBtn2);
                            sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);
                        }

                    });

                }

            //}

        }
    }



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


                    RemoteCommunications rm = new RemoteCommunications();
                    view = inflater.inflate(R.layout.tabremotedb, container, false);
                    sitesMusic = (ListView) view.findViewById(R.id.sitesMusic);
                    searchText = (EditText) view.findViewById(R.id.searchText);
                    sGroup = (Spinner) view.findViewById(R.id.spGroup);
                    btnAdd = (Button) view.findViewById(R.id.btnAdd);
                    isExecutedOutSite = Boolean.FALSE;
                    fa = this;


                    if (MainActivity.isAmministrator()) {
                        btnAdd.setVisibility(View.VISIBLE);

                    } else {
                        btnAdd.setVisibility(View.INVISIBLE);
                    }


                    if (!MainActivity.createFirstTime)  {

                        groupsS = new ArrayList<String>();
                        groupsV = new ArrayList<StackGroup>();

                        List<StackGroup> ListNameGroup = new ArrayList<StackGroup>();

                        rm.getGroups(ListNameGroup, new GetGroupCallback() {
                            @Override
                            public void done(List<StackGroup> returnedGroups) {


                                for (int i = 0; i < returnedGroups.size(); i++) {
                                    int Level = returnedGroups.get(i).getGroupLevel();
                                    if (Level <= MainActivity.getGroupLevel()) {
                                        groupsV.add(returnedGroups.get(i));
                                        groupsS.add(returnedGroups.get(i).getGroupName());
                                    }
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, groupsS);
                                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                sGroup.setAdapter(adapter);
                            }
                        });

                        if (GroupSeletced == null) {
                            GroupSeletced = "English Channels";
                        }


                        ///////////////////////////////////////////////////////////////////////////
                        //// this populates the lists and this have to be different with the admin
                        ///////////////////////////////////////////////////////////////////////////
                        List<StackSite> returnedSite = new ArrayList<StackSite>();
                        rm.getStackSites(returnedSite, GroupSeletced, new GetSiteCallback() {
                            @Override
                            public void done(List<StackSite> returnedSite, boolean Result) {
                                itemsSS = returnedSite;
                                List<StackSite> itemsSSF = getFilterStackSite(itemsSS, searchText.getText().toString());
                                adapterBtn2 = new MyListAdapterExt(getContext(), itemsSSF, sitesMusic, GroupVSeletced);
                                sitesMusic.setAdapter(adapterBtn2);
                                //sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);
                            }

                        });


                        MainActivity.createFirstTime=Boolean.TRUE;

                     }



                btnAdd.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {



                        MainActivity.posRemoteListBeforeExecuted = sitesMusic.getFirstVisiblePosition();
                        Intent intent = new Intent(MainActivity.getAppContext(), ModChannel.class);


                        intent.putExtra("TitleChannel", "");
                        intent.putExtra("DescrChannel", "");
                        intent.putExtra("IconChannel", MainActivity.imageNullDefault);
                        intent.putExtra("isAccepted", -1);
                        intent.putExtra("NameGroup", GroupVSeletced.getGroupName());
                        intent.putExtra("LevelGroup", GroupVSeletced.getGroupLevel());
                        intent.putExtra("TypeGroup", GroupVSeletced.getGroupType());

                        intent.putExtra("GroupLevel", GroupVSeletced.getGroupLevel());
                        tabFromDB.fa.startActivity(intent);


                    }
                });




                sGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        int intGroupSelected = sGroup.getSelectedItemPosition();
                        GroupSeletced = groupsS.get(intGroupSelected);
                        GroupVSeletced = groupsV.get(intGroupSelected);
                        List<StackSite> returnedSite = new ArrayList<StackSite>();
                        RemoteCommunications rml = new RemoteCommunications();
                        rml.getStackSites(returnedSite, GroupSeletced, new GetSiteCallback() {
                            @Override
                            public void done(List<StackSite> returnedSite, boolean Result) {
                                itemsSS = returnedSite;
                                List<StackSite> itemsSSF = getFilterStackSite(itemsSS, searchText.getText().toString());
                                //SitesLocalStore sls = new SitesLocalStore(MainActivity.getAppContext());
                                //sls.storeStackListArray(returnedSite);
                                adapterBtn2 = new MyListAdapterExt(getContext(), itemsSSF, sitesMusic, GroupVSeletced);
                                sitesMusic.setAdapter(adapterBtn2);
                                sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);

                            }

                        });

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });





                searchText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int aft) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        if (GroupSeletced == null) {
                            GroupSeletced = "English Channels";
                        }

                        RemoteCommunications rm = new RemoteCommunications();
                        List<StackSite> returnedSite = new ArrayList<StackSite>();
                        rm.getStackSites(returnedSite, GroupSeletced, new GetSiteCallback() {
                            @Override
                            public void done(List<StackSite> returnedSite, boolean Result) {
                                itemsSS = returnedSite;
                                List<StackSite> itemsSSF = getFilterStackSite(itemsSS, searchText.getText().toString());
                                adapterFiltered = new MyListAdapterExt(getContext(), itemsSSF, sitesMusic, GroupVSeletced);
                                sitesMusic.setAdapter(adapterFiltered);
                                sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);
                            }

                        });


                    }
                });

                return view;

            }


            List<StackSite> getFilterStackSite(List<StackSite> StackSiteLocalList, String NameString) {


                List<StackSite> stackSitesResult;
                stackSitesResult = new ArrayList<StackSite>();

                for (Iterator<StackSite> iterator = StackSiteLocalList.iterator(); iterator.hasNext(); ) {
                    StackSite stackSite = iterator.next();

                    String nameFromList = stackSite.getName();
                    if (nameFromList.toLowerCase().contains(NameString.toLowerCase())) {
                        stackSitesResult.add(stackSite);
                    }
                }

                return stackSitesResult;
            }


        }



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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gnardelli on 11/29/15.
 * This is the java file associated to the form used to import new channel in the internal local database
 */


public class AddNewChannel extends Activity {

    private CheckBox checkBoxPosition;
    private CheckBox checkBoxType;
    private MyDBHandler dbHandler;
    private Spinner listChannelAlreadyAdded;
    private Spinner listGroup;
    List<String> groupsS;
    private static Activity fa;
    private Bundle extras;
    List<StackGroup> groupsV;
    private TextView labelAddPosition;
    private TextView labelAddLink;
    private TextView labelGroup;
    private Button btnCancelNew;
    private Button btnAddNew;
    private int posGroup;
    private String NameGroupSelected;
    private StackGroup NameGroupVSelected;
    private String TitleChannel;
    private String TitleStaticChannel;
    private String DescrChannel;
    private String IconChannel;
    private String LinkChannel;
    private int Origin;
    private int isFilm;
    private StackSite sSite;
    private TextView editAddName;
    private TextView editAddLink;
    private TextView editAddDescription;
    private TextView editAddIcon;
    private boolean isNewChannel;


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_add_new_channel);
        fa=this;

        extras = getIntent().getExtras();
        TitleChannel = extras.getString("TitleChannel");
        TitleStaticChannel = extras.getString("TitleStaticChannel");
        listChannelAlreadyAdded = (Spinner) findViewById(R.id.editAddPosition);
        labelAddPosition = (TextView) findViewById(R.id.labelAddPosition);
        labelGroup = (TextView) findViewById(R.id.labelAddGroup);

        checkBoxPosition = (CheckBox) findViewById(R.id.checkBoxPosition);
        checkBoxType = (CheckBox) findViewById(R.id.checkBoxType);

        checkBoxPosition.setChecked(Boolean.TRUE);
        checkBoxType.setChecked(Boolean.FALSE);

        listChannelAlreadyAdded.setVisibility(View.INVISIBLE);
        labelAddPosition.setVisibility(View.INVISIBLE);

        editAddName = (TextView) findViewById(R.id.editAddName);
        editAddLink = (TextView) findViewById(R.id.editAddLink);
        labelAddLink = (TextView) findViewById(R.id.labelAddLink);
        editAddDescription = (TextView) findViewById(R.id.editAddDescription);
        editAddIcon = (TextView) findViewById(R.id.editAddIcon);
        btnAddNew = (Button) findViewById(R.id.btnAddNew);

        listGroup = (Spinner) findViewById(R.id.editAddGroup);

        if (TitleChannel.equals("")) {
            // new channel

            isNewChannel = Boolean.TRUE;
            checkBoxType.setVisibility(View.INVISIBLE);
            // SitesLocalStore sls = new SitesLocalStore(MainActivity.getAppContext());
            RemoteCommunications rm = new RemoteCommunications();
            groupsS = new ArrayList<String>();
            groupsV = new ArrayList<StackGroup>();


            List<StackGroup> ListNameGroup = new ArrayList<StackGroup>();

            rm.getGroups(ListNameGroup, new GetGroupCallback() {
                @Override
                public void done(List<StackGroup> returnedGroups) {

                    //SitesLocalStore sls = new SitesLocalStore(MainActivity.getAppContext());

                    groupsV = new ArrayList<StackGroup>();

                    for (int i = 0; i < returnedGroups.size(); i++) {
                        int Level = returnedGroups.get(i).getGroupLevel();
                        if (Level <= MainActivity.getGroupLevel()) {
                            groupsV.add(returnedGroups.get(i));
                            groupsS.add(returnedGroups.get(i).getGroupName());
                        }
                    }


                    //sls.storeGroupListArray(groupsV);
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.getAppContext(), android.R.layout.simple_spinner_item, groupsS);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    listGroup.setAdapter(adapter);

                    int spinnerPosition = adapter.getPosition("Others Channels");
                    listGroup.setSelection(spinnerPosition);

                }
            });


            checkBoxPosition.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    boolean isAppend = checkBoxPosition.isChecked();
                    if (isAppend) {
                        listChannelAlreadyAdded.setVisibility(View.INVISIBLE);
                        labelAddPosition.setVisibility(View.INVISIBLE);
                    } else {
                        listChannelAlreadyAdded.setVisibility(View.VISIBLE);
                        labelAddPosition.setVisibility(View.VISIBLE);
                    }
                }
            });



        } else {

            isNewChannel = Boolean.FALSE;
            btnAddNew.setText("EDIT");
            listGroup.setVisibility(View.INVISIBLE);
            checkBoxPosition.setVisibility(View.INVISIBLE);
            listChannelAlreadyAdded.setVisibility(View.INVISIBLE);
            labelAddPosition.setVisibility(View.INVISIBLE);
            labelGroup.setVisibility(View.INVISIBLE);

            setTitle("Edit the local channel: " + TitleChannel);
            DescrChannel = extras.getString("DescrChannel");
            LinkChannel =  extras.getString("LinkChannel");
            IconChannel =  extras.getString("IconChannel");
            Origin = extras.getInt("Origin");
            isFilm = extras.getInt("isFilm");

            if (isFilm==1) {
                checkBoxType.setChecked(Boolean.TRUE);
            } else {
                checkBoxType.setChecked(Boolean.FALSE);
            }





            editAddName.setText(TitleChannel);
            editAddDescription.setText(DescrChannel);
            editAddIcon.setText(IconChannel);

            if (Origin==0) {
                labelAddLink.setVisibility(View.INVISIBLE);
                editAddLink.setVisibility(View.INVISIBLE);
                checkBoxType.setVisibility(View.INVISIBLE);
            } else {
                editAddLink.setVisibility(View.VISIBLE);
                labelAddLink.setVisibility(View.VISIBLE);
                //checkBoxType.setVisibility(View.VISIBLE);
                checkBoxType.setVisibility(View.INVISIBLE);
            }
            editAddLink.setText(LinkChannel);


        }





        btnCancelNew = (Button) findViewById(R.id.btnCancelNew);
        btnCancelNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });




        btnAddNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                String editAddNameStr = editAddName.getText().toString();
                String editAddLinkStr = editAddLink.getText().toString();
                String editAddIconStr = editAddIcon.getText().toString();
                String editAddDescriptionStr = editAddDescription.getText().toString();





                if (editAddIconStr.equals("")) {
                    editAddIconStr = MainActivity.imageNullDefault;
                }

                if (editAddNameStr.equals("")) {
                    Toast.makeText(MainActivity.getAppContext(), "The Name cannot be empty",
                            Toast.LENGTH_SHORT).show();
                } else if (editAddLinkStr.equals("")) {
                    Toast.makeText(MainActivity.getAppContext(), "The Link cannot be empty",
                            Toast.LENGTH_SHORT).show();
                } else if (!editAddLinkStr.startsWith("http://") && !editAddLinkStr.startsWith("https://")) {
                    Toast.makeText(MainActivity.getAppContext(), "The Link is not starting with http:// or https://",
                            Toast.LENGTH_SHORT).show();
                } else {



                    // add in the database
                    sSite = new StackSite();
                    sSite.setName(editAddNameStr);
                    sSite.setStaticName(editAddNameStr);
                    sSite.setLink(editAddLinkStr);
                    sSite.setAbout(editAddDescriptionStr);
                    sSite.setImgUrl(editAddIconStr);
                    sSite.setOrigin(Origin);
                    boolean isFilm = checkBoxType.isChecked();
                    if (isFilm) {
                        sSite.setTypeStream(1);
                    } else {
                        sSite.setTypeStream(0);
                    }

                    String Sentence;
                    if (isNewChannel) {
                        Sentence="Are you sure to Add this channel in the local playlist?";
                    } else {
                        Sentence="Are you sure to update this channel in the local playlist?";
                    }


                    AlertDialog.Builder myAlert = new AlertDialog.Builder(fa);
                    myAlert.setMessage(Sentence)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                                if (isNewChannel) {

                                                    posGroup = listGroup.getSelectedItemPosition();
                                                    NameGroupSelected= groupsS.get(posGroup);
                                                    NameGroupVSelected= groupsV.get(posGroup);
                                                    sSite.setOrigin(1);
                                                    boolean isAppend = checkBoxPosition.isChecked();
                                                    if (isAppend) {
                                                        dbHandler.addSite(sSite);
                                                    } else {

                                                        int posSpinner = listChannelAlreadyAdded.getSelectedItemPosition();
                                                        dbHandler.addSiteBefore(sSite, posSpinner);

                                                    }

                                                } else {

                                                    TitleStaticChannel = extras.getString("TitleStaticChannel");
                                                    dbHandler.updateSite(sSite,TitleStaticChannel);

                                                }

                                                List<StackSite> itemsLocal;
                                                itemsLocal = dbHandler.getStackSites();
                                                if (itemsLocal.size()==0) {
                                                    tabLocal.editEmptyLocalList.setVisibility(View.VISIBLE);
                                                } else {
                                                    tabLocal.editEmptyLocalList.setVisibility(View.INVISIBLE);
                                                }
                                                tabLocal.sitesLocal.setCheeseList(itemsLocal);
                                                tabLocal.sitesLocal.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                                StableArrayAdapter adapterLocal = new StableArrayAdapter(MainActivity.getAppContext(), R.layout.row_site_local, itemsLocal);

                                                int index = tabLocal.sitesLocal.getFirstVisiblePosition();
                                                tabLocal.sitesLocal.getChildAt(0);
                                                tabLocal.sitesLocal.setAdapter(adapterLocal);
                                                tabLocal.sitesLocal.setSelection(index);
                                                finish();

                                                String AddMod = "Add";

                                                RemoteCommunications rm = new RemoteCommunications();
                                                rm.storeDataInBackground(sSite, NameGroupSelected, AddMod, new GetSiteCallback() {
                                                    @Override
                                                    public void done(List<StackSite> returnedSite, boolean Result) {

                                                        MyListAdapterExt adapterFiltered = new MyListAdapterExt(MainActivity.getAppContext(), returnedSite, tabFromDB.sitesMusic, NameGroupVSelected);
                                                        tabFromDB.sitesMusic.setAdapter(adapterFiltered);
                                                        tabFromDB.isExecutedOutSite = Boolean.TRUE;
                                                        finish();
                                                    }

                                                });


                                            dialog.dismiss();
                                        }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setTitle("Update the local channel")
                            .create();
                    myAlert.show();
                }


            }
        });

        Context ct = MainActivity.getAppContext();

        dbHandler = new MyDBHandler(tabLocal.Fa.getContext(), null, null,1);
        List<String> ListNameChannel;
        ListNameChannel = dbHandler.getNamesFromStackSites();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ct, android.R.layout.simple_spinner_item, ListNameChannel);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        listChannelAlreadyAdded.setAdapter(adapter);



        if (ListNameChannel.size()<=0) {
            //  the checkbox spinning and the label are not visible
            // the checkbox become true so only the add have to used in the database
            listChannelAlreadyAdded.setVisibility(View.INVISIBLE);
            labelAddPosition.setVisibility(View.INVISIBLE);
            checkBoxPosition.setVisibility(View.INVISIBLE);
        }


    }




}

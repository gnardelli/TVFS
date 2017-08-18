

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
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;



public class ModChannel extends Activity {


    private MyDBHandler dbHandler;
    List<String> groupsS;
    private TextView labelAddPosition;
    private static Activity fa;
    private Button btnCan;
    private Button btnMod;
    private Button btnApp;
    private Button btnDel;
    private Button btnGrup;
    private Button btnLink;
    private StackSite sSite;
    private Bundle extras;
    private String TitleChannel;
    private String DescrChannel;
    private String IconChannel;
    private String NameGroup;
    private boolean ResultStore;
    private StackGroup NameGroupV;
    private int isAccepted;
    private int posGroup;
    private TextView editAddName;
    private TextView editAddDescription;
    private TextView editAddIcon;
    private StackSite stNew;


    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.activity_mod_channel);
        fa =  this;

        extras = getIntent().getExtras();

        TitleChannel = extras.getString("TitleChannel");
        setTitle("Edit the channel: " + TitleChannel);
        DescrChannel = extras.getString("DescrChannel");
        NameGroup =  extras.getString("NameGroup");
        isAccepted = extras.getInt("isAccepted");


        IconChannel = extras.getString("IconChannel");

        int LevelGroup =  extras.getInt("LevelGroup");
        int TypeGroup =   extras.getInt("TypeGroup");
        NameGroupV = new StackGroup();
        NameGroupV.setGroupLevel(LevelGroup);
        NameGroupV.setGroupName(NameGroup);
        NameGroupV.setGroupType(TypeGroup);


        if (TitleChannel.equals("")) {
            // create a new channel where the group have to be associated. Initially the link is empty
            // title have to be "Add title" and the textfield have to be selected in a way that can be easily changed
            // before delete New Title

            TitleChannel = "Add Name";
            setTitle("Add a new channel");
            stNew = new StackSite();
            stNew.setStaticName(TitleChannel);
            stNew.setName(TitleChannel);
            stNew.setAbout("");
            stNew.setImgUrl(IconChannel);


            RemoteCommunications rm = new RemoteCommunications();
            rm.deleteSiteInBackground(stNew, NameGroup, new GetSiteCallback() {
                @Override
                public void done(List<StackSite> returnedLink, boolean Result) {

                    RemoteCommunications rm = new RemoteCommunications();
                    rm.storeDataInBackground(stNew, NameGroup, "Add", new GetSiteCallback() {
                        @Override
                        public void done(List<StackSite> returnedSite, boolean Result) {

                            MyListAdapterExt adapterFiltered = new MyListAdapterExt(MainActivity.getAppContext(), returnedSite, tabFromDB.sitesMusic, NameGroupV);
                            tabFromDB.sitesMusic.setAdapter(adapterFiltered);
                            tabFromDB.isExecutedOutSite = Boolean.TRUE;

                            editAddName.setText(TitleChannel);
                            editAddDescription.setText(DescrChannel);
                            editAddIcon.setText(IconChannel);

                        }

                    });


                }
            });



        }



        editAddName = (TextView) findViewById(R.id.editAddName);
        editAddDescription = (TextView) findViewById(R.id.editAddDescription);
        editAddIcon = (TextView) findViewById(R.id.editAddIcon);

        editAddName.setText(TitleChannel);
        editAddDescription.setText(DescrChannel);
        editAddIcon.setText(IconChannel);

        btnCan = (Button) findViewById(R.id.btnCan);
        btnDel = (Button) findViewById(R.id.btnDel);
        btnMod = (Button) findViewById(R.id.btnMod);
        btnApp = (Button) findViewById(R.id.btnApp);
        btnGrup = (Button) findViewById(R.id.btnGroup);
        btnLink = (Button) findViewById(R.id.btnLink);


        btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                sSite = new StackSite();
                //lLink.setLinkTxt();
                sSite.setName(TitleChannel);
                sSite.setAccepted(isAccepted);



                AlertDialog.Builder myAlert = new AlertDialog.Builder(fa);
                myAlert.setMessage("Do you really want to Delete the channel and all links associated?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                RemoteCommunications rm = new RemoteCommunications();
                                rm.deleteSiteInBackground(sSite, NameGroup, new GetSiteCallback() {
                                    @Override
                                    public void done(List<StackSite> returnedLink, boolean Result) {

                                        MyListAdapterExt adapterFiltered = new MyListAdapterExt(MainActivity.getAppContext(), returnedLink, tabFromDB.sitesMusic, NameGroupV);
                                        tabFromDB.sitesMusic.setAdapter(adapterFiltered);
                                        tabFromDB.sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);
                                        tabFromDB.isExecutedOutSite = Boolean.TRUE;


                                    }
                                });

                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Delete Channel")
                        .create();
                myAlert.show();


            }
        });

        btnCan.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                if (TitleChannel.equals("Add Name")) {


                    StackSite sSitel = new StackSite();
                    sSitel.setName(TitleChannel);

                    RemoteCommunications rm = new RemoteCommunications();
                    rm.deleteSiteInBackground(sSitel, NameGroup, new GetSiteCallback() {
                        @Override
                        public void done(List<StackSite> returnedLink, boolean Result) {

                            MyListAdapterExt adapterFiltered = new MyListAdapterExt(MainActivity.getAppContext(), returnedLink, tabFromDB.sitesMusic, NameGroupV);
                            tabFromDB.sitesMusic.setAdapter(adapterFiltered);
                            tabFromDB.sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);
                            tabFromDB.isExecutedOutSite = Boolean.TRUE;


                        }
                    });

                }

                finish();
            }
        });

        btnLink.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.getAppContext(), LinksLists.class);
                intent.putExtra("VideoStreamName",TitleChannel);
                startActivity(intent);

            }
        });

        btnGrup.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.getAppContext(), GroupsLists.class);
                intent.putExtra("VideoStreamName",TitleChannel);
                startActivity(intent);
            }
        });

        btnMod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {




                AlertDialog.Builder myAlert = new AlertDialog.Builder(fa);
                myAlert.setMessage("Do you really want to Modify the channel?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                String Name = editAddName.getText().toString();
                                String OldName = TitleChannel;
                                String Description = editAddDescription.getText().toString();
                                String IconPath = editAddIcon.getText().toString();
                                String AddMod = "Mod";
                                StackSite sSite = new StackSite(Name,Description,IconPath);
                                sSite.setStaticName(OldName);

                                RemoteCommunications rm = new RemoteCommunications();
                                rm.storeDataInBackground(sSite, NameGroup, AddMod, new GetSiteCallback() {
                                    @Override
                                    public void done(List<StackSite> returnedSite, boolean Result) {

                                        MyListAdapterExt adapterFiltered = new MyListAdapterExt(MainActivity.getAppContext(), returnedSite, tabFromDB.sitesMusic, NameGroupV);
                                        tabFromDB.sitesMusic.setAdapter(adapterFiltered);
                                        tabFromDB.sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);
                                        tabFromDB.isExecutedOutSite = Boolean.TRUE;
                                    }

                                });

                                dialog.dismiss();
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Modify Channel")
                        .create();
                myAlert.show();



            }
        });


        btnApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final View vf;
                vf = v;
                sSite = new StackSite();
                //lLink.setLinkTxt();
                sSite.setName(TitleChannel);
                sSite.setAccepted(isAccepted);



                AlertDialog.Builder myAlert = new AlertDialog.Builder(fa);
                String Sentence;
                if (isAccepted == 0) {
                    Sentence="Do you really want to approve the channel?";
                } else {
                    Sentence="Do you really want to disapprove the channel?";
                }
                myAlert.setMessage(Sentence)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                RemoteCommunications rm = new RemoteCommunications();
                                rm.approveChannelInBackground(sSite, NameGroup, ResultStore, new GetSiteCallback() {
                                    @Override
                                    public void done(List<StackSite> returnedLink, boolean Result) {

                                        if (Result) {
                                            MyListAdapterExt adapterFiltered = new MyListAdapterExt(MainActivity.getAppContext(), returnedLink, tabFromDB.sitesMusic, NameGroupV);
                                            tabFromDB.sitesMusic.setAdapter(adapterFiltered);
                                            tabFromDB.sitesMusic.setSelection(MainActivity.posRemoteListBeforeExecuted);
                                            tabFromDB.isExecutedOutSite = Boolean.TRUE;


                                            finish();
                                        } else {

                                            Toast.makeText(MainActivity.getAppContext(), "You must have some approved link before to approve the channel", Toast.LENGTH_SHORT).show();
                                        }
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
                        .setTitle("Approve Channel")
                        .create();
                myAlert.show();


            }
        });



        dbHandler = new MyDBHandler(tabLocal.Fa.getContext(), null, null,1);




    }




}

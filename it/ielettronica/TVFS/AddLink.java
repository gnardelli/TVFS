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
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

public class AddLink extends Activity {


    private Button btnCanc;
    private Button btnAdd;
    private Bundle extras;
    private String AddMod;
    private String linkValue;
    private String linkName;
    private String NameChannel;
    private EditText editAddModData;
    private EditText editAddModName;
    private int linkID;
    private StackLink lLink;
    private static Activity fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_link);
        fa = this;
        extras = getIntent().getExtras();
        btnCanc = (Button) this.findViewById(R.id.btnCancelNew);
        btnAdd = (Button)  this.findViewById(R.id.btnAddNew);
        editAddModData = (EditText) this.findViewById(R.id.editAddModData);
        editAddModName = (EditText) this.findViewById(R.id.editAddModName);

        AddMod= extras.getString("AddMod");
        linkID = extras.getInt("linkID");

        NameChannel= extras.getString("NameChannel");   // to be defined in the parents

        if (AddMod.contentEquals("Add")) {
            btnAdd.setText("Add");
        } else if (AddMod.contentEquals("Mod")) {
            linkValue= extras.getString("linkValue");
            linkName=extras.getString("linkName");
            editAddModData.setText(linkValue);
            editAddModName.setText(linkName);
            btnAdd.setText("Edit");

        }


        btnCanc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                lLink = new StackLink();
                lLink.setLinkTxt(editAddModName.getText().toString());
                lLink.setLinkValue(editAddModData.getText().toString());
                lLink.setLinkID(linkID);


                if (AddMod.contentEquals("Add")) {



                    //lLink.setAccepted();





                            AlertDialog.Builder myAlert = new AlertDialog.Builder(fa);
                            myAlert.setMessage("Do you really want to Add the link?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {


                                            RemoteCommunications rm = new RemoteCommunications();
                                            rm.storeLinkInBackground(lLink, NameChannel, AddMod, new GetLinkCallback() {
                                                @Override
                                                public void done(List<StackLink> returnedLink) {

                                                    MyListLinkAdapterExt adapterFiltered = new MyListLinkAdapterExt(MainActivity.getAppContext(), returnedLink, NameChannel,0);
                                                    LinksLists.sitesLinks.setAdapter(adapterFiltered);
                                                    finish();
//                                                    LinksLists.fa.finish();
//                                                    Intent intent = new Intent(MainActivity.getAppContext(), LinksLists.class);
//                                                    intent.putExtra("VideoStreamName", NameChannel);
//                                                    startActivity(intent);
//                                                    finish();

                                                }});
                                                dialog.dismiss();

                                            }
                                        })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .setTitle("Add the Link")
                                    .create();
                            myAlert.show();






                } else if (AddMod.contentEquals("Mod")) {






                            AlertDialog.Builder myAlert = new AlertDialog.Builder(fa);
                            myAlert.setMessage("Do you really want to Modify the link?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {

                                            RemoteCommunications rm = new RemoteCommunications();
                                            rm.storeLinkInBackground(lLink, NameChannel, AddMod, new GetLinkCallback() {
                                                @Override
                                                public void done(List<StackLink> returnedLink) {

                                                    MyListLinkAdapterExt adapterFiltered = new MyListLinkAdapterExt(MainActivity.getAppContext(), returnedLink, NameChannel,0);
                                                    LinksLists.sitesLinks.setAdapter(adapterFiltered);
                                                    finish();


                                                    //LinksLists.sitesLinks.setSelection(MainActivity.posRemoteListBeforeExecuted);
                                                    //LinksLists.isExecutedOutSite = Boolean.TRUE;


//                                                    LinksLists.fa.finish();
//                                                    Intent intent = new Intent(MainActivity.getAppContext(), LinksLists.class);
//                                                    intent.putExtra("VideoStreamName", NameChannel);
//                                                    startActivity(intent);
//                                                    finish();
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
                                    .setTitle("Modify Link")
                                    .create();
                            myAlert.show();


                                        }


                }


        });
    }
}

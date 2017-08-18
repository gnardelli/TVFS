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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class AddGroup extends Activity {


    private Button btnCanc;
    private Button btnAdd;
    private Bundle extras;
    private String AddMod;
    private String linkValue;
    private String linkName;
    private String NameChannel;
    private int linkID;
    private  List<StackGroup> groupFromChannel;
    private StackGroup gGroup;
    private Spinner sGroup;
    List<StackGroup> groupsS;
    private static Activity fa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        fa = this;
        extras = getIntent().getExtras();
        btnCanc = (Button) this.findViewById(R.id.btnCancelNew);
        btnAdd = (Button)  this.findViewById(R.id.btnAddNew);
        sGroup = (Spinner) this.findViewById(R.id.spGroup);

        linkID = extras.getInt("linkID");

        NameChannel= extras.getString("NameChannel");   // to be defined in the parents


        RemoteCommunications rm = new RemoteCommunications();
        List<StackGroup> returnedGroup = new ArrayList<StackGroup>();
        rm.getGroupsFromChannel(returnedGroup, NameChannel, new GetGroupCallback() {

            @Override
            public void done(List<StackGroup> returnedGroups) {

                StackGroup groupss;
                groupFromChannel = new ArrayList<StackGroup>();

                for (int i = 0; i < returnedGroups.size(); i++) {
                    StackGroup gv = returnedGroups.get(i);
                    groupss = new StackGroup();
                    groupss.setGroupName(gv.getGroupName());
                    groupss.setGroupLevel(gv.getGroupLevel());
                    groupFromChannel.add(groupss);
                }




                groupsS = new ArrayList<StackGroup>();
                SitesLocalStore sls = new SitesLocalStore(MainActivity.getAppContext());
                List<StackGroup> groups = sls.getGroupListArray();






                    List<StackGroup> ListNameGroup = new ArrayList<StackGroup>();

                    RemoteCommunications rm = new RemoteCommunications();
                    rm.getGroups(ListNameGroup, new GetGroupCallback() {
                        @Override
                        public void done(List<StackGroup> returnedGroups) {


                            // from returnedGroups we have to remove the value localed in the groupFromChannel
                            List<StackGroup> returnedGroupsFiltered = new ArrayList<StackGroup>();
                            List<String> returnedGroupsFilteredS = new ArrayList<String>();
                            for (int jj = 0; jj < returnedGroups.size(); jj++) {
                                String retGroupName = returnedGroups.get(jj).getGroupName();
                                int retGroupLevel = returnedGroups.get(jj).getGroupLevel();
                                int retGroupType = returnedGroups.get(jj).getGroupType();
                                StackGroup sg = new StackGroup();
                                sg.setGroupName(retGroupName);
                                sg.setGroupType(retGroupType);
                                sg.setGroupLevel(retGroupLevel);
                                boolean isInside = Boolean.FALSE;
                                for (int ii = 0; ii < groupFromChannel.size(); ii++) {
                                    String retGroupAlready = groupFromChannel.get(ii).getGroupName();
                                    if (retGroupName.equals(retGroupAlready)) {
                                        isInside = Boolean.TRUE;
                                    }
                                }

                                if (!isInside) {
                                    returnedGroupsFiltered.add(sg);
                                    returnedGroupsFilteredS.add(retGroupName);
                                }
                            }

                            groupsS = returnedGroupsFiltered;

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.getAppContext(), android.R.layout.simple_spinner_item, returnedGroupsFilteredS);
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            sGroup.setAdapter(adapter);
                        }
                    });







            }
        });






        btnCanc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                gGroup = new StackGroup();
                int intGroupSelected = sGroup.getSelectedItemPosition();
                StackGroup GroupSeletced = groupsS.get(intGroupSelected);

                gGroup.setGroupName(GroupSeletced.getGroupName());


                AlertDialog.Builder myAlert = new AlertDialog.Builder(fa);
                myAlert.setMessage("Do you really want to Add the Group?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                RemoteCommunications rm = new RemoteCommunications();
                                rm.storeGroupInBackground(gGroup, NameChannel, new GetGroupCallback() {
                                    @Override
                                    public void done(List<StackGroup> returnedGroup) {

                                        MyListGroupAdapterExt adapterFiltered = new MyListGroupAdapterExt(MainActivity.getAppContext(), returnedGroup, NameChannel);
                                        GroupsLists.sitesGroups.setAdapter(adapterFiltered);
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
                        .setTitle("Add the Link")
                        .create();
                myAlert.show();


            }


        });
    }
}

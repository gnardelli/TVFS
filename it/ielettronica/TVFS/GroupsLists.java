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
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class GroupsLists extends Activity {

    protected Context mContext;
    //public static boolean isExecutedOutSite;
    private  List<StackGroup> Groupss;
    public static ListView sitesGroups;
    private MyListGroupAdapterExt adapterBtn2;
    private Bundle extras;
    protected static View view;
    private Button btnCanc;
    private Button btnNew;
    private StackLink lLink;
    public static Activity fa;
    private static final String VIDEOSTREAM = "VideoStreamName";


    private ImageView imageState;
    private EditText searchText;
    private AlertDialog OptionDialog;
    private int posSpinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //isExecutedOutSite = Boolean.FALSE;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabgroups);
        fa= this;
        extras = getIntent().getExtras();
        sitesGroups = (ListView) this.findViewById(R.id.sitesGroups);

        btnCanc = (Button) this.findViewById(R.id.btnCanc);
        btnNew = (Button)  this.findViewById(R.id.btnNew);


        btnCanc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        btnNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddGroup.class);
                intent.putExtra("NameChannel", extras.getString(VIDEOSTREAM));
                startActivity(intent);
            }
        });



        RemoteCommunications rm = new RemoteCommunications();
        List<StackGroup> returnedGroup = new ArrayList<StackGroup>();
        rm.getGroupsFromChannel(returnedGroup, extras.getString(VIDEOSTREAM), new GetGroupCallback() {

            @Override
            public void done(List<StackGroup> returnedGroups) {

                StackGroup groupss;
                Groupss = new ArrayList<StackGroup>();

                for (int i = 0; i < returnedGroups.size(); i++) {
                    StackGroup gv = returnedGroups.get(i);
                    groupss = new StackGroup();
                    groupss.setGroupName(gv.getGroupName());
                    groupss.setGroupLevel(gv.getGroupLevel());
                    Groupss.add(groupss);
                }


                String NameChannel = extras.getString(VIDEOSTREAM);
                adapterBtn2 = new MyListGroupAdapterExt(MainActivity.getAppContext(), Groupss, NameChannel);
                sitesGroups.setAdapter(adapterBtn2);


            }
        });


    }
}

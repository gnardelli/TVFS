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

public class LinksLists extends Activity {

    protected Context mContext;
    //public static boolean isExecutedOutSite;
    private  List<StackLink> Linkss;
    public static ListView sitesLinks;
    private MyListLinkAdapterExt adapterBtn2;
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
        setContentView(R.layout.tablinks);
        fa= this;
        extras = getIntent().getExtras();
        sitesLinks = (ListView) this.findViewById(R.id.sitesLinks);

        setTitle(extras.getString(VIDEOSTREAM));

        btnCanc = (Button) this.findViewById(R.id.btnCanc);
        btnNew = (Button)  this.findViewById(R.id.btnNew);

        if (MainActivity.isAmministrator()) {
            btnNew.setVisibility(View.VISIBLE);
        } else {
            btnNew.setVisibility(View.INVISIBLE);
        }


        btnCanc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        btnNew.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AddLink.class);
                intent.putExtra("AddMod", "Add");
                intent.putExtra("NameChannel", extras.getString(VIDEOSTREAM));
                startActivity(intent);
            }
        });



        RemoteCommunications rm = new RemoteCommunications();
        List<StackLink> returnedLink = new ArrayList<StackLink>();
        rm.getLinks(returnedLink, extras.getString(VIDEOSTREAM), new GetLinkCallback() {

            @Override
            public void done(List<StackLink> returnedLinks) {

                StackLink listss;
                Linkss = new ArrayList<StackLink>();

                for (int i = 0; i < returnedLinks.size(); i++) {
                    StackLink lv = returnedLinks.get(i);
                    int ii = i + 1;
                    listss = new StackLink();
                    listss.setLinkValue(lv.getLinkValue());
                    listss.setLinkTxt(lv.getLinkTxt());
                    listss.setAccepted(lv.getAccepted());
                    listss.setLinkID(lv.getLinkID());
                    Linkss.add(listss);

                }


                String NameChannel=extras.getString(VIDEOSTREAM);
                int GroupType = extras.getInt("GroupType");
                adapterBtn2 = new MyListLinkAdapterExt(MainActivity.getAppContext(), Linkss, NameChannel, GroupType);
                sitesLinks.setAdapter(adapterBtn2);


            }
        });


    }
}

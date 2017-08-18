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

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;




public  class MyListGroupAdapterExt extends BaseAdapter {
    private List<StackGroup> mGroups;
    private LayoutInflater mInflater;
    private StackGroup stGroup;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context cloc;
    private LinksView plv;
    private String mVideoStream;
    private View mConvertView;
    private String NameChannel;
    private StackGroup lGroup;

    public MyListGroupAdapterExt(Context c, List<StackGroup> mGroups, String NameChannel) {
        this.mGroups = mGroups;
        this.cloc = c;
        this.NameChannel = NameChannel;
        //Cache a reference to avoid looking it up on every getView() call
        mInflater = LayoutInflater.from(MainActivity.getAppContext());
    }


    @Override
    public int getCount() {
        return mGroups.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mGroups.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //If there's no recycled view, inflate one and tag each of the views
        //you'll want to modify later
        //Log.d("Inside", "GetView");

        plv = null;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.row_site_group, parent, false);
            plv = new LinksView();
            plv.btnDel = (Button) convertView.findViewById(R.id.btnDel);
            plv.imageState = (ImageView) convertView.findViewById(R.id.imgState);
            plv.groupValue = (TextView) convertView.findViewById(R.id.groupTxt);
            this.mConvertView = convertView;

            //This assumes layout/row_left.xml includes a TextView with an id of "textview"
            convertView.setTag(plv);
        } else {
            plv = (LinksView) convertView.getTag();
        }








        plv.btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                lGroup = new StackGroup();
                lGroup=mGroups.get(position);
                //lLink.setLinkTxt();
                //lLink.setLinkID(mGroups.get(position).getLinkID());
                //lLink.setLinkValue(Linkss.get(position).getLinkValue());


                AlertDialog.Builder myAlert;
                try {
                    myAlert = new AlertDialog.Builder(GroupsLists.fa);
                } catch (Exception ex) {
                    myAlert = new AlertDialog.Builder(MainActivity.getAppContext());
                }

                myAlert.setMessage("Do you really want to Cancel the link?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                RemoteCommunications rm = new RemoteCommunications();
                                rm.deleteGroupInBackground(lGroup, NameChannel, new GetGroupCallback() {
                                    @Override
                                    public void done(List<StackGroup> returnedGroup) {


                                        MyListGroupAdapterExt adapterFiltered = new MyListGroupAdapterExt(MainActivity.getAppContext(), returnedGroup, NameChannel);
                                        GroupsLists.sitesGroups.setAdapter(adapterFiltered);

                                        List<StackSite> returnedSite = new ArrayList<StackSite>();
                                        RemoteCommunications rm = new RemoteCommunications();
                                        rm.getStackSites(returnedSite, lGroup.getGroupName(), new GetSiteCallback() {
                                            @Override
                                            public void done(List<StackSite> returnedSite, boolean Result) {

                                                MyListAdapterExt adapterFiltered2 = new MyListAdapterExt(MainActivity.getAppContext(), returnedSite, tabFromDB.sitesMusic, lGroup);
                                                tabFromDB.sitesMusic.setAdapter(adapterFiltered2);
                                                tabFromDB.isExecutedOutSite = Boolean.TRUE;

                                            }
                                        });



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
                        .setTitle("Cancel Link")
                        .create();
                myAlert.show();




            }
        });





        stGroup = mGroups.get(position);
        plv.groupValue.setText(stGroup.getGroupName());


        //Retrieve the tagged view, get the item for that position, and
        //update the text


        //stk = mLinks.get(position);
        //plv.linkTxt.setText(stk.getName());


//            plv.btnPlayLocal.setOnClickListener(new View.OnClickListener() {
//                public void onClick(View v) {
//
//                    final Intent intent = new Intent(v.getContext(), MediaPlayerDemo_Video.class);
//                    final StackSite stkloc = mItems.get(position);
//                    MainActivity.posRemoteListBeforeExecuted = sitesLinks.getFirstVisiblePosition();
//                    //Uri myUri = Uri.parse(stkloc.getLink());
//
//
//                    RemoteCommunications rm = new RemoteCommunications();
//                    List<String> Links = new ArrayList<String>();
//                    rm.getLinks(Links, stkloc.getName(), new GetDataCallback() {
//                        @Override
//                        public void done(List<String> returnedLinks) {
//                            List<String> Links = returnedLinks;
//                            if (Links.size() == 1) {
//                                String linkVal = Links.get(0);
//                                //Uri myUri = Uri.parse(linkVal);
//
//                                intent.putExtra("media", 5);
//                                intent.putExtra("pathValue", linkVal);
//
//                                try {
//                                    startActivity(intent);
//                                } catch (Exception ex) {
//                                    Toast.makeText(cloc, ex.toString(),
//                                            Toast.LENGTH_SHORT).show();
//                                }
//
//                                Toast.makeText(cloc, "Play: " + stkloc.getName(),
//                                        Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        }
//                    });
//
//
//                }
//            });


        return convertView;
    }



    private class LinksView {
        Button btnDel;
        TextView groupValue;
        ImageView imageState;
        //Initially we want the progress indicator visible, and the image invisible

    }
}


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
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;



public  class MyListLinkAdapterExt extends BaseAdapter {
    private List<StackLink> mLinks;
    private LayoutInflater mInflater;
    private StackLink stLink;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;
    private Context cloc;
    private LinksView plv;
    private String mVideoStream;
    private View mConvertView;
    private String NameChannel;
    private StackLink lLink;
    private int GroupType;

    public MyListLinkAdapterExt(Context c, List<StackLink> links, String NameChannel, int GroupType) {
        mLinks = links;
        this.cloc = c;
        this.NameChannel = NameChannel;
        this.GroupType=GroupType;
        //Cache a reference to avoid looking it up on every getView() call
        mInflater = LayoutInflater.from(MainActivity.getAppContext());
    }


    @Override
    public int getCount() {
        return mLinks.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mLinks.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //If there's no recycled view, inflate one and tag each of the views
        //you'll want to modify later
        //Log.d("Inside", "GetView");

        plv = null;
        if (convertView == null) {
            if (MainActivity.isAmministrator()) {
                convertView = mInflater.inflate(R.layout.row_site_links_admin, parent, false);
            } else {
                convertView = mInflater.inflate(R.layout.row_site_links, parent, false);
            }
            plv = new LinksView();
            plv.btnPlay = (Button) convertView.findViewById(R.id.btnPlay);
            plv.btnMod = (Button) convertView.findViewById(R.id.btnMod);
            plv.btnApp = (Button) convertView.findViewById(R.id.btnApp);
            plv.btnDel = (Button) convertView.findViewById(R.id.btnDel);
            plv.imageState = (ImageView) convertView.findViewById(R.id.imgState);
            plv.linkValue = (TextView) convertView.findViewById(R.id.linkTxt);
            this.mConvertView = convertView;

            //This assumes layout/row_left.xml includes a TextView with an id of "textview"
            convertView.setTag(plv);
        } else {
            plv = (LinksView) convertView.getTag();
        }


        if (MainActivity.isAmministrator()) {
            plv.btnMod.setVisibility(View.VISIBLE);
            plv.btnDel.setVisibility(View.VISIBLE);
            plv.btnApp.setVisibility(View.VISIBLE);
        } else {
            plv.btnMod.setVisibility(View.INVISIBLE);
            plv.btnDel.setVisibility(View.INVISIBLE);
            plv.btnApp.setVisibility(View.INVISIBLE);
        }


        StackLink stackLink = mLinks.get(position);

        if (MainActivity.isAmministrator() == Boolean.TRUE) {
            Drawable res;
            if (stackLink.getAccepted() == 1) {
                res = ContextCompat.getDrawable(MainActivity.getAppContext(), R.drawable.good);
            } else {
                res = ContextCompat.getDrawable(MainActivity.getAppContext(), R.drawable.warning);
            }
            plv.imageState.setImageDrawable(res);
        }



        plv.btnPlay.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent;
                StackLink stackLink = mLinks.get(position);

                if (GroupType == 0) {


                    Intent mpdIntent = new Intent(tabLocal.Fa.getContext(), PlayerActivity.class)
                            .setData(Uri.parse(stackLink.getLinkValue()))
                            .putExtra(PlayerActivity.CONTENT_ID_EXTRA, "")
                            .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, PlayerActivity.TYPE_HLS)
                            .putExtra(PlayerActivity.PROVIDER_EXTRA, "");
                    tabFromDB.fa.getContext().startActivity(mpdIntent);


                } else if (GroupType == 1) {


//                    Intent intent2 = new Intent(tabLocal.Fa.getContext(), MediaPlayerDemo_VideoView.class);
//                    intent2.putExtra("pathValue", stackLink.getLinkValue());
//                    MainActivity.posLocalListBeforeExecuted = tabLocal.sitesLocal.getFirstVisiblePosition();
//                    tabLocal.Fa.getContext().startActivity(intent2);



                } else {

//                    intent = new Intent(v.getContext(), MediaPlayerDemo_Video.class);
//                    intent.putExtra("media", 5);
//                    intent.putExtra("pathValue", stackLink.getLinkValue());
//
//                    try {
//                        tabFromDB.fa.startActivity(intent);
//                    } catch (Exception ex) {
//                        Toast.makeText(cloc, ex.toString(),
//                                Toast.LENGTH_SHORT).show();
//                    }

                }







                Toast.makeText(MainActivity.getAppContext(), "Play: " + NameChannel,
                        Toast.LENGTH_SHORT).show();


            }
        });

        plv.btnMod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), AddLink.class);
                intent.putExtra("AddMod", "Mod");
                intent.putExtra("NameChannel", NameChannel);
                intent.putExtra("linkValue", mLinks.get(position).getLinkValue());
                intent.putExtra("linkID", mLinks.get(position).getLinkID());
                intent.putExtra("linkName", mLinks.get(position).getLinkTxt());
                LinksLists.fa.startActivity(intent);


            }
        });


        plv.btnDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                lLink = new StackLink();
                //lLink.setLinkTxt();
                lLink.setLinkID(mLinks.get(position).getLinkID());
                //lLink.setLinkValue(Linkss.get(position).getLinkValue());


                AlertDialog.Builder myAlert;
                try {
                    myAlert = new AlertDialog.Builder(LinksLists.fa);
                } catch (Exception ex) {
                    myAlert = new AlertDialog.Builder(MainActivity.getAppContext());
                }

                myAlert.setMessage("Do you really want to Cancel the link?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                RemoteCommunications rm = new RemoteCommunications();
                                rm.deleteLinkInBackground(lLink, NameChannel, new GetLinkCallback() {
                                    @Override
                                    public void done(List<StackLink> returnedLink) {


                                        MyListLinkAdapterExt adapterFiltered = new MyListLinkAdapterExt(MainActivity.getAppContext(), returnedLink, NameChannel, 0);
                                        LinksLists.sitesLinks.setAdapter(adapterFiltered);

                                        //fa.finish();
                                        //Intent intent = new Intent(MainActivity.getAppContext(), LinksLists.class);
                                        //intent.putExtra("VideoStreamName", NameChannel);
                                        //LinksLists.fa.startActivity(intent);
                                        //LinksLists.fa.finish();

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

        plv.btnApp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {



                lLink = new StackLink();
                //lLink.setLinkTxt();
                lLink.setLinkID(mLinks.get(position).getLinkID());
                lLink.setAccepted(mLinks.get(position).getAccepted());
                //lLink.setLinkValue(Linkss.get(position).getLinkValue());

                AlertDialog.Builder myAlert;
                try {
                    myAlert = new AlertDialog.Builder(LinksLists.fa);
                } catch (Exception ex) {
                    myAlert = new AlertDialog.Builder(MainActivity.getAppContext());
                }

                String Sentence;
                if (mLinks.get(position).getAccepted() == 0) {
                    Sentence="Do you really want to approve the link?";
                } else {
                    Sentence="Do you really want to disapprove the link?";
                }

                myAlert.setMessage(Sentence)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                RemoteCommunications rm = new RemoteCommunications();
                                rm.approveLinkInBackground(lLink, NameChannel, new GetIntegerCallback() {
                                    @Override
                                    public void done(Integer returnedValue) {

                                        //fa.finish();
                                        Intent intent = new Intent(MainActivity.getAppContext(), LinksLists.class);
                                        intent.putExtra("VideoStreamName", NameChannel);
                                        LinksLists.fa.startActivity(intent);
                                        LinksLists.fa.finish();

                                        if (returnedValue==1) {
                                            Toast.makeText(MainActivity.getAppContext(), "Also the Channel '" + NameChannel + "' is disapproved due that not link approved are associated", Toast.LENGTH_SHORT).show();
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
                        .setTitle("Approve Link")
                        .create();
                myAlert.show();



            }
        });



        stLink = mLinks.get(position);
        plv.linkValue.setText(stLink.getLinkTxt());


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
        Button btnPlay;
        Button btnMod;
        Button btnApp;
        Button btnDel;
        TextView linkValue;
        ImageView imageState;
        //Initially we want the progress indicator visible, and the image invisible

    }
}


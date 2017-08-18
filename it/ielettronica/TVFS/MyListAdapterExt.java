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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gnardelli on 1/9/16.
 */


public class MyListAdapterExt extends BaseAdapter {
    private List<StackSite> mItems;
    private LayoutInflater mInflater;
    private Spinner listLinks;
    StackSite stk;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Context cloc;
    PlaylistValues plv;
    MyDBHandler dbHandler;
    StackSite stkloc;
    private int posSpinnerLink;
    private List<StackLink> retLinks;
    private int posSpinner;
    //private String GroupSeletced;
    private StackGroup GroupVSeletced;
    private ListView sitesMusic;
    private AlertDialog OptionDialog;
    private int GroupType;


    public MyListAdapterExt(Context c, List<StackSite> items, ListView sitesMusic, StackGroup GrupVSelected) {
        mItems = items;
        this.cloc = c;
        //Cache a reference to avoid looking it up on every getView() call
        mInflater = LayoutInflater.from(MainActivity.getAppContext());
        dbHandler = new MyDBHandler(tabLocal.Fa.getContext(), null, null, 1);
        //Setup the ImageLoader, we'll use this to display our images
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(MainActivity.getAppContext()).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        this.sitesMusic = sitesMusic;
        this.GroupVSeletced = GrupVSelected;
        //Setup options for ImageLoader so it will handle caching for us.
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();
    }


    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //If there's no recycled view, inflate one and tag each of the views
        //you'll want to modify later
        //Log.d("Inside", "GetView");

        plv = null;
        if (convertView == null) {

            convertView = mInflater.inflate(R.layout.row_site_remote, parent, false);
            plv = new PlaylistValues();

            plv.nameTxt = (TextView) convertView.findViewById(R.id.nameTxt);
            plv.aboutTxt = (TextView) convertView.findViewById(R.id.aboutTxt);
            plv.iconImg = (ImageView) convertView.findViewById(R.id.iconImg);
            plv.btnPlayLocal = (Button) convertView.findViewById(R.id.btnPlayLocal);
            plv.indicator = (ProgressBar) convertView.findViewById(R.id.progress);
            plv.btnAddLocal = (Button) convertView.findViewById(R.id.btnAddLocal);
            plv.imageState = (ImageView) convertView.findViewById(R.id.imgState);
            plv.btnEditDel = (Button) convertView.findViewById(R.id.btnEditDel);
            //This assumes layout/row_left.xml includes a TextView with an id of "textview"
            convertView.setTag(plv);
        } else {
            plv = (PlaylistValues) convertView.getTag();
        }

        //Initially we want the progress indicator visible, and the image invisible
        plv.indicator.setVisibility(View.VISIBLE);
        plv.iconImg.setVisibility(View.INVISIBLE);

        if (MainActivity.isAmministrator()) {
            plv.btnEditDel.setVisibility(View.VISIBLE);
        } else {
            plv.btnEditDel.setVisibility(View.INVISIBLE);
        }


        //Retrieve the tagged view, get the item for that position, and
        //update the text

        ImageLoadingListener listener = new ImageLoadingListener() {


            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                // TODO Auto-generated method stub
                plv.indicator.setVisibility(View.INVISIBLE);
                plv.iconImg.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                //plv.indicator.setVisibility(View.INVISIBLE);
                //plv.iconImg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // TODO Auto-generated method stub

            }

        };

        stk = mItems.get(position);
        plv.nameTxt.setText(stk.getName());
        plv.aboutTxt.setText(stk.getAbout());


        String uri = "@drawable/myresource";  // where myresource.png is the file
        // extension removed from the String

        if (MainActivity.isAmministrator() == Boolean.TRUE) {
            Drawable res;
            if (stk.getAccepted() == 1) {
                res = ContextCompat.getDrawable(MainActivity.getAppContext(), R.drawable.good);
            } else {
                res = ContextCompat.getDrawable(MainActivity.getAppContext(), R.drawable.warning);
            }
            plv.imageState.setImageDrawable(res);
        }


        imageLoader.displayImage(stk.getImgUrl(), plv.iconImg, options, listener);

        plv.btnEditDel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                final StackSite stkloc = mItems.get(position);
                MainActivity.posRemoteListBeforeExecuted = sitesMusic.getFirstVisiblePosition();
                Intent intent = new Intent(MainActivity.getAppContext(), ModChannel.class);


                intent.putExtra("TitleChannel", stkloc.getName());
                intent.putExtra("DescrChannel", stkloc.getAbout());
                intent.putExtra("IconChannel", stkloc.getImgUrl());
                intent.putExtra("isAccepted", stkloc.getAccepted());
                intent.putExtra("NameGroup", GroupVSeletced.getGroupName());
                intent.putExtra("LevelGroup", GroupVSeletced.getGroupLevel());
                intent.putExtra("TypeGroup", GroupVSeletced.getGroupType());

                intent.putExtra("GroupLevel", GroupVSeletced.getGroupLevel());
                tabFromDB.fa.startActivity(intent);


            }
        });

        plv.btnPlayLocal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

//                final Intent intent = new Intent(v.getContext(), MediaPlayerDemo_Video.class);
                final StackSite stkloc = mItems.get(position);
                MainActivity.posRemoteListBeforeExecuted = sitesMusic.getFirstVisiblePosition();


                GroupType = GroupVSeletced.getGroupType();

                RemoteCommunications rm = new RemoteCommunications();
                List<StackLink> Links = new ArrayList<>();
                rm.getLinks(Links, stkloc.getName(), new GetLinkCallback() {
                    @Override
                    public void done(List<StackLink> returnedLinks) {
                        List<StackLink> Links = returnedLinks;
                        if (Links.size() == 1) {
                            MainActivity.posRemoteListBeforeExecuted =  tabFromDB.sitesMusic.getFirstVisiblePosition();
                            if (MainActivity.isAmministrator()) {

                                Intent intent = new Intent(MainActivity.getAppContext(), LinksLists.class);
                                intent.putExtra("VideoStreamName", stkloc.getName());
                                intent.putExtra("GroupType",GroupType);

                                tabFromDB.fa.startActivity(intent);

                            } else {
                                String linkVal = Links.get(0).getLinkValue();
                                //Uri myUri = Uri.parse(linkVal);

                                if (GroupType==0) {




                                    Intent mpdIntent = new Intent(tabLocal.Fa.getContext(), PlayerActivity.class)
                                            .setData(Uri.parse(linkVal))
                                            .putExtra(PlayerActivity.CONTENT_ID_EXTRA, "")
                                            .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, PlayerActivity.TYPE_HLS)
                                            .putExtra(PlayerActivity.PROVIDER_EXTRA, "");
                                    tabFromDB.fa.getContext().startActivity(mpdIntent);





                                }

//                                else if (GroupType==1) {


//                                    Intent intent2 = new Intent(tabLocal.Fa.getContext(), MediaPlayerDemo_VideoView.class);
//                                    intent2.putExtra("pathValue", linkVal);
//                                    tabFromDB.fa.getContext().startActivity(intent2);



//                                } else {

//                                    intent.putExtra("media", 5);
//                                    intent.putExtra("pathValue", linkVal);
//
//                                    try {
//                                        tabFromDB.fa.startActivity(intent);
//                                    } catch (Exception ex) {
//                                        Toast.makeText(cloc, ex.toString(),
//                                                Toast.LENGTH_SHORT).show();
//                                    }

//                                }

                                Toast.makeText(cloc, "Play: " + stkloc.getName(),
                                        Toast.LENGTH_SHORT).show();

                            }


                        } else if (Links.size() == 0) {
                            Toast.makeText(cloc, "the channel:  '" + stkloc.getName() + "' doesn't have any link associated",
                                    Toast.LENGTH_SHORT).show();
                        } else {

                            Intent intent = new Intent(MainActivity.getAppContext(), LinksLists.class);
                            intent.putExtra("VideoStreamName", stkloc.getName());
                            tabFromDB.fa.startActivity(intent);

                        }
                    }
                });


            }
        });

        plv.btnAddLocal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                stkloc = mItems.get(position);

                RemoteCommunications rm = new RemoteCommunications();
                List<StackLink> Links = new ArrayList<>();
                rm.getLinks(Links, stkloc.getName(), new GetLinkCallback() {
                    @Override
                    public void done(List<StackLink> returnedLinks) {

                        AlertDialog.Builder alertDialog;
                        try {
                            alertDialog = new AlertDialog.Builder(tabFromDB.fa.getContext());
                        } catch (Exception ex) {
                            alertDialog = new AlertDialog.Builder(MainActivity.getAppContext());
                        }

                        retLinks = returnedLinks;
                        LinearLayout layout = new LinearLayout(MainActivity.getAppContext());
                        layout.setOrientation(LinearLayout.VERTICAL);

                        if (returnedLinks.size() > 1) {

                            LinearLayout rlayoutLink = new LinearLayout(MainActivity.getAppContext());
                            rlayoutLink.setOrientation(LinearLayout.HORIZONTAL);
                            final TextView textView = new TextView(MainActivity.getAppContext());
                            textView.setText("Links:  ");
                            textView.setGravity(Gravity.RIGHT);
                            textView.setLayoutParams(new FrameLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT));
                            rlayoutLink.addView(textView);
                            listLinks = new Spinner(MainActivity.getAppContext());

                            List<String> retLinksString = new ArrayList<>();
                            for (int i = 0; i < returnedLinks.size(); i++) {
                                retLinksString.add(returnedLinks.get(i).getLinkTxt());
                            }

                            listLinks.setAdapter(new MyCustomAdapter(MainActivity.getAppContext(), R.layout.rowspinnertake, retLinksString, returnedLinks));


                            //ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.getAppContext(), android.R.layout.simple_spinner_item, retLinksString);
                            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            //listLinks.setAdapter(adapter);


                            listLinks.setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));


                            rlayoutLink.addView(listLinks);
                            layout.addView(rlayoutLink);
                        }

                        final Button btnAddToTheEnd = new Button(MainActivity.getAppContext());
                        btnAddToTheEnd.setText("Add");

                        btnAddToTheEnd.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {


                                StackSite stkloc = mItems.get(position);

                                if (listLinks != null) {
                                    posSpinnerLink = listLinks.getSelectedItemPosition();
                                } else {
                                    posSpinnerLink = 0;
                                }


                                if (retLinks.size() == 0) {
                                    Toast.makeText(cloc, "the channel:  '" + stkloc.getName() + "' doesn't have any link associated",
                                            Toast.LENGTH_SHORT).show();
                                } else {


                                    String url;

                                    if (retLinks.size() == 1) {

                                        url = retLinks.get(0).getLinkValue();

                                    } else {
                                        url = retLinks.get(posSpinnerLink).getLinkValue();
                                    }

                                    stkloc.setLink(url);
                                    stkloc.setTypeStream(GroupVSeletced.getGroupType());
                                    stkloc.setStaticName(stkloc.getName());
                                    String nameStation = stkloc.getName();
                                    stkloc.setOrigin(0);
                                    boolean isAdded = dbHandler.addSite(stkloc);
                                    if (isAdded) {
                                        Toast.makeText(MainActivity.getAppContext(), nameStation + " is added in Local Playlist", Toast.LENGTH_SHORT).show();
                                    }
                                    List<StackSite> itemsLocal;
                                    itemsLocal = dbHandler.getStackSites();
                                    if (itemsLocal.size() == 0) {
                                        tabLocal.editEmptyLocalList.setVisibility(View.VISIBLE);
                                    } else {
                                        tabLocal.editEmptyLocalList.setVisibility(View.INVISIBLE);
                                    }

                                    tabLocal.sitesLocal.setCheeseList(itemsLocal);
                                    tabLocal.sitesLocal.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                    StableArrayAdapter adapterLocal = new StableArrayAdapter(tabLocal.Fa.getContext(), R.layout.row_site_local, itemsLocal);
                                    tabLocal.sitesLocal.setAdapter(adapterLocal);

                                    OptionDialog.dismiss();

                                }

                            }
                        });
                        btnAddToTheEnd.setLayoutParams(new FrameLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT));
                        layout.addView(btnAddToTheEnd);


                        final Button btnAddAfter = new Button(MainActivity.getAppContext());
                        final Spinner listChannelAlreadyAdded = new Spinner(MainActivity.getAppContext());
                        List<String> ListNameChannel;
                        ListNameChannel = dbHandler.getNamesFromStackSites();
                        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(MainActivity.getAppContext(), android.R.layout.simple_spinner_item, ListNameChannel);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        listChannelAlreadyAdded.setAdapter(adapter2);
                        posSpinner = listChannelAlreadyAdded.getSelectedItemPosition();

                        if (posSpinner != -1) {
                            LinearLayout rlayout = new LinearLayout(MainActivity.getAppContext());
                            rlayout.setOrientation(LinearLayout.HORIZONTAL);


                            btnAddAfter.setText("Add Before");
                            btnAddAfter.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {


                                    if (listLinks != null) {
                                        posSpinnerLink = listLinks.getSelectedItemPosition();
                                    } else {
                                        posSpinnerLink = 0;
                                    }

                                    StackSite stkloc = mItems.get(position);


                                    if (retLinks.size() == 0) {
                                        Toast.makeText(cloc, "the channel:  '" + stkloc.getName() + "' doesn't have any link associated",
                                                Toast.LENGTH_SHORT).show();
                                    } else {

                                        String url;

                                        if (retLinks.size() == 1) {

                                            url = retLinks.get(0).getLinkValue();

                                        } else {
                                            url = retLinks.get(posSpinnerLink).getLinkValue();
                                        }


                                        stkloc.setLink(url);
                                        stkloc.setOrigin(0);
                                        stkloc.setTypeStream(GroupVSeletced.getGroupType());
                                        String nameStation = stkloc.getName();
                                        posSpinner = listChannelAlreadyAdded.getSelectedItemPosition();

                                        boolean isAdded = dbHandler.addSiteBefore(stkloc, posSpinner);
                                        if (isAdded) {
                                            Toast.makeText(MainActivity.getAppContext(), nameStation + " is added in Local Playlist", Toast.LENGTH_SHORT).show();
                                        }
                                        List<StackSite> itemsLocal;
                                        itemsLocal = dbHandler.getStackSites();
                                        tabLocal.sitesLocal.setCheeseList(itemsLocal);
                                        tabLocal.sitesLocal.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                        StableArrayAdapter adapterLocal = new StableArrayAdapter(tabLocal.Fa.getContext(), R.layout.row_site_local, itemsLocal);
                                        tabLocal.sitesLocal.setAdapter(adapterLocal);

                                        OptionDialog.dismiss();


                                    }


                                }
                            });

                            btnAddAfter.setLayoutParams(new FrameLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT));
                            rlayout.addView(btnAddAfter);
                            listChannelAlreadyAdded.setLayoutParams(new FrameLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            rlayout.addView(listChannelAlreadyAdded);
                            layout.addView(rlayout);
                        }

                        final Button btnCancel = new Button(MainActivity.getAppContext());
                        btnCancel.setText("Cancel");
                        btnCancel.setLayoutParams(new FrameLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT));

                        btnCancel.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                OptionDialog.dismiss();
                            }
                        });


                        layout.addView(btnCancel);
                        alertDialog.setView(layout); // uncomment this line
                        alertDialog.setTitle("Take the Channel");
                        OptionDialog = alertDialog.create();
                        OptionDialog.show();
                    }


                });


            }


        });


        return convertView;
    }


    public class MyCustomAdapter extends ArrayAdapter<String>{

        private Context cx;
        private List<String> retLinksString;
        private List<StackLink> returnedLinks;
        public MyCustomAdapter(Context context, int textViewResourceId,
                               List<String> retLinksString, List<StackLink> returnedLinks) {

            super(context, textViewResourceId, retLinksString);

            // TODO Auto-generated constructor stub
            this.cx=context;
            this.retLinksString = retLinksString;
            this.returnedLinks = returnedLinks;

        }

        @Override
        public View getDropDownView(int position, View convertView,
                                    ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            return getCustomView(position, convertView, parent);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            //return super.getView(position, convertView, parent);

            LayoutInflater inflater=(LayoutInflater)cx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row=inflater.inflate(R.layout.rowspinnertake, parent, false);
            TextView label=(TextView)row.findViewById(R.id.weekofday);
            label.setText(retLinksString.get(position));

            ImageView icon=(ImageView)row.findViewById(R.id.icon);


            if (returnedLinks.get(position).getAccepted()==1) {
                icon.setImageResource(R.drawable.good);
            } else{
                icon.setImageResource(R.drawable.warning);
            }

            return row;
        }
    }

    private class PlaylistValues {
        ImageView iconImg;
        TextView nameTxt;
        TextView aboutTxt;
        Button btnPlayLocal;
        Button btnAddLocal;
        ProgressBar indicator;
        ImageView imageState;
        Button btnEditDel;
        //Initially we want the progress indicator visible, and the image invisible
    }

}


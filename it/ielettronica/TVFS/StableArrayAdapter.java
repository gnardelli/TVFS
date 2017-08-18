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
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import java.util.HashMap;
import java.util.List;


public class StableArrayAdapter extends ArrayAdapter<StackSite> {
    private List<StackSite> mItems;
    private LayoutInflater mInflater;
    StackSite stk;
    ImageLoader imageLoader;
    DisplayImageOptions options;
    Context cloc;
    PlaylistValues plv;
    MyDBHandler dbHandler;
    private Context mContext;

    final int INVALID_ID = -1;

    HashMap<StackSite, Integer> mIdMap = new HashMap<>();



    public StableArrayAdapter (Context c, int textViewResourceId, List<StackSite> items) {


        super(c, textViewResourceId, items);

        mContext =c;
        for (int i = 0; i < items.size(); ++i) {
            mIdMap.put(items.get(i), i);
        }

        for (int i = 0; i < items.size(); ++i) {
            mIdMap.put(items.get(i), i);
        }


        mItems = items;
        this.cloc = c;
        //Cache a reference to avoid looking it up on every getView() call
        mInflater = LayoutInflater.from(c);

        //Setup the ImageLoader, we'll use this to display our images
        dbHandler = new MyDBHandler(tabLocal.Fa.getContext(), null, null,1);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(c).build();
        imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);

        //Setup options for ImageLoader so it will handle caching for us.
        options = new DisplayImageOptions.Builder()
                .cacheInMemory()
                .cacheOnDisc()
                .build();
    }

    @Override
    public int getCount () {
        return mItems.size();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        StackSite item = getItem(position);
        return mIdMap.get(item);
    }



    @Override
    public StackSite getItem (int position) {
        return mItems.get(position);
    }

    @Override
    public View getView (final int position, View convertView, ViewGroup parent) {
        //If there's no recycled view, inflate one and tag each of the views
        //you'll want to modify later
        //Log.d("Inside", "GetView");
        plv = null;
        if (convertView == null) {
            convertView = mInflater.inflate (R.layout.row_site_local, parent, false);
            plv = new PlaylistValues();
            plv.nameTxt = (TextView) convertView.findViewById(R.id.nameTxt);
            plv.aboutTxt = (TextView) convertView.findViewById(R.id.aboutTxt);
            plv.iconImg = (ImageView) convertView.findViewById(R.id.iconImg);
            plv.btnPlayLocal = (Button) convertView.findViewById(R.id.btnPlayLocal);
            plv.indicator = (ProgressBar)convertView.findViewById(R.id.progress);
            plv.btnDelete = (Button) convertView.findViewById(R.id.btnDelete);
            plv.btnEdit = (Button) convertView.findViewById(R.id.btnEdit);
            //This assumes layout/row_left.xml includes a TextView with an id of "textview"
            convertView.setTag (plv);
        } else {
            plv = (PlaylistValues) convertView.getTag();
        }

        //Initially we want the progress indicator visible, and the image invisible
        plv.indicator.setVisibility(View.VISIBLE);
        plv.iconImg.setVisibility(View.INVISIBLE);

        //Retrieve the tagged view, get the item for that position, and
        //update the text

        ImageLoadingListener listener = new ImageLoadingListener(){


            @Override
            public void onLoadingStarted(String arg0, View arg1) {
                plv.indicator.setVisibility(View.INVISIBLE);
                plv.iconImg.setVisibility(View.VISIBLE);

            }

            @Override
            public void onLoadingCancelled(String arg0, View arg1) {
                // nothing to do

            }

            @Override
            public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
                //plv.indicator.setVisibility(View.INVISIBLE);
                //plv.iconImg.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                // nothing to do

            }

        };

        stk = mItems.get(position);
        plv.nameTxt.setText(stk.getName());
        plv.aboutTxt.setText(stk.getAbout());
        imageLoader.displayImage(stk.getImgUrl(), plv.iconImg, options, listener);



        plv.btnPlayLocal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                StackSite stkloc = mItems.get(position);

                if (stkloc.getTypeStream()==0) {


                    MainActivity.posLocalListBeforeExecuted = tabLocal.sitesLocal.getFirstVisiblePosition();

                    Intent mpdIntent = new Intent(tabLocal.Fa.getContext(), PlayerActivity.class)
                            .setData(Uri.parse(stkloc.getLink()))
                            .putExtra(PlayerActivity.CONTENT_ID_EXTRA, "")
                            .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, PlayerActivity.TYPE_HLS)
                            .putExtra(PlayerActivity.PROVIDER_EXTRA, "");
                    tabFromDB.fa.getContext().startActivity(mpdIntent);



                }


//                else if (stkloc.getTypeStream()==1) {


//                    Intent intent2 = new Intent(tabLocal.Fa.getContext(), MediaPlayerDemo_VideoView.class);
//                    intent2.putExtra("pathValue", stkloc.getLink());
//                    MainActivity.posLocalListBeforeExecuted = tabLocal.sitesLocal.getFirstVisiblePosition();
//                    tabLocal.Fa.getContext().startActivity(intent2);


//                } else {

//                    Intent intent = new Intent(tabLocal.Fa.getContext(), MediaPlayerDemo_Video.class);
//                    intent.putExtra("media", 5);
//                    intent.putExtra("pathValue", stkloc.getLink());
//                    MainActivity.posLocalListBeforeExecuted = tabLocal.sitesLocal.getFirstVisiblePosition();
//                    tabLocal.Fa.getContext().startActivity(intent);
 //               }


                Toast.makeText(cloc, "Play: " + stkloc.getName(),
                        Toast.LENGTH_SHORT).show();


            }
        });




        plv.btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                AlertDialog.Builder myAlert = new AlertDialog.Builder(getContext());
                myAlert.setMessage("Are you sure to Delete this channel from local?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                dbHandler = new MyDBHandler(tabLocal.Fa.getContext(), null, null,1);
                                StackSite stkloc = mItems.get(position);
                                dbHandler.deleteSite(stkloc.getName());
                                Toast.makeText(mContext, "delete a value from the database", Toast.LENGTH_SHORT).show();


                                List<StackSite> itemsLocal;
                                itemsLocal = dbHandler.getStackSites();
                                tabLocal.sitesLocal.setCheeseList(itemsLocal);

                                if (itemsLocal.size()==0) {
                                    tabLocal.editEmptyLocalList.setVisibility(View.VISIBLE);
                                } else {
                                    tabLocal.editEmptyLocalList.setVisibility(View.INVISIBLE);
                                }


                                tabLocal.sitesLocal.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                                StableArrayAdapter adapterLocal = new StableArrayAdapter(mContext, R.layout.row_site_local, itemsLocal);



                                int index = tabLocal.sitesLocal.getFirstVisiblePosition();
                                //View v = tabLocal.sitesLocal.getChildAt(0);
                                tabLocal.sitesLocal.setAdapter(adapterLocal);
                                tabLocal.sitesLocal.setSelection(index);


                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setTitle("Delete  the channel locally")
                        .create();
                myAlert.show();




            }
        });


        plv.btnEdit.setOnClickListener(new View.OnClickListener() {
                                           public void onClick(View v) {


                                               StackSite stkloc = mItems.get(position);

                                               Intent intent = new Intent(getContext(), AddNewChannel.class);
                                               intent.putExtra("TitleChannel", stkloc.getName());
                                               intent.putExtra("TitleStaticChannel", stkloc.getStaticName());
                                               intent.putExtra("DescrChannel", stkloc.getAbout());
                                               intent.putExtra("IconChannel", stkloc.getImgUrl());
                                               intent.putExtra("LinkChannel", stkloc.getLink());
                                               intent.putExtra("Origin", stkloc.getOrigin());
                                               intent.putExtra("isFilm", stkloc.getTypeStream());
                                               tabLocal.Fa.startActivity(intent);

//                                               AlertDialog.Builder alertDialog = new AlertDialog.Builder(tabLocal.Fa.getContext());
//
//                                               stkloc = mItems.get(position);
//
//                                               LinearLayout layout = new LinearLayout(mContext);
//                                               layout.setOrientation(LinearLayout.VERTICAL);
//
//                                               final EditText titleBox = new EditText(mContext);
//                                               final EditText oldTitleBox = new EditText(mContext);
//                                               final CheckBox chType = new CheckBox(mContext);
//                                               //titleBox.setHint("Title");
//                                               titleBox.setText(stkloc.getName());
//                                               layout.addView(titleBox);
//
//                                               if (stkloc.getTypeStream() ==0) {
//                                                   chType.setChecked(Boolean.FALSE);
//                                               } else {
//                                                   chType.setChecked(Boolean.TRUE);
//                                               }
//
//                                               oldTitleBox.setText(stkloc.getName());
//
//
//
//                                               final EditText descriptionBox = new EditText(mContext);
//                                               //descriptionBox.setHint("Description");
//                                               descriptionBox.setText(stkloc.getAbout());
//                                               layout.addView(descriptionBox);
//
//
//                                               final EditText iconBox  = new EditText(mContext);
//                                               iconBox.setText(stkloc.getImgUrl());
//                                               layout.addView(iconBox);
//                                               layout.addView(chType);
//                                               alertDialog.setView(layout); // uncomment this line
//
//                                               alertDialog.setPositiveButton("YES",
//                                                       new DialogInterface.OnClickListener() {
//                                                           String NewName;
//                                                           String OldName;
//                                                           String IconName;
//                                                           String NewDescription;
//                                                           int checkType;
//
//                                                           public void onClick(DialogInterface dialog, int which) {
//
//                                                               dbHandler = new MyDBHandler(tabLocal.Fa.getContext(), null, null, 1);
//                                                               NewName = titleBox.getText().toString();
//                                                               IconName = iconBox.getText().toString();
//                                                               OldName = oldTitleBox.getText().toString();
//                                                               NewDescription = descriptionBox.getText().toString();
//                                                               if (!chType.isChecked()) {
//                                                                   checkType = 0;
//                                                               } else {
//                                                                   checkType = 1;
//                                                               }
//
//                                                               stkloc.setName(NewName);
//                                                               stkloc.setAbout(NewDescription);
//                                                               stkloc.setImgUrl(IconName);
//                                                               stkloc.setTypeStream(checkType);
//
//                                                               dbHandler.updateSite(stkloc, OldName);
//
//                                                               List<StackSite> itemsLocal;
//                                                               itemsLocal = dbHandler.getStackSites();
//                                                               tabLocal.sitesLocal.setCheeseList(itemsLocal);
//                                                               tabLocal.sitesLocal.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
//                                                               StableArrayAdapter adapterLocal = new StableArrayAdapter(mContext, R.layout.row_site_local, itemsLocal);
//
//                                                               int index = tabLocal.sitesLocal.getFirstVisiblePosition();
//                                                               View v = tabLocal.sitesLocal.getChildAt(0);
//                                                               tabLocal.sitesLocal.setAdapter(adapterLocal);
//                                                               tabLocal.sitesLocal.setSelection(index);
//
//                                                               Toast.makeText(mContext,
//                                                                       "Update " + NewName + " And " + NewDescription + " In the Database", Toast.LENGTH_SHORT).show();
//
//
//                                                           }
//                                                       });
//
//                                               alertDialog.setNegativeButton("NO",
//                                                       new DialogInterface.OnClickListener() {
//                                                           public void onClick(DialogInterface dialog, int which) {
//                                                               dialog.cancel();
//                                                           }
//                                                       });
//
//                                               alertDialog.create();
//                                               alertDialog.show();





                                           }
                                       }

        );


        return convertView;
    }


private class PlaylistValues {
    ImageView iconImg;
    TextView nameTxt;
    TextView aboutTxt;
    Button btnPlayLocal;
    Button btnDelete;
    Button btnEdit;
    ProgressBar indicator;

    //Initially we want the progress indicator visible, and the image invisible

}
}
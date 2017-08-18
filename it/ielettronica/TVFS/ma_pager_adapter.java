
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

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;



public class ma_pager_adapter extends FragmentPagerAdapter {

    private Context mContext;
    private Map<Integer, String> mFragmentTags;
    private FragmentManager mFragmentManager;

    public ma_pager_adapter(FragmentManager fm, Context mContext) {
        super(fm);
        mFragmentManager = fm;
        this.mContext = mContext;
        mFragmentTags = new HashMap<Integer, String>();
    }

    @Override
    public Fragment getItem(int i) {

        switch (i) {
            case 0:
                tabLocal tLocal = new tabLocal();
                tLocal.getArgument(mContext);
                return tLocal;
            case 1:
                tabFromDB tDB = new tabFromDB();
                return tDB;



        }
        return null;
    }



    @Override
    public int getCount() {
        return 2;
    }//set the number of tabs

    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "Local Channels";
            case 1:
                return "Remote Channels";

        }
        return null;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object obj =  super.instantiateItem(container, position);
        if (obj instanceof Fragment) {
            Fragment f = (Fragment) obj;
            String tag = f.getTag();
            mFragmentTags.put(position,tag);
        }

        return obj;
    }



}

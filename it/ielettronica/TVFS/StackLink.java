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


public class StackLink {


    private String linkTxt;
    private String linkValue;
    private int accepted;
    private int linkID;


    public int getAccepted(){
        return accepted;
    }

    public void setAccepted(int accepted){
        this.accepted=accepted;
    }

    public void setLinkValue(String linkValue) {
        this.linkValue = linkValue;
    }

    public String getLinkValue() {
        return linkValue;
    }

    public void setLinkTxt(String linkTxt) {
        this.linkTxt = linkTxt;
    }

    public String getLinkTxt() {
        return linkTxt;
    }

    public int getLinkID() {
        return linkID;
    }

    public void setLinkID(int linkID) {
        this.linkID=linkID;
    }


}

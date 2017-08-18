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



public class StackSite {

    private String name;
    private String staticName;
    private String link;
    private String about;
    private String imgUrl;
    private int position;
    private int accepted;
    private int typeStream;
    private int origin;

    public StackSite() {
    }

    public StackSite(String name, String about, String imgUrl) {
        this.name = name;
        this.about=about;
        this.imgUrl=imgUrl;
    }

    public StackSite(String name, String link, String about, String imgUrl) {
        this.name = name;
        this.link = link;
        this.about=about;
        this.imgUrl=imgUrl;
    }

    public StackSite(String name,String about, String imgUrl, int accepted) {
        this.name = name;
        this.about=about;
        this.imgUrl=imgUrl;
        this.accepted=accepted;
    }

    public StackSite(String name, String link, String about, String imgUrl, int accepted) {
        this.name = name;
        this.link = link;
        this.about=about;
        this.imgUrl=imgUrl;
        this.accepted=accepted;
    }


    public int getAccepted(){
        return accepted;
    }

    public void setAccepted(int accepted) {
        this.accepted = accepted;
    }


    public String getName() {
        return name;
    }

    public String getStaticName() {
        return staticName;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStaticName(String staticName) {
        this.staticName = staticName;
    }

    public void setPosition(int position) { this.position = position;}

    public String getLink() {
        return link;
    }
    public int getPosition() { return position; }

    public void setLink(String link) {
        this.link = link;
    }
    public String getAbout() {
        return about;
    }
    public void setAbout(String about) {
        this.about = about;
    }
    public String getImgUrl() {
        return imgUrl;
    }
    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public int getTypeStream() {
        return typeStream;
    }

    public void setTypeStream(int typeStream) {
        this.typeStream = typeStream;
    }

    public int getOrigin() {
        return origin;
    }

    public void setOrigin(int origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        return "StackSite [name=" + name + ", staticName=" + staticName + ", link=" + link + ", about="
                + about + ", imgUrl=" + imgUrl + ", typeStream=" + typeStream + ", isAccepted=" + accepted + "]";
    }
}

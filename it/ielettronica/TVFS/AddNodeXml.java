
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
import android.os.Environment;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by gnardelli on 11/29/15.
 * this class can be used to import an element from the xml file
 */

public class AddNodeXml {

    public static void addElement(Context ctx, String nameStr, String aboutStr, String linkStr, String imageStr) throws Exception {


        String filePath = Environment.getExternalStorageDirectory()+"/.Playlist/playlistlocal.xml";

        File file = new File(filePath);
        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
        Document document = docBuilder.parse(file);
        Element root = document.getDocumentElement();



        Element newElement = document.createElement("site");

        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(nameStr));
        newElement.appendChild(name);

        Element about = document.createElement("about");
        about.appendChild(document.createTextNode(aboutStr));
        newElement.appendChild(about);

        Element link = document.createElement("link");
        link.appendChild(document.createTextNode(linkStr));
        newElement.appendChild(link);

        Element image = document.createElement("image");
        image.appendChild(document.createTextNode(imageStr));
        newElement.appendChild(image);


        root.appendChild(newElement);




        DOMSource source = new DOMSource(document);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();


        StreamResult result = new StreamResult(file);
        transformer.transform(source, result);

        System.out.println(root);
    }


}




package com.familledupuis91.gamingtools.utils;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rodol on 03/03/2016.
 */
public class TestXml {
    private String xmlData;
    private static final String ns = null;

    public List parse() throws XmlPullParserException, IOException {

        InputStream in = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }


    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List entries = new ArrayList();
        parser.require(XmlPullParser.START_TAG, ns, "frame");

        //   while (parser.next() != XmlPullParser.END_TAG) {
        //if (parser.getEventType() != XmlPullParser.START_TAG) {
        //    continue;
        //}

        String name = parser.getName();


        parser.getAttributeValue(3);


        if (name.equals("entry")) {
            entries.add(readEntry(parser));
        } else {
            skip(parser);
        }

        // }

        return entries;
    }


    private static class Entry {
        private String title;

        private Entry(String title) {
            this.title = title;
        }


    }


    private Entry readEntry(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "enrty");

        String title = null;
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("title")) {
                title = readTitle(parser);
            } else {
                skip(parser);
            }
        }
        return new Entry(title);
    }

    /**
     *
     */


    private String readTitle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "title");
        return title;
    }


    //extraire la text value d'une balise
    private String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }


    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }


}

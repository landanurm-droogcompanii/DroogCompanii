package ru.droogcompanii.application.data;

import android.content.Context;

import java.io.InputStream;

import ru.droogcompanii.application.data.db_util.WriterToDatabase;
import ru.droogcompanii.application.data.xml_parser.partners_xml_parser.PartnersXmlParser;

/**
 * Created by Leonid on 18.12.13.
 */
public class DataUpdater {

    public static interface XmlProvider {
        InputStream getXml() throws Exception;
    }

    private final WriterToDatabase writerToDatabase;
    private final XmlProvider xmlProvider;

    public DataUpdater(Context context, XmlProvider xmlProvider) {
        this.writerToDatabase = new WriterToDatabase(context);
        this.xmlProvider = xmlProvider;
    }

    public void update() throws Exception {
        PartnersXmlParser.ParsedData parsedData = downloadAndParseXml();
        writerToDatabase.write(parsedData);
    }

    private PartnersXmlParser.ParsedData downloadAndParseXml() throws Exception {
        PartnersXmlParser parser = new PartnersXmlParser();
        return parser.parse(xmlProvider.getXml());
    }
}

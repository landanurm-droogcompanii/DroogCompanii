package ru.droogcompanii.application.data;

import android.content.Context;

import java.io.InputStream;

import ru.droogcompanii.application.data.db_util.WriterToDatabase;
import ru.droogcompanii.application.data.xml_parser.DroogCompaniiXmlParser;

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
        DroogCompaniiXmlParser.ParsedData parsedData = downloadAndParseXml();
        writerToDatabase.write(parsedData);
    }

    private DroogCompaniiXmlParser.ParsedData downloadAndParseXml() throws Exception {
        DroogCompaniiXmlParser parser = new DroogCompaniiXmlParser();
        return parser.parse(xmlProvider.getXml());
    }
}

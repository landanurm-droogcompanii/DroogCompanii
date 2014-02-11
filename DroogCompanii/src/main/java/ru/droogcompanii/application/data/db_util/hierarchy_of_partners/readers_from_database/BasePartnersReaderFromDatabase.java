package ru.droogcompanii.application.data.db_util.hierarchy_of_partners.readers_from_database;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;

import ru.droogcompanii.application.data.db_util.BaseReaderFromDatabase;
import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.PartnersDbHelper;

/**
 * Created by Leonid on 17.12.13.
 */
class BasePartnersReaderFromDatabase extends BaseReaderFromDatabase {

    protected BasePartnersReaderFromDatabase(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper prepareDbHelper(Context context) {
        return new PartnersDbHelper(context);
    }
}

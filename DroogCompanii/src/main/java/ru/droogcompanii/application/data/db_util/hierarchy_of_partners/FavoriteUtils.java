package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import ru.droogcompanii.application.data.db_util.hierarchy_of_partners.readers_from_database.PartnersReader;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.util.Holder;

/**
 * Created by ls on 18.02.14.
 */
public class FavoriteUtils {

    private static final String TABLE_NAME = PartnersContracts.PartnersContract.TABLE_NAME;

    private static final String COLUMN_ID =
            PartnersContracts.PartnersContract.COLUMN_NAME_ID;

    private static final String COLUMN_IS_FAVORITE =
            PartnersContracts.PartnersContract.COLUMN_NAME_IS_FAVORITE;

    private final Context context;

    public FavoriteUtils(Context context) {
        this.context = context;
    }

    public boolean isFavorite(Partner partner) {
        final Holder<Boolean> isFavorite = Holder.from(false);

        PartnersReader partnersReader = new PartnersReader(context);
        String where = "WHERE " + COLUMN_ID + " = " + partner.getId();
        partnersReader.forEachWhere(where, new PartnersReader.CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    int index = cursor.getColumnIndexOrThrow(COLUMN_IS_FAVORITE);
                    isFavorite.value = (cursor.getInt(index) == 1);
                }
            }
        });

        return isFavorite.value;
    }


    public void setFavorite(Partner partner, boolean isFavorite) {
        PartnersDbHelper dbHelper = new PartnersDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        setFavorite(db, partner, isFavorite);
        db.close();
        dbHelper.close();
    }

    private void setFavorite(SQLiteDatabase db, Partner partner, boolean isFavorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);
        db.update(TABLE_NAME, contentValues, COLUMN_ID + "=" + partner.getId(), null);
    }

}

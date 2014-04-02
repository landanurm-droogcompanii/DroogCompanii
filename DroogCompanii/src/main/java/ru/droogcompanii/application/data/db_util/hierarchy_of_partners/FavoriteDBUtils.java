package ru.droogcompanii.application.data.db_util.hierarchy_of_partners;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import ru.droogcompanii.application.data.db_util.CursorHandler;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.hierarchy_of_partners.PartnerPoint;
import ru.droogcompanii.application.util.Holder;

/**
 * Created by ls on 18.02.14.
 */
public class FavoriteDBUtils {

    private static final PartnerHierarchyContracts.PartnersContract
            PARTNERS_CONTRACT = new PartnerHierarchyContracts.PartnersContract();

    private static final PartnerHierarchyContracts.PartnerPointsContract
            PARTNER_POINTS_CONTRACT = new PartnerHierarchyContracts.PartnerPointsContract();

    private final Context context;

    public FavoriteDBUtils(Context context) {
        this.context = context;
    }

    public boolean isFavorite(int partnerId) {
        final Holder<Boolean> isFavorite = Holder.from(false);

        PartnersReader partnersReader = new PartnersReader(context);
        final String where = PARTNERS_CONTRACT.COLUMN_ID + " = " + partnerId;
        partnersReader.handleCursorByCondition(where, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    int index = cursor.getColumnIndexOrThrow(PARTNERS_CONTRACT.COLUMN_IS_FAVORITE);
                    isFavorite.value = (cursor.getInt(index) == 1);
                }
            }
        });

        return isFavorite.value;
    }

    public void setFavorite(int partnerId, boolean isFavorite) {
        PartnerHierarchyDbHelper dbHelper = new PartnerHierarchyDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        setFavorite(db, partnerId, isFavorite);
        db.close();
        dbHelper.close();
    }

    private void setFavorite(SQLiteDatabase db, int partnerId, boolean isFavorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PARTNERS_CONTRACT.COLUMN_IS_FAVORITE, isFavorite ? 1 : 0);
        String where = PARTNERS_CONTRACT.COLUMN_ID + "=" + partnerId;
        db.update(PARTNERS_CONTRACT.TABLE_NAME, contentValues, where, null);
    }

    public List<Partner> getFavoritePartners() {
        String where = " WHERE " + getIsFavoriteCondition();
        PartnersReader partnersReader = new PartnersReader(context);
        return partnersReader.getPartnersByWhere(where);
    }

    public static String getIsFavoriteCondition() {
        return PARTNERS_CONTRACT.COLUMN_IS_FAVORITE + " = 1 ";
    }

    public List<PartnerPoint> getAllFavoritePartnerPoints() {
        final String sql = "SELECT " + PARTNER_POINTS_CONTRACT.TABLE_NAME + ".* " +
                " FROM " + PARTNER_POINTS_CONTRACT.TABLE_NAME + " WHERE EXISTS " +
                "( SELECT * FROM " + PARTNERS_CONTRACT.TABLE_NAME + " WHERE " +
                    PARTNERS_CONTRACT.TABLE_NAME + "." + PARTNERS_CONTRACT.COLUMN_ID + " = " +
                    PARTNER_POINTS_CONTRACT.TABLE_NAME + "." + PARTNER_POINTS_CONTRACT.COLUMN_PARTNER_ID +
                    " AND " +
                    PARTNERS_CONTRACT.TABLE_NAME + "." + PARTNERS_CONTRACT.COLUMN_IS_FAVORITE + " = 1" +
                " )";

        List<PartnerPoint> noPartnerPoints = new ArrayList<PartnerPoint>();
        final Holder<List<PartnerPoint>> resultHolder = Holder.from(noPartnerPoints);

        PartnersHierarchyReaderFromDatabase reader = new PartnersHierarchyReaderFromDatabase(context);
        reader.handleCursorByQuery(sql, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                resultHolder.value = PartnerPointsReader.getPartnerPointsFromCursor(cursor);
            }
        });

        return resultHolder.value;
    }
}

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

    private static final PartnersContracts.PartnersContract
            PARTNERS_CONTRACT = new PartnersContracts.PartnersContract();

    private static final PartnersContracts.PartnerPointsContract
            PARTNER_POINTS_CONTRACT = new PartnersContracts.PartnerPointsContract();

    private final Context context;

    public FavoriteDBUtils(Context context) {
        this.context = context;
    }

    public boolean isFavorite(Partner partner) {
        return isFavorite(partner.getId());
    }

    public boolean isFavorite(PartnerPoint partnerPoint) {
        return isFavorite(partnerPoint.getPartnerId());
    }

    public boolean isFavorite(int partnerId) {
        final Holder<Boolean> isFavorite = Holder.from(false);

        PartnersReader partnersReader = new PartnersReader(context);
        final String where = " WHERE " + PARTNERS_CONTRACT.COLUMN_NAME_ID + " = " + partnerId;
        partnersReader.handleCursorByCondition(where, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                cursor.moveToFirst();
                if (!cursor.isAfterLast()) {
                    int index = cursor.getColumnIndexOrThrow(PARTNERS_CONTRACT.COLUMN_NAME_IS_FAVORITE);
                    isFavorite.value = (cursor.getInt(index) == 1);
                }
            }
        });

        return isFavorite.value;
    }

    public void setFavorite(Partner partner, boolean isFavorite) {
        setFavorite(partner.getId(), isFavorite);
    }

    public void setFavorite(PartnerPoint partnerPoint, boolean isFavorite) {
        setFavorite(partnerPoint.getPartnerId(), isFavorite);
    }

    public void setFavorite(int partnerId, boolean isFavorite) {
        PartnersDbHelper dbHelper = new PartnersDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        setFavorite(db, partnerId, isFavorite);
        db.close();
        dbHelper.close();
    }

    private void setFavorite(SQLiteDatabase db, int partnerId, boolean isFavorite) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(PARTNERS_CONTRACT.COLUMN_NAME_IS_FAVORITE, isFavorite ? 1 : 0);
        String where = PARTNERS_CONTRACT.COLUMN_NAME_ID + "=" + partnerId;
        db.update(PARTNERS_CONTRACT.TABLE_NAME, contentValues, where, null);
    }

    public List<Partner> getFavoritePartners() {
        String where = " WHERE " + PARTNERS_CONTRACT.COLUMN_NAME_IS_FAVORITE + " = 1 ";
        PartnersReader partnersReader = new PartnersReader(context);
        return partnersReader.getPartnersByCondition(where);
    }

    public List<PartnerPoint> getAllFavoritePartnerPoints() {
        final String sql = "SELECT " + PARTNER_POINTS_CONTRACT.TABLE_NAME + ".* " +
                " FROM " + PARTNER_POINTS_CONTRACT.TABLE_NAME + " WHERE EXISTS " +
                "( SELECT * FROM " + PARTNERS_CONTRACT.TABLE_NAME + " WHERE " +
                    PARTNERS_CONTRACT.TABLE_NAME + "." + PARTNERS_CONTRACT.COLUMN_NAME_ID + " = " +
                    PARTNER_POINTS_CONTRACT.TABLE_NAME + "." + PARTNER_POINTS_CONTRACT.COLUMN_NAME_PARTNER_ID +
                " AND " + PARTNERS_CONTRACT.TABLE_NAME + "." +
                    PARTNERS_CONTRACT.COLUMN_NAME_IS_FAVORITE + " = 1" +
                " )";

        List<PartnerPoint> noPartnerPoints = new ArrayList<PartnerPoint>();
        final Holder<List<PartnerPoint>> resultHolder = Holder.from(noPartnerPoints);

        BasePartnersReaderFromDatabase reader = new BasePartnersReaderFromDatabase(context);
        reader.handleCursorByQuery(sql, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                resultHolder.value = PartnerPointsReader.getPartnerPointsFromCursor(cursor);
            }
        });

        return resultHolder.value;
    }
}
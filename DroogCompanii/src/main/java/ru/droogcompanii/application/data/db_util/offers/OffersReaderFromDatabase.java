package ru.droogcompanii.application.data.db_util.offers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import ru.droogcompanii.application.data.db_util.BaseReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.offers.OfferImpl;
import ru.droogcompanii.application.util.CalendarUtils;

/**
 * Created by ls on 11.02.14.
 */
public class OffersReaderFromDatabase extends BaseReaderFromDatabase {

    private int idColumnIndex;
    private int partnerIdColumnIndex;
    private int fromColumnIndex;
    private int toColumnIndex;
    private int shortDescriptionColumnIndex;
    private int fullDescriptionColumnIndex;
    private int imageUrlColumnIndex;

    public OffersReaderFromDatabase(Context context) {
        super(context);
    }

    @Override
    protected SQLiteOpenHelper prepareDbHelper(Context context) {
        return new OffersDbHelper(context);
    }

    public List<Offer> getOffers() {
        return getOffersFromDatabase("");
    }

    public List<Offer> getOffersOf(Partner partner) {
        return getOffersFromDatabase(
                " WHERE " + OffersContract.COLUMN_NAME_PARTNER_ID + " = " + partner.getId()
        );
    }

    private List<Offer> getOffersFromDatabase(String where) {
        initDatabase();

        String sql = "SELECT * FROM " + OffersContract.TABLE_NAME + where + " ;";
        Cursor cursor = db.rawQuery(sql, null);
        List<Offer> offers = getOffersFromCursor(cursor);
        cursor.close();

        closeDatabase();
        return offers;
    }

    private List<Offer> getOffersFromCursor(Cursor cursor) {
        List<Offer> offers = new ArrayList<Offer>();
        calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            offers.add(getOfferFromCursor(cursor));
            cursor.moveToNext();
        }
        return offers;
    }

    private void calculateColumnIndices(Cursor cursor) {
        idColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_NAME_ID);
        partnerIdColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_NAME_PARTNER_ID);
        fromColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_NAME_FROM);
        toColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_NAME_TO);
        shortDescriptionColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_NAME_SHORT_DESCRIPTION);
        fullDescriptionColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_NAME_FULL_DESCRIPTION);
        imageUrlColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_NAME_IMAGE_URL);
    }

    private Offer getOfferFromCursor(Cursor cursor) {
        OfferImpl offer = new OfferImpl();
        offer.id = cursor.getInt(idColumnIndex);
        offer.partnerId = cursor.getInt(partnerIdColumnIndex);
        offer.duration = durationFrom(cursor);
        offer.shortDescription = cursor.getString(shortDescriptionColumnIndex);
        offer.fullDescription = cursor.getString(fullDescriptionColumnIndex);
        offer.imageUrl = cursor.getString(imageUrlColumnIndex);
        return offer;
    }

    private CalendarRange durationFrom(Cursor cursor) {
        long fromInMilliseconds = cursor.getLong(fromColumnIndex);
        long toInMilliseconds = cursor.getLong(toColumnIndex);
        Calendar from = CalendarUtils.createByMilliseconds(fromInMilliseconds);
        Calendar to = CalendarUtils.createByMilliseconds(toInMilliseconds);
        return new CalendarRange(from, to);
    }
}

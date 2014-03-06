package ru.droogcompanii.application.data.db_util.offers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.Calendar;

import ru.droogcompanii.application.data.db_util.BaseReaderFromDatabase;
import ru.droogcompanii.application.data.hierarchy_of_partners.Partner;
import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.offers.OfferImpl;
import ru.droogcompanii.application.data.offers.Offers;
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

    public Offers getOffers() {
        return getOffersFromDatabase("");
    }

    public Offers getOffersOf(Partner partner) {
        return getOffersFromDatabase(
                " WHERE " + OffersContract.COLUMN_NAME_PARTNER_ID + " = " + partner.getId()
        );
    }

    private Offers getOffersFromDatabase(String where) {
        initDatabase();

        Cursor cursor = db.rawQuery(prepareSqlUsedSorting(where), null);
        Offers offers = getOffersFromCursor(cursor);
        cursor.close();

        closeDatabase();
        return offers;
    }

    private static String prepareSql(String where) {
        return "SELECT * FROM " + OffersContract.TABLE_NAME + where + " ;";
    }

    private static String prepareSqlUsedSorting(String where) {
        return "SELECT * FROM " + OffersContract.TABLE_NAME + where +
                " ORDER BY " + OffersContract.COLUMN_NAME_TO + " DESC ;";
    }

    private Offers getOffersFromCursor(Cursor cursor) {
        Offers offers = new Offers();
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
        offer.shortDescription = cursor.getString(shortDescriptionColumnIndex);
        offer.fullDescription = cursor.getString(fullDescriptionColumnIndex);
        offer.imageUrl = cursor.getString(imageUrlColumnIndex);
        setDuration(offer, cursor.getLong(fromColumnIndex), cursor.getLong(toColumnIndex));
        return offer;
    }

    private void setDuration(OfferImpl offer, long fromInMilliseconds, long toInMilliseconds) {
        if (SpecialOffersDBUtils.isDurationOfSpecialOffers(fromInMilliseconds, toInMilliseconds)) {
            return;
        }
        Calendar from = CalendarUtils.createByMilliseconds(fromInMilliseconds);
        Calendar to = CalendarUtils.createByMilliseconds(toInMilliseconds);
        offer.duration = new CalendarRange(from, to);
    }
}

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
import ru.droogcompanii.application.util.Holder;
import ru.droogcompanii.application.util.LogUtils;

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

    public boolean hasOffers(Partner partner) {
        return hasOffers(partner.getId());
    }

    private boolean hasOffers(int partnerId) {
        LogUtils.debug("Partner id:  " + partnerId);

        final Holder<Boolean> hasOffers = Holder.from(false);
        String condition = OffersContract.COLUMN_PARTNER_ID + "=" + partnerId;
        handleCursorByCondition(condition, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                hasOffers.value = cursor.getCount() > 0;
            }
        });
        LogUtils.debug("Has offers ? " + hasOffers.value);
        return hasOffers.value;
    }

    private static interface CursorHandler {
        void handle(Cursor cursor);
    }

    private void handleCursorByCondition(String condition, CursorHandler cursorHandler) {
        initDatabase();
        String where = condition.trim().isEmpty() ? " " : ("WHERE " + condition);
        Cursor cursor = db.rawQuery(prepareSqlUsedSorting(where), null);
        cursorHandler.handle(cursor);
        cursor.close();
        closeDatabase();
    }

    private static String prepareSql(String where) {
        return "SELECT * FROM " + OffersContract.TABLE_NAME + " " + where + " ;";
    }

    private static String prepareSqlUsedSorting(String where) {
        return "SELECT * FROM " + OffersContract.TABLE_NAME + " " + where +
                " ORDER BY " + OffersContract.COLUMN_TO + " DESC ;";
    }

    private void calculateColumnIndices(Cursor cursor) {
        idColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_ID);
        partnerIdColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_PARTNER_ID);
        fromColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_FROM);
        toColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_TO);
        shortDescriptionColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_SHORT_DESCRIPTION);
        fullDescriptionColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_FULL_DESCRIPTION);
        imageUrlColumnIndex = cursor.getColumnIndexOrThrow(OffersContract.COLUMN_IMAGE_URL);
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

    public List<Offer> getOfferList(String condition) {
        final Holder<List<Offer>> result = Holder.absent();
        handleCursorByCondition(condition, new CursorHandler() {
            @Override
            public void handle(Cursor cursor) {
                result.value = getOfferListFromCursor(cursor);
            }
        });
        return result.value;
    }

    private List<Offer> getOfferListFromCursor(Cursor cursor) {
        List<Offer> offerList = new ArrayList<Offer>();
        calculateColumnIndices(cursor);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            offerList.add(getOfferFromCursor(cursor));
            cursor.moveToNext();
        }
        return offerList;
    }
}

package ru.droogcompanii.application.data.db_util.offers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import ru.droogcompanii.application.data.offers.CalendarRange;
import ru.droogcompanii.application.data.offers.Offer;
import ru.droogcompanii.application.data.offers.Offers;

/**
 * Created by ls on 11.02.14.
 */
public class OffersWriterToDatabase {
    private final Context context;
    private SQLiteDatabase db;

    public OffersWriterToDatabase(Context context) {
        this.context = context;
    }

    public void write(Offers offers) {
        OffersDbHelper dbHelper = new OffersDbHelper(context);
        db = dbHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            tryExecuteTransaction(offers);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        db.close();
        db = null;
        dbHelper.close();
    }

    private void tryExecuteTransaction(Offers offers) {
        clearOldData();
        writeOffers(offers.getAllOffers());
    }

    private void clearOldData() {
        db.delete(OffersContract.TABLE_NAME, null, null);
    }

    private void writeOffers(List<Offer> offers) {
        for (Offer each : offers) {
            writeOffer(each);
        }
    }

    private void writeOffer(Offer offer) {
        final String COMMA = ", ";
        String sql = "INSERT INTO " + OffersContract.TABLE_NAME + " (" +
                OffersContract.COLUMN_NAME_ID + COMMA +
                OffersContract.COLUMN_NAME_PARTNER_ID + COMMA +
                OffersContract.COLUMN_NAME_FROM + COMMA +
                OffersContract.COLUMN_NAME_TO + COMMA +
                OffersContract.COLUMN_NAME_SHORT_DESCRIPTION + COMMA +
                OffersContract.COLUMN_NAME_FULL_DESCRIPTION + COMMA +
                OffersContract.COLUMN_NAME_IMAGE_URL +
                ") VALUES(?,?,?,?,?,?,?)";
        SQLiteStatement insertStatement = db.compileStatement(sql);
        insertStatement.clearBindings();
        insertStatement.bindLong(1, offer.getId());
        insertStatement.bindLong(2, offer.getPartnerId());
        insertDuration(insertStatement, offer, 3, 4);
        insertStatement.bindString(5, offer.getShortDescription());
        insertStatement.bindString(6, offer.getFullDescription());
        insertStatement.bindString(7, offer.getImageUrl());
        insertStatement.executeInsert();
    }

    private void insertDuration(SQLiteStatement insertStatement, Offer offer, int indexOfFromColumn, int indexOfToColumn) {
        long from = SpecialOffersDBUtils.getFrom();
        long to = SpecialOffersDBUtils.getTo();
        if (!offer.isSpecial()) {
            CalendarRange duration = offer.getDuration();
            from = duration.from().getTimeInMillis();
            to = duration.to().getTimeInMillis();
        }
        insertStatement.bindLong(indexOfFromColumn, from);
        insertStatement.bindLong(indexOfToColumn, to);
    }


}

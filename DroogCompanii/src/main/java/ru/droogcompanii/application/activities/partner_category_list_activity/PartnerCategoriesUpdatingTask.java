package ru.droogcompanii.application.activities.partner_category_list_activity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.PowerManager;
import android.util.Log;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.DataUpdater;
import ru.droogcompanii.application.util.DataUrlProvider;
import ru.droogcompanii.application.util.LogTagProvider;

/**
 * Created by Leonid on 03.12.13.
 */

class PartnerCategoriesUpdatingTask extends AsyncTask<Void, Void, Void> {
    private final Context context;
    private final DataUpdater dataUpdater;
    private final OnDataUpdatingProgressListener listener;

    public PartnerCategoriesUpdatingTask(Context context, OnDataUpdatingProgressListener listener) {
        this.context = context;
        this.listener = listener;
        this.dataUpdater = new DataUpdater(context, prepareXmlDownloaderFromResources(context));
    }

    private DataUpdater.XmlProvider prepareXmlDownloaderFromResources(final Context context) {
        return new DataUpdater.XmlProvider() {
            @Override
            public InputStream getXml() throws Exception {
                return context.getResources().openRawResource(R.raw.test_data);
            }
        };
    }

    @SuppressWarnings("UnusedDeclaration")
    private DataUpdater.XmlProvider prepareXmlDownloaderFromInternet() {
        return new DataUpdater.XmlProvider() {
            @Override
            public InputStream getXml() throws Exception {
                URL url = new URL(DataUrlProvider.getDataUrl());
                URLConnection connection = url.openConnection();
                return connection.getInputStream();
            }
        };
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPreDataUpdating();
    }

    @Override
    protected Void doInBackground(Void... params) {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.acquire();
        try {
            dataUpdater.update();
        } catch (Exception e) {
            processUpdatingException(e);
        } finally {
            wakeLock.release();
        }
        return null;
    }

    private void processUpdatingException(Exception e) {
        StackTraceElement[] stackTrace = e.getStackTrace();
        for (StackTraceElement each : stackTrace) {
            Log.d(LogTagProvider.get(), each.toString());
        }
    }

    @Override
    protected void onPostExecute(Void result) {
        super.onPostExecute(result);
        listener.onPostDataUpdating();
    }
}

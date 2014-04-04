package ru.droogcompanii.application.ui.screens.synchronization;

import android.content.Context;
import android.content.res.Resources;
import android.os.PowerManager;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.data.DataUpdater;
import ru.droogcompanii.application.util.InputStreamConverter;
import ru.droogcompanii.application.util.constants.DataUrlProvider;
import ru.droogcompanii.application.util.LogUtils;

/**
 * Created by ls on 17.02.14.
 */
public class SynchronizationWorker {
    private final Context context;
    private final DataUpdater dataUpdater;

    public SynchronizationWorker(Context context) {
        this.context = context;
        this.dataUpdater = new DataUpdater(context, prepareXmlProviderFromResources());
    }

    private DataUpdater.XmlProvider prepareXmlProviderFromResources() {
        return new DataUpdater.XmlProvider() {
            @Override
            public InputStream getXml() throws Exception {
                Resources resources = context.getResources();
                return resources.openRawResource(R.raw.test_data);
            }
        };
    }

    @SuppressWarnings("UnusedDeclaration")
    private DataUpdater.XmlProvider prepareXmlProviderFromInternet() {
        return new DataUpdater.XmlProvider() {
            @Override
            public InputStream getXml() throws Exception {
                URL url = new URL(DataUrlProvider.getDataUrl());
                URLConnection connection = url.openConnection();
                InputStream connectionInputStream = connection.getInputStream();
                String input = InputStreamConverter.inputStreamToString(connectionInputStream);
                return InputStreamConverter.stringToInputStream(input);
            }
        };
    }

    public boolean doWork() {
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getClass().getName());
        wakeLock.acquire();
        boolean successful;
        try {
            dataUpdater.update();
            successful = true;
        } catch (Exception e) {
            LogUtils.exception(e);
            successful = false;
        } finally {
            wakeLock.release();
        }
        return successful;
    }
}

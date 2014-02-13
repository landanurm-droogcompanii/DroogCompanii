package ru.droogcompanii.application.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by ls on 13.02.14.
 */
public class NavigationToWebsiteHelper {
    private final Context context;

    public NavigationToWebsiteHelper(Context context) {
        this.context = context;
    }

    public void navigateToSite(String webSite) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webSite));
        context.startActivity(intent);
    }
}

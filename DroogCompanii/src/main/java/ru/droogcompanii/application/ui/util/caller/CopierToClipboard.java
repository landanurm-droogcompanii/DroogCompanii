package ru.droogcompanii.application.ui.util.caller;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

import ru.droogcompanii.application.R;
import ru.droogcompanii.application.ui.util.ApiVersionUtils;

/**
 * Created by ls on 17.02.14.
 */
public class CopierToClipboard {
    private final Context context;
    private final Toast toastOnCopy;

    public CopierToClipboard(Context context) {
        this(context, R.string.labelOnCopiedText);
    }

    public CopierToClipboard(Context context, int idOfMessageTextCopied) {
        this.context = context;
        toastOnCopy = Toast.makeText(context, idOfMessageTextCopied, Toast.LENGTH_SHORT);
    }

    public void copyToClipboard(String text) {
        String defaultLabel = context.getString(R.string.labelOnCopiedText);
        copyToClipboard(defaultLabel, text);
    }

    public void copyToClipboard(String label, String text) {
        if (isOldApiVersion()) {
            copyToClipboardOnOldVersion(text);
        } else {
            copyToClipboardOnNewVersion(label, text);
        }
        toastOnCopy.show();
    }

    private boolean isOldApiVersion() {
        return ApiVersionUtils.isCurrentVersionLowerThan(android.os.Build.VERSION_CODES.HONEYCOMB);
    }

    @SuppressLint("NewApi")
    private void copyToClipboardOnNewVersion(String label, String text) {
        android.content.ClipboardManager clipboard =
                (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        android.content.ClipData clip = android.content.ClipData.newPlainText(label, text);
        clipboard.setPrimaryClip(clip);
    }

    @SuppressWarnings("deprecation")
    private void copyToClipboardOnOldVersion(String text) {
        android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setText(text);
    }

}

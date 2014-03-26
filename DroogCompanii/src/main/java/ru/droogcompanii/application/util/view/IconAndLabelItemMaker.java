package ru.droogcompanii.application.util.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import ru.droogcompanii.application.R;

/**
 * Created by ls on 14.02.14.
 */
public class IconAndLabelItemMaker {
    private final LayoutInflater layoutInflater;

    public IconAndLabelItemMaker(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public View make(int iconId, String text, final Runnable runnableOnClick) {
        View view = layoutInflater.inflate(R.layout.view_icon_and_label_item, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
        imageView.setImageResource(iconId);
        TextView textView = (TextView) view.findViewById(R.id.textView);
        textView.setText(text);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runnableOnClick.run();
            }
        });
        return view;
    }
}

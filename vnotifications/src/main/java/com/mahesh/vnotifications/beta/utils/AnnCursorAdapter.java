package com.mahesh.vnotifications.beta.utils;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.widget.ResourceCursorAdapter;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.mahesh.vnotifications.beta.R;


/**
 * Created by Mahesh on 3/17/14.
 */
public class AnnCursorAdapter extends ResourceCursorAdapter {
    int level = 9, seen = 0;

    public AnnCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView Title = (TextView) view.findViewById(R.id.textViewTitle);
        TextView PostedBy = (TextView) view.findViewById(R.id.textViewBy);
        TextView Time = (TextView) view.findViewById(R.id.textViewTime);
        TextView Tag = (TextView) view.findViewById(R.id.textViewTag);
        level = Integer.parseInt(cursor.getString(cursor.getColumnIndex("level")));
        seen = cursor.getInt(cursor.getColumnIndex("seen"));
        if (seen == 1) {
            Title.setTypeface(null, Typeface.BOLD_ITALIC);
        }

        if (level == 0) {
            Tag.setBackgroundColor(Color.RED);
            PostedBy.setTextColor(Color.RED);
        } else if (level == 1) {
            Tag.setBackgroundColor(Color.BLUE);
            PostedBy.setTextColor(Color.BLUE);
        } else if (level == 2) {
            Tag.setBackgroundColor(Color.GRAY);
            PostedBy.setTextColor(Color.GRAY);
        } else if (level == 3) {
            Tag.setBackgroundColor(Color.MAGENTA);
            PostedBy.setTextColor(Color.MAGENTA);
        } else {

        }
        Title.setText(cursor.getString(cursor.getColumnIndex("title")));
        PostedBy.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("postedby"))));
        Time.setText(cursor.getString(cursor.getColumnIndex("timestamp")));
        Tag.setText(cursor.getString(cursor.getColumnIndexOrThrow("tag")));
    }
}

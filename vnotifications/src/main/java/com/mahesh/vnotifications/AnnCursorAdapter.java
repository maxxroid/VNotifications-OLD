package com.mahesh.vnotifications;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.ResourceCursorAdapter;
import android.text.Html;
import android.view.View;
import android.widget.TextView;



/**
 * Created by Mahesh on 3/17/14.
 */
public class AnnCursorAdapter  extends ResourceCursorAdapter {
    int level=9;

    public AnnCursorAdapter(Context context, int layout, Cursor c, int flags) {
        super(context, layout, c, flags);
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView Title = (TextView) view.findViewById(R.id.textViewTitle);
        TextView Message = (TextView) view.findViewById(R.id.textViewMessage);
        TextView PostedBy = (TextView) view.findViewById(R.id.textViewBy);
        TextView Time = (TextView) view.findViewById(R.id.textViewTime);
        level=Integer.parseInt(cursor.getString(cursor.getColumnIndex("level")));

        if(level==0) {
            Title.setTextColor(Color.RED);
            PostedBy.setTextColor(Color.RED);
            Title.setText(cursor.getString(cursor.getColumnIndex("title")));
            Message.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("message"))));
            PostedBy.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("postedby"))));
            Time.setText(cursor.getString(cursor.getColumnIndex("timestamp")));
        }
        else if(level==1) {
            Title.setTextColor(Color.BLUE);
            PostedBy.setTextColor(Color.BLUE);
            Title.setText(cursor.getString(cursor.getColumnIndex("title")));
            Message.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("message"))));
            PostedBy.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("postedby"))));
            Time.setText(cursor.getString(cursor.getColumnIndex("timestamp")));
        }
        else if(level==2) {
            Title.setTextColor(Color.GREEN);
            PostedBy.setTextColor(Color.GREEN);
            Title.setText(cursor.getString(cursor.getColumnIndex("title")));
            Message.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("message"))));
            PostedBy.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("postedby"))));
            Time.setText(cursor.getString(cursor.getColumnIndex("timestamp")));
        }
        else if(level==3) {
            Title.setTextColor(Color.YELLOW);
            PostedBy.setTextColor(Color.YELLOW);
            Title.setText(cursor.getString(cursor.getColumnIndex("title")));
            Message.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("message"))));
            PostedBy.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("postedby"))));
            Time.setText(cursor.getString(cursor.getColumnIndex("timestamp")));
        }
        else
        {
            Title.setText(cursor.getString(cursor.getColumnIndex("title")));
            Message.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("message"))));
            PostedBy.setText(Html.fromHtml(cursor.getString(cursor.getColumnIndex("postedby"))));
            Time.setText(cursor.getString(cursor.getColumnIndex("timestamp")));
        }
    }
}

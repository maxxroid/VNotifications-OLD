package com.mahesh.vnotifications.beta;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahesh.vnotifications.beta.utils.AnnCursorAdapter;
import com.mahesh.vnotifications.beta.utils.AnnouncementObject;
import com.mahesh.vnotifications.beta.utils.DBAdapter;

/**
 * Created by Mahesh on 3/11/14.
 */
public class MainListView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private static String HEADER_BAR;
    private Activity parent;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    DBAdapter myDb;

    public static MainListView newInstance(int sectionNumber) {
        MainListView fragment = new MainListView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public MainListView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.announcement_homeview, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        parent = activity;
        HEADER_BAR = getString(R.string.title_section1);
        ((HomeActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView section= (TextView) getActivity().findViewById(R.id.textViewSection);
        section.setText("Latest Announcements:");
        openDB();
        populateListViewFromDB();
        registerListClickCallback();
    }
    public void onResume() {
        super.onResume();
        ((HomeActivity) parent).setActionBarTitle(HEADER_BAR);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    private void openDB() {
        myDb = new DBAdapter(getActivity());
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    private void populateListViewFromDB() {
        Cursor cursor = myDb.getAllRows();
        // Set the adapter for the list view
        AnnCursorAdapter testc=new AnnCursorAdapter(getActivity(),R.layout.notice_item,cursor,0);
        ListView myList = (ListView) getActivity().findViewById(R.id.listView);
        myList.setAdapter(testc);
    }

    private void registerListClickCallback() {
        ListView myList = (ListView) getActivity().findViewById(R.id.listView);
        myList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked,
                                    int position, long idInDB) {
                displayViewForId(idInDB);
            }
        });
    }

    private void displayViewForId(long idInDB) {
        Cursor cursor = myDb.getRow(idInDB);
        if (cursor.moveToFirst()) {
            long idDB = cursor.getLong(DBAdapter.COL_ROWID);
            long id= cursor.getLong(DBAdapter.COL_ID);
            String Title = cursor.getString(DBAdapter.COL_TITLE);
            String Message = cursor.getString(DBAdapter.COL_MESSAGE);
            String Timestamp = cursor.getString(DBAdapter.COL_TIMESTAMP);
            String Tag = cursor.getString(DBAdapter.COL_TAG);
            String Postedby = cursor.getString(DBAdapter.COL_POSTEDBY);
            String Level = cursor.getString(DBAdapter.COL_LEVEL);
            AnnouncementObject aob=new AnnouncementObject(idDB,id,Title,Message, Timestamp,Tag,Level,Postedby);

        }
        cursor.close();
    }
}

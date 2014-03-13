package com.mahesh.vnotifications;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.mahesh.vnotifications.utils.DBAdapter;

/**
 * Created by Mahesh on 3/11/14.
 */
public class SampleListView extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    DBAdapter myDb;

    public static SampleListView newInstance(int sectionNumber) {
        SampleListView fragment = new SampleListView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public SampleListView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.display_listview, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((HomeActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        openDB();
        populateListViewFromDB();
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

        // Allow activity to manage lifetime of the cursor.
        // DEPRECATED! Runs on the UI thread, OK for small/short queries.
        //startManagingCursor(cursor);

        // Setup mapping from cursor to view fields:
        String[] fromFieldNames = new String[]
                {DBAdapter.KEY_TITLE, DBAdapter.KEY_MESSAGE, DBAdapter.KEY_TIMESTAMP, DBAdapter.KEY_POSTEDBY};
        int[] toViewIDs = new int[]
                {R.id.textViewTitle, R.id.textViewMessage, R.id.textViewTime, R.id.textViewBy};

        // Create adapter to may columns of the DB onto elemesnt in the UI.
        SimpleCursorAdapter myCursorAdapter =
                new SimpleCursorAdapter(
                        getActivity(),        // Context
                        R.layout.notice_item,    // Row layout template
                        cursor,                    // cursor (set of DB records to map)
                        fromFieldNames,            // DB Column names
                        toViewIDs                // View IDs to put information in
                );

        // Set the adapter for the list view
        ListView myList = (ListView) getActivity().findViewById(R.id.listView);
        myList.setAdapter(myCursorAdapter);


    }
}

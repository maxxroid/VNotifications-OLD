package com.mahesh.vnotifications.beta;

/**
 * Created by Mahesh on 3/17/14.
 */

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mahesh.vnotifications.beta.utils.AnnCursorAdapter;
import com.mahesh.vnotifications.beta.utils.DBAdapter;


/**
 * Created by Mahesh on 3/17/14.
 */
public class Level2ListView extends Fragment {
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

    public static Level2ListView newInstance(int sectionNumber) {
        Level2ListView fragment = new Level2ListView();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public Level2ListView() {
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
        HEADER_BAR = getString(R.string.title_section4);
        ((HomeActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView section = (TextView) getActivity().findViewById(R.id.textViewSection);
        section.setText("Latest Announcements from CR:");
        openDB();
        populateListViewFromDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        closeDB();
    }

    public void onResume() {
        super.onResume();
        ((HomeActivity) parent).setActionBarTitle(HEADER_BAR);
    }

    private void openDB() {
        myDb = new DBAdapter(getActivity());
        myDb.open();
    }

    private void closeDB() {
        myDb.close();
    }

    private void populateListViewFromDB() {
        Cursor cursor = myDb.getLevelRows("2");
        // Set the adapter for the list view
        AnnCursorAdapter testc = new AnnCursorAdapter(getActivity(), R.layout.notice_item, cursor, 0);
        ListView myList = (ListView) getActivity().findViewById(R.id.listView);
        myList.setAdapter(testc);


    }
}


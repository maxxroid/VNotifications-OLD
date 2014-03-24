package com.mahesh.vnotifications;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.mahesh.vnotifications.utils.AnnCursorAdapter;
import com.mahesh.vnotifications.utils.DBAdapter;

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
}

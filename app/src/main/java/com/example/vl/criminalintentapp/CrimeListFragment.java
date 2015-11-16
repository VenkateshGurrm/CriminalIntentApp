package com.example.vl.criminalintentapp;


import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vl on 11/12/2015.
 *///
public class CrimeListFragment extends ListFragment {
    private ArrayList<Crime> mCrimes;
    private static final String TAG = "CrimeListFragment";
    private boolean mSubtitleVisible;
    private CrimeAdapter<Crime> arrayAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        getActivity().setTitle(R.string.crimes_title);

        mCrimes = CrimeLab.getInstance(getActivity()).getmCrimes();
        Log.d("Venky", mCrimes.toString());
         arrayAdapter = new CrimeAdapter( mCrimes);
        /*CrimeAdapter<Crime> arrayAdapter = new CrimeAdapter( mCrimes);
        setListAdapter(arrayAdapter);
        */
        setRetainInstance(true);
        mSubtitleVisible =false;

    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.crime_list, parent, false);

        ListView view = (ListView) v.findViewById(android.R.id.list);
        view.setEmptyView(v.findViewById(android.R.id.empty) );
        setListAdapter(arrayAdapter);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (mSubtitleVisible) {
                AppCompatActivity appCompatActivity = ((AppCompatActivity)getActivity());
                appCompatActivity.getSupportActionBar().setSubtitle(R.string.subtitle);
            }
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            // Use floating context menus on Froyo and Gingerbread
            registerForContextMenu(view);
        } else {
            Log.d("Venky", "dfkdf");
            // Use contextual action bar on Honeycomb and higher
            view.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            view.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                @Override
                public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                }

                @Override
                public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                   // MenuInflater menuInflater = mode.getMenuInflater();
                    MenuInflater menuInflater = getActivity().getMenuInflater();
                    //toolbar.setVisibility(View.INVISIBLE);

                    Log.d("Venky1", menuInflater.toString());
                    menuInflater.inflate(R.menu.crime_list_item_context, menu);


                    return true;
                }

                @Override
                public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                    return false;
                }

                @Override
                public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

                    switch (item.getItemId()){
                        case R.id.menu_item_delete_crime:
                            CrimeAdapter adapter = (CrimeAdapter) getListAdapter();
                            CrimeLab cLab = CrimeLab.getInstance(getActivity());

                            for(int i=   adapter.getCount()-1;i>=0;i--){

                                if(getListView().isItemChecked(i)){
                                    cLab.deleteCrime((Crime)adapter.getItem(i));
                                }
                            }

                           mode.finish();
                            adapter.notifyDataSetChanged();
                            return true;
                        default:
                            return false;
                    }
                }

                @Override
                public void onDestroyActionMode(ActionMode mode) {

                }
            });
        }


        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);
        MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible && showSubtitle != null) {
            showSubtitle.setTitle(R.string.hide_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId() ){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);
                Intent i = new Intent(getActivity(), CrimePagerActivity.class);
                i.putExtra(CrimeFragment.CRIME_ID, crime.getmId());
               // startActivityForResult(i, 0);
                startActivity(i);
               return true;

            case R.id.menu_item_show_subtitle:

                AppCompatActivity appCompatActivity = ((AppCompatActivity)getActivity());

                if (appCompatActivity.getSupportActionBar().getSubtitle() == null){
                    appCompatActivity.getSupportActionBar().setSubtitle(R.string.subtitle);
                    mSubtitleVisible = true;
                    item.setTitle(R.string.hide_subtitle);
                }else{
                    appCompatActivity.getSupportActionBar().setSubtitle(null);
                    mSubtitleVisible = false;
                    item.setTitle(R.string.show_subtitle);
                }

                return true;

            default:
               return super.onOptionsItemSelected(item);
        }

    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Crime c = (Crime) (getListAdapter()).getItem(position);
        //Log.d(TAG, c.getmTitle() + " was clicked");
        //Intent intent = new Intent(getContext(), CriminalActivity.class);
        Intent intent = new Intent(getActivity(), CrimePagerActivity.class);
        intent.putExtra(CrimeFragment.CRIME_ID, c.getmId());
        startActivity(intent);
    }

    private class CrimeAdapter<Crime> extends ArrayAdapter<Crime>{

        public CrimeAdapter(ArrayList<Crime> crimes) {
            super(getActivity(), 0, crimes);
        }

        public View getView(int position, View convertView, ViewGroup parent){

            if(convertView == null){
                convertView = getActivity().getLayoutInflater().inflate(R.layout.simple_list_item_2,null);
            }

            com.example.vl.criminalintentapp.Crime c = (com.example.vl.criminalintentapp.Crime)(getListAdapter()).getItem(position);

            TextView titleTextView =
                    ((TextView) convertView.findViewById(R.id.crime_list_item_titleTextView));

            titleTextView.setText(c.getmTitle());


            TextView dateTextView =
                    (TextView)convertView.findViewById(R.id.crime_list_item_dateTextView);
            dateTextView.setText(c.getmDate().toString());


            CheckBox solvedCheckBox =
                    (CheckBox)convertView.findViewById(R.id.crime_list_item_solvedCheckBox);
            solvedCheckBox.setChecked(c.ismSolved());

            return convertView;

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo c){
        getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

        int position = info.position;

        Crime crime =  ((CrimeAdapter<Crime>)getListAdapter()).getItem(position);

        switch (item.getItemId()) {
            case R.id.menu_item_delete_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime(crime);
                 ((CrimeAdapter<Crime>)getListAdapter()).notifyDataSetChanged();
                return true;
        }
         return super.onContextItemSelected(item);

    }
}

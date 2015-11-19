package com.example.vl.criminalintentapp;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by vl on 11/11/2015.
 */
public class CrimeFragment extends Fragment {
    private static final int REQUEST_CODE_CAMERA = 2;
    private static final String TAG = "CrimeFragment";
    private static final String DIALOG_IMAGE = "image";
    private static final int REQUEST_CONTACT = 3;

    private Crime mCrime;
    private EditText mTitleField;
    private Button mDateButton;
    private Button mTimeButton;
    private CheckBox mSolvedCheckBox;
    public  static String CRIME_ID = "crimeId";
    private static final String DIALOG_DATE = "date";
    private static final String DIALOG_TIME = "time";
    private ImageButton cameraButton;
    private ImageView mPhotoView;
    private Button mSuspectButton;

    private Callbacks mCallbacks;

    private static final int REQUEST_DATE = 0;
    private static final int REQUEST_TIME = 1;

    public interface Callbacks{
        void onCrimeUpdated(Crime crime);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallbacks = (Callbacks)activity;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);
        CrimeFragment fragment = new CrimeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (NavUtils.getParentActivityName(getActivity()) != null) {
                    NavUtils.navigateUpFromSameTask(getActivity());
                }
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        UUID crimeId = (UUID)getArguments().getSerializable(CRIME_ID);
        mCrime = CrimeLab.getInstance(getActivity()).getCrime(crimeId);
    }
    @TargetApi(11)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_crime, container, false);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(R.string.crimes_titl);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (NavUtils.getParentActivityName(getActivity()) != null){
                ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
        }


        mTitleField = (EditText) view.findViewById(R.id.crime_title);
        mTitleField.setText(mCrime.getmTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mCrime.setmTitle(s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mDateButton = (Button) view.findViewById(R.id.crime_date);
        // mDateButton
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        String value1 = dateFormat.format(mCrime.getmDate());
        mDateButton.setText(value1);
        mDateButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
               // DatePickerFragment datePickerFragment = new DatePickerFragment();
                DatePickerFragment datePickerFragment = DatePickerFragment.newInstance(mCrime.getmDate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });
        //mDateButton.setEnabled(false);


        mTimeButton = (Button) view.findViewById(R.id.crime_time);
        SimpleDateFormat tim  = new SimpleDateFormat("hh : mm");
        mTimeButton.setText(tim.format(mCrime.getmDate()));
        mTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                // TimePickerFragment fragment = new TimePickerFragment();
                TimePickerFragment fragment = TimePickerFragment.newInstance(mCrime.getmDate());
                fragment.setTargetFragment(CrimeFragment.this, REQUEST_TIME);
                fragment.show(fm, DIALOG_TIME);
            }
        });

        mSolvedCheckBox = (CheckBox) view.findViewById(R.id.crime_solved);
        mSolvedCheckBox.setChecked(mCrime.ismSolved());
        mSolvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                mCrime.setmSolved(isChecked);
            }
        });


        cameraButton = (ImageButton) view.findViewById(R.id.crime_imageButton);
        cameraButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CrimeCameraActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);

            }
        });


        // If camera is not available, disable camera functionality
        PackageManager pm = getActivity().getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) &&
                !pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))
            cameraButton.setEnabled(false);



        mPhotoView = (ImageView) view.findViewById(R.id.crime_imageView);
        showPhoto();
        mPhotoView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Photo p = mCrime.getPhoto();

                if (p != null) {
                    FragmentManager fm = getActivity().getSupportFragmentManager();

                    String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();

                    ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
                }
            }
        });


        Button reportButton = (Button)view.findViewById(R.id.crime_reportButton);
        reportButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReports());
                i.putExtra(Intent.EXTRA_SUBJECT,
                        getString(R.string.crime_report_subject));
                i= Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });


        mSuspectButton = (Button) view.findViewById(R.id.crime_suspectButton);
        mSuspectButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(i, REQUEST_CONTACT);
            }
        });

        if (mCrime.getSuspect() != null) {
            mSuspectButton.setText(mCrime.getSuspect());
        }

        return view;

    }

    @Override
    public void onStop() {
        super.onStop();
        PictureUtils.cleanImageView(mPhotoView);
    }


    private void showPhoto() {
     // (Re)set the image button's image based on our photo
        Photo p = mCrime.getPhoto();
        BitmapDrawable b = null;
        if (p != null) {
            String path = getActivity()
                    .getFileStreamPath(p.getFilename()).getAbsolutePath();
            b = PictureUtils.getScaledDrawable(getActivity(), path);
        }
        mPhotoView.setImageDrawable(b);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Venky", "On Attach Fired");

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Venky", "On onStart Fired");

        showPhoto();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK) return;
        if (requestCode == REQUEST_DATE) {
            Log.d("Venky", "DATE");
            Date date = (Date)data
                    .getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            mCrime.setmDate(date);
            mDateButton.setText(mCrime.getmDate().toString());
        }else if(requestCode == REQUEST_TIME){

            Log.d("Venky", "TIMEM");
            Date date = (Date)data
                    .getSerializableExtra(TimePickerFragment.TIME);
            mCrime.setmDate(date);
            mTimeButton.setText(mCrime.getmDate().toString());
        }else if(requestCode == REQUEST_CODE_CAMERA){
            String filename = data
                    .getStringExtra(CrimeCameraFragment.FILENAME);
            if (filename != null) {
                Log.i(TAG, "filename: " + filename);

                Photo p = new Photo(filename);
                mCrime.setPhoto(p);

                Log.d(TAG, "Crime: " + mCrime.getmTitle() + " has a photo");
            }
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        CrimeLab.getInstance(getActivity()).saveCrimes();
    }

    private String getCrimeReports(){
        String sovledString = null;

        if(mCrime.ismSolved()){
            sovledString = getString(R.string.crime_report);
        }else{
            sovledString = getString(R.string.crime_report_unsolved);
        }


        String dateFormat = "EEE, MMM dd";
        String dateString = DateFormat.format(dateFormat, mCrime.getmDate())+"";

        String suspect = mCrime.getSuspect();;
        if(suspect == null){
            suspect = getString(R.string.crime_report_no_suspect);
        }else {
            suspect = getString(R.string.crime_report_suspect, suspect);
        }

        String report =     getString(R.string.crime_report,
                mCrime.getmTitle() , dateString, sovledString, suspect);

        return report;

    }


}

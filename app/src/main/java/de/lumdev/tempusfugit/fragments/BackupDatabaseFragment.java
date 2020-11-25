package de.lumdev.tempusfugit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.threeten.bp.Instant;
import org.threeten.bp.OffsetDateTime;
import org.threeten.bp.ZoneOffset;
import org.threeten.bp.format.DateTimeFormatter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.fragment.NavHostFragment;
import de.lumdev.tempusfugit.MainViewModel;
import de.lumdev.tempusfugit.R;

public class BackupDatabaseFragment extends Fragment implements OnBackPressedCallback {

        private MainViewModel viewModel;
        private Toolbar toolbar;
        private TextView backup_tV;
        private Button backup_btn;
        private View.OnClickListener doBackupOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createFile(null);
//                backup_tV.setText(viewModel.getDatabaseAsJson());
            }
        };

        public BackupDatabaseFragment() {
            // Required empty public constructor
        }

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         * @return A new instance of fragment OverviewGroupEventFragment.
         */
        public static de.lumdev.tempusfugit.fragments.BackupDatabaseFragment newInstance() {
            de.lumdev.tempusfugit.fragments.BackupDatabaseFragment fragment = new de.lumdev.tempusfugit.fragments.BackupDatabaseFragment();
            return fragment;
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            final View rootView = inflater.inflate(R.layout.fragment_backup_database, container, false);

            //get Views
            toolbar = getActivity().findViewById(R.id.toolbar_main);
            toolbar.setTitle(R.string.toolbar_label_backup_database_event);
            toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            toolbar.setNavigationContentDescription(R.string.toolbar_settings_navigation_description);
            toolbar.setNavigationOnClickListener((View v) -> {
                NavHostFragment.findNavController(this).navigateUp();
                toolbar.setNavigationIcon(null); //disbale icon in toolbar again
            });

            backup_tV = rootView.findViewById(R.id.tV_label_backup);
            backup_btn = rootView.findViewById(R.id.btn_do_backup);
            backup_btn.setOnClickListener(doBackupOnClickListener);

            // Inflate the layout for this fragment
            return rootView;
        }

        // Request code for creating a PDF document.
        private static final int CREATE_FILE = 1;

        private void createFile(Uri pickerInitialUri) {
            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            // complete list of mime type: https://developer.mozilla.org/en-US/docs/Web/HTTP/Basics_of_HTTP/MIME_types/Common_types
//            intent.setType("application/json");
            intent.setType("text/html");
            String date_now = OffsetDateTime.ofInstant(Instant.now(), ZoneOffset.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_DATE);
            intent.putExtra(Intent.EXTRA_TITLE, date_now+"_backup_tempus_fugit.html");
            // Optionally, specify a URI for the directory that should be opened in
            // the system file picker when your app creates the document.
//            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri);
            startActivityForResult(intent, CREATE_FILE);
        }
        //code snippets from https://developer.android.com/training/data-storage/shared/documents-files
        @Override
        public void onActivityResult(int requestCode, int resultCode,
                                     Intent resultData) {
            if (requestCode == CREATE_FILE
                    && resultCode == Activity.RESULT_OK) {
                // The result data contains a URI for the document or directory that
                // the user selected.
                Uri uri = null;
                if (resultData != null) {
                    uri = resultData.getData();
                    // Perform operations on the document using its URI.
                    writeDocument(uri, viewModel.getDatabaseAsJson());
                }
            }
        }
        private void writeDocument(Uri uri, String stuff_to_write) {
            try {
                ParcelFileDescriptor pfd = getActivity().getContentResolver().
                        openFileDescriptor(uri, "w");
                FileOutputStream fileOutputStream =
                        new FileOutputStream(pfd.getFileDescriptor());
    //                fileOutputStream.write(("Overwritten at " + System.currentTimeMillis() +
    //                        "\n").getBytes());
                fileOutputStream.write((stuff_to_write).getBytes());
                // Let the document provider know you're done by closing the stream.
                fileOutputStream.close();
                pfd.close();
                //output info for successful creation
                backup_tV.setText(getString(R.string.tV_label_backup_successful));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                //output info for error during creation
                backup_tV.setText(getString(R.string.tV_label_backup_not_successful));
            }
        }




    // used for handling back press (on physical/ soft button); see: https://stackoverflow.com/questions/51043428/handling-back-button-in-android-navigation-component
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            getActivity().addOnBackPressedCallback(getViewLifecycleOwner(),this);
        }
        @Override
        public boolean handleOnBackPressed() {
            //Do your job here
            //use next line if you just need navigate up
            NavHostFragment.findNavController(this).navigateUp();
            toolbar.setNavigationIcon(null);
            //Log.e(getClass().getSimpleName(), "handleOnBackPressed");
            return true;
        }
        @Override
        public void onDestroyView() {
            super.onDestroyView();
            getActivity().removeOnBackPressedCallback(this);
        }
}

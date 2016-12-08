package com.firebasestorageone.interfaces;

import com.google.firebase.database.DataSnapshot;

import static android.R.attr.path;

/**
 * Created by hiren.patel on 01-12-2016.
 */

public class FirebaseListeners {

    public interface OnFirebaseReadListener {

        void onReadSuccess(int requestCode, DataSnapshot dataSnapshot);
        void onReadFail(int requestCode);
        void onReadCancel(int requestCode);
    }

    public interface OnFirebaseWriteListener {

        void onWriteSuccess(int requestCode);
        void onWriteFail(int requestCode);
    }

    public interface OnFirebaseUpdateListener{

        void onUpdateSuccess(int requestCode);
    }


    public interface OnFirebaseDeleteListener {

        void onDeleteSuccess(int requestCode);
    }

    public interface OnFirebaseFileDownloadListener{

        void onDownloadSuccess(int requestCode, String path);
        void onDownloadFailed(int requestCode, String exception);
    }

    public interface OnFirebaseFileUploadListener{

        void onUploadSuccess(int requestCode, String path);
        void onUploadFailed(int requestCode, String exception);
    }
}

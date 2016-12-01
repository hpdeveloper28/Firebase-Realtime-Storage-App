package com.firebasestorageone.utils;

import com.firebasestorageone.interfaces.FirebaseListeners;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * Created by hiren.patel on 30-11-2016.
 */

public class FirebaseUtils {

    private static final String TAG = "FirebaseUtils";
    private static FirebaseUtils firebaseUtils;
    private static FirebaseDatabase firebaseDatabase;
    private static DatabaseReference databaseReference;
    private MyValueListener myValueListener;

    public static FirebaseUtils getInstance() {
        if (firebaseUtils == null) {
            init();
        }
        return firebaseUtils;
    }

    public static void init() {
        firebaseUtils = new FirebaseUtils();
        firebaseDatabase = FirebaseDatabase.getInstance();
    }

    public void readData(final int requestCode, String dbReferenceKey, String childKey, final FirebaseListeners.OnFirebaseReadListener onFirebaseReadListener) {
        databaseReference = firebaseDatabase.getReference(dbReferenceKey).child(childKey);
        myValueListener = new MyValueListener(requestCode, onFirebaseReadListener);
        databaseReference.addValueEventListener(myValueListener);
    }

    public void writeData(final int requestCode, String referenceKey, String childKey, Object object, final FirebaseListeners.OnFirebaseWriteListener onFirebaseWriteListener) {
        databaseReference = firebaseDatabase.getReference(referenceKey).child(childKey);
        databaseReference.push().setValue(object, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (onFirebaseWriteListener != null) {
                    if (databaseError == null) {
                        onFirebaseWriteListener.onWriteSuccess(requestCode);
                    } else {
                        onFirebaseWriteListener.onWriteFail(requestCode);
                    }
                }

            }
        });
    }

    public void updateData(final int requestCode, String referenceKey, String childKey, String taskId, Object object, final FirebaseListeners.OnFirebaseUpdateListener onFirebaseUpdateListener) {
        databaseReference = firebaseDatabase.getReference(referenceKey).child(childKey);
        databaseReference.child(taskId).setValue(object, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (onFirebaseUpdateListener != null) {
                    onFirebaseUpdateListener.onUpdateSuccess(requestCode);
                }
            }
        });
    }

    public void deleteData(final int requestCode, String referenceKey, String childKey, String taskId, final FirebaseListeners.OnFirebaseDeleteListener onFirebaseDeleteListener) {
        databaseReference = firebaseDatabase.getReference(referenceKey).child(childKey);
        databaseReference.child(taskId).removeValue(new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (onFirebaseDeleteListener != null) {
                    onFirebaseDeleteListener.onDeleteSuccess(requestCode);
                }
            }
        });
    }

    public void clearReferences() {
        databaseReference.removeEventListener(myValueListener);
        databaseReference = null;
        firebaseDatabase = null;
        firebaseUtils = null;
    }

    private class MyValueListener implements ValueEventListener {

        private int requestCode;
        private FirebaseListeners.OnFirebaseReadListener onFirebaseReadListener;

        public MyValueListener(int requestCode, FirebaseListeners.OnFirebaseReadListener onFirebaseReadListener) {
            this.requestCode = requestCode;
            this.onFirebaseReadListener = onFirebaseReadListener;
        }

        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if (onFirebaseReadListener != null) {
                if (dataSnapshot != null) {
                    onFirebaseReadListener.onReadSuccess(requestCode, dataSnapshot);
                } else {
                    onFirebaseReadListener.onReadFail(requestCode);
                }
            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            if (onFirebaseReadListener != null) {
                onFirebaseReadListener.onReadCancel(requestCode);
            }
        }
    }
}

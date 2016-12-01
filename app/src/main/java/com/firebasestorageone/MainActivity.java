package com.firebasestorageone;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.firebasestorageone.entities.Education;
import com.firebasestorageone.entities.Person;
import com.firebasestorageone.interfaces.FirebaseListeners;
import com.firebasestorageone.utils.FirebaseUtils;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.this.getClass().getSimpleName();
    private final String deviceUniqueId = "5432-3425-6543-7863";
    private final String objectKey = "Person";
    private EditText editTextName, editTextLocation;
    private Button btnAdd, btnUpdate, btnDelete;
    private ListView listView;
    private String currentKeyIndex;
    private List<String> dataList = new ArrayList<>();
    private HashMap<Integer, String> keyHashMap = new HashMap<>();
    private HashMap<String, Person> personHashMap = new HashMap<>();
    private List<Education> innerList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Education education1 = new Education();
        education1.Degree = "BE-IT";
        education1.Result = "First Class";
        innerList.add(education1);

        Education education2 = new Education();
        education2.Degree = "MCA";
        education2.Result = "Distinction";
        innerList.add(education2);

        Education education3 = new Education();
        education3.Degree = "PH.D.";
        education3.Result = "Second Class";
        innerList.add(education3);

//        deviceUniqueId = UUID.randomUUID().toString();

        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentKeyIndex = keyHashMap.get(position);
                String str = dataList.get(position);
                editTextName.setText(str.split("-")[0]);
                editTextLocation.setText(str.split("-")[1]);
            }
        });
        editTextName = (EditText) findViewById(R.id.editTextName);
        editTextLocation = (EditText) findViewById(R.id.editTextLocation);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);

        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        btnUpdate.setOnClickListener(this);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(this);

        FirebaseUtils.getInstance().readData(1, objectKey, deviceUniqueId, new FirebaseListeners.OnFirebaseReadListener() {
            @Override
            public void onReadSuccess(int requestCode, DataSnapshot dataSnapshot) {
                Log.e(TAG, "Child count - " + dataSnapshot.getChildrenCount() + "");
                dataList.clear();
                keyHashMap.clear();
                personHashMap.clear();
                int headPosition = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Person object = postSnapshot.getValue(Person.class);
                    keyHashMap.put(headPosition, postSnapshot.getKey());
                    headPosition++;
                    personHashMap.put(postSnapshot.getKey(), object);
                    dataList.add(object.Name + "-" + object.Location);
                }
                ArrayAdapter<String> itemsAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, dataList);
                listView.setAdapter(itemsAdapter);
            }

            @Override
            public void onReadFail(int requestCode) {

            }

            @Override
            public void onReadCancel(int requestCode) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAdd:
                Person person = new Person();
                person.Name = editTextName.getText().toString().trim();
                person.Location = editTextLocation.getText().toString().trim();
                person.InnerList = innerList;
                FirebaseUtils.getInstance().writeData(1, objectKey, deviceUniqueId, person, new FirebaseListeners.OnFirebaseWriteListener() {
                    @Override
                    public void onWriteSuccess(int requestCode) {

                    }

                    @Override
                    public void onWriteFail(int requestCode) {

                    }
                });

                editTextName.setText("");
                editTextLocation.setText("");
                currentKeyIndex = "";
                break;

            case R.id.btnUpdate:
                if (!currentKeyIndex.isEmpty()) {
                    Person updatedPerson = personHashMap.get(currentKeyIndex);
                    updatedPerson.Name = editTextName.getText().toString().trim();
                    updatedPerson.Location = editTextLocation.getText().toString().trim();
                    FirebaseUtils.getInstance().updateData(1, objectKey, deviceUniqueId, currentKeyIndex, updatedPerson, new FirebaseListeners.OnFirebaseUpdateListener() {
                        @Override
                        public void onUpdateSuccess(int requestCode) {

                        }
                    });
                }
                editTextName.setText("");
                editTextLocation.setText("");
                currentKeyIndex = "";
                break;

            case R.id.btnDelete:
                if (!currentKeyIndex.isEmpty()) {
                    FirebaseUtils.getInstance().deleteData(1, objectKey, deviceUniqueId, currentKeyIndex, new FirebaseListeners.OnFirebaseDeleteListener() {
                        @Override
                        public void onDeleteSuccess(int requestCode) {
                            editTextName.setText("");
                            editTextLocation.setText("");
                            currentKeyIndex = "";
                        }
                    });
                }
                editTextName.setText("");
                editTextLocation.setText("");
                currentKeyIndex = "";
                break;
        }

    }

    @Override
    public void onBackPressed() {
        FirebaseUtils.getInstance().clearReferences();
        super.onBackPressed();
    }
}

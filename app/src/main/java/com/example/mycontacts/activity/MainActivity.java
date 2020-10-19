package com.example.mycontacts.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycontacts.R;
import com.example.mycontacts.adapter.ContactAdapterRecyclerView;
import com.example.mycontacts.db.DbHelper;
import com.example.mycontacts.model.Contact;
import com.example.mycontacts.utils.ContactClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactClickListener {

    RecyclerView recyclerView;
    ArrayList<Contact> contacts = new ArrayList<>();
    ContactAdapterRecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recyclerview);

        FloatingActionButton fab = findViewById(R.id.saveFlaotingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        contacts = getData();

        recyclerView = findViewById(R.id.recyclerView);
        adapter = new ContactAdapterRecyclerView(this,contacts,this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        adapter.notifyDataSetChanged();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_add) {
            Intent intent = new Intent(this,AddActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public ArrayList<Contact> getData(){
        contacts.clear();
        DbHelper helper = new DbHelper(this, DbHelper.DB_NAME,null,DbHelper.DB_VERSION);
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<Contact> contacts = new ArrayList<>();

        String query = "SELECT * FROM "+DbHelper.TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact();
                contact.setId( Integer.parseInt(cursor.getString(0)) );
                contact.setFirstName( cursor.getString(1) );
                contact.setLastName( cursor.getString(2) );
                contact.setMobile( cursor.getString(3) );
                contact.setEmail( cursor.getString(4) );
                contact.setImage( cursor.getString(5) );
                contacts.add(contact);

            }while (cursor.moveToNext());
        }
        return contacts;
    }

    @Override
    public void onContactClick(Contact contact) {
        Intent intent = new Intent(MainActivity.this,ContactDetails.class);
        intent.putExtra("contact",contact.getId());
        startActivity(intent);
        Toast.makeText(MainActivity.this,"item clicked",Toast.LENGTH_SHORT).show();
    }
}

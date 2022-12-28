package com.example.mycrudsqllite;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    String[] daftar;
    ListView listView;
    Menu menu;

    protected Cursor cursor;
    Database database;
    public static MainActivity ma;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,CreateActivity.class));
            }
        });

        ma = this;

        database = new Database(this);
        RefreshList();
    }

    private void RefreshList() {
        SQLiteDatabase db = database.getReadableDatabase();
        cursor = db.rawQuery("select * from mahasiswa",null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            daftar[i] = cursor.getString(0).toString();
        }

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1,daftar));
        listView.setSelected(true);

        listView.setOnClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final String selection = daftar[i];
                final CharSequence[] dialogItem = {"item mahasiswa","update mahasiswa","hapus mahasiswa"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogItem, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(i){
                            case 0:
                                Intent pindah = new Intent(getApplicationContext(),DetailActivity.class);
                                pindah.putExtra("name",selection);
                                startActivity(pindah);
                                break;
                            case 1:
                                pindah = new Intent(getApplicationContext(),UpdateActivity.class);
                                pindah.putExtra("name",selection);
                                startActivity(pindah);
                                break;
                                case 2:
                                    SQLiteDatabase db = database.getReadableDatabase();
                                    db.execSQL("delete from biodata where name = '"+selection+"'");
                                    RefreshList();

                                break;
                        }
                    }
                });

            }
        });
    }
}
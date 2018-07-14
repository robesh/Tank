package com.example.broman.tank;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v7.app.AlertDialog;

public class MainActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setListAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                getResources().getStringArray(R.array.options)));
        listView = findViewById(android.R.id.list);
        listView.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        switch (position)
        {
            case 0: startActivity(new Intent(MainActivity.this, Game.class)); break;
            case 1:
                Dialog dialog = new Dialog(this);
                dialog.setTitle("High Score");
                dialog.setContentView(R.layout.highscore);
                dialog.show();

                /*new AlertDialog.Builder(this)
                        .setTitle("High Score")
                        .setView(R.layout.highscore)
                        .create()
                        .show();*/
                break;
            case 2:
                Dialog d = new Dialog(this);
                d.setContentView(R.layout.editplayer);
                d.setTitle("Edit Player");
                d.setCancelable(true);
                d.show();
                break;
        }
    }
}

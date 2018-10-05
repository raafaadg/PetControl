/*
package com.pet.petcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    List<DataItem> lstData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lstData = new ArrayList<>();

        lstData.add(new DataItem(R.drawable.india,"India"));
        lstData.add(new DataItem(R.drawable.ireland,"IreLand"));
        lstData.add(new DataItem(R.drawable.uk,"UK"));

        lstData.add(new DataItem(R.drawable.usa,"USA"));
        lstData.add(new DataItem(R.drawable.japan,"Japan"));
        lstData.add(new DataItem(R.drawable.germany,"Germany"));
        lstData.add(new DataItem(R.drawable.france,"France"));
        lstData.add(new DataItem(R.drawable.spain,"Spain"));
        lstData.add(new DataItem(R.drawable.italy,"Italy"));
        lstData.add(new DataItem(R.drawable.china,"China"));
        lstData.add(new DataItem(R.drawable.brazil,"Brazil"));
        lstData.add(new DataItem(R.drawable.canada,"Canada"));
        lstData.add(new DataItem(R.drawable.hongkong,"Hong Kong"));
        lstData.add(new DataItem(R.drawable.korea,"Korea"));
        lstData.add(new DataItem(R.drawable.sweden,"Sweden"));


        ListView listView = (ListView)findViewById(R.id.listView);

        CustomAdapter adapter = new CustomAdapter(this,R.layout.itemrow,lstData);

        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent();
                intent.putExtra("Country", lstData.get(position).countryName);
                intent.putExtra("Flag", lstData.get(position).resIdThumbnail);

                intent.setClass(MainActivity.this,Main2Activity.class);
                startActivity(intent);
            }
        });


    }
}
*/

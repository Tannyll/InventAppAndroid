package com.emirci.inventapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.emirci.inventapp.adapter.InventoryAdapter;
import com.emirci.inventapp.model.InventoryModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class InventoryesActivity extends AppCompatActivity {

    TextView textViewUsesUser, textViewBarcode, textViewInventoryType, textViewData;
    public static ArrayList<InventoryModel> inventoryModelList;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventoryes);

        FetchData fetchData = new FetchData();
        fetchData.execute();

        InventoryAdapter adapter = new InventoryAdapter(this, inventoryModelList);

        listView = (ListView) findViewById(R.id.lvInventory);
        listView.setAdapter(adapter);


        //textViewBarcode = (TextView) findViewById(R.id.tvBarcode);
        //textViewInventoryType = (TextView) findViewById(R.id.tvInventoryType);
        //textViewUsesUser = (TextView) findViewById(R.id.tvUsesUser);
        textViewData = (TextView) findViewById(R.id.tvData);


    }
}

package com.emirci.inventapp;

import android.os.AsyncTask;
import android.util.Log;

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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by serdaremirci on 11/9/17.
 */

public class FetchData extends AsyncTask<Void, Void, ArrayList<InventoryModel>> {

    String data = "";
    String dataParsed = "";
    String singleParsed = "";

    @Override
    protected ArrayList<InventoryModel> doInBackground(Void... voids) {

        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            URL url = new URL("http://ws26.osmanlireklam.com.tr:7999/Envanter/api/inventory/list");
            connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            reader = new BufferedReader(new InputStreamReader(stream));

            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
                //data = data + line;
            }

            String finalJson = buffer.toString();

            JSONObject parentObject = new JSONObject(finalJson);


            JSONArray parentArray = parentObject.getJSONArray("");
            //JSONArray parentArray = new JSONArray(finalJson.toString());


            ArrayList<InventoryModel> inventoryModelList = new ArrayList<>();

            for (int i = 0; i < parentArray.length(); i++) {
                JSONObject finalObject = parentArray.getJSONObject(i);
                InventoryModel inventoryModel = new InventoryModel();
                inventoryModel.setBarcode(finalObject.getString("Barcode"));
                inventoryModel.setDepartmentName(finalObject.getString("DepartmentName"));
                inventoryModel.setFeature(finalObject.getString("Feature"));
                inventoryModel.setInsertDate(finalObject.getString("InsertDate"));
                inventoryModel.setInventoryId(finalObject.getInt("InventoryId"));
                inventoryModel.setInventoryType(finalObject.getString("InventoryType"));
                inventoryModel.setInvoiceDate(finalObject.getString("InvoiceDate"));
                inventoryModel.setInvoiceNumber(finalObject.getString("InvoiceNumber"));
                inventoryModel.setModel(finalObject.getString("Model"));
                inventoryModel.setPrice(finalObject.getDouble("Price"));
                inventoryModel.setTrademark(finalObject.getString("Trademark"));
                inventoryModel.setUsesUser(finalObject.getString("UsesUser"));

                inventoryModelList.add(inventoryModel);

            }

            //JSONObject finalObject = jsonObject.getJSONObject(null);

            JSONArray JA = new JSONArray(data);
            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                singleParsed = "Barcode : " + JO.get("Barcode") + "\n" +
                        "InventoryType : " + JO.get("InventoryType") + "\n" +
                        "UsesUser : " + JO.get("UsesUser") + "\n";

                dataParsed = dataParsed + singleParsed + "\n";
            }

            //String barcodeString = finalObject.getString("Barcode");
            //String inventoryTypeString = finalObject.getString("InventoryType");
            //String usesUsertring = finalObject.getString("UsesUser");


            Log.i("ID", buffer.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(ArrayList<InventoryModel> aVoid) {
        super.onPostExecute(aVoid);
        InventoryesActivity.inventoryModelList = aVoid;
        //InventoryesActivity.textViewData.setText(this.dataParsed);
    }
}

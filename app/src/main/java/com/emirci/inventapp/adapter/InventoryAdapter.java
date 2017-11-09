package com.emirci.inventapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.emirci.inventapp.R;
import com.emirci.inventapp.model.InventoryModel;

import java.util.ArrayList;

/**
 * Created by serdaremirci on 11/9/17.
 */

public class InventoryAdapter extends ArrayAdapter<InventoryModel> {

    public InventoryAdapter(Context context, ArrayList<InventoryModel> inventoryes) {
        super(context, 0, inventoryes);

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        InventoryModel model = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.inventory_items, parent, false);
        }

        TextView tvUsesUser = (TextView) convertView.findViewById(R.id.tvItemsUsesUser);
        TextView tvModel = (TextView) convertView.findViewById(R.id.tvItemsModel);


        tvUsesUser.setText(model.getUsesUser());
        tvModel.setText(model.getModel());

        return convertView;

    }
}

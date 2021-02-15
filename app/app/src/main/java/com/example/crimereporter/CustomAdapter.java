package com.example.crimereporter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Display> {
    private ArrayList<Display> dataSet;
    private Context mContext;

    public CustomAdapter(Context context, ArrayList<Display> displayList) {
        super(context, 0, displayList);
        this.dataSet = displayList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //super.getView(position, convertView, parent);

        if (convertView == null) {
            LayoutInflater vi = LayoutInflater.from(mContext);
            convertView = vi.inflate(R.layout.item_display, parent, false);
        }

        Display entry = getItem(position);
        if (entry != null) {
            TextView title = (TextView) convertView.findViewById(R.id.title);
            TextView time = (TextView) convertView.findViewById(R.id.time);
            TextView type = (TextView) convertView.findViewById(R.id.type);
            TextView description = (TextView) convertView.findViewById(R.id.description);

            title.setText(entry.title);
            time.setText(entry.time);
            type.setText(entry.type);
            description.setText(entry.description);
        }

        return convertView;
    }

}

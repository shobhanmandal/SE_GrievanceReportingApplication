package com.example.shobhan.gra_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GrievanceList extends BaseAdapter {

    Context c;
    ArrayList<GrievanceEvent> displayGrievanceList;
    LayoutInflater inflater;

    public GrievanceList(Context c, ArrayList<GrievanceEvent> displayGrievanceList) {
        this.c = c;
        this.displayGrievanceList = displayGrievanceList;
    }

    @Override
    public int getCount() {
        return displayGrievanceList.size();
    }

    @Override
    public Object getItem(int position) {
        return displayGrievanceList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(inflater==null) {
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null) {
            convertView = inflater.inflate(R.layout.list_layout,parent,false);
        }

        TextView displaySubject = convertView.findViewById(R.id.displaySubject);
        TextView displayCategory = convertView.findViewById(R.id.displayCategory);
        TextView displayDescription = convertView.findViewById(R.id.displayDescription);
        TextView displayStatus = convertView.findViewById(R.id.displayStatus);

        ImageView displayImage = convertView.findViewById(R.id.displayImage);

        final String subject = displayGrievanceList.get(position).getGrievanceSubject();
        final String category = displayGrievanceList.get(position).getGrievanceType();
        final String description = displayGrievanceList.get(position).getGrievanceDescription();
        final String status = displayGrievanceList.get(position).getEventType();
        final String image = displayGrievanceList.get(position).getImageLocation();

        displaySubject.setText(subject);
        displayCategory.setText(category);
        displayDescription.setText(description);
        displayStatus.setText(status);
//        displayImage.setImageResource(R.drawable.googleg_standard_color_18);

        if(image!=null && !image.equals("No image Present")){

            Glide.with(c).load(image)
                    .into(displayImage);
        }else {
            displayImage.setImageResource(R.drawable.googleg_standard_color_18);
        }




        return convertView;
    }
}

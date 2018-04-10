package com.example.SE.my_application;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

//public class GrievanceList extends ArrayAdapter<GrievanceEvent> {
//
//    private Activity context;
//    private List<GrievanceEvent> grievanceEventList;
//    private Fragment frag;
//
//    public GrievanceList(Activity context,List<GrievanceEvent> grievanceEventList) {
//        super(context,R.layout.list_layout,grievanceEventList);
//        this.context = context;
//        this.grievanceEventList = grievanceEventList;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//
//        LayoutInflater inflater = context.getLayoutInflater();
//
////        View listViewItem = inflater.in
//
//        View listViewItem = inflater.inflate(R.layout.list_layout,null,true);
//
//        TextView displaySubject = listViewItem.findViewById(R.id.displaySubject);
//        TextView displayCategory = listViewItem.findViewById(R.id.displayCategory);
//        TextView displayDescription = listViewItem.findViewById(R.id.displayDescription);
//        TextView displayStatus = listViewItem.findViewById(R.id.displayStatus);
//
//        ImageView displayImage = listViewItem.findViewById(R.id.displayImage);
//
//        GrievanceEvent grievanceEvent = grievanceEventList.get(position);
//
//        displaySubject.setText(grievanceEvent.getGrievanceSubject());
//        displayCategory.setText(grievanceEvent.getGrievanceType());
//        displayDescription.setText(grievanceEvent.getGrievanceDescription());
//        displayStatus.setText(grievanceEvent.getEventType());
//
////        if(!grievanceEvent.getImageLocation().equals("No image Present")){
////            Glide.with(getContext().getApplicationContext()).load(grievanceEvent.getImageLocation())
////                    .into(displayImage);
////        }
//
//        return  listViewItem;
//
//    }
//}

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
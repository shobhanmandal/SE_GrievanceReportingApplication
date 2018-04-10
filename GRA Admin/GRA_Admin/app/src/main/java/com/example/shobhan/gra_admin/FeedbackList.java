package com.example.shobhan.gra_admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class FeedbackList  extends BaseAdapter {

    Context c;
    ArrayList<Grievance> displayGrievances;
    LayoutInflater inflater1;

    public FeedbackList(Context c, ArrayList<Grievance> displayGrievances) {
        this.c = c;
        this.displayGrievances = displayGrievances;
    }

    @Override
    public int getCount() {
        return displayGrievances.size();
    }

    @Override
    public Object getItem(int position) {
        return displayGrievances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(inflater1==null) {
            inflater1 = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(convertView == null) {
            convertView = inflater1.inflate(R.layout.feedback_layout,parent,false);
        }

        TextView grievanceSubject = convertView.findViewById(R.id.grievanceSubject);
        TextView grievanceDescription = convertView.findViewById(R.id.grievanceDescription);

        final String subject = displayGrievances.get(position).getGrievanceSubject();
        final String description = displayGrievances.get(position).getGrievanceDescription();

        grievanceSubject.setText(subject);
        grievanceDescription.setText(description);

        return convertView;
    }
}

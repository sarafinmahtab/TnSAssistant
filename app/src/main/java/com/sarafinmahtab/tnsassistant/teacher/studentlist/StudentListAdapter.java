package com.sarafinmahtab.tnsassistant.teacher.studentlist;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sarafinmahtab.tnsassistant.R;

import java.util.List;

/**
 * Created by Arafin on 8/11/2017.
 */

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.StdViewHolder> {
    private List<StudentItem> stdListItem;
    private Context context;

    public StudentListAdapter(List<StudentItem> stdListItem, Context context) {
        this.stdListItem = stdListItem;
        this.context = context;
    }

    @Override
    public StdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.std_list_item, parent, false);
        return new StdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StdViewHolder holder, int position) {
        final StudentItem stdItem = stdListItem.get(position);

        holder.textViewStdName.setText(stdItem.getStdListName());
        holder.textViewStdReg.setText(stdItem.getStdListReg());

        holder.stdCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Call " + stdItem.getStdListPhone(), Toast.LENGTH_LONG).show();
            }
        });

        holder.stdLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked " + stdItem.getStdListID(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return stdListItem.size();
    }

    public static class StdViewHolder extends RecyclerView.ViewHolder {

        TextView textViewStdName, textViewStdReg;
        ImageView stdDisplayPic;
        ImageButton stdCall;
        LinearLayout stdLinearLayout;
        CardView std_cardView;

        public StdViewHolder(View itemView) {
            super(itemView);

            textViewStdName = (TextView) itemView.findViewById(R.id.stdlist_name);
            textViewStdReg = (TextView) itemView.findViewById(R.id.stdlist_reg);

            stdDisplayPic = (ImageView) itemView.findViewById(R.id.stdlist_display);
            stdCall = (ImageButton) itemView.findViewById(R.id.std_call);

            stdLinearLayout = (LinearLayout) itemView.findViewById(R.id.std_linear_list);
            std_cardView = (CardView) itemView.findViewById(R.id.std_cardview);
        }
    }
}
package de.lumdev.tempusfugit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.GroupEvent;

public class GroupEventListAdapter extends RecyclerView.Adapter<GroupEventListAdapter.GroupEventViewHolder> {

    class GroupEventViewHolder extends RecyclerView.ViewHolder {
        private final TextView groupEventItemView;

        private GroupEventViewHolder(View itemView) {
            super(itemView);
            groupEventItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<GroupEvent> groupEvents; // Cached copy of GroupEvents

    GroupEventListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public GroupEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.room_with_view_recyclerview_item, parent, false);
        return new GroupEventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupEventViewHolder holder, int position) {
        if (groupEvents != null) {
            GroupEvent current = groupEvents.get(position);
            holder.groupEventItemView.setText(current.name);
        } else {
            // Covers the case of data not being ready yet.
            holder.groupEventItemView.setText("[empty]");
        }
    }

    void setGroupEvents(List<GroupEvent> groupEvents){
        this.groupEvents = groupEvents;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // groupEvents has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (groupEvents != null)
            return groupEvents.size();
        else return 0;
    }

}

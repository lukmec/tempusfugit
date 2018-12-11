package de.lumdev.tempusfugit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.GroupEvent;

public class GroupEventListAdapter2 extends PagedListAdapter<GroupEvent, GroupEventListAdapter2.GroupEventViewHolder2> {

    class GroupEventViewHolder2 extends RecyclerView.ViewHolder {
        private final TextView groupEventItemView;

        private GroupEventViewHolder2(View itemView) {
            super(itemView);
            groupEventItemView = itemView.findViewById(R.id.textView);
        }

        private void clear(){
            groupEventItemView.setText("[empty]");
        }
    }



    Context context;

    protected GroupEventListAdapter2(Context context) {
//        mInflater = LayoutInflater.from(context);
        super(DIFF_CALLBACK);
        this.context = context;
    }


    @NonNull
    @Override
    public GroupEventViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_with_view_recyclerview_item, parent,false);
        return new GroupEventViewHolder2(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupEventViewHolder2 holder,
                                 int position) {
        GroupEvent groupEvent = getItem(position);
        if (groupEvent != null) {
//            holder.bindTo(groupEvent);
            holder.groupEventItemView.setText(groupEvent.name);
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear();
        }
    }

    private static DiffUtil.ItemCallback<GroupEvent> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<GroupEvent>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(GroupEvent oldGroup, GroupEvent newGroup) {
                    return oldGroup.id == newGroup.id;
                }

                @Override
                public boolean areContentsTheSame(GroupEvent oldGroup,
                                                  GroupEvent newGroup) {
                    return oldGroup.equals(newGroup);
                }
            };
}

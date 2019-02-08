package de.lumdev.tempusfugit;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.maltaisn.icondialog.IconHelper;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.GroupEvent;

public class GroupEventAdapter extends PagedListAdapter<GroupEvent, GroupEventAdapter.GroupEventViewHolder> {

    class GroupEventViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView description;
        private final ProgressBar progress;
        private final ImageView icon;
        private final CardView container;

        private GroupEventViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rVCard_groupEventName);
            description = itemView.findViewById(R.id.rVCard_groupEventDescription);
            progress = itemView.findViewById(R.id.rVCard_groupEventProgress);
            icon = itemView.findViewById(R.id.rVCard_groupEventIcon);
            container = itemView.findViewById(R.id.cardView_groupEvent);
        }

        private void clear(){
            name.setText(R.string.placeholder_name);
            description.setText(R.string.placeholder_description);
            progress.setProgress(0);
            icon.setImageResource(R.drawable.ic_add_black_24dp);
            container.setCardBackgroundColor(itemView.getResources().getColor(R.color.design_default_color_background));
        }
        
    }



    Context context;
    IconHelper iconHelper;

    protected GroupEventAdapter(Context context) {
//        mInflater = LayoutInflater.from(context);
        super(DIFF_CALLBACK);
        this.context = context;
        iconHelper = IconHelper.getInstance(context);
    }

    @NonNull
    @Override
    public GroupEventAdapter.GroupEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_group_event, parent,false);
        return new GroupEventAdapter.GroupEventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupEventAdapter.GroupEventViewHolder holder,
                                 int position) {
        GroupEvent groupEvent = getItem(position);
        if (groupEvent != null) {
//            holder.bindTo(groupEvent);
            holder.name.setText(groupEvent.name);
            holder.description.setText(groupEvent.description);
            holder.progress.setProgress(groupEvent.progress);
            setIcon(holder, groupEvent.icon);
            holder.container.setCardBackgroundColor(groupEvent.color);
//            holder.container.setOnClickListener(editGroupEventOnClickListener);
            holder.container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    OverviewGroupEventFragmentDirections.ActionOvrvwGeDestToEdtGeDest action = OverviewGroupEventFragmentDirections.actionOvrvwGeDestToEdtGeDest();
                    action.setGroupEventId(groupEvent.id);
                    Navigation.findNavController(v).navigate(action);
                    return true;
                }
            });
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Log.d("-->", "GroupEventId: "+groupEvent.id);
                    OverviewGroupEventFragmentDirections.ActionOvrvwGeDestToDtlGeDest action = OverviewGroupEventFragmentDirections.actionOvrvwGeDestToDtlGeDest();
                    action.setGroupEventId(groupEvent.id);
                    Navigation.findNavController(v).navigate(action);
                }
            });
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear();
        }
    }



    private void setIcon(GroupEventAdapter.GroupEventViewHolder holder, int iconId){
        try{
            //IconPicker allows "0" as ID, but .setImageRessource justs sets no icon for ID=0, instead of throwing exception
            //--> Raising custom Exception, when ID=0 in order to let the IconHelper display correct Icon
            if (iconId == 0){
                throw new Exception("err: IconId=0, catching special case...");
            }
            holder.icon.setImageResource(iconId);
        }catch (Exception e){
            try {
                iconHelper.addLoadCallback(new IconHelper.LoadCallback() {
                    @Override
                    public void onDataLoaded() {
                        // This happens on UI thread, and is guaranteed to be called.
                        holder.icon.setImageDrawable(iconHelper.getIcon(iconId).getDrawable(context));
                    }
                });
            }catch (Exception e2){
                Toast.makeText(context, "err: Failed to display icon", Toast.LENGTH_SHORT).show();
            }
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

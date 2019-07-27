package de.lumdev.tempusfugit;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.maltaisn.icondialog.IconHelper;

import java.util.ArrayList;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import de.lumdev.tempusfugit.data.Event;
import de.lumdev.tempusfugit.util.EventObservable;
import de.lumdev.tempusfugit.util.EventObserver;
import de.lumdev.tempusfugit.util.MaterialColorHelper;

public class EventAdapter extends PagedListAdapter<Event, EventAdapter.EventViewHolder> implements EventObservable {

    class EventViewHolder extends RecyclerView.ViewHolder {
        private final TextView name;
        private final TextView prio;
        private final TextView tododay;
        private final CheckBox done;
        private final CardView container;

        private EventViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.rVCard_eventName);
            prio = itemView.findViewById(R.id.rVCard_eventPriority);
            tododay = itemView.findViewById(R.id.rVCard_eventToDoDay);
            done = itemView.findViewById(R.id.rVCard_checkbox_event_done);
            container = itemView.findViewById(R.id.cardView_event);
        }

        private void clear(){
            name.setText(R.string.placeholder_name);
            prio.setText(R.string.placeholder_priority);
            tododay.setText(R.string.placeholder_totoday);
            done.setChecked(false);
            container.setCardBackgroundColor(itemView.getResources().getColor(R.color.design_default_color_background));
        }
    }



    Context context;
    IconHelper iconHelper;
    private ArrayList<EventObserver> eventObservers;

    public EventAdapter(Context context) {
//        mInflater = LayoutInflater.from(context);
        super(DIFF_CALLBACK);
        this.context = context;
        iconHelper = IconHelper.getInstance(context);
        eventObservers = new ArrayList<>();
    }

    @NonNull
    @Override
    public EventAdapter.EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_event___linear, parent,false);
        return new EventAdapter.EventViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.EventViewHolder holder,
                                 int position) {
        Event event = getItem(position);
        if (event != null) {
            holder.name.setText(event.name);
            holder.prio.setText(context.getResources().getString(R.string.label_cV_event_priority,event.priority));
            holder.tododay.setText(context.getResources().getString(R.string.label_cV_event_tododay,event.toDoDay));
            //set colors
            holder.name.setTextColor(event.textColor);
            holder.prio.setTextColor(event.textColor);
            holder.tododay.setTextColor(event.textColor);
            holder.container.setCardBackgroundColor(event.color);
            setDone(holder, event.done, event.color,event.textColor);
            //set colorStateList of check box (set checbox color appropriate to element's color) //see https://stackoverflow.com/questions/34340256/how-to-change-checkbox-checked-color-programmatically
            ColorStateList colorStateList = new ColorStateList(
                    new int[][] {
                            new int[] { -android.R.attr.state_checked }, // unchecked
                            new int[] {  android.R.attr.state_checked }  // checked
                    },
                    new int[] {
//                            ContextCompat.getColor(context, R.color.primaryLight50),
                            event.textColor,
                            event.color
                    });
            holder.done.setButtonTintList(colorStateList);

//            holder.done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                @Override
//                public void onCheckedChanged(CompoundButton checkBox, boolean checked) {
////                    OverviewGroupEventFragmentDirections.ActionOvrvwGeDestToEdtGeDest action = OverviewGroupEventFragmentDirections.actionOvrvwGeDestToEdtGeDest();
////                    action.setEventId(event.id);
////                    Navigation.findNavController(v).navigate(action);
//                    setDone(holder, checked, event.color);
//                    //save changed (event is now done true/false) in db via observer
//                    notifyObservers(event.id, checked);
//                }
//            });
//            holder.done.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    boolean checked = ((CheckBox) view).isChecked();
//                    Log.d("-->", "Checkbox (at pos: "+position+") now: "+checked);
//                    setDone(holder, checked, event.color);
//                    //save changed (event is now done true/false) in db via observer
////                    notifyObservers(event.id, checked);
//                }
//            });
            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean checked = ((CheckBox) holder.done).isChecked();
                    if (checked){
                        checked = false;
                    }else{
                        checked = true;
                    }
//                    Log.d("-->", "Checkbox (at pos: "+position+") now: "+checked);
                    setDone(holder, checked, event.color, event.textColor);
                    //save changed (event is now done true/false) in db via observer
                    notifyObserversEventDone(event, checked);
                }
            });
            holder.container.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    //navigate to fragment, where user can edit/ create an event
                    //navigation depends on source and destination, and is therefore handled by underlying fragment
                    notifyObserversActionEditEvent(event);
                    return true;
                }
            });
        } else {
            // Null defines a placeholder item - PagedListAdapter automatically
            // invalidates this row when the actual object is loaded from the
            // database.
            holder.clear();
        }
    }

    private void setDone(EventAdapter.EventViewHolder holder, boolean done, int color, int textColor){
        //--- changed background color (grey out, if done=true)
        int new_color;
        int new_textColor;
        if (done){
//            //darken background (reducing saturation and increasing brightness)
//            new_color = MaterialColorHelper.changeColorSaturation(color, 0.0f);
//            new_color = MaterialColorHelper.changeColorBrightness(new_color, 0.85f);
            //set background with static color tone (e.g. grey)
            new_color = ContextCompat.getColor(context, R.color.doneEventCard);
            //set color of text to static color
            new_textColor = MaterialColorHelper.changeColorSaturation(color, 0.0f);
            new_textColor = MaterialColorHelper.changeColorBrightness(new_textColor, 0.5f);
//            new_textColor = ContextCompat.getColor(context, R.color.primaryLight200));
        }else{
//            //lighten up background
//            new_color = changeColorSaturation(color, 1.3f);
//            new_color = changeColorBrightness(new_color, 1.1f);
            //if not done, then color of card should equal persisted color of event
            new_color = color;
            new_textColor = textColor;
        }
        holder.container.setCardBackgroundColor(new_color);
        holder.name.setTextColor(new_textColor);
        //--- change font (stike through text); according to: https://inducesmile.com/android-tips/android-how-to-strike-through-or-cross-out-a-text-in-android/ and https://stackoverflow.com/questions/9786544/creating-a-strikethrough-text
        if (done) {
//            holder.name.setText(holder.name.getText(), TextView.BufferType.SPANNABLE);
//            Spannable spannable = (Spannable) holder.name.getText();
//            spannable.setSpan(new StrikethroughSpan(), 0, holder.name.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
//            holder.name.setText(holder.name.getText());
            holder.name.setPaintFlags(1281); //after debug info: holder.name.getPaintFlags() = Paint Flags: 1281
//            holder.name.setPaintFlags(holder.name.getPaintFlags() | Paint.LINEAR_TEXT_FLAG);
        }
        //--- set checkbox correctly
        holder.done.setChecked(done);
    }

    private static DiffUtil.ItemCallback<Event> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Event>() {
                // Concert details may have changed if reloaded from the database,
                // but ID is fixed.
                @Override
                public boolean areItemsTheSame(Event oldEvent, Event newEvent) {
                    return oldEvent.id == newEvent.id;
                }
                @Override
                public boolean areContentsTheSame(Event oldEvent,
                                                  Event newEvent) {
                    return oldEvent.equals(newEvent);
                }
            };

    //Moved to helper class "MaterialColorHelper"
//    // Darken a Color with a factor smaller 1f (e.g. 0.8f)
//    // Lighten a Color with a factor greater 1f (e.g. 1.2f)
//    @ColorInt
//    int changeColorBrightness(@ColorInt int color, float factor) {
//        float[] hsv = new float[3];
//        Color.colorToHSV(color, hsv);
//        hsv[2] *= factor;
//        return Color.HSVToColor(hsv);
//    }
//    // Decrease Saturation of a Color with a factor smaller 1f (e.g. 0.8f)
//    // Increase Saturation of a Color with a factor greater 1f (e.g. 1.2f)
//    @ColorInt
//    int changeColorSaturation(@ColorInt int color, float factor) {
//        float[] hsv = new float[3];
//        Color.colorToHSV(color, hsv);
//        hsv[1] *= factor;
//        return Color.HSVToColor(hsv);
//    }

    @Override
    public void registerObserver(EventObserver eventObserver) {
        if(!this.eventObservers.contains(eventObserver)) {
            this.eventObservers.add(eventObserver);
        }
    }
    @Override
    public void removeObserver(EventObserver eventObserver) {
        if(this.eventObservers.contains(eventObserver)) {
            this.eventObservers.remove(eventObserver);
        }
    }
    @Override
    public void notifyObserversEventDone(Event event, boolean done) {
        for (EventObserver observer : this.eventObservers) {
            observer.onEventDone(event, done);
        }
    }
    @Override
    public void notifyObserversActionEditEvent(Event event) {
        for (EventObserver observer : this.eventObservers) {
            observer.onActionEditEvent(event);
        }
    }
}

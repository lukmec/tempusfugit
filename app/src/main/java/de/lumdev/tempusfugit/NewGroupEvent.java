package de.lumdev.tempusfugit;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import de.lumdev.tempusfugit.data.GroupEvent;
import petrov.kristiyan.colorpicker.ColorPicker;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.maltaisn.icondialog.Icon;
import com.maltaisn.icondialog.IconDialog;
import com.maltaisn.icondialog.IconHelper;

import java.util.Locale;

public class NewGroupEvent extends AppCompatActivity implements IconDialog.Callback{

    private NewGroupEventViewModel viewModel;
    EditText eT_name;
    EditText eT_description;
    Button btn_color;
    ImageButton btn_icon;

    int pickedColor;
    Icon[] preselectedIcons;
    Icon pickedIcon;
    boolean iconPicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group_event);

        viewModel = ViewModelProviders.of(this).get(NewGroupEventViewModel.class);
        eT_name = findViewById(R.id.eT_newGE_name);
        eT_description = findViewById(R.id.eT_newGE_description);
        btn_color = findViewById(R.id.btn_newGE_color);
        btn_icon = findViewById(R.id.btn_nGE_icon);
        pickedColor = 0;
        preselectedIcons = null;
        iconPicked = false;
    }

    public void createNewGroupEvent(View v){
        if (isValidInput()) {
//            GroupEvent gEvent = new GroupEvent(eT_name.getText().toString(), eT_description.getText().toString());
            GroupEvent gEvent = new GroupEvent(eT_name.getText().toString(), eT_description.getText().toString(), pickedColor, pickedIcon.getId(), -1);
            viewModel.insertGroupEvent(gEvent);
            Intent intent = new Intent(getApplicationContext(), GroupOverviewActivity.class);
            startActivity(intent);
        }else{
            toast(getString(R.string.err_invalid_input));
        }
    }

    public void selectColor(View v){
        final ColorPicker colorPicker = new ColorPicker(this);
        colorPicker.setOnFastChooseColorListener(new ColorPicker.OnFastChooseColorListener() {
            @Override
            public void setOnFastChooseColorListener(int position,int color) {
                // put code
//                toast("Picked Color: "+color + " pos: "+position);
                pickedColor = color;
                btn_color.setBackgroundColor(color);
            }

            @Override
            public void onCancel(){
                // put code
                toast("Color picking cancelled.");
            }
        })
//        .setColorButtonTickColor(getResources().getColor(R.color.black_de))
        .setColumns(5)
        .setTitle(getString(R.string.colorpicker_select_color))
        .setRoundColorButton(true)
        .show();
    }

    public void selectIcon(View v){

        //FYI: Locale.getDefault() returns "Deutschland" on my phone
//        toast("Locale: "+Locale.getDefault().getDisplayCountry());

        IconDialog iconDialog = new IconDialog();
        iconDialog
                .setSelectedIcons(preselectedIcons)
//        .setMaxSelection(1, false) //default value is 1 --> user can only select 1 icon
                .setShowSelectButton(false)
                //ATTENTION: Search is only provided in english, french, portuguese
                .setSearchEnabled(IconDialog.VISIBILITY_ALWAYS, Locale.getDefault())
                .setTitle(IconDialog.VISIBILITY_NEVER, getString(R.string.iconpicker_select_icon))
                .show(getSupportFragmentManager(), "icon_dialog");
    }
    @Override
    public void onIconDialogIconsSelected(Icon[] icons) {
        //always only take the first picked icon from the dialog (dialog is configured to allow only 1 icon to be selected)
        pickedIcon = icons[0];
        iconPicked = true;
//        btn_icon.setImageResource(pickedIcons[0].getId());
        btn_icon.setImageDrawable(pickedIcon.getDrawable(getApplicationContext()));
    }

    private boolean isValidInput(){
        String name = eT_name.getText().toString();
        String description = eT_description.getText().toString();
        if (!name.equals("") && name != null){
//            toast("Name: "+name);
            if (!description.equals("") && description != null){
//                toast("description: "+description);
                if (pickedColor != 0){
//                    toast("color: "+pickedColor);
                    if(iconPicked) {
//                        toast("Icon picked: "+ pickedIcon.getId());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // Darken a Color with a factor smaller 1f (e.g. 0.8f)
    @ColorInt
    int darkenColor(@ColorInt int color, float factor) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= factor;
        return Color.HSVToColor(hsv);
    }

    public void toast(String textToToast){
        Toast.makeText(getApplicationContext(), textToToast, Toast.LENGTH_SHORT).show();
    }
}

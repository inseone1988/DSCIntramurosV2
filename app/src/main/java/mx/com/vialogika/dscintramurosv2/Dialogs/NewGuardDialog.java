package mx.com.vialogika.dscintramurosv2.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mx.com.vialogika.dscintramurosv2.R;
import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Person;

public class NewGuardDialog extends DialogFragment {

    private String imagePath;
    private Bitmap profileImage;
    private ImageView profileImageHolder;
    private TextView nameHolder,fnameHolder,lnameHolder;
    private Spinner genre,group,position;

    private Guard guard = new Guard();
    private Person person = new Person();
    
    private Button saveElement;

    private String name, fName, lName;

    private NewGuardCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.new_guard_dialog, container,false);
        getitems(rootview);
        setProfileimage();
        return rootview;
    }

    private void getitems(View rootview){
        profileImageHolder = rootview.findViewById(R.id.new_guard_profile_photo);
        saveElement = rootview.findViewById(R.id.sav_element);
        nameHolder = rootview.findViewById(R.id.element_name);
        fnameHolder = rootview.findViewById(R.id.element_fname);
        lnameHolder = rootview.findViewById(R.id.element_lname);
        genre = rootview.findViewById(R.id.gender);
        group = rootview.findViewById(R.id.group);
        position = rootview.findViewById(R.id.position);
        saveElement.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.sav_element:
                    saveElement();
                    break;
            }
        }
    };

    private void getValues(){
        name = nameHolder.getText().toString();
        fName = fnameHolder.getText().toString();
        lName = lnameHolder.getText().toString();
        person.setPersonCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date()));
        person.setPersonName(name);
        person.setPersonFname(fName);
        person.setPersonLname(lName);
        person.setPersonGender(genre.getSelectedItem().toString());
        person.setPersonPosition(position.getSelectedItem().toString());
        guard.setGuardProviderId(1);
        guard.setGuardSite(String.valueOf(1));
        guard.setGuardRange(person.getPersonPosition());
        guard.setPaersonData(person);
        guard.setGuardStatus(1);
    }

    private boolean validValues(){
        getValues();
        if (!name.equals("")){
            return true;
        }
        if (!fName.equals("")){
            return true;
        }
        if (!lName.equals("")){
            return true;
        }
        return false;
    }

    private void saveElement(){
        if (validValues()){
            callback.onGuardSave(guard);
        }
    }

    private void setProfileimage(){
        profileImageHolder.setImageBitmap(profileImage);
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }

    public void setCallback(NewGuardCallback callback) {
        this.callback = callback;
    }

    public void setGuard(Guard guard) {
        this.guard = guard;
        setCurrentdata();
    }
    private void setCurrentdata(){
        if (guard.getPaersonData() != null){
            Person person = guard.getPaersonData();
            if (!guard.getGuardPhotoPath().equals("")){

            }
            nameHolder.setText(person.getPersonName());
            fnameHolder.setText(person.getPersonFname());
            lnameHolder.setText(person.getPersonLname());
        }
    }

    public interface NewGuardCallback{
        void onGuardSave(Guard guard);
    }
}

package mx.com.vialogika.dscintramurosv2.Dialogs;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import mx.com.vialogika.dscintramurosv2.Network.NetworkOperations;
import mx.com.vialogika.dscintramurosv2.R;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Person;
import mx.com.vialogika.dscintramurosv2.Utils.GuardEditMode;
import mx.com.vialogika.dscintramurosv2.Utils.UserKeys;

public class NewGuardDialog extends DialogFragment {

    private String imagePath;
    private Bitmap profileImage;
    private ImageView profileImageHolder;
    private TextView nameHolder,fnameHolder,lnameHolder;
    private Spinner genre,group,position;
    private Spinner groupsSpinner;

    private Guard guard;
    private Person person ;
    
    private Button saveElement;

    private String name, fName, lName;

    private GuardEditMode mode = GuardEditMode.NEW_GUARD;

    private NewGuardCallback callback;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.new_guard_dialog, container,false);
        if (guard==null){
            guard = new Guard();
            person = new Person();
        }else{
            person = guard.getPaersonData();
            if(!guard.hasLocalProfilePhoto()){
                downloadProfileImage();
            }
        }
        getitems(rootview);
        setProfileimage();
        setCurrentdata();
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
        groupsSpinner = rootview.findViewById(R.id.group_spinner);
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

    private void downloadProfileImage(){
        NetworkOperations.getInstance().getImageFromServer(guard, new NetworkOperations.SimpleNetworkCallback<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                profileImageHolder.setImageBitmap(response);
            }

            @Override
            public void onVolleyError(Bitmap response, VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getValues(){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        name = nameHolder.getText().toString();
        fName = fnameHolder.getText().toString();
        lName = lnameHolder.getText().toString();
        person.setPersonCreated(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date()));
        person.setPersonName(name);
        person.setPersonFname(fName);
        person.setPersonLname(lName);
        person.setPersonGender(genre.getSelectedItem().toString());
        person.setPersonPosition(position.getSelectedItem().toString());
        person.setPersonSiteId(preferences.getInt(UserKeys.SP_SITE_ID,0));
        person.setPersonProviderid(preferences.getInt(UserKeys.SP_PROVIDER_ID,0));
        person.setPersonType("Intramuros");
        guard.setGuardProviderId(preferences.getInt(UserKeys.SP_PROVIDER_ID,0));
        guard.setGuardSite(String.valueOf(preferences.getInt(UserKeys.SP_SITE_ID,0)));
        guard.setGuardRange(person.getPersonPosition());
        guard.setPaersonData(person);
        guard.setGuardStatus(1);
        guard.setGuardTurno(group.getSelectedItem().toString());
        guard.setGuardGroup(groupsSpinner.getSelectedItem().toString());
        guard.setGuardBajaTimestamp("0000-00-00 00:00");
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
            if (mode.equals(GuardEditMode.EXISTENT_GUARD)){
                NetworkOperations.getInstance().updateGuard(guard, new NetworkOperations.SimpleNetworkCallback<Boolean>() {
                    @Override
                    public void onResponse(Boolean response) {
                        if (response){
                            Toast.makeText(getContext(), "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                            dismiss();
                        }
                    }

                    @Override
                    public void onVolleyError(Boolean response, VolleyError error) {

                    }
                });
            }else{
                callback.onGuardSave(guard);
            }
        }
    }

    private void setProfileimage(){
        if(guard != null && guard.hasLocalProfilePhoto()){
            Bitmap profilePhoto = BitmapFactory.decodeFile(guard.getGuardPhotoPath());
            if (profilePhoto != null){
                profileImageHolder.setImageBitmap(profilePhoto);
            }else {
                profileImageHolder.setImageResource(R.drawable.ic_guard);
            }
            return;
        }
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
        mode = GuardEditMode.EXISTENT_GUARD;
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

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        callback.onDialogDismiss();
    }

    public interface NewGuardCallback{
        void onGuardSave(Guard guard);
        void onDialogDismiss();
    }
}

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
import android.widget.ImageView;

import mx.com.vialogika.dscintramurosv2.R;

public class NewGuardDialog extends DialogFragment {

    private String imagePath;
    private Bitmap profileImage;
    private ImageView profileImageHolder;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootview = inflater.inflate(R.layout.new_guard_dialog, null);
        getitems(rootview);
        setProfileimage();
        builder.setView(rootview)
                .setTitle("Nuevo elemento")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton(android.R.string.cancel, null);
        Dialog dialog = builder.create();
        dialog.getWindow().setLayout(600,400);
        return dialog;
    }

    private void getitems(View rootview){
        profileImageHolder = rootview.findViewById(R.id.new_guard_profile_photo);
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
}

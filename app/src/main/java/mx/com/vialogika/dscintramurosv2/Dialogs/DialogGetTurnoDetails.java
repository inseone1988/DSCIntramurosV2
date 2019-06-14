package mx.com.vialogika.dscintramurosv2.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Spinner;

import mx.com.vialogika.dscintramurosv2.R;

public class DialogGetTurnoDetails extends DialogFragment {

    private ButtonCallbacks cb;
    private String[] values;

    Spinner grupo,turno;

    public void setCallbacks(ButtonCallbacks callbacks){
        this.cb = callbacks;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootview = inflater.inflate(R.layout.grupo_details_dialog,null);
        getItems(rootview);
        builder.setView(rootview);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                values = new String[]{grupo.getSelectedItem().toString(),turno.getSelectedItem().toString()};
                cb.onPositive(values);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cb.onNegative();
            }
        });
        return builder.create();
    }

    private void getItems(View dialogview){
        grupo = dialogview.findViewById(R.id.plantilla_grupo);
        turno = dialogview.findViewById(R.id.plantilla_turno);
    }

    public interface ButtonCallbacks{
        void onPositive(String[] values);
        void onNegative();
    }
}

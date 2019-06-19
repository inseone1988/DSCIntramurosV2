package mx.com.vialogika.dscintramurosv2.Dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import mx.com.vialogika.dscintramurosv2.R;
import mx.com.vialogika.dscintramurosv2.Room.Apostamiento;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Incidencia;

public class EditAptsDialog extends DialogFragment {
    private Spinner            incidenceSpinner;
    private Spinner            incidenceTypeSpinner;
    private List<Guard>        guards         = new ArrayList<>();
    private List<Apostamiento> apostamientos  = new ArrayList<>();
    private List<String>       guardNamesList = new ArrayList<>();
    private List<String>       apNamesList    = new ArrayList<>();
    private PlantillaAsignment callback;

    private TextView errorTextView;

    private int gId;
    private int apId;
    private Incidencia incidencia = new Incidencia();

    private AutoCompleteTextView guardautocomplete, apostamientoAutocomplete;

    private ArrayAdapter<String>   guardAutocompleteAdapter;
    private ArrayAdapter<String>   apostamientoAutocompleteAdapter;
    private SpinnerAdapter incidenceSpinnerAdapter;
    private SpinnerAdapter   incidenceTypeSpinnerAdapter;

    private Guard current;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        LayoutInflater      inflater      = getActivity().getLayoutInflater();
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        View                rootView      = inflater.inflate(R.layout.apostamiento_asign, null);
        getItems(rootView);
        populateLists();
        setupAdapters();
        setListeners();
        dialogBuilder.setView(rootView)
                .setPositiveButton(android.R.string.ok, null)
                .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog dialog = dialogBuilder.create();
        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button button = ((AlertDialog) dialog).getButton(DialogInterface.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (validateAsignment()){
                            current.setAsigned(true);
                            callback.onAddApostamienti(gId,apId,incidencia);
                            clearFields();
                            displayWarnMessage("Guardado");
                            current = null;
                        }
                    }
                });
            }
        });
        return dialog;
    }

    private boolean isValidGuard(int id){
        for (int i = 0; i < guards.size(); i++) {
            Guard g  = guards.get(i);
            if (!g.isAsigned()){
                return true;
            }
        }
        return false;
    }

    private boolean validateAsignment(){
        if (gId == 0){
            displayWarnMessage("Guardia Invalido");
            return false;
        }
        if (!isValidGuard(gId)){
            displayWarnMessage("Guardia ya asignado");
            return false;
        }
        if (apId == 0){
            displayWarnMessage("Seleccione apostamiento");
            return false;
        }
        if (hasIncidence() && !isvalidIncidence()){
            displayWarnMessage("Seleccione el tipo de incidencia");
            return false;
        }
        return true;
    }

    private boolean isvalidIncidence(){
        return !incidenceTypeSpinner.getSelectedItem().toString().equals("N/A");
    }

    private boolean hasIncidence(){
        return incidencia.getProviderIncidencesUuid() != null && !incidencia.getProviderIncidencesUuid().equals("");
    }

    private void getItems(View rootview) {
        guardautocomplete = rootview.findViewById(R.id.guardspinner);
        apostamientoAutocomplete = rootview.findViewById(R.id.apspinner);
        incidenceSpinner = rootview.findViewById(R.id.incidencia);
        incidenceTypeSpinner = rootview.findViewById(R.id.tipoincidncia);
        errorTextView = rootview.findViewById(R.id.errortext);
    }

    private void setupAdapters() {
        guardAutocompleteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, guardNamesList);
        guardautocomplete.setAdapter(guardAutocompleteAdapter);
        apostamientoAutocompleteAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, apNamesList);
        apostamientoAutocomplete.setAdapter(apostamientoAutocompleteAdapter);
        incidenceSpinnerAdapter = incidenceSpinner.getAdapter();
        incidenceTypeSpinnerAdapter = incidenceTypeSpinner.getAdapter();
    }

    private void setListeners() {
        guardautocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView)view).getText().toString();
                gId = getGuardIdByName(name);
            }
        });
        apostamientoAutocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = ((TextView)view).getText().toString();
                apId = getApostamientoIdByApName(name);
            }
        });
        incidenceSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                incidenceHandler();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private int getApostamientoIdByApName(String name){
        for (int i = 0; i < apostamientos.size(); i++) {
            Apostamiento c = apostamientos.get(i);
            if (c.getPlantillaPlaceApostamientoName().equals(name)){
                return c.getPlantillaPlaceId();
            }
        }
        return 0;
    }

    private int getGuardIdByName(String name){
        for (int i = 0; i < guards.size(); i++) {
            current = guards.get(i);
            if (current.getPaersonData().getPersonFullName().equals(name)){
                if(!current.isAsigned()){
                    return current.getGuardId();
                }else{
                    displayWarnMessage("Elemento ya asignado");
                }
            }
        }
        return 0;
    }

    private void populateLists() {
        for (int i = 0; i < apostamientos.size(); i++) {
            apNamesList.add(apostamientos.get(i).getPlantillaPlaceApostamientoName());
        }
        for (int i = 0; i < guards.size(); i++) {
            guardNamesList.add(guards.get(i).getPaersonData().getPersonFullName());
        }
    }

    private AdapterView.OnItemClickListener autoCTVListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch(view.getId()){
                case R.id.guardspinner:
                    if (!guards.get(position).isAsigned()){
                        gId = guards.get(position).getGuardId();
                        guards.get(position).setAsigned(true);
                    }else{
                        warnGuardAlreadyAsigned();
                    }
                    break;
            }
        }
    };

    private AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (view.getId()) {
                case R.id.apspinner:
                    apId = apostamientos.get(position).getPlantillaPlaceId();
                    break;
                case R.id.incidencia:
                    incidenceHandler();
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void incidenceHandler(){
        if (!incidenceSpinner.getSelectedItem().toString().equals("Tiempo ordinario")){
            incidencia.setProviderIncidencesDatetime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date()));
            incidencia.setProviderIncidencesUuid(UUID.randomUUID().toString());
            incidencia.setProviderIncidencesName(incidenceSpinner.getSelectedItem().toString());
            incidencia.setProviderIncidencesType(incidenceTypeSpinner.getSelectedItem().toString());
        }else{
            incidencia = new Incidencia();
        }
    }

    private void clearFields(){
        guardautocomplete.setText("");
        apostamientoAutocomplete.setText("");
    }

    private void warnGuardAlreadyAsigned(){
        displayWarnMessage("El guardia ya ha sido asignado");
    }

    private void displayWarnMessage(String message){
        errorTextView.setText(message);
        new CountDownTimer(3000,1000){
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                errorTextView.setText("");
            }
        }.start();
    }

    public void setApostamientos(List<Apostamiento> aps) {
        this.apostamientos.addAll(aps);
    }

    public void setGuards(List<Guard> mGuards) {
        this.guards.addAll(mGuards);
    }

    public void setCallback(PlantillaAsignment cb){
        this.callback = cb;
    }

    public interface PlantillaAsignment{
        void onAddApostamienti(int guardId,int apId,Incidencia incidencia);
    }
}

package mx.com.vialogika.dscintramurosv2;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.divyanshu.draw.activity.DrawingActivity;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import mx.com.vialogika.dscintramurosv2.Network.NetworkOperations;
import mx.com.vialogika.dscintramurosv2.Room.SiteIncident;
import mx.com.vialogika.dscintramurosv2.Utils.FileUtils;
import mx.com.vialogika.dscintramurosv2.Utils.Semaforo;
import mx.com.vialogika.dscintramurosv2.Utils.TimeUtils;
import mx.com.vialogika.dscintramurosv2.Utils.UserKeys;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PIE.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PIE#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PIE extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private SiteIncident incident = new SiteIncident();

    private EditText   fecha;
    private EditText   hora;
    private RadioGroup semaforo;
    private Semaforo   incidenceType = Semaforo.GRIS;
    private CheckBox   responsableIdentifiedInput;
    private EditText   eventWhatInput;
    private EditText   eventHowInput;
    private EditText   eventWhereInput;
    private EditText   evetDescriptionInput;
    private EditText   completeNameReports;
    private TextView   test;
    private Button     takeEvidenceButton;
    private Button     takeSignaturesButton;
    private Button     saveAndContinueButton;
    private Spinner    incidentInput;

    private List<Image> evidences = new ArrayList<>();
    private Bitmap signature;

    public static final int REQUEST_CODE_DRAW = 256;


    private String takedEvidences;
    private String signaturesTaked;

    private OnFragmentInteractionListener mListener;

    private List<String> inames = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    public PIE() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PIE.
     */
    // TODO: Rename and change types and number of parameters
    public static PIE newInstance(String param1, String param2) {
        PIE    fragment = new PIE();
        Bundle args     = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_pie_v1, container, false);
        getItems( rootView);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private boolean validate(){
        if(hora.getText().toString().equals("")){
            showToastMessage(hora,"Falta hora de incidente");
            return false;
        }
        if (eventWhatInput.getText().toString().equals("")){
            showToastMessage(eventWhatInput,"¿Que paso?");
            return false;
        }
        if (eventHowInput.getText().toString().equals("")){
            showToastMessage(eventHowInput,"¿Como paso?");
            return false;
        }
        if (eventWhereInput.getText().toString().equals("")){
            showToastMessage(eventWhereInput,"¿Donde paso?");
            return false;
        }
        if (evetDescriptionInput.getText().toString().equals("")){
            showToastMessage(evetDescriptionInput,"Falta narracion del evento");
            return false;
        }
        if (completeNameReports.getText().toString().equals("")){
            showToastMessage(completeNameReports,"Nombre de quien reporta");
            return false;
        }
        return true;
    }

    private void showToastMessage(View view,String message){
        view.requestFocus();
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.take_evidence:
                    ImagePicker.create(PIE.this).start();
                    break;
                case R.id.sign_capture:
                    Intent intent = new Intent(getActivity(), DrawingActivity.class);
                    startActivityForResult(intent,REQUEST_CODE_DRAW);
                    break;
                case R.id.save_and_send:
                    save();
                    break;
                case R.id.event_timex:
                    Calendar c = Calendar.getInstance();
                    new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            String hourformat = hourOfDay + " : " + minute;
                            hora.setText(hourformat);
                        }
                    },c.get(Calendar.HOUR_OF_DAY),c.get(Calendar.MINUTE),true).show();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String code = "1";
        if (ImagePicker.shouldHandle(requestCode,resultCode,data)){
            getEvidencesPaths(ImagePicker.getImages(data));
        }
        if(data != null && requestCode == REQUEST_CODE_DRAW){
            byte[] result = data.getByteArrayExtra("bitmap");
            signature = BitmapFactory.decodeByteArray(result,0,result.length);
            addSignatureInfo();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getEvidencesPaths(List<Image> evidences){
        for (int i = 0; i < evidences.size(); i++) {
            Image c = evidences.get(i);
            incident.setEventEvidence(c.getPath());
        }
    }

    private void addSignatureInfo(){
        final AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Info. de huella")
                .setView(R.layout.signature_info)
                .create();
        dialog.show();
        final EditText sname = dialog.findViewById(R.id.signature_name);
        final EditText srole = dialog.findViewById(R.id.signature_level);
        final TextView warn = dialog.findViewById(R.id.dialog_warn);
        final Button ok = dialog.findViewById(R.id.ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sname.getText().toString().equals("") && !srole.getText().toString().equals("")){
                    if (saveSignature(sname.getText().toString(),srole.getText().toString())){
                        dialog.dismiss();
                        signature = null;
                    }
                }else{
                    warn.setText(R.string.must_fill);
                    new CountDownTimer(3000,1000){
                        @Override
                        public void onTick(long millisUntilFinished) {

                        }

                        @Override
                        public void onFinish() {
                            warn.setText("");
                        }
                    };
                }
            }
        });
        Button cancel = dialog.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private boolean saveSignature(String name, String role){
        try{
            File storage = FileUtils.createImageSignature();
            FileUtils.saveBitmap(signature,storage, Bitmap.CompressFormat.PNG);
            incident.setEventSignature(storage.getAbsolutePath());
            incident.setEventSignatureNames(name);
            incident.setEventSignatureRoles(role);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    private void getItems(View rootview){
        fecha = rootview.findViewById(R.id.event_time);
        hora = rootview.findViewById(R.id.event_timex);
        hora.setOnClickListener(listener);
        hora.setShowSoftInputOnFocus(false);
        fecha.setText(TimeUtils.nowToString("yyyy-MM-dd"));
        semaforo = rootview.findViewById(R.id.event_highlight);
        semaforo.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.pie_sem_null:
                        incidenceType = Semaforo.GRIS;
                        break;
                    case R.id.pie_sem_green:
                        incidenceType = Semaforo.VERDE;
                        break;
                    case R.id.pie_sem_yellow:
                        incidenceType = Semaforo.AMARILLO;
                        break;
                    case R.id.pie_sem_red:
                        incidenceType = Semaforo.ROJO;
                        break;
                }
                test.setText(incidenceType.toSTring());
            }
        });
        incidentInput = rootview.findViewById(R.id.event_type);
        responsableIdentifiedInput = rootview.findViewById(R.id.responsible);
        test = rootview.findViewById(R.id.test);
        eventWhatInput = rootview.findViewById(R.id.event_what);
        eventHowInput = rootview.findViewById(R.id.event_how);
        eventWhereInput = rootview.findViewById(R.id.event_where);
        evetDescriptionInput = rootview.findViewById(R.id.event_facts);
        completeNameReports = rootview.findViewById(R.id.event_user);
        takeEvidenceButton = rootview.findViewById(R.id.take_evidence);
        takeEvidenceButton.setOnClickListener(listener);
        takeSignaturesButton = rootview.findViewById(R.id.sign_capture);
        takeSignaturesButton.setOnClickListener(listener);
        saveAndContinueButton = rootview.findViewById(R.id.save_and_send);
        saveAndContinueButton.setOnClickListener(listener);
        setupSpinner();
    }

    private void setupSpinner(){
        adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_item,inames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        incidentInput.setAdapter(adapter);
        getIncientNames();
    }

    private void getIncientNames(){
        showSpinnerMessage("Loading ....");
        NetworkOperations.getInstance().getIncidenceNames(new NetworkOperations.SimpleNetworkCallback<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                inames.clear();
                inames.addAll(response);
                if (inames.size() == 0){
                    showSpinnerMessage("No hay rubros");
                }else{
                    incidentInput.setEnabled(true);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onVolleyError(List<String> response, VolleyError error) {

            }
        });
    }

    private void showSpinnerMessage(String message){
        inames.clear();
        incidentInput.setEnabled(false);
        inames.add(message);
        adapter.notifyDataSetChanged();
    }

    private void save(){
        if (validate()){
            getValues();
            NetworkOperations.saveIncidence(incident, new NetworkOperations.SimpleNetworkCallback<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try{
                        if (response.getBoolean("success")){
                            Toast.makeText(GlobalAplication.getAppContext(), response.getString("message"), Toast.LENGTH_SHORT).show();
                            MainActivity activity = (MainActivity) getContext();
                            activity.loadfragment(PIE.newInstance("",""));
                        }
                    }catch(Exception e ){
                        e.printStackTrace();
                    }
                }

                @Override
                public void onVolleyError(JSONObject response, VolleyError error) {

                }
            });
        }
    }

    private void getValues(){
        //Event when is redundant
        //TODO:Make a methos to get pictures and signatures
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        incident.setEventCaptureTimestamp(TimeUtils.nowToString("yyyy-MM-dd"));
        incident.setEventDate(fecha.getText().toString());
        incident.setEventTime(hora.getText().toString());
        incident.setEventName(incidentInput.getSelectedItem().toString());
        incident.setEventRiskLevel(incidenceType.toSTring());
        incident.setEventResponsable(responsableIdentifiedInput.isChecked() ? "si":"no");
        incident.setEventWhat(eventWhatInput.getText().toString());
        incident.setEventHow(eventWhatInput.getText().toString());
        incident.setEventWhere(evetDescriptionInput.getText().toString());
        incident.setEventUser(completeNameReports.getText().toString());
        incident.setEventUserSite(String.valueOf(preferences.getInt(UserKeys.SP_SITE_ID,0)));
        incident.setEventEditStatus(true);
        incident.setEventType("Incidencia intramuros");
        incident.setEventUUID(UUID.randomUUID().toString());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

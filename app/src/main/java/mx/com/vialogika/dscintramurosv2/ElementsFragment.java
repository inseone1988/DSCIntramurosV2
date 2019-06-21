package mx.com.vialogika.dscintramurosv2;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.FileProvider;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import net.gotev.uploadservice.ServerResponse;
import net.gotev.uploadservice.UploadInfo;
import net.gotev.uploadservice.UploadStatusDelegate;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.com.vialogika.dscintramurosv2.Adapters.ElementAdapter;
import mx.com.vialogika.dscintramurosv2.Dialogs.NewGuardDialog;
import mx.com.vialogika.dscintramurosv2.Network.NetworkOperations;
import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Person;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ElementsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ElementsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    public static final int REQUEST_IMAGE_CAPTURE = 16143;

    public static final  String ACTIVE_ELEMENTS = "Activo";
    public static final  String BAJA_ELEMENTS   = "Baja";
    private static final String ARG_PARAM1      = "param1";
    private static final String ARG_PARAM2      = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView               rv;
    private RecyclerView.Adapter       adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Guard>        filter       = new ArrayList<>();
    private List<Guard>        activeGuards = new ArrayList<>();
    private List<Guard>        bajaGuards   = new ArrayList<>();
    private List<CharSequence> gruposStatus = new ArrayList<>();
    private String currentPhotoPath;

    private EditText searchbox;

    private DatabaseOperations dbo;

    private Spinner              sp;
    private FloatingActionButton addElementDFab;

    private UploadStatusDelegate delegate = new UploadStatusDelegate() {
        @Override
        public void onProgress(Context context, UploadInfo uploadInfo) {
            System.out.println(uploadInfo.getProgressPercent());
        }

        @Override
        public void onError(Context context, UploadInfo uploadInfo, ServerResponse
        serverResponse, Exception exception) {

        }

        @Override
        public void onCompleted(Context context, UploadInfo uploadInfo, ServerResponse serverResponse) {
            try{
                JSONObject response = new JSONObject(serverResponse.getBodyAsString());
                if (response.getBoolean("success")){
                    DatabaseOperations op     = DatabaseOperations.getInstance();
                    Guard              guard  = new Guard(response.getJSONObject("guard"));
                    Person             person = new Person(response.getJSONObject("person"));
                    //This must ocurr only when new guard is saved
                    guard.setGuardPhotoPath(currentPhotoPath);
                    person.setPersonProfilePhotoPath(currentPhotoPath);
                    guard.setPaersonData(person);
                    op.saveNewGuard(guard);
                    System.out.println(new String(serverResponse.getBody()));
                    Toast.makeText(GlobalAplication.getAppContext(), "Guardia guardado", Toast.LENGTH_SHORT).show();
                }
            }catch(Exception e){
                e.printStackTrace();
            }

        }

        @Override
        public void onCancelled(Context context, UploadInfo uploadInfo) {

        }
    };


    public ElementsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ElementsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ElementsFragment newInstance(String param1, String param2) {
        ElementsFragment fragment = new ElementsFragment();
        Bundle           args     = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_elements, container, false);
        getElements(root);
        dbo = DatabaseOperations.getInstance();
        loadGuards();
        return root;
    }

    private void getElements(View rootView) {
        rv = rootView.findViewById(R.id.element_recyclerview);
        addElementDFab = rootView.findViewById(R.id.add_element_fab);
        addElementDFab.setOnClickListener(listener);
        searchbox = rootView.findViewById(R.id.searchg);
        searchbox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                searchGuardByName(s.toString());
            }
        });
    }

    private void searchGuardByName(String searchString){
        if (!searchString.equals("")){
            filter.clear();
            for (int i = 0; i < activeGuards.size(); i++) {
                Guard c = activeGuards.get(i);
                String name = c.getPaersonData().getPersonFullName().toLowerCase();
                if (name.contains(searchString)){
                    filter.add(c);
                }
            }
            for (int i = 0; i < bajaGuards.size(); i++) {
                Guard c = bajaGuards.get(i);
                String name = c.getPaersonData().getPersonFullName().toLowerCase();
                if (name.contains(searchString)){
                    filter.add(c);
                }
            }
            adapter.notifyDataSetChanged();
        }else{
            filter.addAll(activeGuards);
            adapter.notifyDataSetChanged();
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_element_fab:
                    takePicture();
                    Toast.makeText(getContext(), "Capture la fotografia del guardia", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void takePicture(){
        Intent intent = new Intent(getContext(),GuardPhotoTake.class);
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case Setup.REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    takePicture();
                }else {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Permisos requeridos")
                            .setMessage("No se puede dar de alta sin el permiso de usar la camara, intente nuevamente otorgando los permisos solicitados")
                            .setPositiveButton(android.R.string.ok,null)
                            .show();
                }
                break;
        }

    }

    private File createImageFIle()throws IOException{
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
        String imageFileName = "DSCProfile_" + timestamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()){
            storageDir.mkdir();
        }
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            String path = extras.getString("path");
            if (path != null && !path.equals("")){
                currentPhotoPath = path;
                File saved = new File(path);
                if (saved.exists()){
                    Bitmap imagebitmap = ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(path),64,64);
                    showNewElementDialog(imagebitmap);
                }
            }else{
                Toast.makeText(getContext(), "Ocurrio un error al crear la imagen", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showNewElementDialog(Bitmap thumb){
        final NewGuardDialog dialog = new NewGuardDialog();
        dialog.setProfileImage(thumb);
        dialog.setCallback(new NewGuardDialog.NewGuardCallback() {
            @Override
            public void onGuardSave(Guard guard) {
                guard.getPaersonData().setPersonProfilePhotoPath(currentPhotoPath);
                guard.setGuardPhotoPath(currentPhotoPath);
                saveNewGuard(guard);
                dialog.dismiss();
            }

            @Override
            public void onDialogDismiss() {
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(),"NEW_GUARD");
    }

    public void editGuard(int position){
        Guard edit = filter.get(position);
        NewGuardDialog dialog = new NewGuardDialog();
        dialog.setGuard(edit);
        dialog.setCallback(new NewGuardDialog.NewGuardCallback() {
            @Override
            public void onGuardSave(Guard guard) {
                Toast.makeText(getContext(), "GuardEdited", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDialogDismiss() {
                adapter.notifyDataSetChanged();
            }
        });
        dialog.show(getActivity().getSupportFragmentManager(),"NEW_GUARD");
    }

    private void saveNewGuard(Guard guard){
        filter.add(guard);
        adapter.notifyDataSetChanged();
        NetworkOperations.getInstance().saveGuard(guard,delegate);
        //dbo.saveNewGuard(guard);
    }

    private AdapterView.OnItemSelectedListener spListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            String selected = sp.getSelectedItem().toString();
            if (selected.equals(ACTIVE_ELEMENTS)) {
                if (adapter != null){
                    displayActiveguards();
                }
            }
            if (selected.equals(BAJA_ELEMENTS)){
                if (adapter != null){
                    displayBajaGuards();
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void setupRecyclerView() {
        adapter = new ElementAdapter(filter, new ElementAdapter.ElementInterface() {
            @Override
            public void OnElementSelect(int position) {
                editGuard(position);
            }
        });
        layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);
    }

    private void filterGuards(List<Guard> guards) {
        activeGuards.clear();
        bajaGuards.clear();
        for (int i = 0; i < guards.size(); i++) {
            Guard c = guards.get(i);
            if (c.isActive()) {
                activeGuards.add(c);
            } else {
                bajaGuards.add(c);
            }
        }
    }

    private void loadGuards() {
        dbo.getGuards(new DatabaseOperations.backgroundOperation() {
            @Override
            public void onOperationFinished(Object callbackResult) {
                final List<Guard> guards = (List<Guard>) callbackResult;
                if (guards != null) {
                    filterGuards(guards);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            setupRecyclerView();
                            if (guards.size() > 0) displayActiveguards();
                            Toast.makeText(getContext(), "Loaded :" + guards.size() + " guards", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void displayActiveguards() {
        filter.clear();
        filter.addAll(activeGuards);
        adapter.notifyDataSetChanged();
    }

    private void displayBajaGuards() {
        filter.clear();
        filter.addAll(bajaGuards);
        adapter.notifyDataSetChanged();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        gruposStatus.addAll(Arrays.asList(new String[]{ACTIVE_ELEMENTS, BAJA_ELEMENTS}));
        inflater.inflate(R.menu.elements_menu, menu);
        MenuItem mSpinner = menu.findItem(R.id.group_select);
        sp = (Spinner) mSpinner.getActionView();
        ArrayAdapter<CharSequence> sAdapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item, gruposStatus);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(sAdapter);
        sp.setOnItemSelectedListener(spListener);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

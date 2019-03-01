package mx.com.vialogika.dscintramurosv2;


import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.Adapters.ElementAdapter;
import mx.com.vialogika.dscintramurosv2.Dialogs.NewGuardDialog;
import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.Room.Guard;

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

    private DatabaseOperations dbo;

    private Spinner sp;
    private FloatingActionButton addElementDFab;


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
        dbo = DatabaseOperations.getInstance(getContext());
        loadGuards();
        return root;
    }

    private void getElements(View rootView) {
        rv = rootView.findViewById(R.id.element_recyclerview);
        addElementDFab = rootView.findViewById(R.id.add_element_fab);
        addElementDFab.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.add_element_fab:
                    takePicture();
                    Toast.makeText(getContext(), "Hello new guard", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }
        }
    };

    private void takePicture(){
        Intent takepictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takepictureIntent.resolveActivity(getActivity().getPackageManager()) != null){
            startActivityForResult(takepictureIntent,REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imagebitmap = (Bitmap) extras.get("data");
            showNewElementDialog(imagebitmap);
        }
    }

    private void showNewElementDialog(Bitmap thumb){
        NewGuardDialog dialog = new NewGuardDialog();
        dialog.setProfileImage(thumb);
        dialog.show(getActivity().getSupportFragmentManager(),"NEW_GUARD");
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
        adapter = new ElementAdapter(filter);
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

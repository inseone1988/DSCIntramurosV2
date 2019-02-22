package mx.com.vialogika.dscintramurosv2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.Adapters.ReportedPlantillaAdapter;
import mx.com.vialogika.dscintramurosv2.Dialogs.DialogGetTurnoDetails;
import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.ViewHolders.ReportedPlantillaView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlantillaView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlantillaView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlantillaView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ProgressBar progressBar;
    private RecyclerView rv;
    private RecyclerView.LayoutManager mLayoutManager;
    private ReportedPlantillaAdapter adapter;
    private FrameLayout noDataView;
    FloatingActionButton mFab;

    private List<String> groups = new ArrayList<>();
    private List<ReportedPlantillaView> dataset = new ArrayList<>();
    private DatabaseOperations dbo;

    private OnFragmentInteractionListener mListener;

    public PlantillaView() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PlantillaView.
     */
    // TODO: Rename and change types and number of parameters
    public static PlantillaView newInstance(String param1, String param2) {
        PlantillaView fragment = new PlantillaView();
        Bundle        args     = new Bundle();
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_plantilla_view, container, false);
        dbo = DatabaseOperations.getInstance(getContext());
        getItem(rootView);
        setupRecyclerView();
        getReportedGroups();
        return rootView;
    }

    private void getItem(View v){
        noDataView = v.findViewById(R.id.empty_view);
        progressBar = v.findViewById(R.id.guard_load_progressbar);
        rv = v.findViewById(R.id.plantilla_reported_container);
        mFab = v.findViewById(R.id.floatingActionButton);
        mFab.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.floatingActionButton:
                    showGrupoDetailsDialog();
                    break;
            }
        }
    };

    private void showGrupoDetailsDialog(){
        DialogGetTurnoDetails dialog = new DialogGetTurnoDetails();
        dialog.show(getFragmentManager(),"GRUPO_EDIT");
    }

    private void getReportedGroups(){
        startProgressBar();
        dbo.getReportedGroups(new DatabaseOperations.backgroundOperation() {
            @Override
            public void onOperationFinished(Object callbackResult) {
                String[] reported = (String[]) callbackResult;
                if (reported != null) {
                    groups.addAll(Arrays.asList(reported));
                    populateDataset();
                }
            }
        }, new DatabaseOperations.UIThreadOperation() {
            @Override
            public void onOperationFinished(@Nullable Object callbackResult) {
                stopProgressbar();
                if (dataset.size() > 0) {
                    showRecyclerView();
                    adapter.notifyDataSetChanged();
                } else {
                    showNoDataView();
                    Toast.makeText(getContext(), "No se han reportado grupos.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void populateDataset(){
        for (int i = 0; i < groups.size(); i++) {
            ReportedPlantillaView rpv = ReportedPlantillaView.getInstance(getContext(),groups.get(i));
            rpv.setGroupNumber(i+1);
            dataset.add(rpv);
        }
    }

    private void setupRecyclerView(){
        if (dataset.size() > 0){
            mLayoutManager = new LinearLayoutManager(getContext());
            adapter = new ReportedPlantillaAdapter(dataset);
            rv.setLayoutManager(mLayoutManager);
            rv.setAdapter(adapter);
        }else{
            showNoDataView();
        }
    }

    private void showNoDataView(){
        rv.setVisibility(View.GONE);
        noDataView.setVisibility(View.VISIBLE);
    }

    private void showRecyclerView(){
        rv.setVisibility(View.VISIBLE);
        noDataView.setVisibility(View.GONE);
    }

    private void startProgressBar(){
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
    }

    private void stopProgressbar(){
        progressBar.setVisibility(View.GONE);
        progressBar.setIndeterminate(false);
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

package mx.com.vialogika.dscintramurosv2;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.Adapters.VetadoAdapter;
import mx.com.vialogika.dscintramurosv2.Network.NetworkOperations;
import mx.com.vialogika.dscintramurosv2.Utils.SearchType;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VetadoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VetadoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VetadoFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView               mrv;
    private VetadoAdapter              adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Vetado>               vetados = new ArrayList<>();

    private ImageButton goSearch;
    private EditText searchStringInput;
    private RadioGroup searchType;
    private LinearLayout progressView;
    private ProgressBar progressBar;
    private TextView statusText;

    private SearchType searchStringType = SearchType.SEARCH_TYPE_PERSON;

    private OnFragmentInteractionListener mListener;

    public VetadoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VetadoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VetadoFragment newInstance(String param1, String param2) {
        VetadoFragment fragment = new VetadoFragment();
        Bundle         args     = new Bundle();
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
        View root = inflater.inflate(R.layout.fragment_vetado, container, false);
        getItems(root);
        return root;
    }

    private void getItems(View rootView){
        mrv = rootView.findViewById(R.id.vetado_rv);
        progressView = rootView.findViewById(R.id.wainting_view);
        statusText = rootView.findViewById(R.id.sttus_description);
        progressBar = rootView.findViewById(R.id.progressBar2);
        initRv();
        searchType = rootView.findViewById(R.id.search_type);
        searchType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.searchTypePerson:
                        searchStringType = SearchType.SEARCH_TYPE_PERSON;
                        break;
                    case R.id.searchTypeProvider:
                        searchStringType = SearchType.SEARCH_TYPE_PROVIDER;
                        break;
                }
            }
        });
        searchStringInput = rootView.findViewById(R.id.search_string);
        goSearch = rootView.findViewById(R.id.go_search);
        goSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard();
                if (validate()){
                    searchVetado();
                }
            }
        });
        progressBar.setVisibility(View.GONE);
        updateStatus("Ingrese termino y tipo de busqueda");
    }

    private void hideSoftKeyboard(){
        View focused = ((Activity)getContext()).getCurrentFocus();
        if (focused != null){
            InputMethodManager inputMethodManager = (InputMethodManager)((Activity)getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(focused.getWindowToken(),0);
        }
    }

    private boolean validate(){
        if (searchStringInput.getText().toString().equals("")){
            return false;
        }
        return true;
    }

    private void initRv(){
        adapter = new VetadoAdapter(vetados);
        layoutManager = new LinearLayoutManager(getContext());
        mrv.setLayoutManager(layoutManager);
        mrv.setAdapter(adapter);
    }
    private void updateStatus(String message){
        statusText.setText(message);
    }

    private void hideProgressView(){
        progressView.setVisibility(View.GONE);
        mrv.setVisibility(View.VISIBLE);
    }

    private void showProgressView(){
        progressView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        mrv.setVisibility(View.GONE);
    }

    private void searchVetado(){
        showProgressView();
        updateStatus("Buscando...");
        vetados.clear();
        NetworkOperations  ops = NetworkOperations.getInstance();
        ops.vetadoSearch(searchStringInput.getText().toString(), searchStringType.toString(), new NetworkOperations.SimpleNetworkCallback<List<Vetado>>() {
            @Override
            public void onResponse(List<Vetado> response) {
                progressBar.setVisibility(View.GONE);
                updateStatus("Procesando respuesta");
                vetados.addAll(response);
                adapter.notifyDataSetChanged();
                if(vetados.size() > 0){
                    hideProgressView();
                }else{
                    updateStatus("No hay resultados");
                    Toast.makeText(getContext(), "No hay Resultados", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onVolleyError(List<Vetado> response, VolleyError error) {
                updateStatus(error.getMessage());
            }
        });
        searchStringInput.setText("");
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

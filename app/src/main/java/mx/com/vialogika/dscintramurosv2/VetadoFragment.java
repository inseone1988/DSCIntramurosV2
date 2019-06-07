package mx.com.vialogika.dscintramurosv2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;

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

    private RecyclerView mrv;
    private VetadoAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Vetado> vetados = new ArrayList<>();

    private ImageButton goSearch;
    private EditText searchStringInput;
    private RadioGroup searchType;

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
                if (validate()){
                    searchVetado();
                }
            }
        });
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

    private void searchVetado(){
        vetados.clear();
        NetworkOperations  ops = NetworkOperations.getInstance();
        ops.vetadoSearch(searchStringInput.getText().toString(), searchStringType.toString(), new NetworkOperations.SimpleNetworkCallback<List<Vetado>>() {
            @Override
            public void onResponse(List<Vetado> response) {
                vetados.addAll(response);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onVolleyError(List<Vetado> response, VolleyError error) {

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
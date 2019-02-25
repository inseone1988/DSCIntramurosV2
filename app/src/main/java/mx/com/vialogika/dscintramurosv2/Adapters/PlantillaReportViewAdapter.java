package mx.com.vialogika.dscintramurosv2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.ApostamientoReportView;
import mx.com.vialogika.dscintramurosv2.R;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Person;

public class PlantillaReportViewAdapter extends RecyclerView.Adapter<PlantillaReportViewAdapter.PlantillaReportViewHolder> {

    private List<ApostamientoReportView> mDatatset;
    private Context context;

    public PlantillaReportViewAdapter(List<ApostamientoReportView> dataset) {
        this.mDatatset = dataset;
    }

    public static class PlantillaReportViewHolder extends RecyclerView.ViewHolder{
        TextView reportedRequired,apName;
        ListView guardslist;
        ArrayAdapter<String> guardsListadapter;
        public PlantillaReportViewHolder(@NonNull View itemView) {
            super(itemView);
            reportedRequired = itemView.findViewById(R.id.reported_required_text);
            guardslist = itemView.findViewById(R.id.guard_list);
            apName = itemView.findViewById(R.id.ap_name_text);
        }
    }

    @NonNull
    @Override
    public PlantillaReportViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context = viewGroup.getContext();
        View rootview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plantilla_edit,viewGroup,false);
        return new PlantillaReportViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantillaReportViewHolder plantillaReportViewHolder, int i) {
        ApostamientoReportView current = mDatatset.get(i);
        String reqRep = current.apostamientoRequired() +"/"+current.apostamientoGuardCount();
        plantillaReportViewHolder.guardsListadapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getApGuardNames(current.getPersons()));
        plantillaReportViewHolder.guardslist.setAdapter(plantillaReportViewHolder.guardsListadapter);
        plantillaReportViewHolder.reportedRequired.setText(reqRep);
        plantillaReportViewHolder.apName.setText(current.getApostamiento().getPlantillaPlaceApostamientoName());
    }

    private Context getContext(){
        return this.context;
    }

    private List<String> getApGuardNames(List<Person> guards){
        List<String> guardsNames = new ArrayList<>();
        if (guards.size() > 0){
            for (int i = 0; i < guards.size(); i++) {
                guardsNames.add(guards.get(i).getPersonFullName());
            }
        }else{
            guardsNames.add("Apostamiento sin guardias");
        }
        return guardsNames;
    }

    @Override
    public int getItemCount() {
        return mDatatset.size();
    }
}

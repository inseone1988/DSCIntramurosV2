package mx.com.vialogika.dscintramurosv2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.ApostamientoReportView;
import mx.com.vialogika.dscintramurosv2.R;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Person;

public class PlantillaReportViewAdapter extends RecyclerView.Adapter<PlantillaReportViewAdapter.PlantillaReportViewHolder> {

    private List<ApostamientoReportView> mDatatset;
    private Context context;

    private AdapterCallbacks cb;

    public PlantillaReportViewAdapter(List<ApostamientoReportView> dataset,AdapterCallbacks callbacks) {
        this.mDatatset = dataset;
        this.cb = callbacks;
    }

    public static class PlantillaReportViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView reportedRequired,apName;
        ListView guardslist;
        ArrayAdapter<String> guardsListadapter;
        LinearLayout guards_wrapper;
        public PlantillaReportViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cv_container);
            reportedRequired = itemView.findViewById(R.id.reported_required_text);
            guardslist = itemView.findViewById(R.id.guard_list);
            apName = itemView.findViewById(R.id.ap_name_text);
            guards_wrapper = itemView.findViewById(R.id.guards_wrapper);
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
    public void onBindViewHolder(@NonNull PlantillaReportViewHolder plantillaReportViewHolder,final int i) {
        ApostamientoReportView current = mDatatset.get(i);
        int mHeigth = current.getGuards().size() > 1 ? (120 * (current.getGuards().size())) : 200;
        String reqRep =  current.apostamientoGuardCount()+"/"+current.apostamientoRequired();
        plantillaReportViewHolder.guardsListadapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,getApGuardNames(current.getGuards()));
        plantillaReportViewHolder.guardslist.setAdapter(plantillaReportViewHolder.guardsListadapter);
        plantillaReportViewHolder.guardslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String gname = ((TextView)view).getText().toString();
                cb.onReportViewClick(i,position,gname);
            }
        });
        plantillaReportViewHolder.reportedRequired.setText(reqRep);
        plantillaReportViewHolder.apName.setText(current.getApostamiento().getPlantillaPlaceApostamientoName());
        plantillaReportViewHolder.guards_wrapper.getLayoutParams().height = mHeigth;
    }

    private Context getContext(){
        return this.context;
    }

    private List<String> getApGuardNames(List<Guard> guards){
        List<String> guardsNames = new ArrayList<>();
        if (guards.size() > 0){
            for (int i = 0; i < guards.size(); i++) {
                guardsNames.add(guards.get(i).getPaersonData().getPersonFullName());
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

    public interface AdapterCallbacks{
        void onReportViewClick(int arvpos,int gpos,String guardname);
    }
}
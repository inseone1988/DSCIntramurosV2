package mx.com.vialogika.dscintramurosv2.Adapters;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mx.com.vialogika.dscintramurosv2.R;
import mx.com.vialogika.dscintramurosv2.ViewHolders.ReportedPlantillaView;

public class ReportedPlantillaAdapter extends RecyclerView.Adapter<ReportedPlantillaAdapter.ReportedPlantillaViewHolder> {

    private List<ReportedPlantillaView> dataset;

    public ReportedPlantillaAdapter(List<ReportedPlantillaView> dataset) {
        this.dataset = dataset;
    }

    public static class ReportedPlantillaViewHolder extends RecyclerView.ViewHolder{
        TextView plantillaNo;
        TextView sitename;
        TextView providername;
        TextView gReported;
        TextView gRequired;
        public ReportedPlantillaViewHolder(View itemview) {
            super(itemview);
            plantillaNo = itemview.findViewById(R.id.plantilla_no_text);
            sitename = itemview.findViewById(R.id.plantilla_site_text);
            providername = itemview.findViewById(R.id.plantilla_provider_text);
            gReported = itemview.findViewById(R.id.plantilla_total);
            gRequired = itemview.findViewById(R.id.plantilla_count);
        }
    }

    @NonNull
    @Override
    public ReportedPlantillaViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View rootview = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.reported_plantilla_view,viewGroup,false);
        return new ReportedPlantillaViewHolder(rootview);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportedPlantillaViewHolder holder, int i) {
        ReportedPlantillaView plantillaView = dataset.get(i);
        holder.plantillaNo.setText(String.valueOf(plantillaView.getGroupNumber()));
        holder.sitename.setText(plantillaView.getSiteName());
        holder.providername.setText(plantillaView.getProvider());
        holder.gReported.setText(String.valueOf(plantillaView.getGroupReported()));
        holder.gRequired.setText(String.valueOf(plantillaView.getGroupTotal()));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}

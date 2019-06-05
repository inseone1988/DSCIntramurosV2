package mx.com.vialogika.dscintramurosv2.Adapters;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.com.vialogika.dscintramurosv2.R;
import mx.com.vialogika.dscintramurosv2.Vetado;

public class VetadoAdapter extends RecyclerView.Adapter<VetadoAdapter.VetadoViewHolder> {

    private List<Vetado> dataset;

    public VetadoAdapter(List<Vetado> dataset) {
        this.dataset = dataset;
    }

    public static class VetadoViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView provider;
        TextView role;
        TextView due;

        public VetadoViewHolder(@NonNull View itemView) {
            super(itemView);
            this.name = itemView.findViewById(R.id.vetado_name);
            this.provider = itemView.findViewById(R.id.vetado_provider);
            this.role = itemView.findViewById(R.id.vetado_role);
            this.due = itemView.findViewById(R.id.vetado_restrict_due);
        }
    }


    @NonNull
    @Override
    public VetadoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mRoot = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.vetado_view,viewGroup,false);
        return new VetadoViewHolder(mRoot);
    }

    @Override
    public void onBindViewHolder(@NonNull VetadoViewHolder vetadoViewHolder, int i) {
        Vetado c = dataset.get(i);
        Resources res = vetadoViewHolder.name.getContext().getResources();
        String vname = String.format(res.getString(R.string.vetado_mane),c.getPerson_fullname());
        String pname = String.format(res.getString(R.string.vetado_provider),c.getProvider_alias());
        String vrole = String.format(res.getString(R.string.vetado_role),c.getRestriction_type());
        String due = String.format(res.getString(R.string.vetado_due),c.getDue_date());
        vetadoViewHolder.name.setText(vname);
        vetadoViewHolder.provider.setText(pname);
        vetadoViewHolder.role.setText(vrole);
        vetadoViewHolder.due.setText(due);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }
}

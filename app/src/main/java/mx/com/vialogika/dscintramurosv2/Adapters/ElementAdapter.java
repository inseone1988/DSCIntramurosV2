package mx.com.vialogika.dscintramurosv2.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {

    public static class ElementViewHolder extends RecyclerView.ViewHolder{
        public ElementViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ElementViewHolder elementViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
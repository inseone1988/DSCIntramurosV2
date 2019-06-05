package mx.com.vialogika.dscintramurosv2.Adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mx.com.vialogika.dscintramurosv2.Dialogs.NewGuardDialog;
import mx.com.vialogika.dscintramurosv2.ElementAdapterOnClickListener;
import mx.com.vialogika.dscintramurosv2.ElementsFragment;
import mx.com.vialogika.dscintramurosv2.R;
import mx.com.vialogika.dscintramurosv2.Room.Guard;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ElementViewHolder> {

    private Context     mContext;
    private List<Guard> dataset;
    private ElementInterface cb;

    private int position;

    public ElementAdapter(List<Guard> dataset,ElementInterface mcb) {
        this.dataset = dataset;
        this.cb = mcb;
    }

    public static class ElementViewHolder extends RecyclerView.ViewHolder{
        CardView elementcardView;
        ImageView profilePicholder;
        TextView elementName, elmentPosition, elementCreated,elementStatus;
        public ElementViewHolder(@NonNull View itemView) {
            super(itemView);
            elementcardView = itemView.findViewById(R.id.element_card_view);
            profilePicholder = itemView.findViewById(R.id.profile_picture);
            elementName = itemView.findViewById(R.id.guardname);
            elmentPosition = itemView.findViewById(R.id.position);
            elementCreated = itemView.findViewById(R.id.created);
            elementStatus = itemView.findViewById(R.id.status);
        }
    }

    @NonNull
    @Override
    public ElementViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        mContext = viewGroup.getContext();
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.element_display_holder,viewGroup,false);
        return new ElementViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull ElementViewHolder elementViewHolder, int i) {
        final int position = i;
        Guard current = dataset.get(i);
        if (photoExists(current.getGuardPhotoPath()) && current.hasLocalProfilePhoto()){
            elementViewHolder.profilePicholder.setImageBitmap(BitmapFactory.decodeFile(current.getGuardPhotoPath()));
        }else{
            elementViewHolder.profilePicholder.setImageResource(R.drawable.ic_guard);
        }
        Resources res = getContext().getResources();
        elementViewHolder.elementName.setText(String.format(res.getString(R.string.guard_name),current.getPaersonData().getPersonFullName()));
        elementViewHolder.elmentPosition.setText(String.format(res.getString(R.string.guard_position),current.getGuardRange()));
        elementViewHolder.elementCreated.setText(String.format(res.getString(R.string.create_date),current.getPaersonData().getPersonCreated()));
        elementViewHolder.elementStatus.setText(String.format(res.getString(R.string.active),setElementStatus(current.getGuardStatus())));
        elementViewHolder.profilePicholder.setOnClickListener(new ElementAdapterOnClickListener(i){
            @Override
            public void onClick(View v) {
                super.onClick(v);
                cb.OnElementSelect(position);
            }
        });
    }

    private String setElementStatus(int status){
        return status == 1 ? "Activo" : "Baja";
    }

    private boolean photoExists(String path){
        File picture = new File(path);
        if (picture.exists()){
            return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public Context getContext() {
        return mContext;
    }

    public interface ElementInterface{
        void OnElementSelect(int position);
    }
}
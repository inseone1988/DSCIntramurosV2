package mx.com.vialogika.dscintramurosv2.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.ElementAdapterOnClickListener;
import mx.com.vialogika.dscintramurosv2.Network.NetworkOperations;
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
        CardView  elementcardView;
        ImageView profilePicholder;
        TextView  elementName, elmentPosition, elementCreated,elementStatus;
        ImageButton deleteElement;
        public ElementViewHolder(@NonNull View itemView) {
            super(itemView);
            elementcardView = itemView.findViewById(R.id.element_card_view);
            profilePicholder = itemView.findViewById(R.id.profile_picture);
            elementName = itemView.findViewById(R.id.guardname);
            elmentPosition = itemView.findViewById(R.id.position);
            elementCreated = itemView.findViewById(R.id.created);
            elementStatus = itemView.findViewById(R.id.status);
            deleteElement = itemView.findViewById(R.id.deleteguard);
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
        final Guard current = dataset.get(i);
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
                if(current.isActive()){
                    cb.OnElementSelect(position);
                }else{
                    Toast.makeText(mContext, "Elemento no editable", Toast.LENGTH_SHORT).show();
                }
            }
        });
        elementViewHolder.deleteElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteElement(current);
            }
        });
        if(!current.isActive()){
            elementViewHolder.deleteElement.setVisibility(View.GONE);
        }else{
            elementViewHolder.deleteElement.setVisibility(View.VISIBLE);
        }
    }

    private String setElementStatus(int status){
        return status == 1 ? "Activo" : "Baja";
    }

    private void deleteElement(final Guard guard){
        guard.setGuardStatus(0);
        new AlertDialog.Builder(getContext())
                .setTitle("Dar de baja")
                .setMessage("¿Dar de baja elemento?")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        guard.save();
                    }
                })
                .setNegativeButton(android.R.string.cancel,null)
                .show();
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
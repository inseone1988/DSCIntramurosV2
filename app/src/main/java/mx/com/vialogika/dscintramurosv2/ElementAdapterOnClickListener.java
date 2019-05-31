package mx.com.vialogika.dscintramurosv2;

import android.view.View;

public class ElementAdapterOnClickListener implements View.OnClickListener {
    int mPosition;

    public ElementAdapterOnClickListener(int position){
        this.mPosition = position;
    }

    @Override
    public void onClick(View v) {

    }
}

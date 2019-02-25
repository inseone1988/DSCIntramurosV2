package mx.com.vialogika.dscintramurosv2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mx.com.vialogika.dscintramurosv2.Adapters.PlantillaReportViewAdapter;
import mx.com.vialogika.dscintramurosv2.Room.Apostamiento;
import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Person;
import mx.com.vialogika.dscintramurosv2.Room.Plantilla;

public class PlantillaEdit extends AppCompatActivity {

    private Toolbar toolbar;
    private String grupo;
    private String turno;
    private DatabaseOperations dbo;

    private List<Apostamiento> aps = new ArrayList<>();
    private List<ApostamientoReportView> dataset = new ArrayList<>();
    private List<Person> persons = new ArrayList<>();
    private List<Guard> guards = new ArrayList<>();
    private List<Plantilla> plantilla = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla_edit);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getIntentExtras();
        getitems();
        setToolbarTitle();
        dbo = DatabaseOperations.getInstance(this);
    }

    private void getObjects(){
        dbo.getApostamientos(new DatabaseOperations.backgroundOperation() {
            @Override
            public void onOperationFinished(Object callbackResult) {
                List<Apostamiento> apostamientos = (List<Apostamiento>) callbackResult;
                if (apostamientos != null){
                    aps.addAll(apostamientos);
                }
            }
        });
    }

    private void setupApostamientosViewclas(){
        for (int i = 0; i < aps.size(); i++) {

        }
    }

    private void setToolbarTitle(){
        toolbar.setTitle("edicion de plantilla");
    }

    private void getitems(){
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(listener);
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.fab:
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    break;
            }
        }
    };

    private void getIntentExtras(){
        Intent intent = getIntent();
        if (intent.hasExtra("grupo")){
            grupo = intent.getStringExtra("grupo");
            turno = intent.getStringExtra("turno");
            Toast.makeText(this, grupo +turno, Toast.LENGTH_SHORT).show();
        }else{
            finish();
            Log.d("Plantilla Edit","No se ha podido obtener el grupo a editar");
        }
    }

}

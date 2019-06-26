package mx.com.vialogika.dscintramurosv2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Database;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import mx.com.vialogika.dscintramurosv2.Adapters.PlantillaReportViewAdapter;
import mx.com.vialogika.dscintramurosv2.Dialogs.EditAptsDialog;
import mx.com.vialogika.dscintramurosv2.Network.NetworkOperations;
import mx.com.vialogika.dscintramurosv2.Room.Apostamiento;
import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.Room.Guard;
import mx.com.vialogika.dscintramurosv2.Room.Incidencia;
import mx.com.vialogika.dscintramurosv2.Room.Plantilla;
import mx.com.vialogika.dscintramurosv2.Utils.CryptoHash;
import mx.com.vialogika.dscintramurosv2.Utils.TimeUtils;

public class PlantillaEdit extends AppCompatActivity {

    private Toolbar            toolbar;
    private String             grupo;
    private String             turno;
    private DatabaseOperations dbo;

    private RecyclerView               rv;
    private PlantillaReportViewAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private List<Apostamiento>           aps       = new ArrayList<>();
    private List<ApostamientoReportView> dataset   = new ArrayList<>();
    private List<Guard>                  guards    = new ArrayList<>();
    private List<Plantilla>              plantilla = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plantilla_edit);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getIntentExtras();
        getitems();
        setToolbarTitle();
        dbo = DatabaseOperations.getInstance();
        getObjects();
    }

    private void getObjects() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                dbo.getApostamientos(grupo, new DatabaseOperations.backgroundOperation() {
                    @Override
                    public void onOperationFinished(Object callbackResult) {
                        Object[]           response   = (Object[]) callbackResult;
                        List<Apostamiento> lAps       = (List<Apostamiento>) response[0];
                        List<Plantilla>    rPlantilla = (List<Plantilla>) response[1];
                        List<Guard>        lGuards    = (List<Guard>) response[2];
                        if (aps != null) aps.addAll(lAps);
                        if (rPlantilla != null) plantilla.addAll(rPlantilla);
                        if (lGuards != null) guards.addAll(lGuards);
                        addDataToView();
                    }
                });
            }
        }).start();

    }

    private void addDataToView() {
        if (plantilla.size() > 0) {
            getAlreadyReportedAps();
        } else {
            for (int i = 0; i < aps.size(); i++) {
                dataset.add(new ApostamientoReportView(aps.get(i)));
            }
        }
        Handler h = DatabaseOperations.getHandler();
        h.post(new Runnable() {
            @Override
            public void run() {
                if (adapter == null) {
                    setupRV();
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void getAlreadyReportedAps() {
        for (int i = 0; i < aps.size(); i++) {
            Apostamiento ap = aps.get(i);
            if (!ap.isAlreadyInView()) {
                dataset.add(new ApostamientoReportView(ap));
                ap.setAlreadyInView(true);
            }
        }
        for (int i = 0; i < plantilla.size(); i++) {
            Plantilla current = plantilla.get(i);
            for (int j = 0; j < dataset.size(); j++) {
                ApostamientoReportView repView = dataset.get(j);
                if (repView.getApostamientoId() == Integer.valueOf(current.getEdoFuerzaPlaceId())) {
                    repView.addGuard(getAlreadyAsignedGuardByHash(current.getEdoFuerzaGuardId()));
                }
            }
        }
    }

    private Guard getAlreadyAsignedGuardByHash(String hash) {
        for (int i = 0; i < guards.size(); i++) {
            Guard current = guards.get(i);
            if(current.getGuardHash() == null){
                current.setGuardStatus(0);
                current.save();
                continue;
            }
            if (current.getGuardHash().equals(hash)) {
                current.setAsigned(true);
                return current;
            }
        }
        return null;
    }

    private boolean APAlreadyReported(int placeId) {
        for (int i = 0; i < dataset.size(); i++) {
            Apostamiento currentMAp = dataset.get(i).getApostamiento();
            if (currentMAp.getPlantillaPlaceId() == placeId) {
                return true;
            }
        }
        return false;
    }

    private Apostamiento getApById(int apId) {
        for (int i = 0; i < aps.size(); i++) {
            Apostamiento current = aps.get(i);
            if (current.getPlantillaPlaceId() == apId) {
                return current;
            }
        }
        return null;
    }

    private void setToolbarTitle() {
        toolbar.setTitle("Edicion de plantilla");
    }

    private void getitems() {
        rv = findViewById(R.id.apostamiento_edit_rv);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(listener);
    }

    private void setupRV() {
        adapter = new PlantillaReportViewAdapter(dataset, new PlantillaReportViewAdapter.AdapterCallbacks() {
            @Override
            public void onReportViewClick(int arvpos, int gpos, String guardname) {
                if (dataset.get(arvpos).getGuards().size() > 0) {
                    int gid = dataset.get(arvpos).getGuards().get(gpos).getGuardId();
                    if (!guardname.equals("Apostamiento sin guardias")) {
                        removeGuardFromPlantilla(arvpos, gpos, gid);
                    }
                } else {
                    Toast.makeText(PlantillaEdit.this, "No hay guardias en el apostamiento", Toast.LENGTH_SHORT).show();
                }
            }
        });
        layoutManager = new LinearLayoutManager(this);
        rv.setAdapter(adapter);
        rv.setLayoutManager(layoutManager);
    }

    private void removeGuardFromPlantilla(final int arvpos, final int gpos, final int gid) {
        AlertDialog.Builder adb       = new AlertDialog.Builder(this);
        String              guardname = dataset.get(arvpos).getGuards().get(gpos).getPaersonData().getPersonFullName();
        adb.setTitle("Confirmacion")
                .setMessage("Desea quitar el elemento " + guardname + " de la plantilla?")
                .setNegativeButton(android.R.string.cancel, null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbo.removeguardfromPlantilla(gid, grupo);
                        dataset.get(arvpos).getGuards().remove(gpos);
                        adapter.notifyDataSetChanged();
                    }
                })
                .show();
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.fab:
                    showAsignApostamientoDialog();
                    break;
            }
        }
    };

    private void showAsignApostamientoDialog() {
        EditAptsDialog aptsAssignDialog = new EditAptsDialog();
        aptsAssignDialog.setApostamientos(aps);
        aptsAssignDialog.setGuards(guards);
        aptsAssignDialog.setCallback(new EditAptsDialog.PlantillaAsignment() {
            @Override
            public void onAddApostamienti(int guardId, int apId, Incidencia incidencia) {
                addApostamiento(guardId, apId, incidencia);
            }
        });
        aptsAssignDialog.show(getSupportFragmentManager(), "ASSIGN_APTS");
    }

    private void addApostamiento(int gid, int apid, Incidencia incidencia) {
        Guard g = getGuardById(gid);
        //TODO: Obtener provedor y site por SP
        Plantilla plantilla = new Plantilla();
        plantilla.setId(TimeUtils.unixWithSalt());
        plantilla.setEdoFuerzaProviderId("1");
        plantilla.setEdoFuerzaSiteId("1");
        plantilla.setEdoFuerzaGuardId(CryptoHash.sha1(String.valueOf(gid)));
        plantilla.setEdoFuerzaPlaceId(String.valueOf(apid));
        plantilla.setEdoFuerzaTurno(grupo);
        plantilla.setEdoFuerzaTiempo(turno);
        plantilla.setEdoFuerzaGuardJob(g.getGuardRange());
        plantilla.setEdoFuerzaDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH).format(new Date()));
        savePlantillaPlace(plantilla);
        addGuardToView(apid, gid);
        if (incidencia.isValidIncidence()) {
            plantilla.setEdoFuerzaIncidenceId(incidencia.getProviderIncidencesUuid());
            saveIncidence(incidencia);
        }
    }

    private void savePlantillaPlace(Plantilla pl) {
        dbo.savePlantillaPlace(pl);
    }

    private void addGuardToView(int apid, int guardid) {
        Guard g = getGuardById(guardid);
        for (int i = 0; i < dataset.size(); i++) {
            ApostamientoReportView c = dataset.get(i);
            if (c.getApostamientoId() == apid) {
                c.addGuard(g);
                g.setAsigned(true);
            }
        }
        adapter.notifyDataSetChanged();
    }

    private Guard getGuardById(int gid) {
        for (int i = 0; i < guards.size(); i++) {
            Guard c = guards.get(i);
            if (c.getGuardId() == gid) {
                return c;
            }
        }
        return null;
    }

    public void saveIncidence(Incidencia incidencia) {
        dbo.saveIncidence(incidencia);
    }

    private void getIntentExtras() {
        Intent intent = getIntent();
        if (intent.hasExtra("grupo")) {
            grupo = intent.getStringExtra("grupo");
            turno = intent.getStringExtra("turno");
            Toast.makeText(this, grupo + turno, Toast.LENGTH_SHORT).show();
        } else {
            finish();
            Log.d("Plantilla Edit", "No se ha podido obtener el grupo a editar");
        }
    }

    private void confirmSendPantillaToServer() {
        new AlertDialog.Builder(this)
                .setTitle("Enviar plantilla")
                .setMessage("¿Deseas reportar la plantilla ahora?")
                .setNegativeButton(android.R.string.cancel,null)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sendPlantillaToServer();
                    }
                })
                .show();
    }

    private void sendPlantillaToServer() {
        NetworkOperations nop = NetworkOperations.getInstance();
        nop.sendPlantilla(grupo);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case NetworkOperations.SEND_PLANTILLA_TO_SERVER:
                confirmSendPantillaToServer();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuItem item = menu.add(Menu.NONE, NetworkOperations.SEND_PLANTILLA_TO_SERVER, Menu.NONE, "Enviar plantilla");
        item.setIcon(R.drawable.ic_cloud_upload_black_24dp);
        item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }


}

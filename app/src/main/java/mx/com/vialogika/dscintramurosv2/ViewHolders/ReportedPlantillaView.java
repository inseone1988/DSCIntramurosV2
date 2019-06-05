package mx.com.vialogika.dscintramurosv2.ViewHolders;

import android.content.Context;

import mx.com.vialogika.dscintramurosv2.Room.DatabaseOperations;
import mx.com.vialogika.dscintramurosv2.User;

public class ReportedPlantillaView {

    private int groupNumber;
    private String siteName;
    private String provider;
    private String grupo;
    private int groupTotal;
    private int groupReported;

    private ReportedPlantillaView(String siteName,String provider,String grupo,int[] grupoTotals) {
        this.siteName = siteName;
        this.provider = provider;
        this.grupo = grupo;
        this.groupTotal = grupoTotals[1];
        this.groupReported = grupoTotals[0];
    }

    public static ReportedPlantillaView getInstance(Context context, final String grupo){
        final User                  userdata = new User(context);
        DatabaseOperations          dbo      = DatabaseOperations.getInstance();
        final ReportedPlantillaView plstatus = new ReportedPlantillaView(userdata.getUserSite(),userdata.getUsersCorp(),grupo,new int[]{0,0});
        dbo.getGroupData(grupo, new DatabaseOperations.backgroundOperation() {
            @Override
            public void onOperationFinished(Object callbackResult) {
                int[] result = (int[])callbackResult;
                plstatus.setGroupTotal(result[1]);
                plstatus.setGroupReported(result[0]);
            }
        });
        return plstatus;
    }

    public int getGroupNumber() {
        return groupNumber;
    }

    public void setGroupNumber(int groupNumber) {
        this.groupNumber = groupNumber;
    }

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public int getGroupTotal() {
        return groupTotal;
    }

    public void setGroupTotal(int groupTotal) {
        this.groupTotal = groupTotal;
    }

    public int getGroupReported() {
        return groupReported;
    }

    public void setGroupReported(int groupReported) {
        this.groupReported = groupReported;
    }
}

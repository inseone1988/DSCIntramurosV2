package mx.com.vialogika.dscintramurosv2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.otaliastudios.cameraview.CameraListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.FileCallback;
import com.otaliastudios.cameraview.PictureResult;

import java.io.File;

import mx.com.vialogika.dscintramurosv2.Utils.FileUtils;

public class GuardPhotoTake extends AppCompatActivity {

    private CameraView cameraView;
    private FloatingActionButton takepictureFAB;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_photo_take);
        getItems();
    }

    private void getItems(){
        cameraView = findViewById(R.id.camera);
        cameraView.addCameraListener(new CameraListener() {
            @Override
            public void onPictureTaken(@NonNull final PictureResult result) {
                if (result.isSnapshot()){
                    File profileHolder = FileUtils.createProfileImageFile();
                    result.toFile(profileHolder, new FileCallback() {
                        @Override
                        public void onFileReady(@Nullable File file) {
                            Intent data = new Intent();
                            data.putExtra("path",file.getAbsolutePath());
                            setResult(RESULT_OK,data);
                            finish();
                        }
                    });
                }
            }
        });
        takepictureFAB = findViewById(R.id.floatingActionButton2);
        takepictureFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraView.takePictureSnapshot();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cameraView.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraView.destroy();
    }
}

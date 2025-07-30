package com.christopheraldoo.messagedialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        System.out.println("Oncreate");
    }

    public void showToast(String pesan) {
        Toast.makeText(this, pesan, Toast.LENGTH_SHORT).show();
    }
    public void showAlert(String pesan) {
        AlertDialog.Builder buatAlert = new AlertDialog.Builder( this);
        buatAlert.setTitle("PERHATIAN !!");
        buatAlert.setMessage(pesan);

        buatAlert.show();
    }
    public void showAlertButton(String pesan) {
        AlertDialog.Builder showAlert = new AlertDialog.Builder(this);
        showAlert.setTitle("Peringatan !!");
        showAlert.setMessage(pesan);

        showAlert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showToast("Data Sudah di Hapus");
            }
        });
        showAlert.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showToast("Data tidak di hapus");
            }
        });
        showAlert.show();
    }
    public void btnToast(View view) {
        showToast("BELAJAR PESAN MENGGUNAKAN TOAST");
    }
    
    public void btnAlert(View view) {
        showAlert("Belajar Pesan menggunakan Alert");
    }
    
    public void btnAlertDialogButton(View view) {
        showAlertButton("yakin akan menghapus??");
    }
}
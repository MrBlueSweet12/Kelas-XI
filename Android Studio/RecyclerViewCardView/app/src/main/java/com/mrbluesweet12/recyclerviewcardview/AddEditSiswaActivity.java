package com.mrbluesweet12.recyclerviewcardview;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditSiswaActivity extends AppCompatActivity {
    
    private EditText etNama, etAlamat;
    private Button btnSimpan, btnBatal;
    private DatabaseHelper databaseHelper;
    private boolean isEditMode = false;
    private int siswaId = -1;
    
    public static final String EXTRA_SISWA_ID = "extra_siswa_id";
    public static final String EXTRA_NAMA = "extra_nama";
    public static final String EXTRA_ALAMAT = "extra_alamat";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_siswa);
        
        initViews();
        initDatabase();
        checkEditMode();
        setupClickListeners();
    }
    
    private void initViews() {
        etNama = findViewById(R.id.et_nama);
        etAlamat = findViewById(R.id.et_alamat);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnBatal = findViewById(R.id.btn_batal);
    }
    
    private void initDatabase() {
        databaseHelper = new DatabaseHelper(this);
    }
    
    private void checkEditMode() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            siswaId = extras.getInt(EXTRA_SISWA_ID, -1);
            if (siswaId != -1) {
                isEditMode = true;
                setTitle("Edit Siswa");
                
                // Load existing data
                String nama = extras.getString(EXTRA_NAMA, "");
                String alamat = extras.getString(EXTRA_ALAMAT, "");
                
                etNama.setText(nama);
                etAlamat.setText(alamat);
                btnSimpan.setText("Update");
            } else {
                setTitle("Tambah Siswa");
                btnSimpan.setText("Simpan");
            }
        } else {
            setTitle("Tambah Siswa");
            btnSimpan.setText("Simpan");
        }
    }
    
    private void setupClickListeners() {
        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSiswa();
            }
        });
        
        btnBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    
    private void saveSiswa() {
        String nama = etNama.getText().toString().trim();
        String alamat = etAlamat.getText().toString().trim();
        
        // Validasi input
        if (TextUtils.isEmpty(nama)) {
            etNama.setError("Nama tidak boleh kosong");
            etNama.requestFocus();
            return;
        }
        
        if (TextUtils.isEmpty(alamat)) {
            etAlamat.setError("Alamat tidak boleh kosong");
            etAlamat.requestFocus();
            return;
        }
        
        if (nama.length() < 2) {
            etNama.setError("Nama minimal 2 karakter");
            etNama.requestFocus();
            return;
        }
        
        if (alamat.length() < 5) {
            etAlamat.setError("Alamat minimal 5 karakter");
            etAlamat.requestFocus();
            return;
        }
        
        // Create or update siswa
        Siswa siswa = new Siswa(nama, alamat);
        
        if (isEditMode) {
            // Update existing siswa
            siswa.setId(siswaId);
            int result = databaseHelper.updateSiswa(siswa);
            if (result > 0) {
                Toast.makeText(this, "Data siswa berhasil diperbarui", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Gagal memperbarui data siswa", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new siswa
            long result = databaseHelper.addSiswa(siswa);
            if (result != -1) {
                Toast.makeText(this, "Data siswa berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Gagal menambahkan data siswa", Toast.LENGTH_SHORT).show();
            }
        }
    }
    
    @Override
    protected void onDestroy() {
        if (databaseHelper != null) {
            databaseHelper.close();
        }
        super.onDestroy();
    }
}

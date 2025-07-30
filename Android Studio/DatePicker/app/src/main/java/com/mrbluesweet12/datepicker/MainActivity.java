package com.mrbluesweet12.datepicker;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private EditText editTextDate;
    private Calendar calendar;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inisialisasi view
        initViews();

        // Setup date format
        dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        calendar = Calendar.getInstance();
    }

    private void initViews() {
        editTextDate = findViewById(R.id.editTextDate);

        // Set click listener untuk EditText
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    private void showDatePickerDialog() {
        // Mendapatkan tanggal saat ini
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        // Membuat DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Update calendar dengan tanggal yang dipilih
                        calendar.set(Calendar.YEAR, selectedYear);
                        calendar.set(Calendar.MONTH, selectedMonth);
                        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);

                        // Format tanggal dan set ke EditText
                        String selectedDate = dateFormat.format(calendar.getTime());
                        editTextDate.setText(selectedDate);

                        // Tampilkan toast sebagai konfirmasi
                        Toast.makeText(MainActivity.this, "Tanggal dipilih: " + selectedDate, Toast.LENGTH_SHORT).show();
                    }
                },
                year, month, dayOfMonth
        );

        // Opsional: Set batasan tanggal (uncomment jika diperlukan)
        // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis()); // Tidak bisa pilih tanggal sebelum hari ini
        // datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 365)); // Maksimal 1 tahun ke depan

        // Tampilkan dialog
        datePickerDialog.show();
    }
}
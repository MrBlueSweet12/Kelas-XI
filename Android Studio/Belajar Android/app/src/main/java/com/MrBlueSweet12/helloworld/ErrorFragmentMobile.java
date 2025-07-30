package com.MrBlueSweet12.helloworld;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Fragment untuk menampilkan pesan error pada aplikasi mobile
 */
public class ErrorFragmentMobile extends Fragment {
    private static final String TAG = "ErrorFragmentMobile";
    
    private TextView messageTextView;
    private Button dismissButton;
    private ImageView errorImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate");
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_error_mobile, container, false);
        
        messageTextView = view.findViewById(R.id.error_message);
        dismissButton = view.findViewById(R.id.dismiss_button);
        errorImageView = view.findViewById(R.id.error_image);
        
        // Set pesan error default
        messageTextView.setText(getResources().getString(R.string.error_fragment_message));
        
        // Set gambar error
        errorImageView.setImageResource(android.R.drawable.ic_dialog_alert);
        
        // Set listener untuk tombol dismiss
        dismissButton.setText(getResources().getString(R.string.dismiss_error));
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getParentFragmentManager().beginTransaction().remove(ErrorFragmentMobile.this).commit();
            }
        });
        
        return view;
    }
    
    /**
     * Set pesan error kustom
     */
    public void setErrorMessage(String message) {
        if (messageTextView != null) {
            messageTextView.setText(message);
        }
    }
}
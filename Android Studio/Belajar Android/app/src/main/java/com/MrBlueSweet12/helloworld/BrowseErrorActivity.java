package com.MrBlueSweet12.helloworld;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;

/**
 * BrowseErrorActivity menampilkan contoh penggunaan ErrorFragment
 * untuk aplikasi mobile
 */
public class BrowseErrorActivity extends AppCompatActivity {
    private static final int TIMER_DELAY = 3000;
    private static final int SPINNER_WIDTH = 100;
    private static final int SPINNER_HEIGHT = 100;

    private ErrorFragmentMobile mErrorFragment;
    private SpinnerFragment mSpinnerFragment;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_error);
        
        // Tampilkan pesan bahwa ini adalah halaman error
        Toast.makeText(this, "Halaman Error Demo", Toast.LENGTH_SHORT).show();
        
        // Tampilkan error fragment
        testError();
    }

    private void testError() {
        mErrorFragment = new ErrorFragmentMobile();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_browse_fragment, mErrorFragment)
                .commit();

        mSpinnerFragment = new SpinnerFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.main_browse_fragment, mSpinnerFragment)
                .commit();

        final Handler handler = new Handler(Looper.myLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getSupportFragmentManager()
                        .beginTransaction()
                        .remove(mSpinnerFragment)
                        .commit();
                // Tampilkan pesan error kustom
                mErrorFragment.setErrorMessage("Terjadi kesalahan saat memuat data");
            }
        }, TIMER_DELAY);
    }

    public static class SpinnerFragment extends Fragment {
        @Override
        public View onCreateView(
                LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            ProgressBar progressBar = new ProgressBar(container.getContext());
            if (container instanceof FrameLayout) {
                FrameLayout.LayoutParams layoutParams =
                        new FrameLayout.LayoutParams(SPINNER_WIDTH, SPINNER_HEIGHT, Gravity.CENTER);
                progressBar.setLayoutParams(layoutParams);
            }
            return progressBar;
        }
    }
}
package home.stanislavpoliakov.meet16_practice;

import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/**
 * Стартовая точка нашей программы. Инициализируем фрагменты и погнали уже во фрагментах (ожидание ввода)
 */
public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        fragmentManager.beginTransaction()
                .add(R.id.upperFrame, UpperFragment.newInstance(), "upper")
                .add(R.id.lowerFrame, LowerFragment.newInstance(), "lower")
                .commitNow();
    }
}

package tech.xiaosuo.com.switchbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SwitchButtonView.SwitchButtonStatusChangedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchButtonView switchButtonView = findViewById(R.id.swb_view);
        switchButtonView.setChecked(true);
        switchButtonView.setSwitchButtonStatusChangedListener(this);
    }

    @Override
    public void OnSwitchButtonCheckedStatusChanged() {
        Toast.makeText(getApplicationContext()," swith button click",Toast.LENGTH_SHORT).show();
    }
}

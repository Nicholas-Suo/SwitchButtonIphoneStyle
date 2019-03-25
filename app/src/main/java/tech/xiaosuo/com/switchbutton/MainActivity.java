package tech.xiaosuo.com.switchbutton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SwitchButtonView.SwitchButtonClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SwitchButtonView switchButtonView = findViewById(R.id.swb_view);
        switchButtonView.setChecked(true);
        switchButtonView.setOnSwitchButtonListener(this);
    }

    @Override
    public void OnSwitchButtonClick() {
        Toast.makeText(getApplicationContext()," swith button click",Toast.LENGTH_SHORT).show();
    }
}

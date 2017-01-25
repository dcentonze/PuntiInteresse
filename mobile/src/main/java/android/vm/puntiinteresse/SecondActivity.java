package android.vm.puntiinteresse;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SecondActivity extends Activity {

    TextView nameTv;
    Intent intent;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        nameTv=(TextView)findViewById(R.id.name_tv);
        intent= getIntent();
        username=intent.getStringExtra("username");
        nameTv.setText(username);
    }


}

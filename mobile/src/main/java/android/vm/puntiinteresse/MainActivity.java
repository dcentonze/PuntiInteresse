package android.vm.puntiinteresse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by acbes on 25/01/2017.
 */

public class MainActivity extends Activity {

    TextView welcomeTv;
    Button changeBt;
    EditText changeTextEdit;
    MainActivity activity = this;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        welcomeTv=(TextView)findViewById(R.id.welcome_tv);
        changeBt=(Button)findViewById(R.id.change_bt);
        changeTextEdit=(EditText)findViewById(R.id.edit_txt);
        changeBt.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                //welcomeTv.setText(changeTextEdit.getText());
                //username=changeTextEdit.getText().toString();
                Intent intent = new Intent(activity,SecondActivity.class);
                //intent.putExtra("username",username);
                startActivity(intent);
            }
        });

    }



}

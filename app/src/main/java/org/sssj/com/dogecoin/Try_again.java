package org.sssj.com.dogecoin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Try_again extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_try_again);

        Button btntryAgain = (Button) findViewById(R.id.btntryagain);
        btntryAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent in=new Intent(Try_again.this, MainActivity.class);
                startActivity(in);
            }
        });
    }
}

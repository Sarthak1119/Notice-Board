package starlabs.noticeboard;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddActivity extends AppCompatActivity {

    Button btn_cs,btn_ec,btn_me,btn_ce,btn_ee;
     TextView text_link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        btn_cs = findViewById(R.id.btn_cs);
        btn_ec = findViewById(R.id.btn_ec);
        btn_me = findViewById(R.id.btn_me);
        btn_ce = findViewById(R.id.btn_ce);
        btn_ee = findViewById(R.id.btn_ee);


        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        btn_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cs = new Intent(AddActivity.this, CsActivity.class);
                startActivity(cs);


            }
        });

        btn_ec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ec = new Intent(AddActivity.this, EcActivity.class);
                startActivity(ec);

            }
        });

        btn_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent me = new Intent(AddActivity.this, MeActivity.class);
                startActivity(me);
            }
        });

        btn_ce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ce = new Intent(AddActivity.this, CeActivity.class);
                startActivity(ce);
            }
        });

        btn_ee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent ee = new Intent(AddActivity.this, EeActivity.class);
                startActivity(ee);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_add, menu);
        return super.onCreateOptionsMenu(menu);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        if(id==R.id.action_notification)
        {
            gotonotication();
        }
        return super.onOptionsItemSelected(item);
    }

    private void gotonotication() {
        startActivity(new Intent(AddActivity.this,NoticeActivity.class));
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(AddActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }


}

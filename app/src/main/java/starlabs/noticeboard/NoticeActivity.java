package starlabs.noticeboard;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class NoticeActivity extends AppCompatActivity {

   

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice);


        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().replace(R.id.frame, new BaseFragment()).commit();

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_notice, menu);
        return super.onCreateOptionsMenu(menu);

    }

        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_logout) {
                logout();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

    private void logout() {
             FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(NoticeActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
    }


}


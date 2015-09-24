package zhou.gank.io

import android.content.Intent
import android.os.Bundle
import android.support.annotation.Nullable
import android.support.v7.app.AppCompatActivity
import android.view.View
import groovy.transform.CompileStatic
import zhou.gank.io.data.DataManager
import zhou.gank.io.data.TypeProvider
import zhou.gank.io.model.Gank
import zhou.gank.io.ui.activity.HomeActivity

@CompileStatic
class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main);
    }

    public void Open(View view) {
        startActivity(new Intent(this, HomeActivity.class));
    }
}
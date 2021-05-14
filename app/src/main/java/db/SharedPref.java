package db;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.app.teacher.R;

public class SharedPref {

    Context context;
    SharedPreferences sharedPreferences;

    public SharedPref(Context context){
        this.context = context;
        sharedPreferences = this.context.getSharedPreferences("MyTeacherApp", Context.MODE_PRIVATE);
    }

    public void saveToken(String token) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("UserToken", token);
        edit.commit();
        Toast.makeText(context, context.getResources().getString(R.string.dataSaved), Toast.LENGTH_SHORT).show();
    }

    public String loadToken(){
        return sharedPreferences.getString("UserToken","");
    }
}

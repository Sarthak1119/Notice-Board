package starlabs.noticeboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by Rishabh on 29-03-2018.
 */

public class SharedData {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //to secure tha data
    int mode=0;

    //context to take area of another class
    Context context;
    //to take the sharedPreferences file name
    String Filename="NoticeBoard";
    //to take the boolean variable name
    String data="data";

    //to create constructor for saadifile


    public SharedData(Context context) {
        this.context = context;
        //to create SharedPreferences file
        sharedPreferences=context.getSharedPreferences(Filename,mode);

        //to apply the crud operation on saadifile
        editor=sharedPreferences.edit();
    }

    //for the second time user
    public void secondTime()
    {
        //to create the boolean value as true
        editor.putBoolean(data,true);
        //to save the operation
        editor.commit();
    }

    //for first time user
    public void firstTime()
    {
        if(!this.defaultboolean())
        {
            Intent register=new Intent(context,MainActivity.class);
            register.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            register.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            context.startActivity(register);
        }
    }

    private boolean defaultboolean() {
        return sharedPreferences.getBoolean(data,false);
    }


}

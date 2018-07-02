package com.yevzor.lutsak.weather;

import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, ">> onCreate");

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, new WeatherFragment())
                .commit();

        Log.d(TAG, "<< onCreate");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        Log.d(TAG, "onCreateOptionsMenu");

        getMenuInflater().inflate(R.menu.weather, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Log.d(TAG, "onOptionsItemSelected");

        if(item.getItemId() == R.id.change_city){
            showInputDialog();
        }
        return false;
    }

    private void showInputDialog(){
        Log.d(TAG, "showInputDialog");

        AlertDialog.Builder builder;
        final EditText editText;

        builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.change_city));

        editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_TEXT);

        builder.setView(editText);
        builder.setPositiveButton(getResources().getString(R.string.change_city),
                new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                changeCity(editText.getText().toString());
            }
        });

        builder.show();
    }

    public void changeCity(String city){
        Log.d(TAG, "changeCity");

        WeatherFragment weatherFragment;
        CityPreference cityPreference;

        weatherFragment =
                (WeatherFragment)getSupportFragmentManager().findFragmentById(R.id.container);
        weatherFragment.changeCity(city);
        cityPreference = new CityPreference(this);
        cityPreference.setCity(city);
    }
}
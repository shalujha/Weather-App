package com.example.weather_app;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView t,t1,t2;
    EditText editText;
    String city="";
    public void clickFunction(View view){
        t=(TextView)findViewById(R.id.weather_content); // textview at which we have to set content:
        t1=findViewById(R.id.Temperature_content);
        t2=findViewById(R.id.Country_content);
        editText=(EditText)findViewById(R.id.editText);
        city=editText.getText().toString();
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(),0);
        DownloadTask task=new DownloadTask();
        String url="https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=4c1c4f025b47879106e2030ee6175c4e";
        try {
            String result=task.execute(url).get();
            Log.i("Result :",result);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Result : ","Failed");
        }
    }
    class DownloadTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            Log.i("url : ",strings[0]);
            URL url;
            HttpURLConnection urlConnection;
            String result="";
            try {
                url=new URL(strings[0]);
                urlConnection=(HttpURLConnection)url.openConnection();
                InputStream in=urlConnection.getInputStream();
                InputStreamReader reader=new InputStreamReader(in);
                int data=reader.read();
                while(data!=-1){
                    char current=(char)data;
                    result+=current;
                    data=reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject=new JSONObject(s);
                String weather_info=jsonObject.getString("weather");
                JSONArray jsonArray=new JSONArray(weather_info);
                int count=0;
                for(int i=0;i<jsonArray.length();i++){
                    JSONObject jsonpart=jsonArray.getJSONObject(i);

                    t.setText(jsonpart.getString("main"));

                   // Log.i("description",jsonpart.getString("description"));
                   // count+=1;
                    t.setText("weather : "+(jsonpart.getString("description")));
                }
                JSONObject temp_obj=jsonObject.getJSONObject("main");
                t1.setText("Temperature : "+temp_obj.getString("temp"));
                JSONObject country_obj=jsonObject.getJSONObject("sys");
                t2.setText("Country : "+country_obj.getString("country"));
            //    String temp_info=temp_obj.getString("main");
                /*
                 JSONArray temp_jsonArray=new JSONArray(temp_obj);
                for(int i=0;i<temp_jsonArray.length();i++){
                    String jsonpart= (String) temp_jsonArray.get(i);
                    Log.i("Key : ", jsonpart);
                  //  t.setText("Temperature : "+jsonpart.getString("temp"));
                  //  t.setText("Pressure : "+jsonpart.getString("humidity"));
                  //  t.setText("Humidity : "+jsonpart.getString("pressure"));
                    // Log.i("description",jsonpart.getString("description"));
                    // count+=1;
                 //   t.setText("weather : "+(jsonpart.getString("description")));
                } */
/*
                List<String>list_of_required_key_info=new ArrayList<>();
                list_of_required_key_info.add("weather");
                list_of_required_key_info.add("coord");
                list_of_required_key_info.add("main");
                list_of_required_key_info.add("wind");
                list_of_required_key_info.add("clouds");
                for(int i=0;i<list_of_required_key_info.size();i++){
                    Log.i("Key : ", list_of_required_key_info.get(i));
                    String weather_info=jsonObject.getString(list_of_required_key_info.get(i));
                    JSONArray jsonArray=new JSONArray(weather_info);
                    for(int j=0;j<jsonArray.length();j++){
                        JSONObject jsonpart=jsonArray.getJSONObject(i);
                        if(weather_info.equals("weather")){
                            t.setText(jsonpart.getString("main"));
                            t.setText(jsonpart.getString("description"));
                        }else if(weather_info.equals("coord")){
                            t.setText(jsonpart.getString("lon"));
                            t.setText(jsonpart.getString("lat"));
                        }else if(weather_info.equals("main")){
                            t.setText(jsonpart.getString("temp"));
                            t.setText(jsonpart.getString("pressure"));
                            t.setText(jsonpart.getString("humidity"));
                        }else if(weather_info.equals("wind")){
                            t.setText(jsonpart.getString("speed"));
                            t.setText(jsonpart.getString("deg"));
                        }else if(weather_info.equals("clouds")){
                            t.setText(jsonpart.getString("all"));
                        }
                    }
                }
                 */
                /*
                for (Iterator<String> it = jsonObject.keys(); it.hasNext(); ) {
                    String keys = it.next();
                    Log.i("keys : ",keys);
                    String info=jsonObject.getString(keys);
                    JSONArray jsonArray=new JSONArray(info);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonpart=jsonArray.getJSONObject(i);
                      //  t.setText(jsonpart.getString());

                    }

                }*/


            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Enter valid city name ",Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(s);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
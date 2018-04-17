package com.example.usuari.socket;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    ListView lvtabla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvtabla = this.findViewById(R.id.lvsocket);
    }
    public void enviar(View v){
        EditText edtnum = this.findViewById(R.id.edtTexto);
        String num = edtnum.getText().toString();
        Comunication com = new Comunication();
        com.execute("10.0.2.2","9000",num);
    }
    private class Comunication extends AsyncTask<String, Void,String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Integer[] tabla = null;
            try {
                JSONArray jarray = new JSONArray(s);
                tabla = new Integer[jarray.length()];
                for (int i=0;i<jarray.length();i++){
                    tabla[i]=jarray.getInt(i);
                }
                ArrayAdapter<Integer> adp = new ArrayAdapter<Integer>(MainActivity.this,android.R.layout.simple_list_item_1,tabla);
                lvtabla.setAdapter(adp);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(String... strings) {
            BufferedReader bf = null;
            Socket sc = null;
            String resultado = "";
            try {
                sc = new Socket(strings[0],Integer.parseInt(strings[1]));
                PrintStream ps = new PrintStream(sc.getOutputStream());
                ps.println(strings[2]);
                ps.flush();
                InputStream is = sc.getInputStream();
                bf = new BufferedReader(new InputStreamReader(is));
                resultado=bf.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(bf!=null){
                    try {
                        bf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return resultado;
        }
    }
}

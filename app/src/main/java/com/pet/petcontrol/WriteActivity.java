package com.pet.petcontrol;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class WriteActivity extends AppCompatActivity implements ArquivoDialogListener{

    Calendar myCalendar = Calendar.getInstance();

    EditText etc_nome_adot;
    EditText etc_nome_animal;
    EditText etc_idade;
    EditText etc_carac;
    EditText etc_resp;
    EditText etc_data_cap_cas;
    EditText etc_data_entrega;
    EditText et_aux;

    ProgressDialog pd;

    CheckBox cb_castrado;
    RadioGroup rg_sexo;
    RadioButton rb;
    Button bt_cadastro;

    String data_cap_cas = "";
    String sexo = "Indefinido";
    String idade = "";
    String carac = "";
    String cast = "NÃO";
    String resp = "";
    String data_entrega = "";
    String nome_adot = "";
    String nome_animal = "";
    String adocao = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        loadView();
        etc_data_cap_cas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_aux = findViewById(R.id.etc_data_cap_cas);

                new DatePickerDialog(WriteActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etc_data_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_aux = findViewById(R.id.etc_data_entrega);

                new DatePickerDialog(WriteActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etc_data_cap_cas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et_aux = findViewById(R.id.etc_data_cap_cas);

                    new DatePickerDialog(WriteActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        etc_data_entrega.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et_aux = findViewById(R.id.etc_data_entrega);

                    new DatePickerDialog(WriteActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        bt_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(WriteActivity.this);
                pd.setMessage("Gravando Dados");
                pd.setCancelable(false);
                pd.show();
                new SendRequest().execute();
            }
        });

    }

    public void loadView(){
        etc_nome_adot =(EditText) findViewById(R.id.etc_nome_adot);
        etc_nome_animal =(EditText) findViewById(R.id.etc_nome_animal);
        etc_idade =(EditText) findViewById(R.id.etc_idade);
        etc_carac =(EditText) findViewById(R.id.etc_carac);
        etc_resp =(EditText) findViewById(R.id.etc_resp);
        etc_data_cap_cas =(EditText) findViewById(R.id.etc_data_cap_cas);
        etc_data_entrega =(EditText) findViewById(R.id.etc_data_entrega);
        cb_castrado =(CheckBox) findViewById(R.id.cb_castrado);
        rg_sexo =(RadioGroup) findViewById(R.id.rg_sexo);
        bt_cadastro =(Button) findViewById(R.id.bt_cadastro);
    }

    public void rbclick(View v){
        int rbID = rg_sexo.getCheckedRadioButtonId();
        rb = (RadioButton) findViewById(rbID);
        sexo = rb.getText().toString();
    }

    public void cbclick(View v){
        if(cb_castrado.isChecked())
            cast = "SIM";
        else
            cast = "NÃO";
    }



    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(et_aux);
        }

    };


    private void updateLabel(EditText edittext) {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        edittext.setText(sdf.format(myCalendar.getTime()));
    }

    public void loadData(){
        data_cap_cas = etc_data_cap_cas.getText().toString();
        idade = etc_idade.getText().toString();
        carac = etc_carac.getText().toString();
        resp = etc_resp.getText().toString();
        data_entrega = etc_data_entrega.getText().toString();
        nome_adot = etc_nome_adot.getText().toString();
        nome_animal = etc_nome_animal.getText().toString();
    }

    public class SendRequest extends AsyncTask<String, Void, String> {


        protected void onPreExecute(){}

        protected String doInBackground(String... arg0) {

            try{
                loadData();
                URL url = new URL("https://script.google.com/macros/s/AKfycbwfOQNryyyAxg5ke7OAObivQkoRjB2EAGJxeT7-E62phPtu1Ew/exec");
                // https://script.google.com/macros/s/AKfycbyuAu6jWNYMiWt9X5yp63-hypxQPlg5JS8NimN6GEGmdKZcIFh0/exec
                JSONObject postDataParams = new JSONObject();

                String id= "1pDufFOJy2zFSv8RRrG-5P2FDuMssYPaQWG5nuw8SYVc";

                postDataParams.put("inf",0);
                postDataParams.put("id",id);
                postDataParams.put("data_cap_cas",data_cap_cas);
                postDataParams.put("idade",idade);
                postDataParams.put("carac",carac);
                postDataParams.put("resp",resp);
                postDataParams.put("data_entrega",data_entrega);
                postDataParams.put("nome_adot",nome_adot);
                postDataParams.put("nome_animal",nome_animal);
                postDataParams.put("cast",cast);
                postDataParams.put("sexo",sexo);


                Log.e("params",postDataParams.toString());

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);

                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getPostDataString(postDataParams));

                writer.flush();
                writer.close();
                os.close();

                int responseCode=conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line="";

                    while((line = in.readLine()) != null) {

                        sb.append(line);
                        break;
                    }

                    in.close();
                    return sb.toString();

                }
                else {
                    return new String("false : "+responseCode);
                }
            }
            catch(Exception e){
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            Log.e("resp",result);
            askPhoto();
        }
    }
    public void openDialog(){
        ArquivoDialog arquivoDialog = new ArquivoDialog();
        arquivoDialog.show(getSupportFragmentManager(), "Selecionar Imagem");
    }
    public void askPhoto(){
        if (pd.isShowing())
            pd.dismiss();

        AskPhotoDialog askPhotoDialog = new AskPhotoDialog();
        askPhotoDialog.show(getSupportFragmentManager(), "Deseja anexar uma Imagem?");
    }

    @Override
    public void applyTexts(String escolha) {
        if(escolha.equals("foto"))
            startActivity(new Intent(WriteActivity.this, CameraActivity.class));
        else if(escolha.equals("album"))
            startActivity(new Intent(WriteActivity.this, GalleryActivity.class));
        else if(escolha.equals("want"))
            openDialog();
        else if(escolha.equals("askShare"))
            askShare();
        else if(escolha.equals("share")) {
            startActivity(Intent.createChooser(new Share().Share(), "Share Image"));
            startActivity(new Intent(WriteActivity.this, CameraActivity.class));
        }
        else if(escolha.equals("main"))
            startActivity(new Intent(WriteActivity.this,MainActivity.class));
    }

    private void askShare() {
        ShareDialog shareDialog = new ShareDialog();
        shareDialog.show(getSupportFragmentManager(), "Deseja Compartilhar a Adoção?");
    }


    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while(itr.hasNext()){

            String key= itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }
}

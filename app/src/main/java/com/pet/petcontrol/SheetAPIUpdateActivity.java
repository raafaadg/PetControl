package com.pet.petcontrol;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.material.snackbar.Snackbar;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.pet.petcontrol.util.InternetConnection;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class SheetAPIUpdateActivity extends AppCompatActivity
        implements EasyPermissions.PermissionCallbacks {
    GoogleAccountCredential mCredential;
    ProgressDialog mProgress;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };

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

    int id;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        loadView();
        mProgress = new ProgressDialog(this);
        mProgress.setMessage("Enviando Dados para Planilha");

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());

        etc_data_cap_cas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_aux = findViewById(R.id.etc_data_cap_cas);

                new DatePickerDialog(SheetAPIUpdateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etc_data_entrega.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_aux = findViewById(R.id.etc_data_entrega);

                new DatePickerDialog(SheetAPIUpdateActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        etc_data_cap_cas.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et_aux = findViewById(R.id.etc_data_cap_cas);

                    new DatePickerDialog(SheetAPIUpdateActivity.this, date, myCalendar
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

                    new DatePickerDialog(SheetAPIUpdateActivity.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            }
        });

        bt_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(SheetAPIUpdateActivity.this);
                pd.setMessage("Gravando Dados");
                pd.setCancelable(false);
                pd.show();
                if (InternetConnection.checkConnection(getApplicationContext())) {
                    getResultsFromApi();
                } else {
                    Snackbar.make(v, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
                }
            }
        });
        inputData();
    }

    private void inputData() {

        id = Integer.parseInt(getIntent().getStringExtra("ID"));
        data_cap_cas = getIntent().getStringExtra("Data_cap_cas");
        idade = getIntent().getStringExtra("Idade");
        carac = getIntent().getStringExtra("Carac");
        resp = getIntent().getStringExtra("Resp");
        data_entrega = getIntent().getStringExtra("Data_entrega");
        nome_adot = getIntent().getStringExtra("Nome_adot");
        nome_animal = getIntent().getStringExtra("Nome_animal");
        cast = getIntent().getStringExtra("Cast");
        sexo = getIntent().getStringExtra("Sexo");
        adocao = getIntent().getStringExtra("Adocao");

        etc_data_cap_cas.setText(data_cap_cas);
        etc_idade.setText(idade);
        etc_carac.setText(carac);
        etc_resp.setText(resp);
        etc_data_entrega.setText(data_entrega);
        etc_nome_adot.setText(nome_adot);
        etc_nome_animal.setText(nome_animal);
        cb_castrado.setChecked(cast.equals("SIM"));
        if(sexo.equals("Fêmea"))
            rg_sexo.check(R.id.rb_femea);
        if(sexo.equals("Macho"))
            rg_sexo.check(R.id.rb_macho);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
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

    public List<String> loadData(List<String> aux){

        data_cap_cas = etc_data_cap_cas.getText().toString();
        idade = etc_idade.getText().toString();
        carac = etc_carac.getText().toString();
        resp = etc_resp.getText().toString();
        data_entrega = etc_data_entrega.getText().toString();
        nome_adot = etc_nome_adot.getText().toString();
        nome_animal = etc_nome_animal.getText().toString();
        aux.add(data_cap_cas);
        aux.add(sexo);
        aux.add(idade);
        aux.add(carac);
        aux.add(cast);
        aux.add(resp);
        aux.add(data_entrega);
        aux.add(nome_adot);
        aux.add(nome_animal);

        return aux;
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
        bt_cadastro.setText("Atualizar Dado");
    }

    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    private void getResultsFromApi() {
        if (! isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
            Log.e("api","No network connection available.");

        } else {
            new MakeRequestTask(mCredential).execute();
        }
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi();
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    Log.e("api",
                            "This app requires Google Play Services. Please install " +
                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi();
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {
                    getResultsFromApi();
                }
                break;
        }
    }

    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                SheetAPIUpdateActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, String> {
        private com.google.api.services.sheets.v4.Sheets mService = null;
        private Exception mLastError = null;

        MakeRequestTask(GoogleAccountCredential credential) {
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Google Sheets API Android Quickstart")
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected String doInBackground(Void... params) {
            try {
                return getDataFromApi();
            } catch (Exception e) {
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         * @throws IOException
         */
        private String getDataFromApi() throws IOException {
            try {
                String spreadsheetId = "1pDufFOJy2zFSv8RRrG-5P2FDuMssYPaQWG5nuw8SYVc";
                String range = "2Adocao/Castracao2018!A3:M";

                String valueInputOption = "USER_ENTERED";
                String insertDataOption = "OVERWRITE";
                List<String> resultsFinal = new ArrayList<String>();

                int idAux = id + 2;
                String strID = String.valueOf(idAux);
                resultsFinal.add(strID);
                String rangeFinal = "2Adocao/Castracao2018!A"+ strID+ "M" + strID;

                List<String> valuesString = loadData(resultsFinal);

                valuesString.add(adocao);
                if(sexo.equals("Macho")){
                    valuesString.add("0");
                    valuesString.add("1");
                }else if(sexo.equals("Fêmea")){
                    valuesString.add("1");
                    valuesString.add("0");
                }else{
                    valuesString.add("0");
                    valuesString.add("0");
                }


                List<Object> valuesObject = new ArrayList<Object>(valuesString);
                List<List<Object>> values = new ArrayList<List<Object>>();
                values.add(valuesObject);

                ValueRange body = new ValueRange()
                        .setValues(values);
                AppendValuesResponse result =
                        this.mService.spreadsheets().values().append(spreadsheetId,
                                rangeFinal, body)
                                .setValueInputOption(valueInputOption)
                                .setInsertDataOption(insertDataOption)
                                .execute();

                return result.toString();
            }catch (IOException e){
                Log.e("api","Erro Main");
                return "Erro";
            }
        }



        @Override
        protected void onPreExecute() {
//            mOutputText.setText("");
            mProgress.show();
        }

        @Override
        protected void onPostExecute(String output) {
            mProgress.hide();
            if (output.equals("Erro")) {
                Log.e("api",output);
            } else {
                Log.e("api","Dados Armazenados usando Google Sheets API");

                startActivity(new Intent(SheetAPIUpdateActivity.this,MainActivity.class));
            }

        }

        @Override
        protected void onCancelled() {
            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            SheetAPIUpdateActivity.REQUEST_AUTHORIZATION);
                } else {
                    Log.e("api","The following error occurred:\n"
                            + mLastError.getMessage());
                }
            } else {
                Log.e("api","Request cancelled.");

            }
        }
    }
}
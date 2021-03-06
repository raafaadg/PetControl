package com.pet.petcontrol;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.pet.petcontrol.adapter.MyArrayAdapter;
import com.pet.petcontrol.model.MyDataModel;
import com.pet.petcontrol.parser.JSONparser;
import com.pet.petcontrol.util.InternetConnection;
import com.pet.petcontrol.util.Keys;
import com.facebook.drawee.backends.pipeline.Fresco;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ReadActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<MyDataModel> list;
    private MyArrayAdapter adapter;
    SearchView searchView;
    int current_page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        Fresco.initialize(this);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /**
         * Array List for Binding Data from JSON to this List
         */
        list = new ArrayList<>();
        /**
         * Binding that List to Adapter
         */
        adapter = new MyArrayAdapter(this, list);

        /**
         * Getting List and Setting List Adapter
         */
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);



        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ReadActivity.this,
                        "SEGUROU o item " + String.valueOf(position),
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Snackbar.make(findViewById(R.id.parentLayout),
                        list.get(position).getNumero() + " => " +
                                list.get(position).getData_cap_cas() + " => " +
                                list.get(position).getSexo() + " => " +
                                list.get(position).getIdade() + " => " +
                                list.get(position).getCarac() + " => " +
                                list.get(position).getCast() + " => " +
                                list.get(position).getResp() + " => " +
                                list.get(position).getData_entrega() + " => " +
                                list.get(position).getNome_adot() + " => " +
                                list.get(position).getNome_animal() + " => " +
                                list.get(position).getAdocao() + " => " +
                                list.get(position).getFemea() + " => " +
                                list.get(position).getMacho() + " => "
                        , Snackbar.LENGTH_LONG).show();
            }
        });


        /**
         * Just to know onClick and Printing Hello Toast in Center.
         */
        Toast toast = Toast.makeText(getApplicationContext(), "Click on FloatingActionButton to Load JSON", Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        if (InternetConnection.checkConnection(getApplicationContext())) {
            new GetDataTask().execute();
        } else {
            Toast.makeText(this, "Internet Connection Not Available", Snackbar.LENGTH_LONG).show();
        }

    }

    /**
     * Creating Get Data Task for Getting Data From Web
     */
    class GetDataTask extends AsyncTask<Void, Void, Void> {

        ProgressDialog dialog;
        int jIndex;
        int x;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /**
             * Progress Dialog for User Interaction
             */

            x=list.size();

            if(x==0)
                jIndex=0;
            else
                jIndex=x;

            dialog = new ProgressDialog(ReadActivity.this);
            dialog.setTitle("Hey Wait Please..."+x);
            dialog.setMessage("I am getting your JSON");
            dialog.show();
        }

        @Nullable
        @Override
        protected Void doInBackground(Void... params) {

            /**
             * Getting JSON Object from Web Using okHttp
             */
            JSONObject jsonObject = JSONparser.getDataFromWeb();

            try {
                /**
                 * Check Whether Its NULL???
                 */
                if (jsonObject != null) {
                    /**
                     * Check Length...
                     */
                    if(jsonObject.length() > 0) {
                        /**
                         * Getting Array named "contacts" From MAIN Json Object
                         */
                        JSONArray array = jsonObject.getJSONArray(Keys.KEY_CONTACTS);

                        /**
                         * Check Length of Array...
                         */


                        int lenArray = array.length();
                        if(lenArray > 0) {
                            for( ; jIndex < lenArray; jIndex++) {

                                /**
                                 * Creating Every time New Object
                                 * and
                                 * Adding into List
                                 */
                                MyDataModel model = new MyDataModel();

                                /**
                                 * Getting Inner Object from contacts array...
                                 * and
                                 * From that We will get Name of that Contact
                                 *
                                 */
                                JSONObject innerObject = array.getJSONObject(jIndex);
                                String numero = innerObject.getString(Keys.KEY_numero);
                                String data_cap_cas = innerObject.getString(Keys.KEY_data_cap_cas);
                                String sexo = innerObject.getString(Keys.KEY_sexo);
                                String idade = innerObject.getString(Keys.KEY_idade);
                                String carac = innerObject.getString(Keys.KEY_carac);
                                String cast = innerObject.getString(Keys.KEY_cast);
                                String resp = innerObject.getString(Keys.KEY_resp);
                                String data_entrega = innerObject.getString(Keys.KEY_data_entrega);
                                String nome_adot = innerObject.getString(Keys.KEY_nome_adot);
                                String nome_animal = innerObject.getString(Keys.KEY_nome_animal);
                                String adocao = innerObject.getString(Keys.KEY_adocao);
                                String femea = innerObject.getString(Keys.KEY_femea);
                                String macho = innerObject.getString(Keys.KEY_macho);

                                /**
                                 * Getting Object from Object "phone"
                                 */
                                //JSONObject phoneObject = innerObject.getJSONObject(Keys.KEY_PHONE);
                                //String phone = phoneObject.getString(Keys.KEY_MOBILE);

                                model.setNumero(numero);
                                model.setData_cap_cas(data_cap_cas);
                                model.setSexo(sexo);
                                model.setIdade(idade);
                                model.setCarac(carac);
                                model.setCast(cast);
                                model.setResp(resp);
                                model.setData_entrega(data_entrega);
                                model.setNome_adot(nome_adot);
                                model.setNome_animal(nome_animal);
                                model.setAdocao(adocao);
                                model.setFemea(femea);
                                model.setMacho(macho);

                                /**
                                 * Adding name and phone concatenation in List...
                                 */
                                list.add(model);
                            }
                        }
                    }
                } else {

                }
            } catch (JSONException je) {
                Log.i(JSONparser.TAG, "" + je.getLocalizedMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            /**
             * Checking if List size if more than zero then
             * Update ListView
             */
            if(list.size() > 0) {
                adapter.notifyDataSetChanged();
                getData(current_page,"");
            } else {
                Snackbar.make(findViewById(R.id.parentLayout), "No Data Found", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void getData(int pageno, String query){

        ArrayList<MyDataModel> output = new ArrayList<>();
        ArrayList<MyDataModel> filteredOutput = new ArrayList<>();

        for(int i=pageno-1; i<pageno+9;i++){
            output.add(list.get(i));
        }

        if(searchView!=null){
            for(MyDataModel item:output){
                if(item.getNome_adot().toLowerCase().startsWith(query.toLowerCase()))
                    filteredOutput.add(item);
            }
        }else
            filteredOutput=output;

        adapter = new MyArrayAdapter(ReadActivity.this,filteredOutput);
        listView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search,menu);
        MenuItem searchItem = menu.findItem(R.id.app_bar_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Insira Nome do Adotante, Nome do PET ou caracteristica para buscar");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                getData(current_page,newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
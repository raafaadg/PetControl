package com.pet.petcontrol.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pet.petcontrol.R;
import com.pet.petcontrol.model.MyDataModel;

import java.util.List;

public class MyArrayAdapter extends ArrayAdapter<MyDataModel> {

    List<MyDataModel> modelList;
    Context context;
    private LayoutInflater mInflater;

    // Constructors
    public MyArrayAdapter(Context context, List<MyDataModel> objects) {
        super(context, 0, objects);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        modelList = objects;
    }

    @Override
    public MyDataModel getItem(int position) {
        return modelList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder vh;
        if (convertView == null) {
            View view = mInflater.inflate(R.layout.layout_row_view, parent, false);
            vh = ViewHolder.create((RelativeLayout) view);
            view.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        MyDataModel item = getItem(position);

        vh.tv_numero.setText(item.getNumero());
        vh.tv_data_cap_cas.setText(item.getData_cap_cas());
        vh.tv_sexo.setText(item.getSexo());
        vh.tv_idade.setText(item.getIdade());
        vh.tv_carac.setText(item.getCarac());
        vh.tv_cast.setText(item.getCast());
        vh.tv_resp.setText(item.getResp());
        vh.tv_data_entrega.setText(item.getData_entrega());
        vh.tv_nome_adot.setText(item.getNome_adot());
        vh.tv_nome_animal.setText(item.getNome_animal());
        vh.tv_adocao.setText(item.getAdocao());
        vh.tv_femea.setText(item.getFemea());
        vh.tv_macho.setText(item.getMacho());

        return vh.rootView;
    }

    private static class ViewHolder {
        public final RelativeLayout rootView;

        public final TextView tv_numero;
        public final TextView tv_data_cap_cas;
        public final TextView tv_sexo;
        public final TextView tv_idade;
        public final TextView tv_carac;
        public final TextView tv_cast;
        public final TextView tv_resp;
        public final TextView tv_data_entrega;
        public final TextView tv_nome_adot;
        public final TextView tv_nome_animal;
        public final TextView tv_adocao;
        public final TextView tv_femea;
        public final TextView tv_macho;

        private ViewHolder(RelativeLayout rootView,
                           TextView tv_numero,
                           TextView tv_data_cap_cas,
                           TextView tv_sexo,
                           TextView tv_idade,
                           TextView tv_carac,
                           TextView tv_cast,
                           TextView tv_resp,
                           TextView tv_data_entrega,
                           TextView tv_nome_adot,
                           TextView tv_nome_animal,
                           TextView tv_adocao,
                           TextView tv_femea,
                           TextView tv_macho) {
            this.rootView = rootView;
            this.tv_numero = tv_numero;
            this.tv_data_cap_cas = tv_data_cap_cas;
            this.tv_sexo = tv_sexo;
            this.tv_idade = tv_idade;
            this.tv_carac = tv_carac;
            this.tv_cast = tv_cast;
            this.tv_resp = tv_resp;
            this.tv_data_entrega = tv_data_entrega;
            this.tv_nome_adot = tv_nome_adot;
            this.tv_nome_animal = tv_nome_animal;
            this.tv_adocao = tv_adocao;
            this.tv_femea = tv_femea;
            this.tv_macho = tv_macho;
        }

        public static ViewHolder create(RelativeLayout rootView) {
            TextView tv_numero = (TextView) rootView.findViewById(R.id.tv_numero);
            TextView tv_data_cap_cas = (TextView) rootView.findViewById(R.id.tv_data_cap_cas);
            TextView tv_sexo = (TextView) rootView.findViewById(R.id.tv_sexo);
            TextView tv_idade = (TextView) rootView.findViewById(R.id.tv_idade);
            TextView tv_carac = (TextView) rootView.findViewById(R.id.tv_carac);
            TextView tv_cast = (TextView) rootView.findViewById(R.id.tv_cast);
            TextView tv_resp = (TextView) rootView.findViewById(R.id.tv_resp);
            TextView tv_data_entrega = (TextView) rootView.findViewById(R.id.tv_data_entrega);
            TextView tv_nome_adot = (TextView) rootView.findViewById(R.id.tv_nome_adot);
            TextView tv_nome_animal = (TextView) rootView.findViewById(R.id.tv_nome_animal);
            TextView tv_adocao = (TextView) rootView.findViewById(R.id.tv_adocao);
            TextView tv_femea = (TextView) rootView.findViewById(R.id.tv_femea);
            TextView tv_macho = (TextView) rootView.findViewById(R.id.tv_macho);
            return new ViewHolder(rootView,
                    tv_numero,
                    tv_data_cap_cas,
                    tv_sexo,
                    tv_idade,
                    tv_carac,
                    tv_cast,
                    tv_resp,
                    tv_data_entrega,
                    tv_nome_adot,
                    tv_nome_animal,
                    tv_adocao,
                    tv_femea,
                    tv_macho);
        }
    }
}
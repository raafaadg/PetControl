package com.pet.petcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

public class ShareDialog extends AppCompatDialogFragment {

    ArquivoDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedinstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_compartilharg,null);

        builder.setView(view)
                .setTitle("Deseja Compartilhar a Adoção?")
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts("main");
                        Toast.makeText(getContext(),
                                "Cadastro Efetivado com Sucesso!!",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts("share");
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener =(ArquivoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()+
            "deve implementar ArquivoDialogListner");
        }
    }
}

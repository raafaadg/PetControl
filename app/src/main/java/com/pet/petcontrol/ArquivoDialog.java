package com.pet.petcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class ArquivoDialog extends AppCompatDialogFragment {

    ArquivoDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedinstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_dialog,null);

        builder.setView(view)
                .setTitle("Selecionar Imagem")
                .setNegativeButton("Abrir Álbum", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts("album");

                    }
                })
                .setPositiveButton("Tirar Foto", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts("foto");
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

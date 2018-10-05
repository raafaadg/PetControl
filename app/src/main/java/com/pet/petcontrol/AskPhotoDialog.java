package com.pet.petcontrol;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class AskPhotoDialog extends AppCompatDialogFragment {

    ArquivoDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedinstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.activity_askphoto,null);

        builder.setView(view)
                .setTitle("Deseja Anexar uma Foto?")
                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts("askShare");

                    }
                })
                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.applyTexts("want");
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

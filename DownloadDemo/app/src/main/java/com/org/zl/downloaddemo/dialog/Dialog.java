package com.org.zl.downloaddemo.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.org.zl.downloaddemo.Inteface.DialogLinster;
import com.org.zl.downloaddemo.R;


/**
 * Created by QCY on 2016/12/26.
 * 所有自定义dialog
 */

public class Dialog {

    public static AlertDialog getDownLoadDialog(Context mContext, String message, final DialogLinster mDialogLinster){
        AlertDialog mdialog=null;

        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = View.inflate(mContext, R.layout.download_dialog, null);
        // 设置自定义对话框的布局
        builder.setView(view);
        final TextView deletMusicName=(TextView)view.findViewById(R.id.deletMusicName);
        deletMusicName.setText(message);
        final TextView buttonYes=(TextView)view.findViewById(R.id.buttonYes);
        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogLinster.Success();
            }
        });
        TextView buttonNo=(TextView)view.findViewById(R.id.buttonNo);
        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialogLinster.Failed();
            }
        });
        mdialog = builder.create();
        return   mdialog;
    }
}

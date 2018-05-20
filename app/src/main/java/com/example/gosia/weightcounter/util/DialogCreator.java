package com.example.gosia.weightcounter.util;

import android.content.Context;
import android.content.DialogInterface;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.gosia.weightcounter.R;

public class DialogCreator {

    private static DialogCreator instance = new DialogCreator();
    private MaterialDialog materialDialog = null;

    private DialogCreator() {
    }

    public static DialogCreator getInstance() {
        return instance;
    }

    public MaterialDialog getMaterialDialog() {
        return materialDialog;
    }


    public void showErrorDialog(Context context, int resourceId) {
        showErrorDialog(context, context.getString(resourceId));
    }

    private void showErrorDialog(Context context, String text) {
        if (materialDialog != null) {
            materialDialog.dismiss();
            materialDialog = null;
        }

        materialDialog = new MaterialDialog.Builder(context)
                .title(context.getString(R.string.error_occurred))
                .titleColorRes(R.color.colorGold)
                .backgroundColorRes(R.color.colorBlack)
                .content(text)
                .contentColor(context.getResources().getColor(R.color.colorRed2))
                .positiveText(R.string.ok)
                .positiveColor(context.getResources().getColor(R.color.colorGold))
                .cancelable(false)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        MaterialDialog dialog1 = DialogCreator.this.materialDialog;
                        if (dialog1 != null && !ObjectsComparator.equals(dialog1, dialog)) {
                            dialog1.dismiss();
                        }
                        DialogCreator.this.materialDialog = null;
                    }
                })
                .build();
        materialDialog.show();
    }

    public void showDialog(Context context, int titleId, int contentId) {
        showDialog(context, context.getString(titleId), context.getString(contentId));
    }

    private void showDialog(Context context, String title, String content) {
        if (materialDialog != null) {
            materialDialog.dismiss();
            materialDialog = null;
        }

        materialDialog = new MaterialDialog.Builder(context)
                .title(R.string.saved)
                .titleColorRes(R.color.colorGold)
                .backgroundColorRes(R.color.colorBlack)
                .content(R.string.added)
                .contentColor(context.getResources().getColor(R.color.colorWhite))
                .positiveText(R.string.ok)
                .positiveColor(context.getResources().getColor(R.color.colorGold))
                .cancelable(false)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        MaterialDialog dialog1 = DialogCreator.this.materialDialog;
                        if (dialog1 != null && !ObjectsComparator.equals(dialog1, dialog)) {
                            dialog1.dismiss();
                        }
                        DialogCreator.this.materialDialog = null;
                    }
                })
                .build();
        materialDialog.show();
    }
}

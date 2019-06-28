package com.example.siditoure.androidtranscription;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.preference.DialogPreference;
import android.util.AttributeSet;

import java.util.Locale;
import java.util.stream.IntStream;

public class ColorPickerPreferenceDialog extends DialogPreference implements ColorPickerView.OnColorChangedListener {
    private int mColor = 0;

    public ColorPickerPreferenceDialog(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Déclenché dès qu'on ferme la boîte de dialogue
     */
    protected void onDialogClosed(boolean positiveResult) {
        // Si l'utilisateur a cliqué sur « OK »
        if (positiveResult) {
            persistInt(mColor);
            // Ou getSharedPreferences().edit().putInt(getKey(), mColor).commit();
        }
        super.onDialogClosed(positiveResult);
    }

    /**
     * Pour construire la boîte de dialogue
     */
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        // On récupère l'ancienne couleur ou la couleur par défaut
        int oldColor = getSharedPreferences().getInt(getKey(), Color.BLACK);
        // On insère la vue dans la boîte de dialogue
        builder.setView(new ColorPickerView(getContext(), this, oldColor));

        super.onPrepareDialogBuilder(builder);
    }

    /**
     * Déclenché à chaque fois que l'utilisateur choisit une couleur
     */
    public void colorChanged(int color) {
        mColor = color;
    }
}

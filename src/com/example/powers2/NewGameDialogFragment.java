package com.example.powers2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class NewGameDialogFragment extends DialogFragment {

	private NewGameDialogListener listener;

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle(R.string.new_game_dialog_title)
		       .setMessage(R.string.new_game_dialog_message)
		       .setPositiveButton(R.string.new_game_dialog_positive, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (listener != null) {
									listener.onDialogPositiveClick(NewGameDialogFragment.this);
								}
							}
			    })
			    .setNegativeButton(R.string.new_game_dialog_negative, new OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (listener != null) {
									listener.onDialogNegativeClick(NewGameDialogFragment.this);
								}
							}
				});
		return builder.create();
	}

	public void setNewGameDialogListener(NewGameDialogListener listener) {
		this.listener = listener;
	}

	public interface NewGameDialogListener {
		void onDialogPositiveClick(DialogFragment dialog);

		void onDialogNegativeClick(DialogFragment dialog);
	}

}

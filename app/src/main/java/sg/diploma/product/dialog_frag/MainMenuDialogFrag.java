package sg.diploma.product.dialog_frag;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import sg.diploma.product.event.Publisher;
import sg.diploma.product.event.events.EventEndProg;

public final class MainMenuDialogFrag extends DialogFragment{
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		isShown = true;

		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

		builder.setMessage("Exit Game?")
		.setPositiveButton("+ve", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				Publisher.Broadcast(new EventEndProg());
				isShown = false;
			}
		})
		.setNegativeButton("-ve", new DialogInterface.OnClickListener(){
			public void onClick(DialogInterface dialog, int id){
				isShown = false;
			}
		});

		return builder.create();
	}

	@Override
	public void show(@NonNull FragmentManager manager, @Nullable String tag){
		super.show(manager, tag);
	}

	public static boolean isShown;

	static{
		isShown = false;
	}
}
package MinesweeperUI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.chris.minesweeper.feature.R;

public class Dialog extends AppCompatDialogFragment {
    private static final String TAG = "Dialog";
    public onButtonClick monButtonClick;
    TextView dialog_firstLine;
    TextView dialog_secondLine;
    TextView dialog_thirdLine;

    @Override
    public AlertDialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        dialog_firstLine = view.findViewById(R.id.dialog_firstLine);
        dialog_secondLine = view.findViewById(R.id.dialog_secondLine);
        dialog_thirdLine = view.findViewById(R.id.dialog_thirdLine);

        dialog_firstLine.setText(getArguments().getString("first"));
        dialog_secondLine.setText(getArguments().getString("second"));
        if (getArguments().getString("third", "null").equals("null")) {
            dialog_thirdLine.setVisibility(View.INVISIBLE);
            dialog_firstLine.setTextColor(Color.GREEN);
            dialog_secondLine.setTextColor(Color.GREEN);
        } else {
            dialog_thirdLine.setText(getArguments().getString("third"));
            dialog_thirdLine.setVisibility(View.VISIBLE);
            dialog_firstLine.setTextColor(Color.RED);
            dialog_secondLine.setTextColor(Color.RED);
            dialog_thirdLine.setTextColor(Color.RED);
        }


        Button retry = view.findViewById(R.id.dialog_button);
        retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                monButtonClick.retryButton(true);
                getDialog().dismiss();
            }
        });
        Button menu = view.findViewById(R.id.dialog_button2);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                monButtonClick.retryButton(false);
                getDialog().dismiss();
            }
        });

        builder.setView(view)
                .setTitle(getArguments().getString("title"));

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            monButtonClick = (onButtonClick) getActivity();
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ClassCastException: " + e.getMessage());
        }
    }

    public interface onButtonClick {
        void retryButton(boolean pressed);
    }
}

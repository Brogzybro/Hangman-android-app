package com.example.brage.hangman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private RadioGroup radioGroup;
    private int gameModeSelected = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    finishAndRemoveTask();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    break;
            }
        }
    };

    public void onStartClickBtn(View view){
        int selectedId = radioGroup.getCheckedRadioButtonId();

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra("language", selectedId);
        intent.putExtra("gameMode", gameModeSelected);
        startActivity(intent);
    }

    public void onHelpBtnClick(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_help, null);
        TextView helpText = (TextView)popupView.findViewById(R.id.helpTextTV);
        TextView helpTitle = (TextView)popupView.findViewById(R.id.helpTitleTV);

        if (radioGroup.getCheckedRadioButtonId() == R.id.radioEng){
            helpText.setText(getResources().getString(R.string.help_eng));
            helpTitle.setText(getResources().getString(R.string.heltTitle_eng));
        }

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int heiht = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, heiht, focusable);

        popupWindow.showAtLocation(view, Gravity.CENTER, 0 , 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return false;
            }
        });
    }

    public void onExitBtnClick(View view){
        String message = getResources().getString(R.string.exit_nor);
        String yes = getResources().getString(R.string.yes_nor);
        String no = getResources().getString(R.string.no_nor);
        if (radioGroup.getCheckedRadioButtonId() == R.id.radioEng){
            message = getResources().getString(R.string.exit_eng);
            yes = getResources().getString(R.string.yes_eng);
            no = getResources().getString(R.string.no_eng);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message).setPositiveButton(yes, dialogListener).setNegativeButton(no, dialogListener).show();
    }

    public void changeGameMode(View view){
        if (view.getId() == R.id.mode1Btn){
            gameModeSelected = 1;
            view.setBackground(getDrawable(R.drawable.game_mode_1_s));
            Button buttonNotPressed = (Button)findViewById(R.id.mode0Btn);
            buttonNotPressed.setBackground(getDrawable(R.drawable.game_mode_0));
        }else{
            gameModeSelected = 0;
            view.setBackground(getDrawable(R.drawable.game_mode_0_s));
            Button buttonNotPressed = (Button)findViewById(R.id.mode1Btn);
            buttonNotPressed.setBackground(getDrawable(R.drawable.game_mode_1));
        }
    }
}

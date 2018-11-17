package com.example.brage.hangman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;

public class SetWordActivity extends AppCompatActivity {

    private Intent intent;
    private int antWins = 0;
    private int antLoss = 0;
    static final int RESULT_WIN = 1;
    static final int RESULT_LOSS = 2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.set_word_activity);
        intent = getIntent();
        setText();
    }

    public void setText(){
        int gottenId = intent.getIntExtra("language", R.id.radioNor);
        if (gottenId == R.id.radioEng){
            TextView setWordHelp = (TextView) findViewById(R.id.setWordHelpText);
            TextView yourWordTV = (TextView) findViewById(R.id.yourWordTV);
            TextView wirteHereTV = (TextView) findViewById(R.id.writeHereTV);
            TextView wrongInputTV = (TextView) findViewById(R.id.wrongInputTV);
            setWordHelp.setText(getText(R.string.setWordDesc_eng));
            yourWordTV.setText(getText(R.string.setWordInputPreset_eng));
            wirteHereTV.setText(getText(R.string.writeHere_eng));
            wrongInputTV.setText(getText(R.string.wrongInput_eng));
        }
    }

    public void onOKBtnClick(View view){
        TextView yourWord = (TextView)findViewById(R.id.yourWordTV);
        String word = yourWord.getText().toString();

        if (word.contains(" ")){
            TextView wrongInputTV = (TextView) findViewById(R.id.wrongInputTV);
            wrongInputTV.setVisibility(View.VISIBLE);
        }else{
            int language = intent.getIntExtra("language", R.id.radioNor);
            int gamemode = intent.getIntExtra("gameMode", 0);

            word = fixWord(word);
            Intent intentStart = new Intent(this, GameActivity.class);
            intentStart.putExtra("language", language);
            intentStart.putExtra("gameMode", gamemode);
            intentStart.putExtra("twoPlayers", true);
            intentStart.putExtra("word", word);
            intentStart.putExtra("wins", antWins);
            intentStart.putExtra("loss", antLoss);
            startActivityForResult(intentStart, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                int result = data.getIntExtra("result", 1);
                if (result == 1) {
                    antWins++;
                    Log.i("-------------------", "wins: " + antWins);
                }
                if (result == 2) {
                    antLoss++;
                    Log.i("-------------------", "loss: " + antLoss);
                }

            }
        }
    }

    private String fixWord(String word){
        Locale thisLocale = Locale.forLanguageTag("NOB");
        int gottenId = intent.getIntExtra("language", R.id.radioNor);
        if (gottenId == R.id.radioEng){
            thisLocale = Locale.ENGLISH;
        }
        return word.toLowerCase(thisLocale);
    }


    public void onBackBtnClick(View view){
        finish();
    }

}

package com.example.brage.hangman;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity{
    private ArrayList<String> listOFWords = new ArrayList();
    private ArrayList<Character> listOfGuessedLetters = new ArrayList<>(); //List of correct guessed letters
    private Intent intent;
    private ImageView background;
    private TypedArray backgrounds;
    private String wordOfTheGame= "";
    private TextView wordOfGameTV;
    private TextView failedLettersTV;
    private TextView numberOfGameWins; // number of games won TextView
    private TextView numberOfGameLoss; // number of games lost TextView
    private Button nextGameBtn;
    private int numberOfFailes = 0; // number of failed guesses this round
    private int numberOfLetters = 29;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);
        intent = getIntent();
        background = (ImageView)findViewById(R.id.gameBg);
        wordOfGameTV = (TextView)findViewById(R.id.wordOfTheGameTV);
        failedLettersTV = (TextView)findViewById(R.id.failedLettersTV);
        numberOfGameWins = (TextView)findViewById(R.id.numerOfWins);
        numberOfGameLoss = (TextView)findViewById(R.id.numerOfLoss);
        nextGameBtn = (Button)findViewById(R.id.nextGameBtn);
        if (intent.getIntExtra("gameMode", 0) == 1){
            backgrounds = getResources().obtainTypedArray(R.array.bgList1);
        }else{
            backgrounds = getResources().obtainTypedArray(R.array.bgList0);
        }
        background.setImageResource(backgrounds.getResourceId(numberOfFailes, 0));
        readFromFile();
        setWordOfGameTV();
    }

    private boolean readFromFile(){
        int gottenId = intent.getIntExtra("language", R.id.radioNor);
        int fileId = R.raw.norwegian_words;
        if (gottenId == R.id.radioEng){
            fileId = R.raw.english_words;
            numberOfLetters = 27;
            for (int i = 26; i <29 ; i++) {
                int id = getResources().getIdentifier("button"+i, "id", getPackageName());
                Button letterBtn = (Button)findViewById(id);
                letterBtn.setVisibility(View.INVISIBLE);
            }
            TextView winsTV = (TextView)findViewById(R.id.wins);
            TextView lossTV = (TextView)findViewById(R.id.loss);
            winsTV.setText(getResources().getString(R.string.wins_eng));
            lossTV.setText(getResources().getString(R.string.loss_eng));
            nextGameBtn.setText(getResources().getString(R.string.next_eng));
        }
        try{
            InputStream is = getResources().openRawResource(fileId);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String word = bufferedReader.readLine();

            while(word != null){
                listOFWords.add(word);
                word = bufferedReader.readLine();
            }
            bufferedReader.close();
        }catch (IOException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String selectRandomWord(){
        Random random = new Random();
        if (listOFWords.size() == 0){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("There are no more words to play :(").setPositiveButton("Ok!", dialogListener).show();
        }
    int randomInt = random.nextInt(listOFWords.size());
    String word = listOFWords.get(randomInt);
        return word;
}

    private void removeWordFromList(String word){
        listOFWords.remove(word);
    }

    public void letterOnClickBtn(View view){
        Button button = (Button)view;
        String selected = button.getText().toString();
        selected = selected.trim();
        selected = selected.toLowerCase();
        Character selectedChar = selected.charAt(0);
        view.setVisibility(View.INVISIBLE);
        if (wordOfTheGame.indexOf(selectedChar) >= 0){ //Character is in the word!

            listOfGuessedLetters.add(selectedChar);
            updateWordOfTheGameTV(false);

        }else{
            numberOfFailes++;
            if (numberOfFailes > 9){
                numberOfFailes = 10;
                gameLost();
            }
            String failedLettersString = failedLettersTV.getText().toString();
            failedLettersString += selectedChar + " ";
            failedLettersTV.setText(failedLettersString);
            background.setImageResource(backgrounds.getResourceId(numberOfFailes, 0));
        }
    }

    private void setWordOfGameTV(){
        wordOfTheGame = selectRandomWord();
        updateWordOfTheGameTV(false);
    }

    private void updateWordOfTheGameTV(boolean gameIsOver){
        String out = "";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        boolean isWon = true;
        for (int i = 0; i < wordOfTheGame.length(); i++) {
            Character wotdCharI = wordOfTheGame.charAt(i);
            boolean isGuessed = false;

            for (int j = 0; j < listOfGuessedLetters.size(); j++) {
                if (listOfGuessedLetters.get(j).equals(wotdCharI)){
                    isGuessed = true;
                }
            }
            if (isGuessed){
                out += wotdCharI +" ";
                SpannableString guessedLetter = new SpannableString(wotdCharI +" ");
                builder.append(guessedLetter);

            }else{
                SpannableString notGuessed;
                if (gameIsOver){
                    notGuessed = new SpannableString(wotdCharI +" ");
                    notGuessed.setSpan(new ForegroundColorSpan(Color.RED), 0, notGuessed.length(), 0);
                }else{
                    notGuessed = new SpannableString("_ ");
                }
                builder.append(notGuessed);
                out += "_ ";
                isWon = false;
            }
        }
        wordOfGameTV.setText(builder, TextView.BufferType.SPANNABLE);
        if (isWon){
            // GameWon
            gameWon();
        }
    }

    private void gameWon(){
        int numbOfCurrentWins = Integer.parseInt(numberOfGameWins.getText().toString());
        numbOfCurrentWins ++;
        numberOfGameWins.setText("" + numbOfCurrentWins);
        gameFinnished();
    }

    private void gameLost(){
        int numberOfCurrentLoss = Integer.parseInt(numberOfGameLoss.getText().toString());
        numberOfCurrentLoss ++;
        numberOfGameLoss.setText("" + numberOfCurrentLoss);
        updateWordOfTheGameTV(true);
        gameFinnished();
    }

    private void gameFinnished(){
        enableLetterBtns(false);
        nextGameBtn.setVisibility(View.VISIBLE);
    }

    public void newGame(){
        removeWordFromList(wordOfTheGame);
        listOfGuessedLetters.clear();
        setWordOfGameTV();
        numberOfFailes = 0;
        failedLettersTV.setText("");
        for (int i = 0; i < numberOfLetters ; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button letterBtn = (Button)findViewById(id);
            letterBtn.setVisibility(View.VISIBLE);
            letterBtn.setEnabled(true);
        }
        background.setImageResource(backgrounds.getResourceId(numberOfFailes, 0));
    }

    public void onNextGameBtnClick(View view){
        nextGameBtn.setVisibility(View.INVISIBLE);
        newGame();
    }

    public void onExitBtnClick(View view){
        finish();
    }

    public void enableLetterBtns(Boolean enable){
        for (int i = 0; i < numberOfLetters ; i++) {
            int id = getResources().getIdentifier("button"+i, "id", getPackageName());
            Button letterBtn = (Button)findViewById(id);
            letterBtn.setEnabled(enable);
        }
    }

    DialogInterface.OnClickListener dialogListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    finish();
                    break;
            }

        }
    };

}

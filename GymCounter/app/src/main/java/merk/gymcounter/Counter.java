package merk.gymcounter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.content.DialogInterface;
import android.view.View.OnClickListener;
import android.os.CountDownTimer;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import android.os.Vibrator;
import java.util.ArrayList;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.media.Ringtone;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;


public class Counter extends AppCompatActivity {

    private Exercise curEx;
    private Button btnNewEx, btnAddSet, btnSave, btnDelete;
    private TextView txtTimer, txtSets;
    private ListView lvReps;
    private ArrayList<String> strRepArr;
    private ArrayAdapter<String> repAdapter;
    CountDownTimer timer;
    SQLiteDatabase sqlLiteDatabase;
    exerciseDBHelper sqlHelper;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_counter);

        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtSets = (TextView) findViewById(R.id.txtSetsCompleted);
        lvReps = (ListView) findViewById(R.id.lvSetHistory);

        btnNewEx = (Button) findViewById(R.id.btnNewExercise);
        btnNewEx.setOnClickListener(new NewExerciseClick());

        btnAddSet = (Button) findViewById(R.id.btnAddSet);
        btnAddSet.setOnClickListener(new AddSetClick());

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new SaveClick());
        btnSave.setEnabled(false);

        btnDelete = (Button) findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new DeleteClick());
        btnDelete.setEnabled(false);

        sqlLiteDatabase = getBaseContext().openOrCreateDatabase("exercise.db", MODE_PRIVATE, null);
        sqlHelper = new exerciseDBHelper(getApplicationContext());
        sqlHelper.onCreate(sqlLiteDatabase);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Counter Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://merk.gymcounter/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Counter Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://merk.gymcounter/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

    //When adding new exercise, get exercise info from user via popout box
    private class NewExerciseClick implements OnClickListener {
        public void onClick(View arg0) {
            //Todo: curex.save();

            View view = LayoutInflater.from(Counter.this).inflate(R.layout.exercise_input, null);

            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Counter.this);
            alertBuilder.setView(view);

            final EditText nameInput = (EditText) view.findViewById(R.id.name);
            final EditText setsInput = (EditText) view.findViewById(R.id.sets);
            final EditText repsInput = (EditText) view.findViewById(R.id.reps);
            final EditText secondsInput = (EditText) view.findViewById(R.id.seconds);
            final EditText weightInput = (EditText) view.findViewById(R.id.weight);

            //Get exercise info from user
            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    String strName = nameInput.getText().toString();                        //Name
                    int intSets = Integer.parseInt(setsInput.getText().toString());         //sets
                    int intReps = Integer.parseInt(repsInput.getText().toString());         //Reps
                    int intseconds = Integer.parseInt(secondsInput.getText().toString());   //seconds
                    int intWeight = Integer.parseInt(weightInput.getText().toString());     //weight

                    curEx = new Exercise(strName, intSets, intReps, intseconds, intWeight);

                    //Set Exercise name on form
                    TextView name = (TextView) findViewById(R.id.txtExerciseName);
                    name.setText(curEx.getName());
                    txtSets.setText("Sets: " + curEx.getSetsCompleted() + "/" + curEx.getSetsToAttempt());

                    //Set timer with requested seconds
                    long millis = curEx.getSecondsBetweenSets() * 1000;
                    txtTimer.setText(getTimeString(millis));
                    timer = new CounterClass(curEx.getSecondsBetweenSets() * 1000, 1000);

                    //Set weight on form
                    TextView txtWeight = (TextView) findViewById(R.id.txtWeight);
                    txtWeight.setText(curEx.getWeight() + " lbs.");

                    //Add reps to listview
                    strRepArr = new ArrayList<String>();
                    repAdapter = new ArrayAdapter<String>(getApplicationContext(),
                            R.layout.rep_listitem, strRepArr);
                    lvReps.setAdapter((repAdapter));

                    btnAddSet.setVisibility(View.VISIBLE);
                    btnAddSet.setEnabled(true);

                    btnSave.setEnabled(false);
                    btnDelete.setEnabled(false);
                }

            });

            Dialog dialog = alertBuilder.create();
            dialog.show();
        }
    }


    //ON AddSet, ask user for number of reps completed and start countdown timer to next set
    private class AddSetClick implements OnClickListener {
        public void onClick(View arg0) {
            View view = LayoutInflater.from(Counter.this).inflate(R.layout.reps_input, null);
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(Counter.this);
            alertBuilder.setView(view);

            final EditText repsInput = (EditText) view.findViewById(R.id.reps);
            repsInput.setText(String.valueOf(curEx.getRepsToAttempt()));

            //Ask user to input number of reps completed
            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    int intReps = Integer.parseInt(repsInput.getText().toString());

                    curEx.completeSet(intReps);
                    txtSets.setText("Sets: " + curEx.getSetsCompleted() + "/" + curEx.getSetsToAttempt());

                    strRepArr.add("Set " + curEx.getSetsCompleted() + ": " + intReps);
                    repAdapter.notifyDataSetChanged();

                    //Start timer, don't allow user to add new set while timer is runner.
                    //User can now delete exercise (will stop timer)
                    timer.start();
                    btnAddSet.setEnabled(false);
                    btnDelete.setEnabled(true);
                }

            });

            Dialog dialog = alertBuilder.create();
            dialog.show();
        }

    }

    //ON Save Set, put the current exercise in the SQLite database for historical purposes
    private class SaveClick implements OnClickListener {
        public void onClick(View arg0) {
            sqlHelper.insert(curEx.getName(), curEx.getSetsCompleted(), curEx.getWeight(), curEx.getRepsString());

            //Don't allow exercise to be deleted, it's already been saved. Allow new Ex to be added now.
            btnDelete.setEnabled(false);
            btnSave.setEnabled(false);
            btnNewEx.setEnabled(true);
        }

    }

    //ON delete, remove the current exercise
    private class DeleteClick implements OnClickListener {
        public void onClick(View arg0) {
            curEx = null;

            //Clear name
            TextView name = (TextView) findViewById(R.id.txtExerciseName);
            name.setText("Add an exercise!");

            //Clear sets
            txtSets.setText("");

            //Cancel timer if it's running, and reset timer text
            timer.cancel();
            txtTimer.setText("00:00");
            timer = null;

            //Clear weight
            TextView txtWeight = (TextView) findViewById(R.id.txtWeight);
            txtWeight.setText("");

            //Clear Listview
            strRepArr = new ArrayList<String>();
            repAdapter = new ArrayAdapter<String>(getApplicationContext(),
                    R.layout.rep_listitem, strRepArr);
            lvReps.setAdapter((repAdapter));

            //Allow new exercise to be added, don't allow blank exercise to be deleted/saved
            btnAddSet.setVisibility(View.INVISIBLE);
            btnSave.setEnabled(false);
            btnDelete.setEnabled(false);
            btnNewEx.setEnabled(true);
        }

    }

    //Used as timer in between sets
    public class CounterClass extends CountDownTimer {
        public CounterClass(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        //When timer finishes, vibrate phone and play tone to notify user and reset the timer.
        @Override
        public void onFinish() {

            //Vibrate to notify the user that it's time for next set
            long[] pattern = {0, 250, 200, 250, 200, 350, 300};
            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            v.vibrate(pattern, -1);


            //Play tone to notify the user that it's time for next set
            Uri defaultRingtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            MediaPlayer mediaPlayer = new MediaPlayer();

            try {
                mediaPlayer.setDataSource(getApplicationContext(), defaultRingtoneUri);
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_NOTIFICATION);
                mediaPlayer.prepare();
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        mp.release();
                    }
                });
                mediaPlayer.start();
            } catch (IllegalArgumentException e) {e.printStackTrace();}
            catch (SecurityException e) {e.printStackTrace();}
            catch (IllegalStateException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }


            //Reset timer text
            long millis = curEx.getSecondsBetweenSets() * 1000;
            txtTimer.setText(getTimeString(millis));

            timer = new CounterClass(curEx.getSecondsBetweenSets() * 1000, 1000);


            //Enable user to add new set or save/add new exercise
            btnAddSet.setEnabled(curEx.getSetsCompleted() < curEx.getSetsToAttempt());
            btnSave.setEnabled(curEx.getSetsCompleted() == curEx.getSetsToAttempt());
            btnNewEx.setEnabled(curEx.getSetsCompleted() != curEx.getSetsToAttempt());
        }

        //Update timer text on the form
        @Override
        public void onTick(long millisUntilFinished) {
            txtTimer.setText(getTimeString(millisUntilFinished));
        }
    }

    //Get text for the timer text in MM:SS format
    private String getTimeString(Long millis) {
        return String.format("%02d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis),
                TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

}

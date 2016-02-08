package merk.gymcounter;

import android.widget.ArrayAdapter;
import android.widget.SectionIndexer;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Context;
import android.widget.ListView;
import java.util.List;

/*
 */
public class Exercise {


    private String name;
    private int setsCompleted;
    private int setsToAttempt;
    private int repsToAttempt;
    private int secondsBetweenSets;
    private int weight;
    private int[] repsCompleted;

    public Exercise(String startName, int startSetsToAttempt, int startRepsToAttempt, int startSecondsBetweenSets, int startWeight)
    {
        name = startName;
        setsCompleted = 0;
        setsToAttempt = startSetsToAttempt;
        repsToAttempt = startRepsToAttempt;
        secondsBetweenSets = startSecondsBetweenSets;
        weight = startWeight;

        repsCompleted = new int[setsToAttempt];
    }

    public int getSecondsBetweenSets()
    {
        return secondsBetweenSets;
    }

    public int getRepsToAttempt()
    {
        return repsToAttempt;
    }

    public int getSetsToAttempt()
    {
        return setsToAttempt;
    }

    public int getSetsCompleted() { return setsCompleted; }

    public int getWeight()
    {
        return weight;
    }

    public String getName()
    {
        return name;
    }

    //Added recordedReps to set history
    public void completeSet(int recordedReps)
    {
        repsCompleted[setsCompleted] = recordedReps;

        setsCompleted++;
    }

    public int getRepsCompletedSingleSet(int index) {
        return repsCompleted[index];
    }

    //Get all reps of exercise in single string, for database
    public String getRepsString() {
        String returnReps = "";

        for(int index = 0; index < repsCompleted.length; index++)
        {
            returnReps = returnReps + repsCompleted[index] + ",";
        }

        return returnReps.substring(0,returnReps.length() - 1);
    }

    //Handled by Counter
    public boolean saveExercise()
    {

        //TODO: save exercise to database

        return true;
    }


}

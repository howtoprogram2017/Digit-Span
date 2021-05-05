package com.example.android.digit_span.utility;


import android.util.Log;

import com.example.android.digit_span.data.TestContract.TestEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘博林 on 7/27/2017.
 */

public class DStest {
    public String testerName = "";
    Boolean isAuditory;
    Boolean isForward;
    public Boolean BackwardMode = false;
    private static final String TAG = "DStest";
    List<LevelTest> Level_tests;
    int currentLevel = 0;
    public int topLevel = 0;

    public LevelTest LastLevelTest;

    public int addlevel() {
        int add = LastLevelTest.addLevel();
        currentLevel += add;
        LevelTest newTest = new LevelTest(currentLevel);
        Level_tests.add(newTest);
        LastLevelTest = newTest;
        return add;
    }

    public void setcorrectanswer(List<Integer> integers) {
        Log.d(TAG, "setcorrectanswer: " + String.valueOf(integers.get(0)) + String.valueOf(integers.get(1)) );
        LastLevelTest.CorrectAnswer[LastLevelTest.stage] = integers;
    }

    public void setplayeranswer(List<Integer> integers) {
        LastLevelTest.PlayerAnswer[LastLevelTest.stage] = integers;
        Log.d(TAG, "setplaeranswer" + String.valueOf(integers.get(0)) + String.valueOf(integers.get(1)) + String.valueOf(integers.get(2)));
    }

    public Boolean isWin() {
        int stage = LastLevelTest.stage;
        if (LastLevelTest.eacheresult[stage] = LastLevelTest.StageWin()) {
            LastLevelTest.winnum++;
        }

        LastLevelTest.stage++;
        return LastLevelTest.eacheresult[stage];

    }

    public int getStage() {
        return LastLevelTest.stage;
    }

    public Boolean isWinLevel() {
        if (LastLevelTest.winnum == 2) {
            topLevel = currentLevel;
            return true;
        } else {
            return false;
        }
    }

    public Boolean isLostLevel() {
        return (LastLevelTest.stage == (LastLevelTest.winnum + 2));
    }

    public DStest(int startlevel, Boolean isAuditory,Boolean isForward, String name) {
        testerName = name;
        this.isAuditory = isAuditory;
        this.isForward=isForward;
        Level_tests = new ArrayList<LevelTest>();

        currentLevel = startlevel;
        LevelTest newTest = new LevelTest(currentLevel);
        LastLevelTest = newTest;
        Level_tests.add(newTest);
    }
    public DStest(int startlevel){
        this(startlevel,true,true,null);
    }

    public class LevelTest {
        Boolean[] eacheresult = {false, false, false};
        int winnum;
        int stage;
        int level;
        List<Integer>[] CorrectAnswer;
        List<Integer>[] PlayerAnswer;

        LevelTest(int level) {
            this.level = level;
            winnum = 0;
            stage = 0;
            CorrectAnswer = new ArrayList[3];
            PlayerAnswer = new ArrayList[3];

        }

        public int addLevel() {
            if (winnum == 2 && stage == 2) {
                return 2;
            } else if (winnum == 2 && stage == 3) {
                return 1;
            } else {
                return -1;
            }
        }

        Boolean isequal(List<Integer> list1, List<Integer> list2) {
            boolean equal = true;
            for (int i = 0; i < list1.size(); i++) {
                final int ii = i;
                if (list1.get(i) != list2.get(i)) {
                    return false;
                }
            }
            return equal;
        }

        Boolean StageWin() {
            return isequal(CorrectAnswer[stage], PlayerAnswer[stage]);
        }
    }

    public static void inverse(List<Integer> list) {       //used in BackwardMode
        int j;
        for (int lo = 0, hi = list.size() - 1; lo < hi; lo++, hi--) {
            j = list.get(lo);
            list.set(lo, list.get(hi));
            list.set(hi, j);
        }

    }

    public String saveasjson()  {

            JSONArray jsonArray = new JSONArray();    //LevelTests
        try {
            for (int i = 0; i < Level_tests.size(); i++) {
                LevelTest thislevel = Level_tests.get(i);
                JSONObject levelTest = new JSONObject();

                levelTest.put(TestEntry.WINNUM, thislevel.winnum);
                JSONArray eachresult=new JSONArray();
                eachresult.put(thislevel.eacheresult[0]).put(thislevel.eacheresult[1]).put(thislevel.eacheresult[2]);
                levelTest.put(TestEntry.EACHRESULT,eachresult);
                JSONArray correctAnswer=new JSONArray();
                for (int j=0;j<thislevel.stage;j++){
                    correctAnswer.put(thislevel.CorrectAnswer[j]);
                }
                levelTest.put(TestEntry.CORRECTANSWER,correctAnswer);
                JSONArray playerAnswer=new JSONArray();
                for (int j=0;j<thislevel.stage;j++){
                    playerAnswer.put(thislevel.PlayerAnswer[j]);
                }
                levelTest.put(TestEntry.PLAYERANSWER,playerAnswer);
                jsonArray.put(levelTest);
            }

            Log.d(TAG, "saveasjson: " + jsonArray.toString());

        } catch (JSONException e){
            e.printStackTrace();
        }


        return jsonArray.toString();
    }
    private JSONArray List2Json(List mlist){
        JSONArray jsonArray=new JSONArray();
        for(int i=0;i<mlist.size();i++){
            jsonArray.put(mlist.get(i));
        }
        return jsonArray;
    }
}

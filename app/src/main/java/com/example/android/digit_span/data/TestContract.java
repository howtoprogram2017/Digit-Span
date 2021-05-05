package com.example.android.digit_span.data;

import android.net.Uri;

/**
 * Created by 刘博林 on 7/28/2017.
 */

public class TestContract {
    private TestContract(){}
    public static final int FORWARD_ONLY=0;
    public static final int BACKWARD_ONLY=1;
    public static final int FORWARD_BACKWARD=2;
    public static final String MODE="mode";

    public static final String CONTENT_AUTHORITY = "com.example.android.digit_span";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_TESTS = "tests";
    public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_TESTS);

    public static final class TestEntry{
        public static final String TABLE_NAME="tests";

        public static final String _ID="_id";
        public static final String TESTER_NAME="name";
        public static final String ISAUDITORY ="auditory";
        public static final String ISFORWARD ="isforward";
        public static final String TOP="top";
        public static final String ALLRESULT="allreselt";

        //used in LevelTest
        public static final String WINNUM="winnum";
        public static final String EACHRESULT="eachresult";
        public static final String CORRECTANSWER="correctanswer";
        public static final String PLAYERANSWER="playeranswer";

    }
}

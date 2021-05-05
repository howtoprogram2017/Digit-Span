package com.example.android.digit_span.utility;

import com.example.android.digit_span.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 刘博林 on 7/25/2017.
 */

public class Digit {
    public static List<Digit> mdigit;
    public int i;
    public int mAudioResource;
    Digit(int i,int mAudioResource){
        this.i=i;
        this.mAudioResource =mAudioResource;
    }
    public static void FindResoucre(){
        mdigit=new ArrayList<Digit>();
        mdigit.add(new Digit(0, R.raw.zero));
        mdigit.add(new Digit(1,R.raw.one));
        mdigit.add(new Digit(2,R.raw.two));
        mdigit.add(new Digit(3,R.raw.three));
        mdigit.add(new Digit(4,R.raw.four));
        mdigit.add(new Digit(5,R.raw.five));
        mdigit.add(new Digit(6,R.raw.six));
        mdigit.add(new Digit(7,R.raw.seven));
        mdigit.add(new Digit(8,R.raw.eight));
        mdigit.add(new Digit(9,R.raw.nine));
    };

}

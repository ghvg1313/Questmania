package com.akili.etc.triviacrashsaga.Entity;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by kangleif on 12/3/2015.
 */
public class PersonalRecord {

    public Date date;
    public String recordContent="";
    public String extraString="";

    public PersonalRecord(String content){
        date = new Date();
        recordContent = content;
        extraString = "";
    }

    public PersonalRecord(String content, String extraContent){
        date = new Date();
        recordContent = content;
        extraString = extraContent;
    }
}

package com.example.meetin;

import java.util.List;

public class Model {
    public String date,starttime,endtime,Did;
    public String[] participants;

    public void setDid(String s){Did = s;}

    public String getDid(){return Did;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants.toArray(new String[0]);
    }
    public String[] getParticipants(){return participants;}

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }
}

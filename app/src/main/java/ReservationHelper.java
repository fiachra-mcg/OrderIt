package com.example.orderit;

public class ReservationHelper {

    String partySize, time, name, number, date, email;

    public ReservationHelper(String partySize, String time, String name, String number, String date,
                             String email) {
        this.partySize = partySize;
        this.time = time;
        this.name = name;
        this.number = number;
        this.date = date;
        this.email = email;
    }


    public String getPartySize() {
        return partySize;
    }

    public void setPartySize(String partySize) {
        this.partySize = partySize;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) { this.number = number; }

    public String getDate() {
        return date;
    }

    public void setDate(String date) { this.date = date; }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) { this.email = email; }

}

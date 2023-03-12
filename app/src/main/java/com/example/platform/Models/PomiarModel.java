package com.example.platform.Models;

public class PomiarModel {
    String dataa;
    String nazwa_sensora;
    String wilgotnosc;


    public PomiarModel(String dataa, String nazwa_sensora, String wilgotnosc) {
        this.dataa = dataa;
        this.nazwa_sensora = nazwa_sensora;
        this.wilgotnosc = wilgotnosc;
    }

    public String getDataa() {
        return dataa;
    }

    public String getNazwa_sensora() {
        return nazwa_sensora;
    }

    public String getWilgotnosc() {
        return wilgotnosc;
    }

}


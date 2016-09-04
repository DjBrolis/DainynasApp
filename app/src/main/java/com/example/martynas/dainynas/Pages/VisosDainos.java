package com.example.martynas.dainynas.Pages;

import com.example.martynas.dainynas.Daina;

import java.util.List;

/**
 * Created by Martynas on 2016-09-04.
 */
public class VisosDainos {
    public String[] dainuPavadinimai;
    public Long [] dainuId;

    public VisosDainos (List<Daina> dainaList){
        dainuPavadinimai = new String[dainaList.size()];
        dainuId = new Long[dainaList.size()];

        for (int i = 0; i < dainaList.size(); i++) {
            dainuPavadinimai [i] = dainaList.get(i).pavadinimas;
            dainuId [i] = dainaList.get(i).getId();
        }
    }
}

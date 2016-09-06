package com.example.martynas.dainynas;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Martynas on 2016-09-06.
 */
@Table(name = "SetingsDB")
public class SettingsDB extends Model{
    @Column(name = "ZodziaiDysis")
    public int zodziaiDydis;
}

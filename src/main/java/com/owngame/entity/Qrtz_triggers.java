package com.owngame.entity;

/**
 * Created by Administrator on 2016-9-19.
 */
public class Qrtz_triggers {
    String schedule_name;
    String trigger_name;
    String trigger_state;

    public String getSchedule_name() {
        return schedule_name;
    }

    public void setSchedule_name(String schedule_name) {
        this.schedule_name = schedule_name;
    }

    public String getTrigger_name() {
        return trigger_name;
    }

    public void setTrigger_name(String trigger_name) {
        this.trigger_name = trigger_name;
    }

    public String getTrigger_state() {
        return trigger_state;
    }

    public void setTrigger_state(String trigger_state) {
        this.trigger_state = trigger_state;
    }
}

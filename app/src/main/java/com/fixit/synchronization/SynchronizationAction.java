package com.fixit.synchronization;

import java.util.Date;

/**
 * Created by Kostyantin on 3/27/2017.
 */

public class SynchronizationAction {

    public enum Action {
        INSERT,
        UPDATE,
        DELETE,
        OVERRIDE;

        private static Action find(String action) {
            if(action.equalsIgnoreCase(INSERT.name())) {
                return INSERT;
            } else if(action.equalsIgnoreCase(UPDATE.name())) {
                return UPDATE;
            } else if(action.equalsIgnoreCase(DELETE.name())) {
                return DELETE;
            } else if(action.equalsIgnoreCase(OVERRIDE.name())) {
                return OVERRIDE;
            }
            return null;
        }

        public String toLowerCaseName() {
            return name().toLowerCase();
        }
    }

    private String action;
    private Date date;

    public SynchronizationAction(String action) {
        this(action, new Date());
    }

    public SynchronizationAction(String action, Date date) {
        this.action = action;
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Action getActionEnum() {
        return Action.find(action);
    }

    @Override
    public String toString() {
        return "SynchronizationAction{" +
                "action='" + action + '\'' +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SynchronizationAction action1 = (SynchronizationAction) o;

        if (action != null ? !action.equals(action1.action) : action1.action != null) return false;
        return date != null ? date.equals(action1.date) : action1.date == null;

    }

    @Override
    public int hashCode() {
        int result = action != null ? action.hashCode() : 0;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }
}
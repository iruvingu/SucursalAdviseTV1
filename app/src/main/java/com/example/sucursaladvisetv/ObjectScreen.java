package com.example.sucursaladvisetv;

import java.sql.Timestamp;
import java.util.Date;

public class ObjectScreen {
    private Date refreshed;

    public ObjectScreen(Date refreshed) {
        this.refreshed = refreshed;
    }

    public ObjectScreen() {
    }

    public Date getRefreshed() {
        return refreshed;
    }

    public void setRefreshed(Date refreshed) {
        this.refreshed = refreshed;
    }

    @Override
    public String toString() {
        return "ObjectScreen{" +
                "refreshed = " + refreshed +
                '}';
    }
}

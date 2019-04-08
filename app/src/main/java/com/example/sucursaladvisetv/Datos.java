package com.example.sucursaladvisetv;

import com.google.gson.annotations.SerializedName;

class Datos {
    @SerializedName("data")
    String data;
    @SerializedName("url")
    String url;
    @SerializedName("created")
    Long created;
    @SerializedName("uId")
    String uId;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Long getCreated() {
        return created;
    }

    public void setCreated(Long created) {
        this.created = created;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    @Override
    public String toString() {
        return "Datos{" +
                "data='" + data + '\'' +
                ", url='" + url + '\'' +
                ", created=" + created +
                ", uId='" + uId + '\'' +
                '}';
    }
}

package com.example.sucursaladvisetv;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClass {

    public static InterfaceRetrofit newInstance(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://us-central1-fintech-desarrollo-mx.cloudfunctions.net/")
                //http://us-central1-fintech-desarrollo-mx.cloudfunctions.net//IDPANTALLA
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        InterfaceRetrofit peticionRetrofit = retrofit.create(InterfaceRetrofit.class);
        return  peticionRetrofit;
    }


    public static class Response{
        @SerializedName("success")
        Boolean sucess;
        @SerializedName("statusCode")
        Integer statusCode;
        @SerializedName("result")
        String result;

        public Boolean getSucess() {
            return sucess;
        }

        public void setSucess(Boolean sucess) {
            this.sucess = sucess;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "Response{" +
                    "sucess=" + sucess +
                    ", statusCode=" + statusCode +
                    ", result='" + result + '\'' +
                    '}';
        }
    }

    public static class Request{

        public Request(String uId, String time, boolean status) {
            this.uId = uId;
            this.time = time;
            this.status = status;
        }

        private String uId;

        private String time;

        private boolean status;

        public void setUId(String uId){
            this.uId = uId;
        }

        public String getUId(){
            return uId;
        }

        public void setTime(String time){
            this.time = time;
        }

        public String getTime(){
            return time;
        }

        public void setStatus(boolean status){
            this.status = status;
        }

        public boolean isStatus(){
            return status;
        }

        @Override
        public String toString(){
            return
                    "Request{" +
                            "uId = '" + uId + '\'' +
                            ", time = '" + time + '\'' +
                            ", status = '" + status + '\'' +
                            "}";
        }
    }

    public static class ResponseMedia{
        @SerializedName("success")
        Boolean success;
        @SerializedName("statusCode")
        Integer statusCode;
        @SerializedName("result")
        ArrayList result;

        public Boolean getSuccess() {
            return success;
        }

        public void setSuccess(Boolean success) {
            this.success = success;
        }

        public Integer getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(Integer statusCode) {
            this.statusCode = statusCode;
        }

        public ArrayList<Datos> getResult() {
            return result;
        }

        public void setResult(ArrayList result) {
            this.result = result;
        }

        @Override
        public String toString() {
            return "ResponseMedia{" +
                    "success=" + success +
                    ", statusCode=" + statusCode +
                    ", result=" + result +
                    '}';
        }
    }
}

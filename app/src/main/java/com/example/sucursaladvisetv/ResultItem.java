package com.example.sucursaladvisetv;

import com.google.gson.annotations.SerializedName;

public class ResultItem{

	@SerializedName("uId")
	private String uId;

	@SerializedName("data")
	private String data;

	@SerializedName("created")
	private long created;

	@SerializedName("url")
	private String url;

	public void setUId(String uId){
		this.uId = uId;
	}

	public String getUId(){
		return uId;
	}

	public void setData(String data){
		this.data = data;
	}

	public String getData(){
		return data;
	}

	public void setCreated(long created){
		this.created = created;
	}

	public long getCreated(){
		return created;
	}

	public void setUrl(String url){
		this.url = url;
	}

	public String getUrl(){
		return url;
	}

	@Override
 	public String toString(){
		return 
			"ResultItem{" + 
			"uId = '" + uId + '\'' + 
			",data = '" + data + '\'' + 
			",created = '" + created + '\'' + 
			",url = '" + url + '\'' + 
			"}";
		}
}
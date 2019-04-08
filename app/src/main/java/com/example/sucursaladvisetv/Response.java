package com.example.sucursaladvisetv;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class Response{

	@SerializedName("result")
	private List<ResultItem> result;

	@SerializedName("success")
	private boolean success;

	@SerializedName("statusCode")
	private int statusCode;

	public void setResult(List<ResultItem> result){
		this.result = result;
	}

	public List<ResultItem> getResult(){
		return result;
	}

	public void setSuccess(boolean success){
		this.success = success;
	}

	public boolean isSuccess(){
		return success;
	}

	public void setStatusCode(int statusCode){
		this.statusCode = statusCode;
	}

	public int getStatusCode(){
		return statusCode;
	}

	@Override
 	public String toString(){
		return 
			"Response{" + 
			"result = '" + result + '\'' + 
			",success = '" + success + '\'' + 
			",statusCode = '" + statusCode + '\'' + 
			"}";
		}
}
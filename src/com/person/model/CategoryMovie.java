package com.person.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.person.module.http.DataFetchException;
import com.person.module.http.ObjectBase;

public class CategoryMovie extends ObjectBase {
	private static final long serialVersionUID = 1L;
	public ArrayList<CategoryMovieItem> mCategoryMovieList = new ArrayList<CategoryMovie.CategoryMovieItem>();
	
	@Override
	public void createFromResponse(String response) throws DataFetchException {
		try{
			JSONObject jsonData = new JSONObject(response);
			if (jsonData.getInt("errcode") != 0){
				throw new DataFetchException(-1, "数据解析出错");
			}
			
			JSONArray array = jsonData.getJSONArray("result");
			for(int i = 0; i < array.length(); i++){
				CategoryMovieItem item = new CategoryMovieItem();
				
				JSONObject tmp = array.getJSONObject(i);
				item.typeId = tmp.getString("typeid");
				item.typeName = tmp.getString("typename");
				
				mCategoryMovieList.add(item);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new DataFetchException(-1, "数据解析出错");
		}
	}

	public static class CategoryMovieItem implements Serializable{
		private static final long serialVersionUID = 1L;
		public String typeId;
		public String typeName;
	}
	
}

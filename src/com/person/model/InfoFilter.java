package com.person.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.person.model.CategoryMovie.CategoryMovieItem;
import com.person.module.http.DataFetchException;
import com.person.module.http.ObjectBase;

public class InfoFilter extends ObjectBase {
	private static final long serialVersionUID = 1L;

	public ArrayList<FilterPair> mFilterPair = new ArrayList<InfoFilter.FilterPair>();
	
	@Override
	public void createFromResponse(String response) throws DataFetchException {
		try{
			JSONObject jsonData = new JSONObject(response);
			if (jsonData.getInt("errcode") != 0){
				throw new DataFetchException(-1, "数据解析出错");
			}
			
			JSONArray array = jsonData.getJSONArray("result");
			for(int i = 0; i < array.length(); i++){
				FilterPair item = new FilterPair();
				
				JSONObject tmp = array.getJSONObject(i);
				item.parentname = tmp.getString("parentname");
				item.type = tmp.getString("type");
				
				JSONArray arrayChild = tmp.getJSONArray("data");
				for(int j = 0; j < arrayChild.length(); j++){
					FilterItem filterItem = new FilterItem();
					filterItem.name = arrayChild.getJSONObject(j).optString("name");
					item.mFilterItem.add(filterItem);
				}
				
				mFilterPair.add(item);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new DataFetchException(-1, "数据解析出错");
		}
	}

	public static class FilterPair{
		public String parentname;
		public String type;
		public ArrayList<FilterItem> mFilterItem = new ArrayList<InfoFilter.FilterItem>();
	}
	
	public static class FilterItem{
		public String name;
	}
	
}

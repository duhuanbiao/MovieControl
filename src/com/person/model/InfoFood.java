package com.person.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.person.module.http.DataFetchException;
import com.person.module.http.ObjectBase;

public class InfoFood extends ObjectBase {
	private static final long serialVersionUID = 1L;

	public ArrayList<FoodItem> mFoodList = new ArrayList<FoodItem>();
	
	@Override
	public void createFromResponse(String response) throws DataFetchException {
		try{
			JSONObject jsonData = new JSONObject(response);
			if (jsonData.getInt("errcode") != 0){
				throw new DataFetchException(-1, "数据解析出错");
			}
			
			JSONArray array = jsonData.getJSONArray("result");
			for(int i = 0; i < array.length(); i++){
				FoodItem item = new FoodItem();
				
				JSONObject tmp = array.getJSONObject(i);
				item.menuid = tmp.getString("menuid");
				item.bigimg = tmp.getString("bigimg");
				item.smallimg = tmp.getString("smallimg");
				item.menuname = tmp.getString("menuname");
				item.price = tmp.getInt("cost");
				
				mFoodList.add(item);
			}
			
		}catch(Exception e){
			e.printStackTrace();
			throw new DataFetchException(-1, "数据解析出错");
		}
	}

	public static class FoodItem{
		public String menuid;
		public String bigimg;
		public String smallimg;
		public String menuname;
		public int price;
		
		//临时使用
		public int orderCount;
	}
	
}

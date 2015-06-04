package com.app.secnodhand.base;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.app.secnodhand.MyApplication;


public class MyBaseAdapter<T> extends BaseAdapter {
	protected LayoutInflater inflater;
	protected List<T> dataList;

	public MyBaseAdapter() {
		this.inflater = LayoutInflater.from(MyApplication.mContext);
		this.dataList = new ArrayList<T>();
	}

    public MyBaseAdapter(List<T> dataList){
        this.inflater=LayoutInflater.from(MyApplication.mContext);
        this.dataList=dataList;
    }

	public List<T> getData() {
		return dataList;
	}

	public void refreshUIByReplaceData(List<T> list) {
		this.dataList = list;
		notifyDataSetChanged();
	}

	public void refreshUIByAddData(List<T> list) {
		this.dataList.addAll(list);
		notifyDataSetChanged();
	}

	public void refreshUIByAddItemData(T item) {
		this.dataList.add(item);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return dataList.size();
	}

	@Override
	public Object getItem(int position) {
		return dataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return null;
	}


}

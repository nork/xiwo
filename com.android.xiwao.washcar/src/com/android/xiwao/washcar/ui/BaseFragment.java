package com.android.xiwao.washcar.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.xiwao.washcar.R;

public abstract class BaseFragment extends Fragment{
	@Override  
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
        View view = inflater.inflate(R.layout.fragment, null);  
        return view;         
    } 
	
    public abstract void initContentView();
}

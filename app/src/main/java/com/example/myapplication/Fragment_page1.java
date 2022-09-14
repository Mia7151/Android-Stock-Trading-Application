package com.example.myapplication;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.highsoft.highcharts.common.hichartsclasses.HIChart;
import com.highsoft.highcharts.common.hichartsclasses.HIColumn;
import com.highsoft.highcharts.common.hichartsclasses.HIOptions;
import com.highsoft.highcharts.common.hichartsclasses.HISeries;
import com.highsoft.highcharts.common.hichartsclasses.HITitle;
import com.highsoft.highcharts.core.HIChartView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Fragment_page1#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Fragment_page1 extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Fragment_page1() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Fragment_page1.
     */
    // TODO: Rename and change types and number of parameters
    public static Fragment_page1 newInstance(String param1, String param2) {
        Fragment_page1 fragment = new Fragment_page1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        String stock = searchRes.stock_get ;


        View v = inflater.inflate(R.layout.fragment_page1,container,false);
        WebView myWebView = v.findViewById(R.id.webview);
        myWebView.getSettings().setAllowFileAccess(true);
        myWebView.getSettings().setJavaScriptEnabled (true);
        myWebView.loadUrl("file:///android_asset/highcharts1.html");
        myWebView.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                //Here you want to use .loadUrl again
                //on the webview object and pass in
                //"javascript:<your javaScript function"
                myWebView.loadUrl("javascript:show3('" + stock + "')"); //if passing in an object. Mapping may need to take place
            }
        });


        /*
        HIOptions options = new HIOptions();

        HIChart chart = new HIChart();
        chart.setType("column");
        options.setChart(chart);

        HITitle title = new HITitle();
        title.setText("Demo chart");

        options.setTitle(title);

        HIColumn series = new HIColumn();
        series.setData(new ArrayList<>(Arrays.asList(49.9, 71.5, 106.4, 129.2, 144, 176, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4)));
        options.setSeries(new ArrayList<HISeries>(Collections.singletonList(series)));

        chartView.setOptions(options);*/




        return v;
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_page1, container, false);
    }
}
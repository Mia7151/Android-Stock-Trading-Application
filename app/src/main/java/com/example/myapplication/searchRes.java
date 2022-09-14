package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class searchRes extends AppCompatActivity {
    static Integer count =0;

    private static final String TAG = "MyActivity";
    private static final String TAG1 = "souce1";
    private static final String TAG2 = "peers";
    private static final String TAG3 = "res11111";
    public  Integer s11;
    public  Double sum;

    static  String stock_get;
    List<String> fav_list = new ArrayList<>();
    String fav_list_name1;
    List<String> fav_list_name = new ArrayList<>();
    Double fav_list_pricea1;
    List<Double> fav_list_pricea = new ArrayList<>();
    Double fav_list_priceb1;
    List<Double> fav_list_priceb = new ArrayList<>();
    Double fav_list_pricec1;
    List<Double> fav_list_pricec = new ArrayList<>();
    private Button button;





    Gson gson = new Gson();



    //for news
    /*
    List<String> list_source = new ArrayList<>();
    List<String> list_img = new ArrayList<>();
    List<String> list_title = new ArrayList<>();
    List<Long> list_time = new ArrayList<>();
    List<String> list_url = new ArrayList<>();
    List<String> list_summary = new ArrayList<>();
    List<String> list_time1 = new ArrayList<>();
    List<String> list_time2 = new ArrayList<>();

     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);

        setTheme(R.style.Theme_MyApplication);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_res);



        Intent intent2 = getIntent();
        String stock = intent2.getStringExtra("stock");
        stock_get  = stock;

        Toolbar myToolbar = (Toolbar) findViewById(R.id.bar1);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle(stock);

        //add back home arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //for spinner
        RelativeLayout r = (RelativeLayout) findViewById(R.id.details);
        r.setVisibility(View.GONE);
        LinearLayout linlaHeaderProgress1 = (LinearLayout) findViewById(R.id.linlaHeaderProgress1);
        linlaHeaderProgress1.setVisibility(View.VISIBLE);



        //favorite



        makeApiCall_companydesp(stock);
        makeApiCall_latestprice(stock);

        //get companypeers data
        makeApiCall_companypeers(stock);
        //social sentiment
        makeApiCall_socialsentiment(stock);
        // recommendation




        //for two tabs
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager2 viewPager2 = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(this);
        viewPager2.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager2,
                new TabLayoutMediator.TabConfigurationStrategy() {
                    final int[] tabIcons = {R.drawable.chartline, R.drawable.clocktimethree};
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

                        tab.setIcon(tabIcons[position]);

                    }
                }).attach();

        //get 6 hours age highcharts
        /*
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Long to_unix = timestamp.getTime() / 1000;
        Log.d(TAG, String.valueOf(to_unix));
        Long from_unix = to_unix - 6*60*60;
        Log.d(TAG, String.valueOf(from_unix));
        makeApiCall_histdata(stock,from_unix,to_unix);*/

        //webview
        WebView myWebView1 = (WebView) findViewById(R.id.webview1);
        myWebView1.getSettings().setAllowFileAccess(true);
        myWebView1.getSettings().setJavaScriptEnabled (true);
        myWebView1.loadUrl("file:///android_asset/highcharts3.html");

        myWebView1.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                //Here you want to use .loadUrl again
                //on the webview object and pass in
                //"javascript:<your javaScript function"
                myWebView1.loadUrl("javascript:show('" + stock + "')"); //if passing in an object. Mapping may need to take place
            }
        });





        WebView myWebView4 = (WebView) findViewById(R.id.webview4);
        myWebView4.getSettings().setAllowFileAccess(true);
        myWebView4.getSettings().setJavaScriptEnabled (true);
        myWebView4.loadUrl("file:///android_asset/highcharts4.html");
        myWebView4.setWebViewClient(new WebViewClient(){
            public void onPageFinished(WebView view, String url){
                //Here you want to use .loadUrl again
                //on the webview object and pass in
                //"javascript:<your javaScript function"
                myWebView4.loadUrl("javascript:show1('" + stock + "')"); //if passing in an object. Mapping may need to take place
            }
        });

        //news list
        makeApiCall_news(stock);
        //Log.d(TAG1, String.valueOf(list_source));
        //RecyclerView recyclerView = findViewById(R.id.recyclerView111);
        //MyAdapter myAdapter = new MyAdapter(this,list_source,list_time1,list_summary,list_img);
        //recyclerView.setAdapter(myAdapter);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));


        // click trade button
        button = (Button) findViewById(R.id.button_id);
        button.setOnClickListener(new View.OnClickListener() {

                                      @Override
                                      public void onClick(View arg0) {

                                          // custom dialog
                                          final Dialog dialog = new Dialog(searchRes.this);
                                          //round dialog


                                          dialog.setContentView(R.layout.trade_dialog);
                                          dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);

                                          //round dialog
                                          //dialog.setContentView(R.layout.trade_dialog);
                                          TextView t1 = (TextView) dialog.findViewById(R.id.detail_trade_dialog_title);
                                          String s1 = "Trade " + fav_list_name1 + " shares";
                                          t1.setText(s1);
                                          TextView text = (TextView) dialog.findViewById(R.id.detail_trade_calculate);

                                          DecimalFormat f1 = new DecimalFormat("##0.00");
                                          DecimalFormat f = new DecimalFormat("##0.0");
                                          String pricea_string = f1.format(fav_list_pricea1);
                                          String zero_input = "0*$"+ pricea_string+"/share = 0.00";
                                          text.setText(zero_input);

                                          SharedPreferences pref = getSharedPreferences("mykey", 0);
                                          SharedPreferences.Editor editor = pref.edit();
                                          String money = pref.getString("money","");
                                          double money1 = Double.parseDouble(money);
                                          TextView text2 = (TextView) dialog.findViewById(R.id.detail_trade_balance);
                                          String money_res = "$" + f1.format(money1) +" to buy " + stock_get;
                                          text2.setText(money_res);


                                          EditText editText=(EditText) dialog.findViewById(R.id.detail_trade_Et);
                                          editText.addTextChangedListener(new TextWatcher() {
                                              @Override
                                              public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                  s11 = Integer.parseInt(s.toString());
                                                  sum = s11 * fav_list_pricea1;
                                                  String s2 = f.format(s11);
                                                  String sum1 = f1.format(sum);

                                                  String s3 = s2 + "*$" +"" + pricea_string + "/share = " + sum1;

                                                  text.setText(s3);

                                              }

                                              @Override
                                              public void beforeTextChanged(CharSequence s, int start, int count,
                                                                            int after) {
                                              }

                                              @Override
                                              public void afterTextChanged(Editable s) {
                                              }
                                              });



                                          Button buy = (Button) dialog.findViewById(R.id.detail_trade_buy_btn);
                                          buy.setOnClickListener(new View.OnClickListener(){
                                              @SuppressLint("ResourceType")
                                              @Override
                                              public void onClick(View v) {
                                                  if(s11==0){
                                                      Toast.makeText(searchRes.this,"Cannot buy non-positive shares", Toast.LENGTH_SHORT).show();
                                                  }else if ( sum >money1){
                                                      Toast.makeText(searchRes.this,"Not enough money to buy", Toast.LENGTH_SHORT).show();
                                                  }else{

                                                      dialog.dismiss();
                                                      //deal with buy situation store
                                                      SharedPreferences pref_p = getSharedPreferences("port", 0);
                                                      SharedPreferences.Editor editor_p = pref_p.edit();
                                                      if (pref_p.contains(stock_get)){
                                                          String res = pref_p.getString(stock_get,null);
                                                          List<String> res_j = gson.fromJson(res, new TypeToken<List<String>>(){}.getType());
                                                          Integer share_pre = Integer.parseInt(res_j.get(1));
                                                          Double avg_cost_pre = Double.parseDouble(res_j.get(2));
                                                          Double avg_cost = ((s11*fav_list_pricea1) + (share_pre * avg_cost_pre))/(share_pre + s11);

                                                          //store back
                                                          List<String> info_list = new ArrayList<>();
                                                          info_list.add(stock_get);info_list.add(Integer.toString(s11+share_pre));info_list.add(f1.format(avg_cost));info_list.add(f1.format(fav_list_pricea1));
                                                          String json = gson.toJson(info_list);
                                                          editor_p.putString(stock_get,json);
                                                          editor_p.commit();

                                                      }else {
                                                          List<String> info_list = new ArrayList<>();
                                                          Double avg_cost = fav_list_pricea1;
                                                          info_list.add(stock_get);info_list.add(Integer.toString(s11));info_list.add(f1.format(avg_cost));info_list.add(f1.format(fav_list_pricea1));
                                                          String json = gson.toJson(info_list);
                                                          editor_p.putString(stock_get,json);
                                                          editor_p.commit();
                                                      };

                                                      //store money back
                                                      Double money2 = money1 - fav_list_pricea1*s11;
                                                      editor.putString("money",Double.toString(money2));
                                                      editor.commit();

                                                      //deal with show res
                                                      DecimalFormat f = new DecimalFormat("##0.00");
                                                      String res_show = pref_p.getString(stock_get,null);
                                                      List<String> res_j = gson.fromJson(res_show, new TypeToken<List<String>>(){}.getType());
                                                      Integer share_pre = Integer.parseInt(res_j.get(1));
                                                      Double avg_cost_pre = Double.parseDouble(res_j.get(2));
                                                      Double total_cost = share_pre * avg_cost_pre;
                                                      Double change = (fav_list_pricea1 - avg_cost_pre)*share_pre;
                                                      Double market_value = fav_list_pricea1 * share_pre;
                                                      Log.d("change", String.valueOf(change));

                                                      TextView so1 = (TextView) findViewById(R.id.so1);
                                                      so1.setText(res_j.get(1));
                                                      TextView ac1 = (TextView) findViewById(R.id.ac1);
                                                      ac1.setText("$"+res_j.get(2));
                                                      TextView tc1 = (TextView) findViewById(R.id.tc1);
                                                      tc1.setText("$"+ f.format(total_cost));
                                                      TextView ch1 = (TextView) findViewById(R.id.ch1);
                                                      ch1.setText("$"+f.format(change));
                                                      TextView mv1 = (TextView) findViewById(R.id.mv1);
                                                      mv1.setText("$"+f.format(market_value));
                                                      if (change < 0){
                                                          ch1.setTextColor(getResources().getColor(R.color.red));
                                                          mv1.setTextColor(getResources().getColor(R.color.red));
                                                      }else if(change>0){
                                                          ch1.setTextColor(getResources().getColor(R.color.green));
                                                          mv1.setTextColor(getResources().getColor(R.color.green));
                                                      }else {
                                                          ch1.setTextColor(Color.parseColor("#8a8a8a"));
                                                          mv1.setTextColor(Color.parseColor("#8a8a8a"));
                                                      }





                                                  final Dialog dialog2 = new Dialog(searchRes.this);
                                                  dialog2.setContentView(R.layout.trade_res_buy);
                                                  dialog2.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round_green);

                                                  dialog2.show();
                                                  TextView textView_1  = (TextView) dialog2.findViewById(R.id.show_message);
                                                  textView_1.setText("You have successfully bought " + s11.toString() +"\n"+ " shares of " + stock_get);

                                                  Button done1  = (Button) dialog2.findViewById(R.id.done1);
                                                  done1.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                                          dialog2.dismiss();
                                                      }
                                                  });
                                              }}
                                          });

                                          Button sell = (Button) dialog.findViewById(R.id.detail_trade_sell_btn);
                                          sell.setOnClickListener(new View.OnClickListener(){
                                              @SuppressLint("ResourceType")
                                              @Override
                                              public void onClick(View v) {
                                                  SharedPreferences pref_p = getSharedPreferences("port", 0);
                                                  SharedPreferences.Editor editor_p = pref_p.edit();
                                                  String res = pref_p.getString(stock_get,null);
                                                  List<String> res_j = gson.fromJson(res, new TypeToken<List<String>>(){}.getType());
                                                  Integer share_pre;
                                                  if (res_j == null){
                                                      share_pre = 0;
                                                  }else{
                                                    share_pre = Integer.parseInt(res_j.get(1));}

                                                  TextView so1 = (TextView) findViewById(R.id.so1);
                                                  TextView ac1 = (TextView) findViewById(R.id.ac1);
                                                  TextView tc1 = (TextView) findViewById(R.id.tc1);
                                                  TextView ch1 = (TextView) findViewById(R.id.ch1);
                                                  TextView mv1 = (TextView) findViewById(R.id.mv1);

                                                  if(s11==0){
                                                      Toast.makeText(searchRes.this,"Cannot sell non-positive shares", Toast.LENGTH_LONG).show();
                                                  }else if (s11 > share_pre){
                                                      Toast.makeText(searchRes.this,"Not enough shares to sell", Toast.LENGTH_LONG).show();
                                                  } else{
                                                      dialog.dismiss();
                                                      //deal with sell situation store
                                                      Double avg_cost_pre = Double.parseDouble(res_j.get(2));
                                                      if (s11 == share_pre){
                                                          pref_p.edit().remove(stock_get).commit();
                                                          so1.setText("0");
                                                          //TextView ac1 = (TextView) findViewById(R.id.ac1);
                                                          ac1.setText("$0.00");
                                                          //TextView tc1 = (TextView) findViewById(R.id.tc1);
                                                          tc1.setText("$0.00");
                                                          //TextView ch1 = (TextView) findViewById(R.id.ch1);
                                                          ch1.setText("$0.00");
                                                          ch1.setTextColor(Color.parseColor("#8a8a8a"));
                                                          // TextView mv1 = (TextView) findViewById(R.id.mv1);
                                                          mv1.setText("$0.00");
                                                          mv1.setTextColor(Color.parseColor("#8a8a8a"));

                                                          //store money
                                                          Double money2 = money1 + fav_list_pricea1*s11;
                                                          editor.putString("money",Double.toString(money2));
                                                          editor.commit();
                                                      }else{
                                                      Double avg_cost = (-(s11*fav_list_pricea1) + (share_pre * avg_cost_pre))/(share_pre - s11);

                                                      // store moeny
                                                          Double money2 = money1 + fav_list_pricea1*s11;
                                                          editor.putString("money",Double.toString(money2));
                                                          editor.commit();

                                                      //store back
                                                      List<String> info_list = new ArrayList<>();
                                                      info_list.add(stock_get);info_list.add(Integer.toString(share_pre-s11));info_list.add(f1.format(avg_cost));info_list.add(f1.format(fav_list_pricea1));
                                                      String json = gson.toJson(info_list);
                                                      editor_p.putString(stock_get,json);
                                                      editor_p.commit();



                                                      //deal with show res
                                                      String res_show = pref_p.getString(stock_get,null);
                                                      List<String> res_j2 = gson.fromJson(res_show, new TypeToken<List<String>>(){}.getType());
                                                      Integer share_pre2 = Integer.parseInt(res_j2.get(1));
                                                      Double avg_cost_pre2 = Double.parseDouble(res_j2.get(2));
                                                      Double total_cost = share_pre2 * avg_cost_pre2;
                                                      Double change = (fav_list_pricea1 - avg_cost_pre2)*share_pre2;
                                                      Double market_value = fav_list_pricea1 * share_pre2;

                                                      //TextView so1 = (TextView) findViewById(R.id.so1);
                                                      so1.setText(res_j2.get(1));
                                                      //TextView ac1 = (TextView) findViewById(R.id.ac1);
                                                      ac1.setText("$"+f1.format(avg_cost_pre2));
                                                      //TextView tc1 = (TextView) findViewById(R.id.tc1);
                                                      tc1.setText("$"+f1.format(total_cost));
                                                      //TextView ch1 = (TextView) findViewById(R.id.ch1);
                                                      ch1.setText("$"+f1.format(change));
                                                     // TextView mv1 = (TextView) findViewById(R.id.mv1);
                                                      mv1.setText("$"+f1.format(market_value));
                                                      Log.d("avg", String.valueOf(avg_cost));
                                                      if (fav_list_pricea1 - avg_cost_pre2 < 0){
                                                          ch1.setTextColor(getResources().getColor(R.color.red));
                                                          mv1.setTextColor(getResources().getColor(R.color.red));
                                                      }else if(fav_list_pricea1 - avg_cost_pre2>0){
                                                          ch1.setTextColor(getResources().getColor(R.color.green));
                                                          mv1.setTextColor(getResources().getColor(R.color.green));
                                                      }}




                                                      final Dialog dialog1 = new Dialog(searchRes.this);
                                                  dialog1.setContentView(R.layout.trade_res);
                                                  dialog1.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round_green);

                                                  dialog1.show();
                                                  TextView textView_1  = (TextView) dialog1.findViewById(R.id.show_message);
                                                  textView_1.setText("You have successfully sold " + s11 + "\n"+ " shares of " + stock_get);


                                                  Button done  = (Button) dialog1.findViewById(R.id.done);
                                                  done.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                                          dialog1.dismiss();
                                                      }
                                                  });
                                              }}
                                          });


                                          dialog.show();
                                      }


                                  }


        );




    }

    public List<String> getQuoteList1(){
        Gson gson = new Gson();
        SharedPreferences preferenceManager = getSharedPreferences("mykey", 0); // 0 - for private mode
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(preferenceManager.getString("stock_f",
                null), type);
    }

    public List<String> getQuoteList2(){
        Gson gson = new Gson();
        SharedPreferences preferenceManager = getSharedPreferences("mykey", 0); // 0 - for private mode
        Type type = new TypeToken<List<String>>() {
        }.getType();
        return gson.fromJson(preferenceManager.getString("stock_f_name",
                null), type);
    }
    public List<Double> getQuoteList3(){
        Gson gson = new Gson();
        SharedPreferences preferenceManager = getSharedPreferences("mykey", 0); // 0 - for private mode
        Type type = new TypeToken<List<Double>>() {
        }.getType();
        return gson.fromJson(preferenceManager.getString("stock_f_pricea",
                null), type);
    }

    public List<Double> getQuoteList4(){
        Gson gson = new Gson();
        SharedPreferences preferenceManager = getSharedPreferences("mykey", 0); // 0 - for private mode
        Type type = new TypeToken<List<Double>>() {
        }.getType();
        return gson.fromJson(preferenceManager.getString("stock_f_priceb",
                null), type);
    }

    public List<Double> getQuoteList5(){
        Gson gson = new Gson();
        SharedPreferences preferenceManager = getSharedPreferences("mykey", 0); // 0 - for private mode
        Type type = new TypeToken<List<Double>>() {
        }.getType();
        return gson.fromJson(preferenceManager.getString("stock_f_pricec",
                null), type);
    }








    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu1, menu);

        //decide whether exit
        SharedPreferences pref = getSharedPreferences("mykey", 0); // 0 - for private mode
        SharedPreferences.Editor editor = pref.edit();
        if (pref.contains("stock_f")){
            String res1 = pref.getString("stock_f","");
            List<String> stockList = getQuoteList1();
            if (stockList.contains(stock_get)){
                menu.getItem(0).setIcon(R.drawable.star1);
                Drawable drawable = menu.getItem(0).getIcon();
                drawable.mutate();
                drawable.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
            };}


        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.action_search1);
        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        // Get SearchView autocomplete object.



        //favorite

        Gson gson = new Gson();
        searchMenu.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                SharedPreferences pref = getSharedPreferences("mykey", 0); // 0 - for private mode
                SharedPreferences.Editor editor = pref.edit();
                if (pref.contains("stock_f")){
                    //String res1 = pref.getString("stock_f","");
                    //String res2 = pref.getString("stock_f_name","");

                    List<String> stockList = getQuoteList1();
                    List<String> stockList_name = getQuoteList2();
                    List<Double> stockList_pricea = getQuoteList3();
                    List<Double> stockList_priceb = getQuoteList4();
                    List<Double> stockList_pricec = getQuoteList5();



                    if (stockList.contains(stock_get)){
                        stockList.remove(stock_get);
                        stockList_name.remove(fav_list_name1);
                        stockList_pricea.remove(fav_list_pricea1);
                        stockList_priceb.remove(fav_list_priceb1);
                        stockList_pricec.remove(fav_list_pricec1);


                        item.setIcon(R.drawable.staroutline);
                        Drawable drawable = menu.getItem(0).getIcon();
                        drawable.mutate();
                        drawable.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                        Toast.makeText(searchRes.this,stock_get+" is removed from favorites", Toast.LENGTH_LONG).show();
                    }else{
                        stockList.add(stock_get);
                        stockList_name.add(fav_list_name1);
                        stockList_pricea.add(fav_list_pricea1);
                        stockList_priceb.add(fav_list_priceb1);
                        stockList_pricec.add(fav_list_pricec1);


                        item.setIcon(R.drawable.star1);
                        Drawable drawable = menu.getItem(0).getIcon();
                        drawable.mutate();
                        drawable.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                        Toast.makeText(searchRes.this,stock_get+" is added to favorites", Toast.LENGTH_LONG).show();
                    }
                    String jsonText = gson.toJson(stockList);
                    String jsonText1 = gson.toJson(stockList_name);
                    String jsonText2 = gson.toJson(stockList_pricea);
                    String jsonText3 = gson.toJson(stockList_priceb);
                    String jsonText4 = gson.toJson(stockList_pricec);

                    editor.putString("stock_f", jsonText);
                    editor.putString("stock_f_name", jsonText1);
                    editor.putString("stock_f_pricea", jsonText2);
                    editor.putString("stock_f_priceb", jsonText3);
                    editor.putString("stock_f_pricec", jsonText4);


                }else{
                    fav_list.add(stock_get);
                    fav_list_name.add(fav_list_name1);
                    fav_list_pricea.add(fav_list_pricea1);
                    fav_list_priceb.add(fav_list_priceb1);
                    fav_list_pricec.add(fav_list_pricec1);

                    item.setIcon(R.drawable.star1);
                    Drawable drawable = menu.getItem(0).getIcon();
                    drawable.mutate();
                    drawable.setColorFilter(getResources().getColor(R.color.red), PorterDuff.Mode.SRC_ATOP);
                    String jsonText = gson.toJson(fav_list);
                    String jsonText1 = gson.toJson(fav_list_name);
                    String jsonText2 = gson.toJson(fav_list_pricea);
                    String jsonText3 = gson.toJson(fav_list_priceb);
                    String jsonText4 = gson.toJson(fav_list_pricec);

                    editor.putString("stock_f", jsonText);
                    editor.putString("stock_f_name", jsonText1);
                    editor.putString("stock_f_pricea", jsonText2);
                    editor.putString("stock_f_priceb", jsonText3);
                    editor.putString("stock_f_pricec", jsonText4);

                    Toast.makeText(searchRes.this,stock_get+" is added to favorites", Toast.LENGTH_LONG).show();
                }
                editor.commit();

                return true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                //backbuton
                if (searchRes.count == 2){
                    searchRes.count = 0;
                }else{
                startActivity(new Intent(this,MainActivity.class));}
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void makeApiCall_companydesp(String text) {
        ApiCall.companydesp(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONObject companydesp = new JSONObject(response);

                    TextView textView_name = (TextView) findViewById(R.id.name);
                    String name = companydesp.getString("name");
                    textView_name.setText(name);

                    //store name
                    fav_list_name1 = name;


                    TextView textView_symbol = (TextView) findViewById(R.id.symbol);
                    textView_symbol.setText(text);

                    ImageView img = (ImageView) findViewById(R.id.img);
                    String logo = companydesp.getString("logo");
                    Picasso.get().load(logo).into(img);

                    TextView textView_ins = (TextView) findViewById(R.id.ind1);
                    String insd = companydesp.getString("finnhubIndustry");
                    textView_ins.setText(insd);

                    //About web
                    String web = companydesp.getString("weburl");
                    TextView textView_web = (TextView) findViewById(R.id.web1);
                    String myweb = "<a style='text-decoration:underline; color='#FF3700B3'' href='" + web + "'>" + web + "</a>" ;
                    if (Build.VERSION.SDK_INT >= 24) {
                        textView_web.setText(Html.fromHtml(myweb, Html.FROM_HTML_MODE_LEGACY));
                    } else {
                        textView_web.setText(Html.fromHtml(myweb));
                    }

                    // ipo
                    String date = companydesp.getString("ipo");
                    String s1 = date.split("-")[0];
                    String s2 = date.split("-")[1];
                    String s3 = date.split("-")[2];
                    String date1 = s2 + "-" + s3 + "-" +s1;
                    TextView textView_ipo = (TextView) findViewById(R.id.ipo1);
                    textView_ipo.setText(date1);

                    LinearLayout linlaHeaderProgress1 = (LinearLayout) findViewById(R.id.linlaHeaderProgress1);
                    linlaHeaderProgress1.setVisibility(View.GONE);
                    RelativeLayout r = (RelativeLayout) findViewById(R.id.details);
                    r.setVisibility(View.VISIBLE);





                    Log.d(TAG, String.valueOf(companydesp));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });


    }

    private void makeApiCall_latestprice(String text) {
        ApiCall.latestprice(this, text, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONObject latestprice = new JSONObject(response);
                    Double c = latestprice.getDouble("c");
                    DecimalFormat f = new DecimalFormat("##0.00");
                    String f1 = f.format(c);
                    String c1 = "$" + f1;
                    TextView textView_c = (TextView) findViewById(R.id.c);
                    textView_c.setText(c1);

                    fav_list_pricea1 = c;


                    TextView textView_pc = (TextView) findViewById(R.id.price_change);
                    Double d = latestprice.getDouble("d");
                    Double dp = latestprice.getDouble("dp");
                    Double o = latestprice.getDouble("o");
                    Double l = latestprice.getDouble("l");
                    Double h = latestprice.getDouble("h");
                    Double pc = latestprice.getDouble("pc");


                    String d1 = f.format(d);
                    String dp1 = f.format(dp);
                    String d2 = "$" + d1 + " (" + dp1 + "%)";
                    textView_pc.setText(d2);

                    fav_list_priceb1 = d;
                    fav_list_pricec1 = dp;


                    ImageView arrow = (ImageView) findViewById(R.id.arrow);
                    if (d > 0) {
                        textView_pc.setTextColor(Color.rgb(0, 200, 0));
                        arrow.setImageResource(R.drawable.trendingup1);
                        arrow.setColorFilter(getResources().getColor(R.color.green));
                    } else if (d < 0) {
                        textView_pc.setTextColor(Color.rgb(200, 0, 0));
                        arrow.setImageResource(R.drawable.trendingdown);
                        arrow.setColorFilter(getResources().getColor(R.color.red));
                    }

                    //portfolio
                    //deal with show res
                    SharedPreferences pref_p = getSharedPreferences("port", 0);
                    SharedPreferences.Editor editor_p = pref_p.edit();
                    if (pref_p.contains(stock_get)){
                        String res_show = pref_p.getString(stock_get,null);
                        List<String> res_j = gson.fromJson(res_show, new TypeToken<List<String>>(){}.getType());
                        Integer share_pre = Integer.parseInt(res_j.get(1));
                        Double avg_cost_pre = Double.parseDouble(res_j.get(2));
                        Double total_cost = share_pre * avg_cost_pre;
                        Double change_pp = (fav_list_pricea1 - avg_cost_pre)*share_pre;
                        Double market_value = fav_list_pricea1 * share_pre;
                        Log.d(TAG3, String.valueOf(fav_list_pricea1));


                        TextView so1 = (TextView) findViewById(R.id.so1);
                        TextView ac1 = (TextView) findViewById(R.id.ac1);
                        TextView tc1 = (TextView) findViewById(R.id.tc1);
                        TextView ch1 = (TextView) findViewById(R.id.ch1);
                        TextView mv1 = (TextView) findViewById(R.id.mv1);
                        //TextView so1 = (TextView) findViewById(R.id.so1);
                        so1.setText(res_j.get(1));
                        //TextView ac1 = (TextView) findViewById(R.id.ac1);
                        ac1.setText("$"+res_j.get(2));
                        //TextView tc1 = (TextView) findViewById(R.id.tc1);
                        tc1.setText("$"+f.format(total_cost));
                        ch1.setText("$" + f.format(change_pp));
                        mv1.setText("$"+f.format(market_value));

                        if ( change_pp < 0){
                            ch1.setTextColor(getResources().getColor(R.color.red));
                            mv1.setTextColor(getResources().getColor(R.color.red));
                        }else if( change_pp >0){
                            ch1.setTextColor(getResources().getColor(R.color.green));
                            mv1.setTextColor(getResources().getColor(R.color.green));
                        }
                    }









                    //stats
                    TextView textView1 = (TextView) findViewById(R.id.op1);
                    String op = "$" + f.format(o);
                    textView1.setText(op);

                    TextView textView2 = (TextView) findViewById(R.id.lp1);
                    String lp = "$" + f.format(l);
                    textView2.setText(lp);

                    TextView textView3 = (TextView) findViewById(R.id.hp1);
                    String hp = "$" + f.format(h);
                    textView3.setText(hp);

                    TextView textView4 = (TextView) findViewById(R.id.pc1);
                    String pc1 = "$" + f.format(pc);
                    textView4.setText(pc1);

                    Log.d(TAG, String.valueOf(latestprice));

                    /*
                    LinearLayout linlaHeaderProgress1 = (LinearLayout) findViewById(R.id.linlaHeaderProgress1);
                    linlaHeaderProgress1.setVisibility(View.GONE);
                    RelativeLayout r = (RelativeLayout) findViewById(R.id.details);
                    r.setVisibility(View.VISIBLE);*/


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });


    }

    private void makeApiCall_histdata(String text, Long from_unix, Long to_unix) {
        ApiCall.histdata(this, text, from_unix, to_unix,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject histdata = new JSONObject(response);
                    List<List> List1 = new ArrayList<>();
                    int len = histdata.getJSONArray("c").length();
                    for (int i=0; i<len; i++){
                        JSONArray timestamp = histdata.getJSONArray("t");
                        Long time = timestamp.getLong(i)*1000;
                        Double price = histdata.getJSONArray("c").getDouble(i);
                        List list = new ArrayList();
                        list.add(time);
                        list.add(price);
                        List1.add(list);
                    }
                    Log.d(TAG, String.valueOf(List1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });


    }

    private void makeApiCall_companypeers(String text) {
        ApiCall.companypeers(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONArray companypeers = new JSONArray(response);
                    List<String> comp_peers = new ArrayList<>();
                    for (int i=0; i<companypeers.length();i++){
                        comp_peers.add(String.valueOf(companypeers.getString(i)));
                    }

                    RecyclerView recyclerView_peers = findViewById(R.id.recyclerView_peer);
                    recyclerView_peers.setLayoutManager(new LinearLayoutManager(searchRes.this,LinearLayoutManager.HORIZONTAL, false));
                    MyAdapter2 adapter2 = new MyAdapter2(searchRes.this,comp_peers);
                    recyclerView_peers.setAdapter(adapter2);


                    Log.d(TAG2, String.valueOf(comp_peers));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    private void makeApiCall_socialsentiment(String text) {
        ApiCall.socialsentiment(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONObject socialsentimentdata = new JSONObject(response);
                    Log.d(TAG, String.valueOf(socialsentimentdata));
                    JSONArray reddit = socialsentimentdata.getJSONArray("reddit");
                    JSONArray twitter = socialsentimentdata.getJSONArray("twitter");
                    Integer reddit_len = reddit.length();
                    Integer twitter_len = twitter.length();
                    Integer mention1 = 0;
                    Integer posmention1 = 0;
                    Integer negmention1 = 0;
                    for (int i=0 ; i<reddit_len ; i++){
                        Integer mention_r = reddit.getJSONObject(i).getInt("mention");
                        mention1 +=  mention_r;
                        Integer posmention_r = reddit.getJSONObject(i).getInt("positiveMention");
                        posmention1 += posmention_r;
                        Integer negmention_r = reddit.getJSONObject(i).getInt("negativeMention");
                        negmention1 += negmention_r;
                    }
                    Integer mention2 = 0;
                    Integer posmention2 = 0;
                    Integer negmention2 = 0;
                    for (int i=0 ; i<twitter_len ; i++){
                        Integer mention_t = twitter.getJSONObject(i).getInt("mention");
                        mention2 +=  mention_t;
                        Integer posmention_t = twitter.getJSONObject(i).getInt("positiveMention");
                        posmention2 += posmention_t;
                        Integer negmention_t = twitter.getJSONObject(i).getInt("negativeMention");
                        negmention2 += negmention_t;
                    }

                    Log.d(TAG, String.valueOf(negmention2));

                    TextView a1 = (TextView) findViewById(R.id.A1);
                    String mention11 = Integer.toString(mention1);
                    a1.setText(mention11);

                    TextView a2 = (TextView) findViewById(R.id.A2);
                    String posmention11 = Integer.toString(posmention1);
                    a2.setText(posmention11);

                    TextView a3 = (TextView) findViewById(R.id.A3);
                    String negmention11 = Integer.toString(negmention1);
                    a3.setText(negmention11);

                    TextView a4 = (TextView) findViewById(R.id.A4);
                    String mention21 = Integer.toString(mention2);
                    a4.setText(mention21);

                    TextView a5 = (TextView) findViewById(R.id.A5);
                    String posmention22 = Integer.toString(posmention2);
                    a5.setText(posmention22);

                    TextView a6 = (TextView) findViewById(R.id.A6);
                    String negmention22 = Integer.toString(negmention2);
                    a6.setText(negmention22);




                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }

    private void makeApiCall_news_response(String response) {
        //parsing logic, please change it as per your requirement
        try {
            JSONArray news = new JSONArray(response);
            Log.d(TAG, String.valueOf(news));

            List<String> list_source = new ArrayList<>();
            List<String> list_img = new ArrayList<>();
            List<String> list_title = new ArrayList<>();
            List<Long> list_time = new ArrayList<>();
            List<String> list_url = new ArrayList<>();
            List<String> list_summary = new ArrayList<>();
            for (int i=0; i < news.length(); i++){
                list_source.add(news.getJSONObject(i).getString("source"));
                list_img.add(news.getJSONObject(i).getString("image"));
                list_title.add(news.getJSONObject(i).getString("headline"));
                list_time.add(news.getJSONObject(i).getLong("datetime"));
                list_url.add(news.getJSONObject(i).getString("url"));
                list_summary.add(news.getJSONObject(i).getString("summary"));
            }

            List<String> list_time1 = new ArrayList<>();
            List<String> list_time2 = new ArrayList<>();
            Timestamp timestamp
                    = new Timestamp(System.currentTimeMillis());
            Long now = timestamp.getTime() / 1000;

            Calendar mydate = Calendar.getInstance();
            for (int i=0; i<list_time.size();i++){
                Long now_diff = (now - list_time.get(i))/3600;
                list_time1.add(now_diff.toString() + " hours ago");
                mydate.setTimeInMillis(list_time.get(i)*1000);
                String a = mydate.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH) +" "+mydate.get(Calendar.DAY_OF_MONTH)+", "+mydate.get(Calendar.YEAR);
                list_time2.add(a);
            }

            //first news block
            TextView v_source1 = (TextView) findViewById(R.id.source1);
            v_source1.setText(list_source.get(0));
            TextView v_newstime1 = (TextView) findViewById(R.id.new_time1);
            v_newstime1.setText(list_time1.get(0));
            TextView v_title1 = (TextView) findViewById(R.id.title1);
            v_title1.setText(list_title.get(0));
            ImageView newsimg = (ImageView) findViewById(R.id.img_news1);
            newsimg.setClipToOutline(true);
            String news_img = list_img.get(0);
            Picasso.get().load(news_img).fit().centerCrop().into(newsimg);

            //news list
            Log.d(TAG, String.valueOf(list_source));
            Log.d(TAG, String.valueOf(list_time1));
            Log.d(TAG, String.valueOf(list_title));
            Log.d(TAG, String.valueOf(list_img));
            RecyclerView recyclerView = findViewById(R.id.recyclerView111);


            MyAdapter myAdapter = new MyAdapter(this,list_source,list_time1,list_title,list_img,list_time2,list_summary,list_url);
            recyclerView.setAdapter(myAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));

            /*
            LinearLayout linlaHeaderProgress1 = (LinearLayout) findViewById(R.id.linlaHeaderProgress1);
            linlaHeaderProgress1.setVisibility(View.GONE);
            RelativeLayout r = (RelativeLayout) findViewById(R.id.details);
            r.setVisibility(View.VISIBLE);*/




            newsimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Dialog dialog = new Dialog(searchRes.this);
                    dialog.setContentView(R.layout.news_dialog);
                    dialog.getWindow().setBackgroundDrawableResource(R.drawable.dialog_round);

                    TextView source_dialog = (TextView) dialog.findViewById(R.id.source_dialog);
                    source_dialog.setText(list_source.get(0));
                    TextView news = (TextView) dialog.findViewById(R.id.time_news);
                    news.setText(list_time2.get(0));
                    TextView titled = (TextView) dialog.findViewById(R.id.title_dialog);
                    titled.setText(list_title.get(0));
                    TextView summaryd = (TextView) dialog.findViewById(R.id.summary_dialog);
                    summaryd.setText(list_summary.get(0));

                    String url1 = list_url.get(0);
                    String title1 = list_title.get(0);
                    ImageButton chrome=(ImageButton) dialog.findViewById(R.id.chrome);
                    chrome.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url=url1;
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            searchRes.this.startActivity(browserIntent);
                        }
                    });

                    ImageButton twitter=(ImageButton) dialog.findViewById(R.id.twitter);
                    twitter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = "https://twitter.com/intent/tweet?text="+title1+"&url="+url1;
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            searchRes.this.startActivity(browserIntent);
                        }
                    });

                    ImageButton facebook=(ImageButton) dialog.findViewById(R.id.facebook);
                    facebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url="https://www.facebook.com/sharer/sharer.php?u="+url1+"&amp;src=sdkpreparse";
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                            searchRes.this.startActivity(browserIntent);
                        }
                    });



                    dialog.show();

                }
            });




        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void makeApiCall_news(String text) {

        ApiCall.news(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                makeApiCall_news_response(response);
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

    }




}

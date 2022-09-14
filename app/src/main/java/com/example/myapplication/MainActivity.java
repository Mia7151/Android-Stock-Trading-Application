package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.MenuItemCompat;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity implements CallBackItemTouch{


    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    private Handler handler;
    private AutoSuggestAdapter autoSuggestAdapter;
    private static final String TAG1 = "souce111";
    Gson gson = new Gson();
    List<String> stock = new ArrayList<>();
    List<String> inc = new ArrayList<>();
    List<Double> price_f = new ArrayList<>();
    List<Double> d_f = new ArrayList<>();
    List<Double> pc_d = new ArrayList<>();
    List<Integer> shares = new ArrayList<>();
    MyAdapter1 myAdapter1;
    MyAdapter3 myAdapter3;

    CoordinatorLayout coordinatorLayout;
    RecyclerView recyclerView;
    RecyclerView recyclerView_p;

    // for portfolio row
    List<String> stock_symbol = new ArrayList<>();
    List<Integer> stock_shares = new ArrayList<>();
    List<Double> market_value1 = new ArrayList<>();
    List<Double> price_change = new ArrayList<>();
    List<Double> price_change_p = new ArrayList<>();
    List<Double> avg_cost = new ArrayList<>();
    List<Double> current_price = new ArrayList<>();


    //for 15s
    Handler handler1 = new Handler();
    Runnable runnable;
    Handler handler2 = new Handler();
    Runnable runnable2;
    int delay = 15*1000; //Delay for 15 seconds.  One second = 1000 milliseconds.

    DecimalFormat f1 = new DecimalFormat("##0.00");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // for spinner
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setProgressBarIndeterminateVisibility(true);

        setTheme(R.style.Theme_MyApplication);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //for spinner
        LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        linlaHeaderProgress.setVisibility(View.VISIBLE);



        // initialize money
        SharedPreferences pref = getSharedPreferences("mykey", 0);
        SharedPreferences.Editor editor = pref.edit();


        Double money_get_d = 25000.00;
        if (pref.contains("money")){
            //String money_string = f1.format(money_get_d);
            //editor.putString("money",money_string);
            //editor.commit();
            String money_get = pref.getString("money","");
            money_get_d = Double.parseDouble(money_get);
            String money_get_s = f1.format(money_get_d);
            TextView money_show = (TextView) findViewById(R.id.cashb);
            money_show.setText("$"+money_get_s);

        }else{
            Double money = 25000.00;
            String money_string = f1.format(money);
            editor.putString("money",money_string);
            editor.commit();
        }

        //for networth
        //List<String> stock_symbol = new ArrayList<>();
        //List<Integer> stock_shares = new ArrayList<>();
        //List<Double> avg_cost = new ArrayList<>();
        //List<Double> current_price = new ArrayList<>();
        SharedPreferences pref_p = getSharedPreferences("port", 0);
        SharedPreferences.Editor editor_p = pref_p.edit();
        Map<String, ?> allEntries = pref_p.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
            List<String> res_j = gson.fromJson(entry.getValue().toString(), new TypeToken<List<String>>(){}.getType());
            stock_symbol.add(res_j.get(0));
            stock_shares.add(Integer.parseInt(res_j.get(1)));
            avg_cost.add(Double.parseDouble(res_j.get(2)));
            current_price.add(Double.parseDouble(res_j.get(3)));
        }

        Double value_own = 0.00;
        //List<Double> market_value1 = new ArrayList<>();
        //List<Double> price_change = new ArrayList<>();
        //List<Double> price_change_p = new ArrayList<>();
        for (int i=0; i<stock_shares.size();i++){
            Double v = stock_shares.get(i) * current_price.get(i);
            value_own += v;
            market_value1.add(v);
            price_change.add(stock_shares.get(i)*(current_price.get(i) - avg_cost.get(i)));
            price_change_p.add(100*(stock_shares.get(i)*(current_price.get(i) - avg_cost.get(i)))/(avg_cost.get(i))*stock_shares.get(i));
        }
        Double value_fill_in =  value_own + money_get_d;
        TextView net_worth = (TextView) findViewById(R.id.net_worth);
        net_worth.setText("$"+f1.format(value_fill_in));




        // draw port rows

        recyclerView_p = findViewById(R.id.recyclerViewppp);
        myAdapter3 = new MyAdapter3(this, stock_symbol, stock_shares, market_value1, price_change, price_change_p);
        if (stock_symbol.size()>0){
            for (int i=0; i<stock_symbol.size();i++){
                makeApiCall_latestprice_por(stock_symbol.get(i),i);
            }
        }

        recyclerView_p.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_p.setAdapter(myAdapter3);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL);
        recyclerView_p.addItemDecoration(dividerItemDecoration);
        ItemTouchHelper itemTouchHelper1 = new ItemTouchHelper(simpleCallback);
        itemTouchHelper1.attachToRecyclerView(recyclerView_p);




        //List<String> res = new ArrayList<>();
        /*
        editor.putString("stock_f", "[]");
        editor.putString("stock_f_name", "[]");
        editor.putString("stock_f_pricea", "[]");
        editor.putString("stock_f_priceb", "[]");
        editor.putString("stock_f_pricec", "[]");
        editor.commit();*/

        //pref.edit().remove("stock_f").commit();
        //pref.edit().remove("stock_f_name").commit();
        //pref.edit().remove("stock_f_pricea").commit();
        //pref.edit().remove("stock_f_priceb").commit();
        //pref.edit().remove("stock_f_pricec").commit();
        //String res111 = pref.getString("stock_f_pricec", "");
        //TextView v = (TextView) findViewById(R.id.res111);
        //v.setText(res111);
        //Log.d(TAG1, res111);

        //for test fav

        //stock.add("AAPL");
        //stock.add("TSLA");
        //stock.add("AMZN");
        //inc.add("AAPL INC");
        //inc.add("TSLA INC");
        //inc.add("AMZN INC");
        /*
        price_f.add(100.0);
        price_f.add(101.000);
        price_f.add(102.000);
        d_f.add(-2.890);
        d_f.add(3.4);
        d_f.add(3.6);
        pc_d.add(-0.6);
        pc_d.add(0.78);
        pc_d.add(-0.7);*/


        if (pref.contains("stock_f")){
        stock = getQuoteList1();
        Log.d("fav1", String.valueOf(stock));
        inc = getQuoteList2();
        Log.d("fav2", String.valueOf(inc));
        //Log.d("price222",res2);
        //Log.d("price222",res3);
        price_f = getQuoteList3();
        Log.d("fav3", String.valueOf(price_f));
        d_f = getQuoteList4();
        Log.d("fav4", String.valueOf(d_f));
        pc_d = getQuoteList5();
        Log.d("fav5", String.valueOf(pc_d));
        Log.d("fav", "-----------");



        if (stock.size() != 0){
        recyclerView = findViewById(R.id.recyclerView222);
        myAdapter1 = new MyAdapter1(this, stock, inc, price_f, d_f, pc_d);

        for (int i=0; i<stock.size();i++){
            makeApiCall_latestprice_fav(stock.get(i),i);
        }

        //for drag and drop
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper.Callback callback = new MyItemTuchHelperCallBack(this);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        //touchHelper.attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(myAdapter1);
        enableSwipeToDeleteAndUndo();}}


        //finnhub click

        TextView finnhub= (TextView) findViewById(R.id.finnhub); //txt is object of TextView
        finnhub.setMovementMethod(LinkMovementMethod.getInstance());
        finnhub.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse("https://www.finnhub.io/"));
                startActivity(browserIntent);
            }
        });


       // hide spinner
        // for spinner
        //LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
        //linlaHeaderProgress.setVisibility(View.GONE);


        // for every 15s

        if (stock_symbol.size() ==0 & stock.size() ==0){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                linlaHeaderProgress.setVisibility(View.GONE);
            }
        }, 400);}


    }



    @Override
    protected void onResume() {
        //start handler as activity become visible

        handler1.postDelayed( runnable = new Runnable() {
            public void run() {
                //do something
                List<String> stock_15s = new ArrayList<>();
                SharedPreferences pref_p = getSharedPreferences("port", 0);
                SharedPreferences.Editor editor_p = pref_p.edit();

                Map<String, ?> allEntries = pref_p.getAll();
                for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
                    //Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());
                    List<String> res_j = gson.fromJson(entry.getValue().toString(), new TypeToken<List<String>>(){}.getType());
                    stock_15s.add(res_j.get(0));}
                if (stock_15s.size() > 0 ){
                    for (int i=0; i < stock_15s.size(); i++){
                        makeApiCall_latestprice_15(stock_15s.get(i),i);
                    }
                }




                //Log.d("15s refresh","1");
                handler1.postDelayed(runnable, delay);
            }
        }, delay);

        handler2.postDelayed( runnable2 = new Runnable() {
            public void run() {
                //do something
                SharedPreferences pref = getSharedPreferences("mykey", 0);
                SharedPreferences.Editor editor = pref.edit();
                if (pref.contains("stock_f")){
                List<String> stock_15 = new ArrayList<>();
                stock_15 = getQuoteList1();
                if (stock_15.size() > 0) {
                    for (int i=0; i<stock_15.size();i++) {
                        makeApiCall_latestprice_15_1(stock_15.get(i),i);
                    }
                }}

                Log.d("15-2s refresh","1");
                handler2.postDelayed(runnable2, delay);
            }
        }, delay);

        super.onResume();
    }

// If onPause() is not included the threads will double up when you
// reload the activity

    @Override
    protected void onPause() {
        handler1.removeCallbacks(runnable);
        handler2.removeCallbacks(runnable2); //stop handler when activity not visible
        super.onPause();
    }

    private void enableSwipeToDeleteAndUndo() {

        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback((Context) this) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                Collections.swap(stock,fromPosition,toPosition);
                Collections.swap(inc,fromPosition,toPosition);
                Collections.swap(price_f,fromPosition,toPosition);
                Collections.swap(d_f,fromPosition,toPosition);
                Collections.swap(pc_d,fromPosition,toPosition);

                recyclerView.getAdapter().notifyItemMoved(fromPosition,toPosition);

                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();

                myAdapter1.removeItem(position);


            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);


    }


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END, 0) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            int fromPosition = viewHolder.getAdapterPosition();
            int toPosition = target.getAdapterPosition();
            Collections.swap(stock_symbol,fromPosition,toPosition);
            Collections.swap(stock_shares,fromPosition,toPosition);
            Collections.swap(market_value1,fromPosition,toPosition);
            Collections.swap(price_change,fromPosition,toPosition);
            Collections.swap(price_change_p,fromPosition,toPosition);

            recyclerView_p.getAdapter().notifyItemMoved(fromPosition,toPosition);

            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };





    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the search menu action bar.
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        // Get the search menu.
        MenuItem searchMenu = menu.findItem(R.id.action_search);
        // Get SearchView object.
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenu);
        // Get SearchView autocomplete object.
        final SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete)searchView.findViewById(androidx.appcompat.R.id.search_src_text);
        searchAutoComplete.setBackgroundColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.BLACK);
        searchAutoComplete.setDropDownBackgroundResource(android.R.color.white);
        // Create a new ArrayAdapter and add data to search auto complete object.

        ///////////////////////
        autoSuggestAdapter = new AutoSuggestAdapter(this, android.R.layout.simple_dropdown_item_1line);
        searchAutoComplete.setThreshold(1);
        searchAutoComplete.setAdapter(autoSuggestAdapter);
        searchAutoComplete.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        //searchAutoComplete.setText(autoSuggestAdapter.getObject(position));
                        String res = autoSuggestAdapter.getObject(position);
                        String tokensVal = res.split(" ")[0];
                        searchAutoComplete.setText(tokensVal);
                    }
                });
        searchAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(searchAutoComplete.getText())) {
                        makeApiCall(searchAutoComplete.getText().toString());
                    }
                }
                return false;
            }
        });

        // Below event is triggered when submit search query.
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Intent intent1 = new Intent(MainActivity.this,searchRes.class);
                intent1.putExtra("stock",query);
                startActivity(intent1);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void makeApiCall(String text) {
        ApiCall.make(this, text, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                List<String> stringList = new ArrayList<>();
                try {
                    JSONArray responseArray = new JSONArray(response);
                    for (int i = 0; i < responseArray.length(); i++) {
                        JSONObject row = responseArray.getJSONObject(i);
                        String symbol = row.getString("symbol");
                        String description = row.getString("description");
                        String input = symbol +" | " +description;
                        stringList.add(input);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //IMPORTANT: set data here and notify
                autoSuggestAdapter.setData(stringList);
                autoSuggestAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
    }



    @Override
    public void itemTuchOnMove(int oldPosition, int newPosition) {
        stock.add(newPosition,stock.remove(oldPosition));
        inc.add(newPosition,inc.remove(oldPosition));
        price_f.add(newPosition,price_f.remove(oldPosition));
        d_f.add(newPosition,d_f.remove(oldPosition));
        pc_d.add(newPosition,pc_d.remove(oldPosition));
        myAdapter1.notifyItemMoved(oldPosition,newPosition);

    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int position) {
        String s1 = stock.get(viewHolder.getAdapterPosition());


        stock.remove(position);
        inc.remove(position);
        price_f.remove(position);
        d_f.remove(position);
        pc_d.remove(position);

        SharedPreferences pref = getSharedPreferences("mykey", 0);
        SharedPreferences.Editor editor = pref.edit();
        String jsonText = gson.toJson(stock);
        String jsonText1 = gson.toJson(inc);
        String jsonText2 = gson.toJson(price_f);
        String jsonText3 = gson.toJson(d_f);
        String jsonText4 = gson.toJson(pc_d);

        editor.putString("stock_f", jsonText);
        editor.putString("stock_f_name", jsonText1);
        editor.putString("stock_f_pricea", jsonText2);
        editor.putString("stock_f_priceb", jsonText3);
        editor.putString("stock_f_pricec", jsonText4);

        editor.commit();

        final String deleted1 = stock.get(viewHolder.getAdapterPosition());
        final int deletedIndex = viewHolder.getAdapterPosition();

        myAdapter1.removeItem(viewHolder.getAdapterPosition());


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

    private void makeApiCall_latestprice_15(String text, Integer integer) {
        ApiCall.latestprice(this, text, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONObject latestprice = new JSONObject(response);
                    Double c = latestprice.getDouble("c");

                    SharedPreferences pref = getSharedPreferences("mykey", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    String money_get = pref.getString("money","");
                    Double money_get_d = Double.parseDouble(money_get);

                    current_price.set(integer,c);
                    market_value1.set(integer,c*stock_shares.get(integer));
                    price_change.set(integer,(c - avg_cost.get(integer))*stock_shares.get(integer));
                    price_change_p.set(integer,100*(stock_shares.get(integer)*(c - avg_cost.get(integer)))/(avg_cost.get(integer))*stock_shares.get(integer));
                    myAdapter3.notifyDataSetChanged();

                    double net_worth_15 = money_get_d;
                    for (int i=0; i<stock_shares.size(); i++){
                        net_worth_15 += stock_shares.get(i)*current_price.get(i);
                    }

                    TextView net_worth = (TextView) findViewById(R.id.net_worth);
                    net_worth.setText("$"+f1.format(net_worth_15));





                    Log.d("15s", String.valueOf(latestprice));
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
    private void makeApiCall_latestprice_15_1(String text, Integer integer) {
        ApiCall.latestprice(this, text, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONObject latestprice = new JSONObject(response);
                    Log.d("15s_2", text + String.valueOf(latestprice));
                    Double c = latestprice.getDouble("c");
                    Double d = latestprice.getDouble("d");
                    Double dp = latestprice.getDouble("dp");
                    price_f.set(integer,c);
                    d_f.set(integer,d);
                    pc_d.set(integer,dp);
                    myAdapter1.notifyDataSetChanged();





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
    private void makeApiCall_latestprice_fav(String text, Integer integer) {
        ApiCall.latestprice(this, text, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONObject latestprice = new JSONObject(response);
                    Log.d("15s_3", text + String.valueOf(latestprice));
                    Double c = latestprice.getDouble("c");
                    Double d = latestprice.getDouble("d");
                    Double dp = latestprice.getDouble("dp");
                    price_f.set(integer,c);
                    d_f.set(integer,d);
                    pc_d.set(integer,dp);
                    myAdapter1.notifyDataSetChanged();


                    LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
                    linlaHeaderProgress.setVisibility(View.GONE);

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

    private void makeApiCall_latestprice_por(String text, Integer integer) {
        ApiCall.latestprice(this, text, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {
                //parsing logic, please change it as per your requirement
                try {
                    JSONObject latestprice = new JSONObject(response);
                    Double c = latestprice.getDouble("c");

                    SharedPreferences pref = getSharedPreferences("mykey", 0);
                    SharedPreferences.Editor editor = pref.edit();
                    String money_get = pref.getString("money","");
                    Double money_get_d = Double.parseDouble(money_get);

                    current_price.set(integer,c);
                    market_value1.set(integer,c*stock_shares.get(integer));
                    price_change.set(integer,(c - avg_cost.get(integer))*stock_shares.get(integer));
                    price_change_p.set(integer,100*(stock_shares.get(integer)*(c - avg_cost.get(integer)))/(avg_cost.get(integer))*stock_shares.get(integer));
                    myAdapter3.notifyDataSetChanged();



                    double net_worth_15 = money_get_d;
                    for (int i=0; i<stock_shares.size(); i++){
                        net_worth_15 += stock_shares.get(i)*current_price.get(i);
                    }

                    TextView net_worth = (TextView) findViewById(R.id.net_worth);
                    net_worth.setText("$"+f1.format(net_worth_15));


                    LinearLayout linlaHeaderProgress = (LinearLayout) findViewById(R.id.linlaHeaderProgress);
                    linlaHeaderProgress.setVisibility(View.GONE);


                    Log.d("15s_4", String.valueOf(latestprice));
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

}


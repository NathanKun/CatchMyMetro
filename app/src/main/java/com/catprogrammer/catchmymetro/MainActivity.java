package com.catprogrammer.catchmymetro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.nathankun.catchmymetro.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class MainActivity extends AppCompatActivity {
    private final static int HOMETOSCHOOL = 1;
    private final static int SCHOOLTOHOME = 2;
    private final static int HOMETOROUEN = 3;
    private final static int TECHNOPOLETOSCHOOL = 4;
    private final static int SCHOOLTOTECHNOPOLE = 5;

    private static int choosing = 1;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowing(choosing);
            }
        });

        setShowing(HOMETOSCHOOL);
    }

    public void setShowing(int choice) {
        RequestParams params = new RequestParams();

        choosing = choice;

        TextView tw_choosing = findViewById(R.id.choosing);
        switch (choice) {
            case HOMETOSCHOOL:
                tw_choosing.setText(R.string.btn_homeToSchool);
                params.put("sens", "1");
                params.put("destinations", "{\"1\":\"Technopôle SAINT-ETIENNE-DU-ROUVRAY\"}");
                params.put("stopId", "101065    ");
                params.put("lineId", "175");
                break;
            case SCHOOLTOHOME:
                tw_choosing.setText(R.string.btn_schoolToHome);
                params.put("sens", "2");
                params.put("destinations", "{\"1\":\"Boulingrin ROUEN\"}");
                params.put("stopId", "102093");
                params.put("lineId", "175");
                break;
            case HOMETOROUEN:
                tw_choosing.setText(R.string.btn_homeToRouen);
                params.put("sens", "2");
                params.put("destinations", "{\"1\":\"Boulingrin ROUEN\"}");
                params.put("stopId", "100784");
                params.put("lineId", "175");
                break;
            case TECHNOPOLETOSCHOOL:
                tw_choosing.setText(R.string.btn_technopoleToSchool);
                params.put("sens", "1");
                params.put("destinations", "{\"1\":\"Gare de Saint-Etienne SAINT-ÉTIENNE-DU-ROUVRAY\"}");
                params.put("stopId", "102088");
                params.put("lineId", "126");
                break;
            case SCHOOLTOTECHNOPOLE:
                tw_choosing.setText(R.string.btn_schoolToTechnopole);
                params.put("sens", "2");
                params.put("destinations", "{\"1\":\"Bel Air PETIT-COURONNE\",\"2\":\"Lycée Val de Seine LE GRAND QUEVILLY\"}");
                params.put("stopId", "102089");
                params.put("lineId", "126");
                break;
            default:
                break;
        }

        showMetroTime(params);
    }

    public void btn_onclick_homeToSchool(View view) {
        setShowing(HOMETOSCHOOL);
    }

    public void btn_onclick_schoolToHome(View view) {
        setShowing(SCHOOLTOHOME);
    }

    public void btn_onclick_homeToRouen(View view) {
        setShowing(HOMETOROUEN);
    }

    public void btn_onclick_technopoleToSchool(View view) {
        setShowing(TECHNOPOLETOSCHOOL);
    }

    public void btn_onclick_schoolToTechnopole(View view) {
        setShowing(SCHOOLTOTECHNOPOLE);
    }

    public void showMetroTime(RequestParams params) {
        TextView textView = findViewById(R.id.str);
        textView.setText("Loading");

        AstuceApiClient.post(params, new AsyncHttpResponseHandler() {
            private TextView tv;

            AsyncHttpResponseHandler init(TextView tv) {
                System.out.println("builded");
                this.tv = tv;
                return this;
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                StringBuilder responseStr;
                Element body;
                try {
                    body = Jsoup.parseBodyFragment(new String(responseBody, "UTF-8")).body();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }

                responseStr = new StringBuilder(body.getElementsByTag("span").get(0).text());

                Elements lis = body.getElementsByTag("li");
                List<String> lisStr = new ArrayList<>();

                for (Object e : lis.toArray()) {
                    lisStr.add(((Element) e).text());
                }

                for(String li : lisStr) {
                    responseStr.append("\n");
                    if(li.contains("temps réel")) {
                        responseStr.append(li.replace("temps réel", "实时："));
                    }else {
                        responseStr.append("非实时：").append(li);
                    }
                }

                tv.setText(responseStr.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                StringBuilder strBuilder = new StringBuilder();
                String body = null;
                try {
                    body = Jsoup.parseBodyFragment(new String(responseBody, "UTF-8")).body().text();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                strBuilder.append("===================faild====================");
                strBuilder.append("\nstatusCode\n");
                strBuilder.append(statusCode);
                strBuilder.append("\nHeader\n");
                strBuilder.append(Arrays.toString(headers));
                strBuilder.append("\nerror\n");
                strBuilder.append(error.getMessage());
                strBuilder.append("\nresponseBody\n");
                strBuilder.append(body);
                strBuilder.append("\n===================faild====================");

                tv.setText(strBuilder.toString());

            }
        }.init(textView));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.nathankun.catchmymetro;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


//import android.webkit.JavascriptInterface;


public class MainActivity extends AppCompatActivity {
    private final static int HOMETOSCHOOL = 1;
    private final static int SCHOOLTOHOME = 2;
    private final static int HOMETOROUEN = 3;
    private final static int TECHNOPOLETOSCHOOL = 4;
    private final static int SCHOOLTOTECHNOPOLE = 5;

    private static int choosing = 1;

    private static int delayMs = 1500;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setShowing(choosing);
            }
        });

        setShowing(HOMETOSCHOOL);
    }

    public void setShowing(int url_number) {
        choosing = url_number;
        String url = "";
        String url_homeToSchool = "https://reseau-astuce.fr/fr/horaires-a-larret/28/StopTimeTable/toit-familial/" +
                "284/georges-braque-technopole-boulingrin/175/georges-braque-gd-quevilly-technopole-saint-etienn/1?" +
                "KeywordsStop=Toit%20Familial%20-%20SOTTEVILLE-LES-ROUEN";
        String url_schoolToHome = "https://reseau-astuce.fr/fr/horaires-a-larret/28/StopTimeTable/technopole/" +
                "754/georges-braque-technopole-boulingrin/175/boulingrin-rouen/2?" +
                "KeywordsStop=Technop%C3%B4le%20-%20SAINT-ETIENNE-DU-ROUVRAY";
        String url_homeToRouen = "https://reseau-astuce.fr/fr/horaires-a-larret/28/StopTimeTable/toit-familial/" +
                "284/georges-braque-technopole-boulingrin/175/boulingrin-rouen/2?" +
                "KeywordsStop=Toit%20Familial%20-%20SOTTEVILLE-LES-ROUEN";
        String url_technopoleToSchool = "https://reseau-astuce.fr/fr/horaires-a-larret/28/StopTimeTable/technopole/" +
                "754/gare-de-saint-etienne-du-rouvray-bel-air/126/gare-de-saint-etienne-saint-etienne-du-rouvray/1?" +
                "KeywordsStop=Technop%C3%B4le%20-%20SAINT-ETIENNE-DU-ROUVRAY";
        String url_schoolToTechnopole = "https://reseau-astuce.fr/fr/horaires-a-larret/28/StopTimeTable/technopole/" +
                "754/gare-de-saint-etienne-du-rouvray-bel-air/126/bel-air-petit-couronne/2?" +
                "KeywordsStop=Technop%C3%B4le%20-%20SAINT-ETIENNE-DU-ROUVRAY";

        TextView tw_choosing = (TextView) findViewById(R.id.choosing);
        switch (url_number) {
            case HOMETOSCHOOL:
                url = url_homeToSchool;
                tw_choosing.setText(R.string.btn_homeToSchool);
                break;
            case SCHOOLTOHOME:
                url = url_schoolToHome;
                tw_choosing.setText(R.string.btn_schoolToHome);
                break;
            case HOMETOROUEN:
                url = url_homeToRouen;
                tw_choosing.setText(R.string.btn_homeToRouen);
                break;
            case TECHNOPOLETOSCHOOL:
                url = url_technopoleToSchool;
                tw_choosing.setText(R.string.btn_technopoleToSchool);
                break;
            case SCHOOLTOTECHNOPOLE:
                url = url_schoolToTechnopole;
                tw_choosing.setText(R.string.btn_schoolToTechnopole);
                break;
            default:
                url = "CASE_ERROR";
                break;
        }

        showMetroTime(url);
    }

    public void webViewStopLoading() {
        final WebView webview = (WebView) findViewById(R.id.browser); // WebView
        webview.stopLoading();
    }

    public void btn_onclick_homeToSchool(View view) {
        webViewStopLoading();
        setShowing(HOMETOSCHOOL);
    }

    public void btn_onclick_schoolToHome(View view) {
        webViewStopLoading();
        setShowing(SCHOOLTOHOME);
    }

    public void btn_onclick_homeToRouen(View view) {
        webViewStopLoading();
        setShowing(HOMETOROUEN);
    }

    public void btn_onclick_technopoleToSchool(View view) {
        webViewStopLoading();
        setShowing(TECHNOPOLETOSCHOOL);
    }

    public void btn_onclick_schoolToTechnopole(View view) {
        webViewStopLoading();
        setShowing(SCHOOLTOTECHNOPOLE);
    }

    public void showMetroTime(String url) {
        TextView str = (TextView) findViewById(R.id.str);

        final WebView webview = (WebView) findViewById(R.id.browser); // WebView
        String newUA = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0"; // user agent，为了不进入手机页面
        webview.getSettings().setUserAgentString(newUA);
        webview.getSettings().setJavaScriptEnabled(true); // 允许javascript
        //webview.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
        webview.setVisibility(View.GONE); // 隐藏WebView


        webview.setWebViewClient(new WebViewClient() {
            // 页面加在完毕后执行下面的js函数
            @Override
            public void onPageFinished(WebView view, String url) {
                //webview.loadUrl("javascript:;setTimeout( function () {HtmlViewer.showHTML('<html>'+document.getElementById('next-departure-result').innerHTML+'</html>');}, " + String.valueOf(delayMs) + ")");
                // 利用js函数延迟delayMs，确保想要获得的那部分网页已经被网页通过ajax加载出来，再提取出那部分网页代码，并传递出去
                webview.loadUrl("javascript:setTimeout( function () {console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);}, " + String.valueOf(delayMs) + ")");
                //webview.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);");
            }
        });

        // 分析网页内容获取地铁时间信息
        webview.setWebChromeClient(new WebChromeClient() {
            public boolean onConsoleMessage(ConsoleMessage cmsg) {
                // check secret prefix
                if (cmsg.message().startsWith("MAGIC")) {

                    String html = cmsg.message().substring(5); // strip off prefix
                    Document doc = Jsoup.parse("<html>" + html + "</html>");
                    TextView str = (TextView) findViewById(R.id.str);
                    str.setText("Analyzing");

                    Element next_departure_result = doc.body().getElementById("next-departure-result"); // 得到包括时间信息的最小有id的tag
                    if (next_departure_result == null) {
                        str.setText("Internet Failed");
                        webview.setVisibility(View.VISIBLE);
                    } else {
                        if (next_departure_result.text() == "") {
                            str.setText("Still loading 1");
                            retryGettingHtml(webview);
                            if (next_departure_result.text() == "") {
                                str.setText("Still loading 2");
                                retryGettingHtml(webview);
                                if (next_departure_result.text() == "") {
                                    str.setText("Still loading 3");
                                    retryGettingHtml(webview);
                                    if (next_departure_result.text() == "") {
                                        str.setText("Loading Failed");
                                        webview.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                        } else {
                            Element time = next_departure_result.getElementsByTag("p").get(0); // 分离出包含当前时间的tag
                            Element ui = next_departure_result.getElementsByTag("ul").get(0); // 分离出包含实时时间的tag

                            String out = time.text().substring(0, time.text().length() - 11); // 获得当前时间

                            //\r\n next line


                            Elements ui_elements = ui.getElementsByTag("li"); // 分离出4个包含地铁时间的li
                            Boolean isRealTime = ui_elements.get(0).text().contains("temps réel");
                            for (int i = 0; i < 4; i++) {

                                try {
                                    Element li_element = ui_elements.get(i); // ui中的第i个li
                                    if (!isRealTime) {
                                        /**
                                         * example
                                         *  <li class="colorDest_0">
                                         *      <span class="item-text">
                                         *          4<abbr title="heure">h</abbr>54 vers Technopôle SAINT-ETIENNE-DU-ROUVRAY
                                         *      </span>
                                         *  </li>
                                         */
                                        out += "\r\n" + "非实时：" + li_element.text();
                                    } else {
                                        /**
                                         *  <li class="colorDest_0">
                                         *      <span aria-hidden="true" class="item-img cw-transinfo cw-real-time picto-real-time"></span>
                                         *      <span class="item-text  hide-text-icon">temps réel</span>
                                         *      <span class="item-text">dans
                                         *          <span>5
                                         *              <abbr title="minutes">min</abbr>
                                         *          </span> vers Technopôle SAINT-ETIENNE-DU-ROUVRAY
                                         *      </span>
                                         *  </li>
                                         */

                                        out += "\r\n" + "实时：" + li_element.text().substring(10); // 去掉 temps reel 字符串
                                    }
                                } catch (java.lang.IndexOutOfBoundsException e) {

                                }
                            }

                            str.setText(out);

                            return true;
                        }
                    }
                }

                return false;
            }
        });

        // 加载网页
        webview.loadUrl(url);

        str.setText("Loading url");
    }

    void retryGettingHtml(final WebView webview) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 1s
                webview.loadUrl("javascript:console.log('MAGIC'+document.getElementsByTagName('html')[0].innerHTML);" + String.valueOf(delayMs) + ")");
            }
        }, 1500);
    }
    /*class MyJavaScriptInterface {

        private Context ctx;

        MyJavaScriptInterface(Context ctx) {
            this.ctx = ctx;
        }

        @JavascriptInterface
        public void showHTML(String html) {
            new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
        }

    }*/

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

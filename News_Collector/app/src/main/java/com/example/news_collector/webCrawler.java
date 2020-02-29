package com.example.news_collector;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.*;
import java.util.List;

/*

    정치      JTBC
    경제      초이스경제
    IT        IT NEWS
    보안      보안 뉴스
    기술/과학 IT NEWS
    세계      BBC 코리아

 */

public class webCrawler {
    public JSONArray parsing(String url, String class_name, String title_id, String contents_id){

        JSONArray result = new JSONArray();
        int plus, index = 0;
        List<String> titles, contents, urls;


        /*
        WebCrawler Function
        1. Except News datas which just picture, table, inform for specific peoples
        2. Collecting Headline,  Context, URL
        3. If the URL does not include a base URL, attach the news URL with the base URL.
         */
        try {
            Document doc = Jsoup.connect(url).get();

            Elements div_class = doc.getElementsByClass(class_name);

            Elements elements_titles    = div_class.select("."+title_id);
            Elements elements_contents  = div_class.select("."+contents_id);

            titles = elements_titles.eachText();
            Log.i("Titles_webCrawler", titles.toString());

            contents = elements_contents.eachText();
            Log.i("Contents_webCrawler", contents.toString());


            urls = elements_titles.select("a").eachAttr("href");
            if(urls.isEmpty()){
                urls = elements_contents.select("a").eachAttr("href");
            }
            Log.i("Url_webCrawler", urls.toString());


            plus = 0;
            if(url.equals("http://news.jtbc.joins.com/section/index.aspx?scode=10")){
                for(int i = 0; i <10 + plus ; i++){
                    JSONObject jsonObject = new JSONObject();
                    if(titles.get(i).startsWith("[표]")
                            | titles.get(i).startsWith("[부고]")
                            | titles.get(i).startsWith("[사진]")
                            | titles.get(i).startsWith("[인사")){
                        plus++;
                        continue;
                    }

                    jsonObject.put("title", titles.get(i));
                    jsonObject.put("content", contents.get(i));
                    jsonObject.put("url", "http://news.jtbc.joins.com".concat(urls.get(i)));


                    result.put(index, jsonObject);
                    index++;
                }
            }else if(url.equals("https://www.boannews.com/media/list.asp?mkind=1")){
                for(int i = 0; i <10 + plus ; i++){
                    JSONObject jsonObject = new JSONObject();
                    if(titles.get(i).startsWith("[표]")
                            | titles.get(i).startsWith("[부고]")
                            | titles.get(i).startsWith("[사진]")){
                        plus++;
                        continue;
                    }

                    jsonObject.put("title", titles.get(i));
                    jsonObject.put("content", contents.get(i));
                    jsonObject.put("url", "https://www.boannews.com".concat(urls.get(i)));


                    result.put(index, jsonObject);
                    index++;
                }
            }else if(url.equals("https://www.yna.co.kr/international/all?site=navi_international_dep02")){
                for(int i = 0; i <10 + plus ; i++){
                    JSONObject jsonObject = new JSONObject();
                    if(titles.get(i).startsWith("[표]")
                            | titles.get(i).startsWith("[부고]")
                            | titles.get(i).startsWith("[사진]")){
                        plus++;
                        continue;
                    }

                    jsonObject.put("title", titles.get(i));
                    jsonObject.put("content", contents.get(i));
                    jsonObject.put("url", "http:".concat(urls.get(i)));


                    result.put(index, jsonObject);
                    index++;
                }
            }
            else {

                for (int i = 0; i < 10 + plus ; i++) {
                    JSONObject jsonObject = new JSONObject();
                    if(titles.get(i).startsWith("[표]")
                            | titles.get(i).startsWith("[부고]")
                            | titles.get(i).startsWith("[사진]")){
                        plus++;
                        continue;
                    }

                    jsonObject.put("title", titles.get(i));
                    jsonObject.put("content", contents.get(i));
                    jsonObject.put("url", urls.get(i));


                    result.put(index, jsonObject);
                    index++;
                }
            }

            Log.i("resultObject.length()", Integer.toString(result.length()));
            for(int i = 0; i < 10 ; i++) {
                Log.i("reulst_webCrawler", result.getString(i));
            }
        }catch(IOException e){
            Log.e("webCrawer","IOException Error");
            Log.e("webCrawer",e.toString());
            e.printStackTrace();
        }catch(JSONException e){
            e.printStackTrace();
        }


        return result;
    }



}

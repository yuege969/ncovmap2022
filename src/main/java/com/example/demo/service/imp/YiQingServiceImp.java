package com.example.demo.service.imp;

import com.alibaba.fastjson.JSONArray;
import com.example.demo.entity.CityInfo;
import com.example.demo.entity.ProvinceInfo;
import com.example.demo.service.CityService;
import com.example.demo.service.ProvinceService;
import com.example.demo.service.YiQingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class YiQingServiceImp implements YiQingService {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;


    @Override
    public void getCovid(String appId) {

//        System.out.println("执行定时任务");

        String url = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
        String htmlResult = "";
        htmlResult = httpRequset(url);
        // System.out.println(htmlResult);

        // 正则获取数据
        // 因为html的数据格式看着就像json格式，所以我们正则获取json
        String reg = "window.getAreaStat = (.*?)\\}(?=catch)";
        Pattern totalPattern = Pattern.compile(reg);
        Matcher totalMatcher = totalPattern.matcher(htmlResult);

        String result = "";
        if (totalMatcher.find()) {
            result = totalMatcher.group(1);
            System.out.println(result);
            // 各个省市的是一个列表List，如果想保存到数据库中，要遍历结果，下面是demo
            JSONArray array = JSONArray.parseArray(result);
            //                Connection con = DriverManager.getConnection("VData");
//                Statement stmt = con.createStatement();
            Date date=new Date(System.currentTimeMillis());
            for (int i = 0; i <= 33; i++) {

                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject
                        .parseObject(array.getString(i));
                String provinceName = jsonObject.getString("provinceName"); //省名
                String current = jsonObject.getString("currentConfirmedCount"); //当前确诊
                String confirmed = jsonObject.getString("confirmedCount"); //总共确诊
                String cured = jsonObject.getString("curedCount"); //治愈
                String dead = jsonObject.getString("deadCount"); //死亡人数
                String suspect=jsonObject.getString("suspectedCount"); //可疑人数
//                    stmt.executeUpdate("insert into province values('"+provinceName+"','"+confirmed+"','"+suspect+"','"+cured+
//                            "','"+dead+"','"+current+"','"+date+"')");

                System.out.println("省级疫情情况"+ provinceName+"  "+current+"  "+confirmed+"  "+cured+"  "+dead+"  "+suspect);

                ProvinceInfo provinceInfo=new ProvinceInfo();
                provinceInfo.setProvinceName(provinceName);
                provinceInfo.setConfirmedCount(Long.parseLong(current));
                provinceInfo.setConfirmedCount(Long.parseLong(confirmed));
                provinceInfo.setCuredCount(Long.parseLong(cured));
                provinceInfo.setDeadCount(Long.parseLong(dead));
                provinceInfo.setSuspectedCount(Long.parseLong(suspect));

                provinceService.insert(provinceInfo);

                JSONArray array2 = jsonObject.getJSONArray("cities");
                for (int j = 0; j < array2.size(); j++) {
                    com.alibaba.fastjson.JSONObject jsonObject2 = com.alibaba.fastjson.JSONObject
                            .parseObject(array2.getString(j));
                    String cityname = jsonObject2.getString("cityName");
                    String current2 = jsonObject2.getString("currentConfirmedCount");
                    String confirmed2 = jsonObject2.getString("confirmedCount");
                    String cured2 = jsonObject2.getString("curedCount");
                    String dead2 = jsonObject2.getString("deadCount");
                    String suspect2 = jsonObject2.getString("suspectedCount");

                    System.out.println(provinceName+"省的疫情数据"+ cityname+"  "+current2+"  "+confirmed2+" "+cured2+"  "+dead2+"  "+suspect2);


                    CityInfo cityInfo=new CityInfo();
                    cityInfo.setCityName(cityname);
                    cityInfo.setProvinveId(provinceInfo.getId());
                    cityInfo.setCurrentConfirmedCount(Long.parseLong(current2));
                    cityInfo.setConfirmedCount(Long.parseLong(confirmed2));
                    cityInfo.setCuredCount(Long.parseLong(cured2));
                    cityInfo.setDeadCount(Long.parseLong(dead2));
                    cityInfo.setSuspectedCount(Long.parseLong(suspect2));

                    cityService.insert(cityInfo);


                    System.out.println();
//                        stmt.executeUpdate("insert into city values('"+cityname+"','"+confirmed2+"','"+suspect2+"','"+cured2+"','"+dead2+"','"+current2+"','"+provinceName+"','"+date+"')");
                }
            }
//                stmt.close();
//                con.close();
        }


    }

    @Override
    public String httpRequset(String requesturl) {
        StringBuffer buffer = null;
        BufferedReader bufferedReader = null;
        InputStreamReader inputStreamReader = null;
        InputStream inputStream = null;
        HttpsURLConnection httpsURLConnection = null;
        try {
            URL url = new URL(requesturl);
            httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setDoInput(true);
            httpsURLConnection.setRequestMethod("GET");
            inputStream = httpsURLConnection.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(inputStreamReader);
            buffer = new StringBuffer();
            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return buffer.toString();
    }


    @Scheduled(cron = "0/20 * * * * ?")
    public void updateInfo(){

//        System.out.println("执行定时任务");

        System.out.println("执行定时任务");

        String url = "https://ncov.dxy.cn/ncovh5/view/pneumonia";
        String htmlResult = "";
        htmlResult = httpRequset(url);
        // System.out.println(htmlResult);

        // 正则获取数据
        // 因为html的数据格式看着就像json格式，所以我们正则获取json
        String reg = "window.getAreaStat = (.*?)\\}(?=catch)";
        Pattern totalPattern = Pattern.compile(reg);
        Matcher totalMatcher = totalPattern.matcher(htmlResult);

        String result = "";
        if (totalMatcher.find()) {
            result = totalMatcher.group(1);
            System.out.println(result);
            // 各个省市的是一个列表List，如果想保存到数据库中，要遍历结果，下面是demo
            JSONArray array = JSONArray.parseArray(result);
            //                Connection con = DriverManager.getConnection("VData");
//                Statement stmt = con.createStatement();
            Date date=new Date(System.currentTimeMillis());
            for (int i = 0; i <= 33; i++) {

                com.alibaba.fastjson.JSONObject jsonObject = com.alibaba.fastjson.JSONObject
                        .parseObject(array.getString(i));
                String provinceName = jsonObject.getString("provinceName"); //省名
                String current = jsonObject.getString("currentConfirmedCount"); //当前确诊
                String confirmed = jsonObject.getString("confirmedCount"); //总共确诊
                String cured = jsonObject.getString("curedCount"); //治愈
                String dead = jsonObject.getString("deadCount"); //死亡人数
                String suspect=jsonObject.getString("suspectedCount"); //可疑人数
//                    stmt.executeUpdate("insert into province values('"+provinceName+"','"+confirmed+"','"+suspect+"','"+cured+
//                            "','"+dead+"','"+current+"','"+date+"')");

//                System.out.println("省级疫情情况"+ provinceName+"  "+current+"  "+confirmed+"  "+cured+"  "+dead+"  "+suspect);

                ProvinceInfo provinceInfo=new ProvinceInfo();
                provinceInfo.setProvinceName(provinceName);
                provinceInfo.setCurrentConfirmedCount(Long.parseLong(current));
                provinceInfo.setConfirmedCount(Long.parseLong(confirmed));
                provinceInfo.setCuredCount(Long.parseLong(cured));
                provinceInfo.setDeadCount(Long.parseLong(dead));
                provinceInfo.setSuspectedCount(Long.parseLong(suspect));

//                provinceService.insert(provinceInfo);
                provinceService.update(provinceInfo);

                JSONArray array2 = jsonObject.getJSONArray("cities");
                for (int j = 0; j < array2.size(); j++) {
                    com.alibaba.fastjson.JSONObject jsonObject2 = com.alibaba.fastjson.JSONObject
                            .parseObject(array2.getString(j));
                    String cityname = jsonObject2.getString("cityName");
                    String current2 = jsonObject2.getString("currentConfirmedCount");
                    String confirmed2 = jsonObject2.getString("confirmedCount");
                    String cured2 = jsonObject2.getString("curedCount");
                    String dead2 = jsonObject2.getString("deadCount");
                    String suspect2 = jsonObject2.getString("suspectedCount");

//                    System.out.println(provinceName+"省的疫情数据"+ cityname+"  "+current2+"  "+confirmed2+" "+cured2+"  "+dead2+"  "+suspect2);


                    CityInfo cityInfo=new CityInfo();
                    cityInfo.setCityName(cityname);
                    cityInfo.setProvinveId(provinceInfo.getId());
                    cityInfo.setCurrentConfirmedCount(Long.parseLong(current2));
                    cityInfo.setConfirmedCount(Long.parseLong(confirmed2));
                    cityInfo.setCuredCount(Long.parseLong(cured2));
                    cityInfo.setDeadCount(Long.parseLong(dead2));
                    cityInfo.setSuspectedCount(Long.parseLong(suspect2));

//                    cityService.insert(cityInfo);
                    cityService.update(cityInfo);


//                    System.out.println("定时任务刷新完成");
//                        stmt.executeUpdate("insert into city values('"+cityname+"','"+confirmed2+"','"+suspect2+"','"+cured2+"','"+dead2+"','"+current2+"','"+provinceName+"','"+date+"')");
                }
            }
//                stmt.close();
//                con.close();
        }


    }



}

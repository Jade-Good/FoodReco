package com.ssafy.special.service.etc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssafy.special.dto.response.WeatherStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherStatus weatherStatus;

    @Value("${weather.service-key}")
    private String serviceKey;

    public String getWeather(Double lon, Double let) throws Exception {
        if (lon == 0 && let == 0){
            lon = 127.3940;
            let = 36.3398;
        }

        Map<String, Object> resultMap = new HashMap<>();
        HttpStatus status = null;


        LocalDateTime time = LocalDateTime.now();
        String year = String.valueOf(time.getYear());
        String month = String.valueOf(time.getMonthValue());
        String day = String.format("%02d", time.getDayOfMonth());
        String hour = String.valueOf(time.getHour());
        String minute = String.valueOf(time.getMinute());

        System.out.println(year + ", " + month + ", " + day + ", " + hour + ", " + minute);

        int[] location = mapConv(lon, let, new LamcParameter());
        String ny = String.valueOf(location[1]);
        String nx = String.valueOf(location[0]);

        int tempHour = Integer.parseInt(hour);
        int m = Integer.parseInt(minute);
        if (m <= 45) tempHour--;
        if (tempHour < 0) tempHour = 23;
        String hourStr = tempHour < 10 ? "0" + tempHour : tempHour + "";

        int baseHour = Integer.parseInt(hour);
        if (m > 45) baseHour = Integer.parseInt(hour) + 1;
        String baseHourStr = baseHour < 10 ? "0" + baseHour : baseHour + "";
        baseHourStr += "00";

        System.out.println("BaseHourStr : " + baseHourStr);
        StringBuilder urlBuilder = new StringBuilder("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode("1000", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode("JSON", "UTF-8")); /*요청자료형식(XML/JSON) Default: XML*/
        urlBuilder.append("&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(year + month + day, "UTF-8")); /*‘21년 6월 28일 발표*/
        urlBuilder.append("&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(hourStr + "00", "UTF-8")); /*06시 발표(정시단위) */
        urlBuilder.append("&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(nx, "UTF-8")); /*예보지점의 X 좌표값*/
        urlBuilder.append("&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(ny, "UTF-8")); /*예보지점의 Y 좌표값*/

        System.out.println(urlBuilder.toString());

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        String data = sb.toString();

//        System.out.println(data);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> weatherData = objectMapper.readValue(data, new TypeReference<Map<String, Object>>() {
        });
        Map<String, Object> relevantWeatherDataMap = new HashMap<>();

        String weather = "";
        Map<String, Object> items = (Map<String, Object>) ((Map<String, Object>) ((Map<String, Object>) weatherData.get("response")).get("body")).get("items");
        List<Map<String, Object>> itemList = (List<Map<String, Object>>) items.get("item");
        for (Map<String, Object> weatherObject : itemList) {
            String category = (String) weatherObject.get("category");

            // SKY, PCP, PTY 카테고리인 경우에만 처리
            if (category.equals("SKY")) {

                String fcstTime = (String) weatherObject.get("fcstTime");
                if (fcstTime.equals(baseHourStr)) {
                    Map<String, String> dataMap = new HashMap<>();
                    String fcstValue = (String) weatherObject.get("fcstValue");
                    if (fcstValue.equals("1")) {
                        weather = "맑음";
                    } else if (fcstValue.equals("2")) {
                        weather = "비";
                    } else if (fcstValue.equals("3")) {
                        weather = "구름";
                    } else if (fcstValue.equals("4")) {
                        weather = "흐림";
                    }

                }
            }
        }
        return weather;
    }

    LamcParameter map = new LamcParameter();

    public static class LamcParameter {
        double Re, grid, slat1, slat2, olon, olat, xo, yo;
        boolean first;
    }

    public static int[] mapConv(double lon, double lat, LamcParameter map) {


        // TODO: args를 받아서 처리하는 로직을 구현해야 합니다.
        // x, y 또는 lon, lat 값을 args로부터 설정하세요.

        map.Re = 6371.00877;
        map.grid = 5.0;
        map.slat1 = 30.0;
        map.slat2 = 60.0;
        map.olon = 126.0;
        map.olat = 38.0;
        map.xo = 210 / map.grid;
        map.yo = 675 / map.grid;
        map.first = true;


        double lon1 = 0, lat1 = 0, x1 = 0, y1 = 0;


        lon1 = lon;
        lat1 = lat;
        double[] tmp = lamcproj(lon1, lat1, x1, y1, map);
        int x = (int) (tmp[0] + 1.5);
        int y = (int) (tmp[1] + 1.5);

        int[] result = new int[]{x, y};

        return result;
    }

    public static double[] lamcproj(double lon, double lat, double x, double y, LamcParameter map) {
        double PI = Math.asin(1.0) * 2.0;
        double DEGRAD = PI / 180.0;
        double RADDEG = 180.0 / PI;
        double re = map.Re / map.grid;
        double slat1 = map.slat1 * DEGRAD;
        double slat2 = map.slat2 * DEGRAD;
        double olon = map.olon * DEGRAD;
        double olat = map.olat * DEGRAD;

        double sn = Math.tan(PI * 0.25 + slat2 * 0.5) / Math.tan(PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        map.first = false;

        double ra, theta;

        ra = Math.tan(PI * 0.25 + lat * DEGRAD * 0.5);
        ra = re * sf / Math.pow(ra, sn);
        theta = lon * DEGRAD - olon;
        if (theta > PI) theta -= 2.0 * PI;
        if (theta < -PI) theta += 2.0 * PI;
        theta *= sn;
        x = ra * Math.sin(theta) + map.xo;
        y = ro - ra * Math.cos(theta) + map.yo;

        return new double[]{x, y};

    }

}

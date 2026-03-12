package com.weather.mapper;

import com.weather.weather.City;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CityMapper {
    private static final Map<String, City> cityMap = new HashMap<>();
    private static final List<City> provinces = new ArrayList<>();
    private static final Map<String, List<City>> cityMapByParent = new HashMap<>();

    static {
        initCityData();
    }

    private static void initCityData() {
        City guangdong = createCity("440000", "广东省", "", 1, "113.28064", "23.12518");
        City jiangsu = createCity("320000", "江苏省", "", 1, "118.76286", "32.06087");
        City zhejiang = createCity("330000", "浙江省", "", 1, "120.15358", "30.28746");
        City beijing = createCity("110000", "北京市", "", 1, "116.40529", "39.90498");
        City shanghai = createCity("310000", "上海市", "", 1, "121.47370", "31.23039");
        City tianjin = createCity("120000", "天津市", "", 1, "117.19018", "39.12559");
        City chongqing = createCity("500000", "重庆市", "", 1, "106.50496", "29.53315");
        City sichuan = createCity("510000", "四川省", "", 1, "104.06573", "30.65941");
        City shandong = createCity("370000", "山东省", "", 1, "117.00092", "36.67581");
        City henan = createCity("410000", "河南省", "", 1, "113.66541", "34.75798");
        City hubei = createCity("420000", "湖北省", "", 1, "114.29857", "30.58436");
        City hunan = createCity("430000", "湖南省", "", 1, "112.98228", "28.19409");
        City anhui = createCity("340000", "安徽省", "", 1, "117.28304", "31.86119");
        City fujian = createCity("350000", "福建省", "", 1, "119.29659", "26.10107");
        City jiangxi = createCity("360000", "江西省", "", 1, "115.89215", "28.67649");

        City beijingShi = createCity("110100", "北京市辖区", "110000", 2, "116.40529", "39.90498");
        City shanghaiShi = createCity("310100", "上海市辖区", "310000", 2, "121.47370", "31.23039");
        City tianjinShi = createCity("120100", "天津市辖区", "120000", 2, "117.19018", "39.12559");
        City chongqingShi = createCity("500100", "重庆市辖区", "500000", 2, "106.50496", "29.53315");

        City guangzhou = createCity("440100", "广州市", "440000", 2, "113.28064", "23.12518");
        City shenzhen = createCity("440300", "深圳市", "440000", 2, "114.08595", "22.54700");
        City dongguan = createCity("441900", "东莞市", "440000", 2, "113.74626", "23.02067");
        City foshan = createCity("440600", "佛山市", "440000", 2, "113.12272", "23.02876");
        City zhuhai = createCity("440400", "珠海市", "440000", 2, "113.57668", "22.27075");

        City nanjing = createCity("320100", "南京市", "320000", 2, "118.76286", "32.06087");
        City suzhou = createCity("320500", "苏州市", "320000", 2, "120.61958", "31.30125");
        City wuxi = createCity("320200", "无锡市", "320000", 2, "120.30167", "31.57468");

        City hangzhou = createCity("330100", "杭州市", "330000", 2, "120.15358", "30.28746");
        City ningbo = createCity("330200", "宁波市", "330000", 2, "121.54979", "29.86834");

        City chengdu = createCity("510100", "成都市", "510000", 2, "104.06573", "30.65941");
        City jinan = createCity("370100", "济南市", "370000", 2, "117.00092", "36.67581");
        City zhengzhou = createCity("410100", "郑州市", "410000", 2, "113.66541", "34.75798");
        City wuhan = createCity("420100", "武汉市", "420000", 2, "114.29857", "30.58436");
        City changsha = createCity("430100", "长沙市", "430000", 2, "112.98228", "28.19409");
        City hefei = createCity("340100", "合肥市", "340000", 2, "117.28304", "31.86119");
        City fuzhou = createCity("350100", "福州市", "350000", 2, "119.29659", "26.10107");
        City nanchang = createCity("360100", "南昌市", "360000", 2, "115.89215", "28.67649");

        addBeijingDistricts(beijingShi);
        addShanghaiDistricts(shanghaiShi);
        addGuangzhouDistricts(guangzhou);
        addShenzhenDistricts(shenzhen);
        addCommonDistricts(nanjing);
        addCommonDistricts(hangzhou);

        provinces.addAll(Arrays.asList(guangdong, jiangsu, zhejiang, beijing, shanghai, 
            tianjin, chongqing, sichuan, shandong, henan, hubei, hunan, anhui, fujian, jiangxi));

        addToParentMap(guangdong, Arrays.asList(guangzhou, shenzhen, dongguan, foshan, zhuhai));
        addToParentMap(jiangsu, Arrays.asList(nanjing, suzhou, wuxi));
        addToParentMap(zhejiang, Arrays.asList(hangzhou, ningbo));
        addToParentMap(beijing, Collections.singletonList(beijingShi));
        addToParentMap(shanghai, Collections.singletonList(shanghaiShi));
        addToParentMap(tianjin, Collections.singletonList(tianjinShi));
        addToParentMap(chongqing, Collections.singletonList(chongqingShi));
        addToParentMap(sichuan, Collections.singletonList(chengdu));
        addToParentMap(shandong, Collections.singletonList(jinan));
        addToParentMap(henan, Collections.singletonList(zhengzhou));
        addToParentMap(hubei, Collections.singletonList(wuhan));
        addToParentMap(hunan, Collections.singletonList(changsha));
        addToParentMap(anhui, Collections.singletonList(hefei));
        addToParentMap(fujian, Collections.singletonList(fuzhou));
        addToParentMap(jiangxi, Collections.singletonList(nanchang));
    }

    private static void addBeijingDistricts(City parent) {
        List<City> districts = Arrays.asList(
            createCity("110101", "东城区", parent.getCode(), 3, "116.41667", "39.93333"),
            createCity("110102", "西城区", parent.getCode(), 3, "116.36667", "39.91667"),
            createCity("110105", "朝阳区", parent.getCode(), 3, "116.43333", "39.91667"),
            createCity("110106", "丰台区", parent.getCode(), 3, "116.28333", "39.85000"),
            createCity("110108", "海淀区", parent.getCode(), 3, "116.30000", "39.95000"),
            createCity("110111", "房山区", parent.getCode(), 3, "115.98333", "39.73333"),
            createCity("110114", "昌平区", parent.getCode(), 3, "116.23333", "40.21667"),
            createCity("110115", "大兴区", parent.getCode(), 3, "116.33333", "39.71667")
        );
        addToParentMap(parent, districts);
    }

    private static void addShanghaiDistricts(City parent) {
        List<City> districts = Arrays.asList(
            createCity("310101", "黄浦区", parent.getCode(), 3, "121.48333", "31.21667"),
            createCity("310104", "徐汇区", parent.getCode(), 3, "121.43333", "31.18333"),
            createCity("310105", "长宁区", parent.getCode(), 3, "121.41667", "31.21667"),
            createCity("310106", "静安区", parent.getCode(), 3, "121.45000", "31.23333"),
            createCity("310110", "杨浦区", parent.getCode(), 3, "121.51667", "31.26667"),
            createCity("310112", "闵行区", parent.getCode(), 3, "121.38333", "31.11667"),
            createCity("310115", "浦东新区", parent.getCode(), 3, "121.53333", "31.21667"),
            createCity("310117", "松江区", parent.getCode(), 3, "121.23333", "31.01667")
        );
        addToParentMap(parent, districts);
    }

    private static void addGuangzhouDistricts(City parent) {
        List<City> districts = Arrays.asList(
            createCity("440106", "天河区", parent.getCode(), 3, "113.35076", "23.13586"),
            createCity("440104", "越秀区", parent.getCode(), 3, "113.26568", "23.13021"),
            createCity("440103", "荔湾区", parent.getCode(), 3, "113.23000", "23.12000"),
            createCity("440105", "海珠区", parent.getCode(), 3, "113.33000", "23.10000"),
            createCity("440111", "白云区", parent.getCode(), 3, "113.26000", "23.17000"),
            createCity("440112", "黄埔区", parent.getCode(), 3, "113.44000", "23.10000")
        );
        addToParentMap(parent, districts);
    }

    private static void addShenzhenDistricts(City parent) {
        List<City> districts = Arrays.asList(
            createCity("440305", "南山区", parent.getCode(), 3, "113.92826", "22.53543"),
            createCity("440304", "福田区", parent.getCode(), 3, "114.05454", "22.53748"),
            createCity("440303", "罗湖区", parent.getCode(), 3, "114.13177", "22.54733"),
            createCity("440306", "宝安区", parent.getCode(), 3, "113.88387", "22.55789"),
            createCity("440307", "龙岗区", parent.getCode(), 3, "114.24305", "22.71820")
        );
        addToParentMap(parent, districts);
    }

    private static void addCommonDistricts(City parent) {
        List<City> districts = Arrays.asList(
            createCity(parent.getCode() + "1", "主城区", parent.getCode(), 3, parent.getLongitude(), parent.getLatitude()),
            createCity(parent.getCode() + "2", "新区", parent.getCode(), 3, parent.getLongitude(), parent.getLatitude())
        );
        addToParentMap(parent, districts);
    }

    private static City createCity(String code, String name, String parentCode, int level, String lon, String lat) {
        City city = new City();
        city.setCode(code);
        city.setName(name);
        city.setParentCode(parentCode);
        city.setLevel(level);
        city.setLongitude(lon);
        city.setLatitude(lat);
        cityMap.put(code, city);
        return city;
    }

    private static void addToParentMap(City parent, List<City> children) {
        cityMapByParent.put(parent.getCode(), children);
    }

    @Cacheable(value = "city", key = "'provinces'")
    public List<City> getProvinces() {
        return new ArrayList<>(provinces);
    }

    @Cacheable(value = "city", key = "'children:' + #parentCode")
    public List<City> getChildren(String parentCode) {
        return cityMapByParent.getOrDefault(parentCode, new ArrayList<>());
    }

    @Cacheable(value = "city", key = "'code:' + #code")
    public City getCityByCode(String code) {
        return cityMap.get(code);
    }

    @Cacheable(value = "city", key = "'name:' + #name")
    public City getCityByName(String name) {
        return cityMap.values().stream()
                .filter(c -> c.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Cacheable(value = "city", key = "'location:' + #lon + ',' + #lat")
    public City getCityByLocation(String lon, String lat) {
        return cityMap.values().stream()
                .filter(c -> c.getLevel() == 3)
                .min(Comparator.comparingDouble(c ->
                        distance(Double.parseDouble(c.getLatitude()), Double.parseDouble(c.getLongitude()),
                                Double.parseDouble(lat), Double.parseDouble(lon))))
                .orElse(cityMap.values().stream().findFirst().orElse(null));
    }

    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);
        return 6371 * (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
    }
}

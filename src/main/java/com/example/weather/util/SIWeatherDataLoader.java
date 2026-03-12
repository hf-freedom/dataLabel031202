package com.example.weather.util;

import com.example.weather.entity.City;
import com.example.weather.entity.District;
import com.example.weather.entity.Province;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 城市数据加载工具类
 * 加载省-市-县三级结构数据到本地缓存
 */
@Component
public class SIWeatherDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(SIWeatherDataLoader.class);

    /**
     * 省份列表缓存
     */
    private static final List<Province> PROVINCE_LIST = new ArrayList<>();

    /**
     * 编码映射缓存 key: 编码, value: 名称
     */
    private static final Map<String, String> CODE_NAME_MAP = new HashMap<>();

    /**
     * 名称映射缓存 key: 名称, value: 编码
     */
    private static final Map<String, String> NAME_CODE_MAP = new HashMap<>();

    @PostConstruct
    public void init() {
        logger.info("开始加载城市数据...");
        loadCityData();
        logger.info("城市数据加载完成，共加载 {} 个省份", PROVINCE_LIST.size());
    }

    /**
     * 加载城市数据（简化版，包含主要省市）
     */
    private void loadCityData() {
        // 北京市
        Province beijing = createProvince("110000", "北京市");
        City bjCity = createCity("110100", "北京市");
        bjCity.getDistricts().add(createDistrict("110101", "东城区", 116.418, 39.929));
        bjCity.getDistricts().add(createDistrict("110102", "西城区", 116.366, 39.914));
        bjCity.getDistricts().add(createDistrict("110105", "朝阳区", 116.486, 39.928));
        bjCity.getDistricts().add(createDistrict("110108", "海淀区", 116.298, 39.959));
        beijing.getCities().add(bjCity);
        PROVINCE_LIST.add(beijing);

        // 上海市
        Province shanghai = createProvince("310000", "上海市");
        City shCity = createCity("310100", "上海市");
        shCity.getDistricts().add(createDistrict("310101", "黄浦区", 121.490, 31.223));
        shCity.getDistricts().add(createDistrict("310104", "徐汇区", 121.437, 31.195));
        shCity.getDistricts().add(createDistrict("310105", "长宁区", 121.400, 31.221));
        shCity.getDistricts().add(createDistrict("310106", "静安区", 121.446, 31.229));
        shCity.getDistricts().add(createDistrict("310115", "浦东新区", 121.545, 31.222));
        shanghai.getCities().add(shCity);
        PROVINCE_LIST.add(shanghai);

        // 广东省
        Province guangdong = createProvince("440000", "广东省");
        City gzCity = createCity("440100", "广州市");
        gzCity.getDistricts().add(createDistrict("440103", "荔湾区", 113.244, 23.126));
        gzCity.getDistricts().add(createDistrict("440104", "越秀区", 113.267, 23.130));
        gzCity.getDistricts().add(createDistrict("440105", "海珠区", 113.262, 23.084));
        gzCity.getDistricts().add(createDistrict("440106", "天河区", 113.362, 23.125));
        guangdong.getCities().add(gzCity);

        City szCity = createCity("440300", "深圳市");
        szCity.getDistricts().add(createDistrict("440303", "罗湖区", 114.123, 22.571));
        szCity.getDistricts().add(createDistrict("440304", "福田区", 114.055, 22.522));
        szCity.getDistricts().add(createDistrict("440305", "南山区", 113.936, 22.532));
        szCity.getDistricts().add(createDistrict("440306", "宝安区", 113.883, 22.553));
        guangdong.getCities().add(szCity);
        PROVINCE_LIST.add(guangdong);

        // 浙江省
        Province zhejiang = createProvince("330000", "浙江省");
        City hzCity = createCity("330100", "杭州市");
        hzCity.getDistricts().add(createDistrict("330102", "上城区", 120.169, 30.252));
        hzCity.getDistricts().add(createDistrict("330103", "下城区", 120.181, 30.286));
        hzCity.getDistricts().add(createDistrict("330104", "江干区", 120.205, 30.257));
        hzCity.getDistricts().add(createDistrict("330106", "西湖区", 120.130, 30.272));
        zhejiang.getCities().add(hzCity);
        PROVINCE_LIST.add(zhejiang);

        // 江苏省
        Province jiangsu = createProvince("320000", "江苏省");
        City njCity = createCity("320100", "南京市");
        njCity.getDistricts().add(createDistrict("320102", "玄武区", 118.798, 32.049));
        njCity.getDistricts().add(createDistrict("320104", "秦淮区", 118.794, 32.022));
        njCity.getDistricts().add(createDistrict("320106", "鼓楼区", 118.770, 32.066));
        jiangsu.getCities().add(njCity);
        PROVINCE_LIST.add(jiangsu);

        // 四川省
        Province sichuan = createProvince("510000", "四川省");
        City cdCity = createCity("510100", "成都市");
        cdCity.getDistricts().add(createDistrict("510104", "锦江区", 104.082, 30.657));
        cdCity.getDistricts().add(createDistrict("510105", "青羊区", 104.062, 30.674));
        cdCity.getDistricts().add(createDistrict("510107", "武侯区", 104.043, 30.642));
        sichuan.getCities().add(cdCity);
        PROVINCE_LIST.add(sichuan);

        // 湖北省
        Province hubei = createProvince("420000", "湖北省");
        City whCity = createCity("420100", "武汉市");
        whCity.getDistricts().add(createDistrict("420102", "江岸区", 114.309, 30.600));
        whCity.getDistricts().add(createDistrict("420106", "武昌区", 114.307, 30.554));
        hubei.getCities().add(whCity);
        PROVINCE_LIST.add(hubei);

        // 构建编码映射
        buildCodeNameMap();
    }

    private Province createProvince(String code, String name) {
        Province province = new Province();
        province.setCode(code);
        province.setName(name);
        province.setCities(new ArrayList<>());
        return province;
    }

    private City createCity(String code, String name) {
        City city = new City();
        city.setCode(code);
        city.setName(name);
        city.setDistricts(new ArrayList<>());
        return city;
    }

    private District createDistrict(String code, String name, Double longitude, Double latitude) {
        District district = new District();
        district.setCode(code);
        district.setName(name);
        district.setLongitude(longitude);
        district.setLatitude(latitude);
        return district;
    }

    private void buildCodeNameMap() {
        for (Province province : PROVINCE_LIST) {
            CODE_NAME_MAP.put(province.getCode(), province.getName());
            NAME_CODE_MAP.put(province.getName(), province.getCode());
            for (City city : province.getCities()) {
                CODE_NAME_MAP.put(city.getCode(), city.getName());
                NAME_CODE_MAP.put(city.getName(), city.getCode());
                for (District district : city.getDistricts()) {
                    CODE_NAME_MAP.put(district.getCode(), district.getName());
                    NAME_CODE_MAP.put(district.getName(), district.getCode());
                }
            }
        }
    }

    /**
     * 获取所有省份列表
     *
     * @return 省份列表
     */
    public List<Province> getAllProvinces() {
        return PROVINCE_LIST;
    }

    /**
     * 根据编码获取名称
     *
     * @param code 编码
     * @return 名称
     */
    public String getNameByCode(String code) {
        return CODE_NAME_MAP.get(code);
    }

    /**
     * 根据名称获取编码
     *
     * @param name 名称
     * @return 编码
     */
    public String getCodeByName(String name) {
        return NAME_CODE_MAP.get(name);
    }

    /**
     * 根据城市编码获取城市名称
     *
     * @param cityCode 城市编码
     * @return 城市名称
     */
    public String getCityNameByCode(String cityCode) {
        return CODE_NAME_MAP.get(cityCode);
    }
}

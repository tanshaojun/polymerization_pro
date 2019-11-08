package com.polymerization.controller;

import com.alibaba.fastjson.JSONObject;
import com.polymerization.util.ElasticsearchUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * Created with IntelliJ IDEA
 * name tan shaojun
 * Date: 2018/8/2
 * Time: 下午9:30
 */
@Slf4j
@Controller
public class ElCcontroller {

    private final static String INDEX = "test";

    private final static String TYPE = "tst";

    /**
     * 查询数据
     *
     * @return
     */
    @RequestMapping("/list")
    @ResponseBody
    public String list(String key) {
        if (!ElasticsearchUtils.isIndexExist(INDEX)) {
            try {
                ElasticsearchUtils.createIndex(INDEX, TYPE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        EsPage esPage = ElasticsearchUtils.searchDataPage(INDEX, TYPE, 0, 1, 0, 0, "", "", false,
                "key", "key=*3*");
        return JSONObject.toJSONString(esPage);
    }

    /**
     * 删除数据
     *
     * @return
     */
    @RequestMapping("/delete")
    @ResponseBody
    public String delete(String id) {
        ElasticsearchUtils.deleteDataById(INDEX, TYPE, id);
        return "success";
    }

    /**
     * 更新数据
     *
     * @return
     */
    @RequestMapping("/update")
    @ResponseBody
    public String update(String id) {
        Map<String, String> map = new HashMap<>(16);
        Random random = new Random();
        map.put("key", String.valueOf(random.nextInt(100000)));
        map.put("value", String.valueOf(random.nextInt(100000)));
        ElasticsearchUtils.updateDataById(JSONObject.parseObject(JSONObject.toJSONString(map)), INDEX, TYPE, id);
        return "success";
    }

    /**
     * 插入数据
     *
     * @return
     */
    @ResponseBody
    @RequestMapping("/insert")
    public String insert() {
        Map<String, String> map = new HashMap<>(16);
        Random random = new Random();
        map.put("key", String.valueOf(random.nextInt(100000)));
        map.put("value", String.valueOf(random.nextInt(100000)));
        String id = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        log.info("添加的id为：{}", id);
        return ElasticsearchUtils.addData(JSONObject.parseObject(JSONObject.toJSONString(map)), INDEX,
                TYPE, id);
    }
}

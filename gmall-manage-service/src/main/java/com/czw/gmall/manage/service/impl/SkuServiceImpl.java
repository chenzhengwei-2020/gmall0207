package com.czw.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.czw.gmall.beans.*;
import com.czw.gmall.manage.mapper.PmsSkuAttrValueMapper;
import com.czw.gmall.manage.mapper.PmsSkuImageMapper;
import com.czw.gmall.manage.mapper.PmsSkuInfoMapper;
import com.czw.gmall.manage.mapper.PmsSkuSaleAttrValueMapper;
import com.czw.gmall.mq.ActiveMQUtil;
import com.czw.gmall.redis.redisUitl.RedisUtil;
import com.czw.gmall.service.SkuService;
import com.czw.gmall.util.MQUtil;
import io.searchbox.client.JestClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.Jedis;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class SkuServiceImpl implements SkuService {

    @Autowired
    private PmsSkuInfoMapper pmsSkuInfoMapper;

    @Autowired
    private PmsSkuAttrValueMapper pmsSkuAttrValueMapper;

    @Autowired
    private PmsSkuSaleAttrValueMapper pmsSkuSaleAttrValueMapper;

    @Autowired
    private PmsSkuImageMapper pmsSkuImageMapper;

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Autowired
    JestClient jestClient;//es客户端

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        // 插入skuInfo
        pmsSkuInfoMapper.insertSelective(pmsSkuInfo);
        // 插入平台属性关联
        List<PmsSkuAttrValue> skuAttrValueList = pmsSkuInfo.getSkuAttrValueList();
        for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuAttrValueMapper.insertSelective(pmsSkuAttrValue);
        }
        // 插入销售属性关联
        List<PmsSkuSaleAttrValue> skuSaleAttrValueList = pmsSkuInfo.getSkuSaleAttrValueList();
        for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuSaleAttrValueList) {
            pmsSkuSaleAttrValue.setSkuId(pmsSkuInfo.getId());
            pmsSkuSaleAttrValueMapper.insertSelective(pmsSkuSaleAttrValue);
        }
        // 插入图片信息
        List<PmsSkuImage> skuImageList = pmsSkuInfo.getSkuImageList();
        for (PmsSkuImage pmsSkuImage : skuImageList) {
            pmsSkuImage.setSkuId(pmsSkuInfo.getId());
            pmsSkuImageMapper.insertSelective(pmsSkuImage);
        }
        //发送添加sku成功的消息 同步搜索引擎
        MQUtil.createMyQueue(activeMQUtil,"SKU_ADD_SUCCESS","success");
        return "success";
    }

    @Override
    public PmsSkuInfo getById(String skuId) {
        PmsSkuInfo skuInfo = new PmsSkuInfo();
        skuInfo.setId(skuId);
        PmsSkuInfo pmsSkuInfo = pmsSkuInfoMapper.selectOne(skuInfo);
        //图片集合
        PmsSkuImage pmsSkuImage = new PmsSkuImage();
        pmsSkuImage.setSkuId(skuId);
        List<PmsSkuImage> skuImages = pmsSkuImageMapper.select(pmsSkuImage);
        pmsSkuInfo.setSkuImageList(skuImages);
        return pmsSkuInfo;
    }

    @Override
    public PmsSkuInfo getBySkuIdFromRedis(String skuId) {//redis缓存

        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();

        //获取redis连接
        Jedis jedis = redisUtil.getJedis();

        try {

            //定义key
            String skuKey = "sku:" + skuId + ":info";

            //从缓存中取出
            String skuJson = jedis.get(skuKey);

            if (StringUtils.isNotBlank(skuJson)) {
                //如果不为空 则将从缓存中取出的skuJson转换为pmsSkuInfo
                pmsSkuInfo = JSON.parseObject(skuJson, PmsSkuInfo.class);
            } else {
                //如果为空 则从数据库中查询
                String token = UUID.randomUUID().toString();
                //设置redis分布式锁
                String OK = jedis.set("sku:" + skuId + ":lock", token, "nx", "px", 3 * 1000);//获得锁的人有3秒过期时间
                if (StringUtils.isNotBlank(OK) && OK.equals("OK")) {
                    //获得3秒钟访问数据库的时间
                    pmsSkuInfo = getById(skuId);
                    if (pmsSkuInfo != null) {
                        //存入缓存
                        jedis.set("sku:" + skuId + ":info", JSON.toJSONString(pmsSkuInfo));
                    } else {
                        //防止缓存击穿(查询不存在的key 导致数据库压力过大) 给key设置空值
                        jedis.setex("sku:" + skuId + ":info", 60 * 3, JSON.toJSONString(""));
                    }

                    String s = jedis.get("sku:" + skuId + ":lock");
                    if (StringUtils.isNotBlank(s) && s.equals(token)) {//确认删除的是自己的锁
                        //访问mysql后 将释放锁
                        jedis.del("sku:" + skuId + ":lock");
                    }


                } else {//设置失败 自旋
                    Thread.sleep(3000);
                    return getBySkuIdFromRedis(skuId);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return pmsSkuInfo;
    }

    @Override
    public List<PmsSkuInfo> getAllPmsSkuInfo() {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfos) {
            //封装skuAttrValueList
            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(pmsSkuInfo.getId());
            List<PmsSkuAttrValue> pmsSkuAttrValues = pmsSkuAttrValueMapper.select(pmsSkuAttrValue);
            pmsSkuInfo.setSkuAttrValueList(pmsSkuAttrValues);
        }
        return pmsSkuInfos;
    }

    @Override
    public boolean checkPrice(String productSkuId, BigDecimal price) {
        PmsSkuInfo pmsSkuInfo = new PmsSkuInfo();
        pmsSkuInfo.setId(productSkuId);
        PmsSkuInfo skuInfo = pmsSkuInfoMapper.selectOne(pmsSkuInfo);
        if(price.compareTo(skuInfo.getPrice()) == 0){
            return true;
        }
        return false;
    }


    @Override
    public List<PmsSkuInfo> getSkuSaleAttrHash(String productId) {
        List<PmsSkuInfo> pmsSkuInfos = pmsSkuInfoMapper.selectSkuSaleAttrHash(productId);
        return pmsSkuInfos;
    }


}

package com.czw.gmall.manage.mapper;

import com.czw.gmall.beans.PmsSkuInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsSkuInfoMapper extends Mapper<PmsSkuInfo> {

    public List<PmsSkuInfo> selectSkuSaleAttrHash(@Param("productId") String productId);
}

package com.czw.gmall.manage.mapper;

import com.czw.gmall.beans.PmsBaseAttrInfo;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface AttrInfoMapper extends Mapper<PmsBaseAttrInfo> {
    public List<PmsBaseAttrInfo> selectAttrValueListByValueId(@Param("valueIdStr") String valueIdStr);
}

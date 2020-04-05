package com.czw.gmall.service;

import com.czw.gmall.beans.PmsSearchParam;
import com.czw.gmall.beans.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) throws Exception;

    void synEs();//同步es
}

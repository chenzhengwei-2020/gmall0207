package com.czw.gmall.manage.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.czw.gmall.beans.PmsBaseCatalog1;
import com.czw.gmall.beans.PmsBaseCatalog2;
import com.czw.gmall.beans.PmsBaseCatalog3;
import com.czw.gmall.manage.mapper.PmsBaseCatalog1Mapper;
import com.czw.gmall.manage.mapper.PmsBaseCatalog2Mapper;
import com.czw.gmall.manage.mapper.PmsBaseCatalog3Mapper;
import com.czw.gmall.service.CatalogService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    private PmsBaseCatalog1Mapper pmsBaseCatalog1Mapper;

    @Autowired
    private PmsBaseCatalog2Mapper pmsBaseCatalog2Mapper;

    @Autowired
    private PmsBaseCatalog3Mapper pmsBaseCatalog3Mapper;

    @Override
    public List<PmsBaseCatalog1> getCatalog1() {

        return pmsBaseCatalog1Mapper.selectAll();
    }

    @Override
    public List<PmsBaseCatalog2> getCatalog2(PmsBaseCatalog1 pmsBaseCatalog1) {
        PmsBaseCatalog2 pmsBaseCatalog2 = new PmsBaseCatalog2();
        pmsBaseCatalog2.setCatalog1Id(pmsBaseCatalog1.getId());
        return pmsBaseCatalog2Mapper.select(pmsBaseCatalog2);
    }

    @Override
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id) {
        PmsBaseCatalog3 pmsBaseCatalog3 = new PmsBaseCatalog3();
        pmsBaseCatalog3.setCatalog2Id(catalog2Id);
        return pmsBaseCatalog3Mapper.select(pmsBaseCatalog3);
    }
}

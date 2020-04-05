package com.czw.gmall.service;

import com.czw.gmall.beans.PmsBaseCatalog1;
import com.czw.gmall.beans.PmsBaseCatalog2;
import com.czw.gmall.beans.PmsBaseCatalog3;

import java.util.List;

public interface CatalogService {

    public List<PmsBaseCatalog1> getCatalog1();

    public List<PmsBaseCatalog2> getCatalog2(PmsBaseCatalog1 pmsBaseCatalog1);

    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id);
}

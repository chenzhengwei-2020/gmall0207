package com.czw.gmall.manage.contorller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.czw.gmall.beans.PmsBaseCatalog1;
import com.czw.gmall.beans.PmsBaseCatalog2;
import com.czw.gmall.beans.PmsBaseCatalog3;
import com.czw.gmall.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@CrossOrigin
public class CatalogContorller {

    @Reference
    private CatalogService catalogService;

    @RequestMapping("getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> catalog1s = catalogService.getCatalog1();
        return catalog1s;
    }

    @RequestMapping("getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(String catalog1Id){
        PmsBaseCatalog1 pmsBaseCatalog1 = new PmsBaseCatalog1();
        pmsBaseCatalog1.setId(catalog1Id);
        List<PmsBaseCatalog2> catalog2s = catalogService.getCatalog2(pmsBaseCatalog1);
        return catalog2s;
    }

    @RequestMapping("getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(String catalog2Id){

        List<PmsBaseCatalog3> catalog3s = catalogService.getCatalog3(catalog2Id);
        return catalog3s;
    }
}

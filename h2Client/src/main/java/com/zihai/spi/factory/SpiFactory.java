package com.zihai.spi.factory;

import com.zihai.spi.service.SpiService;

import java.util.Iterator;
import java.util.ServiceLoader;

public class SpiFactory {
    public static SpiService getService(){
        ServiceLoader<SpiService>  loader = ServiceLoader.load(SpiService.class);
        Iterator<SpiService> iterator = loader.iterator();
        if(iterator.hasNext())
            return  iterator.next();
        return null;
    }
}

package com.zihai.h2Client.test;

import com.zihai.h2Client.core.CarMapper;
import com.zihai.h2Client.core.dto.Car;
import com.zihai.h2Client.core.dto.CarDto;
import com.zihai.h2Client.util.JsonHelp;


public class MapStructTest {
    public static void main(String[] args) {
        //given
        Car car = new Car( "Morris", 5, "kaixin");

        //when
        CarDto carDto = CarMapper.INSTANCE.carToCarDto( car );

        //then
        System.out.println(JsonHelp.gson.toJson(carDto));
    }
}

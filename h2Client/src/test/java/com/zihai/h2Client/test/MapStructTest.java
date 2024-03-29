package com.zihai.h2Client.test;

import com.zihai.h2Client.config.DTOMapper;
import com.zihai.h2Client.dto.Car;
import com.zihai.h2Client.dto.CarDto;
import com.zihai.h2Client.util.JsonHelp;


public class MapStructTest {
    public static void main(String[] args) {
        //given
        Car car = new Car( "Morris", 5, "kaixin");

        //when
        CarDto carDto = DTOMapper.INSTANCE.carToCarDto( car );

        //then
        System.out.println(JsonHelp.gson.toJson(carDto));
    }
}

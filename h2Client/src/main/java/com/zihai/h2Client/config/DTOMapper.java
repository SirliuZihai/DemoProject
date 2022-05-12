package com.zihai.h2Client.config;

import com.zihai.h2Client.dto.Car;
import com.zihai.h2Client.dto.CarDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface DTOMapper {
 
    DTOMapper INSTANCE = Mappers.getMapper( DTOMapper.class );
 
    @Mapping(source = "numberOfSeats", target = "seatCount")
    CarDto carToCarDto(Car car);
}
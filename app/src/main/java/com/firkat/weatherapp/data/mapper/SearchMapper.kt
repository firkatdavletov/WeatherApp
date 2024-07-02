package com.firkat.weatherapp.data.mapper

import com.firkat.weatherapp.data.network.dto.CityDto
import com.firkat.weatherapp.domain.entity.City

fun CityDto.toEntity(): City = City(id, name, country)
fun List<CityDto>.toEntities(): List<City> = map { it.toEntity() }
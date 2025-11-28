package com.example.semesterproject.persistence

typealias CurrentModel = com.example.semesterproject.models.Current
typealias ForecastModel = com.example.semesterproject.models.ForecastResponse
typealias LocationModel = com.example.semesterproject.models.Location
typealias CurrentEntity = com.example.semesterproject.persistence.entities.Current
typealias LocationEntity = com.example.semesterproject.persistence.entities.Location
typealias ForecastEntity = com.example.semesterproject.persistence.entities.ForecastResponse

object EntityModelConverter {

    fun convertModelToEntity(model: ForecastModel): ForecastEntity {
        val location = model.location
        val current = model.current
        return ForecastEntity(
            id = 0L,
            LocationEntity(
                id = 0L,
                location.name,
                location.region,
                location.country,
                location.lat,
                location.lon,
                location.localtime
            ),
            CurrentEntity(
                id = 0L,
                current.last_updated,
                current.temp_c,
                current.temp_f,
                current.wind_mph,
                current.wind_kph,
                current.wind_dir
            )
        )
    }

    fun convertEntityToModel(entity: ForecastEntity): ForecastModel {
        val location = entity.location
        val current = entity.current
        return ForecastModel(
            LocationModel(
                location.name,
                location.region,
                location.country,
                location.lat,
                location.lon,
                location.localtime
            ),
            CurrentModel(
                current.last_updated,
                current.temp_c,
                current.temp_f,
                current.wind_mph,
                current.wind_kph,
                current.wind_dir
            )
        )
    }

}
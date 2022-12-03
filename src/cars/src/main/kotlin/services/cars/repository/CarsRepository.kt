package services.cars.repository

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.stereotype.Repository
import services.cars.data.CarsTable
import services.cars.data.CarsTable.availability
import services.cars.data.CarsTable.brand
import services.cars.data.CarsTable.carUid
import services.cars.data.CarsTable.model
import services.cars.data.CarsTable.power
import services.cars.data.CarsTable.price
import services.cars.data.CarsTable.registrationNumber
import services.cars.data.CarsTable.type
import services.cars.entity.Car
import services.cars.insecure.Config
import java.util.*

object CarsRepository {

    private val db by lazy {
        Database.connect(
            Config.POSTGRES_DB_ADDRESS,
            "org.postgresql.Driver",
            Config.POSTGRES_USER,
            Config.POSTGRES_PASSWORD
        )
    }

    init {
        transaction(db) {
            if (!CarsTable.exists()) {
                SchemaUtils.create(CarsTable)
                CarsTable.insert {
                    it[CarsTable.id] = 1
                    it[carUid] = UUID.fromString("d28e950c-57a7-11ed-9b6a-0242ac120002")
                    it[brand] = "vw"
                    it[model] = "golf"
                    it[registrationNumber] = "ыхыхыхы"
                    it[power] = 300
                    it[price] = 5000
                    it[type] = "SEDAN"
                    it[availability] = true
                }
                CarsTable.insert {
                    it[CarsTable.id] = 2
                    it[carUid] = UUID.fromString("108f06ac-57a8-11ed-9b6a-0242ac120002")
                    it[brand] = "vw"
                    it[model] = "Tiguan"
                    it[registrationNumber] = "ыхыхыхы"
                    it[power] = 170
                    it[price] = 1000
                    it[type] = "SUV"
                    it[availability] = true
                }
                CarsTable.insert {
                    it[CarsTable.id] = 3
                    it[carUid] = UUID.fromString("109b42f3-198d-4c89-9276-a7520a7120ab")
                    it[brand] = "Mercedes Benz"
                    it[model] = "GLA 250"
                    it[registrationNumber] = "ЛО777Х799"
                    it[power] = 249
                    it[price] = 3500
                    it[type] = "SEDAN"
                    it[availability] = true
                }
            }
        }
    }

    fun get(showAll: Boolean = false): Array<Car> =
        transaction(db) {
            val preset = if (showAll) CarsTable.selectAll() else CarsTable.select(availability eq true)

            preset
                .map { car ->
                    Car(
                        car[CarsTable.id],
                        car[carUid],
                        car[brand],
                        car[model],
                        car[registrationNumber],
                        car[price],
                        car[availability],
                        car[power],
                        car[type]
                    )
                }
                .toTypedArray()
        }

    fun get(uid: UUID): Car? =
        transaction(db) {
            CarsTable
                .select(carUid eq uid)
                .map { car ->
                    Car(
                        car[CarsTable.id],
                        car[carUid],
                        car[brand],
                        car[model],
                        car[registrationNumber],
                        car[price],
                        car[availability],
                        car[power],
                        car[type]
                ) }
                .toTypedArray()
                .firstOrNull()
        }

    fun changeAvailability(uid: UUID, newAvailability: Boolean) =
        transaction(db) {
            CarsTable.update({ CarsTable.carUid eq uid }) {
                it[availability] = newAvailability
            }
        }
}
package inventory.grpc

import mu.KotlinLogging
import inventory.database.CarInventory
import inventory.model.Car
import io.quarkus.grpc.GrpcService
import io.smallrye.mutiny.Uni
import rosa.victor.inventory.model.CarResponse
import rosa.victor.inventory.model.InsertCarRequest
import rosa.victor.inventory.model.InventoryService
import rosa.victor.inventory.model.RemoveCarRequest

@GrpcService
class GrpcInventoryService(private val inventory: CarInventory) : InventoryService {


  private val logger = KotlinLogging.logger {}

  override fun add(request: InsertCarRequest): Uni<CarResponse> {
    val car = Car().apply {
      licensePlateNumber = request.licensePlateNumber
      manufacturer = request.manufacturer
      model = request.model
      id = CarInventory.ids.incrementAndGet()
    }

    logger.info { "Persisting car: $car" }
    inventory.cars?.add(car)

    return Uni.createFrom().item(CarResponse.newBuilder()
      .setLicensePlateNumber(car.licensePlateNumber)
      .setManufacturer(car.manufacturer)
      .setModel(car.model)
      .setId(car.id!!)
      .build())
  }

  override fun remove(request: RemoveCarRequest): Uni<CarResponse> {
    val car = inventory.cars?.first { it.licensePlateNumber == request.licensePlateNumber }
    logger.info { "Removing car: $car" }
    inventory.cars?.remove(car)
    return if (car != null) {
      Uni.createFrom().item(
        car.id?.let {
          CarResponse.newBuilder()
            .setLicensePlateNumber(car.licensePlateNumber)
            .setManufacturer(car.manufacturer)
            .setModel(car.model)
            .setId(it)
            .build()
        }
      )
    } else {
      Uni.createFrom().nullItem()
    }
  }
}
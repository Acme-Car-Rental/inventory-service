package inventory.service

import inventory.database.CarInventory
import inventory.model.Car
import org.eclipse.microprofile.graphql.GraphQLApi
import org.eclipse.microprofile.graphql.Mutation
import org.eclipse.microprofile.graphql.Query

@GraphQLApi
class GraphQLInventoryService(private val inventory: CarInventory) {

  @Query
  fun cars(): MutableList<Car>? = inventory.cars


  @Mutation
  fun register(car: Car): Car {
    car.id  = CarInventory.ids.incrementAndGet()
    inventory.cars?.add(car)
    return car
  }

  @Mutation
  fun remove(licensePlateNumber: String): Boolean {
    return inventory.cars!!.removeIf { it.licensePlateNumber == licensePlateNumber }
  }



}
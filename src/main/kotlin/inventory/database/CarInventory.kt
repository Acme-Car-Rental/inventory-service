package inventory.database

import inventory.model.Car
import jakarta.annotation.PostConstruct
import jakarta.enterprise.context.ApplicationScoped
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicLong

@ApplicationScoped
class CarInventory{

  companion object {
    val ids: AtomicLong = AtomicLong(0)
  }
  var cars: MutableList<Car>? = null

  @PostConstruct
  fun initialize() {
    cars = CopyOnWriteArrayList()
    initialData()
  }

  fun initialData() {
    val mazda = Car(ids.incrementAndGet(), "ABC123", "Mazda", "6")
    val ford = Car(ids.incrementAndGet(), "XYZ987", "Ford", "Mustang")
    cars?.add(mazda)
    cars?.add(ford)
  }
}
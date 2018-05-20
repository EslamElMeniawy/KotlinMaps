package elmeniawy.eslam.pockemon

import android.location.Location

/**
 * Pockemon
 *
 * Created by Eslam El-Meniawy on 20-May-2018.
 * CITC - Mansoura University
 */
class Pockemon(name: String, description: String, image: Int, power: Double, latitude: Double,
               longitude: Double) {
    var name: String? = name
    var description: String? = description
    var image: Int? = image
    var power: Double? = power
    var location: Location? = null
    var catched: Boolean = false

    init {
        this.location = Location(name)
        this.location!!.latitude = latitude
        this.location!!.longitude = longitude
    }

}
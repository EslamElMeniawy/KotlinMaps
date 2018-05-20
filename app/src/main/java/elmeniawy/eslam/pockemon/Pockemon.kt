package elmeniawy.eslam.pockemon

/**
 * Pockemon
 *
 * Created by Eslam El-Meniawy on 20-May-2018.
 * CITC - Mansoura University
 */
class Pockemon {
    var name: String? = null
    var description: String? = null
    var image: Int? = null
    var power: Double? = null
    var latitude: Double? = null
    var longitude: Double? = null
    var catched: Boolean = false

    constructor(name: String, description: String, image: Int, power: Double, latitude: Double,
                longitude: Double) {
        this.name = name
        this.description = description
        this.image = image
        this.power = power
        this.latitude = latitude
        this.longitude = longitude
    }
}
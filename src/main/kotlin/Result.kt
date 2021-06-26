import kotlinx.serialization.Serializable

@Serializable
data class Result(
        val average: Double,
        val unitTimeForVisit: Double,
        val visits: MutableList<Visit>
)
@Serializable
data class Visit (
        val time: Long,
        val offset: Double,
        val maybeAttack: Boolean,
        val requestCount: Int,
        val IPs: Map<String,Int>
        )
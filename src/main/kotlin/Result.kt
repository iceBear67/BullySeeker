import kotlinx.serialization.Serializable

@Serializable
data class Result(
        val average: Double,
        val visits: MutableList<Visit>
)
@Serializable
data class Visit (
        val time: Long,
        val offset: Double,
        val maybeAttack: Boolean,
        val IPs: Map<String,Int>
        )
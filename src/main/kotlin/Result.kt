import kotlinx.serialization.Serializable

@Serializable
data class Result(
        val average: Double,
        val visits: MutableList<Time>
)
@Serializable
data class Time (
        val time: Long,
        val offset: Double,
        val IPs: Map<String,Int>
        )
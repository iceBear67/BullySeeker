import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class Reader(text: String) {
    val seq= mutableSetOf<Pair<String,Long>>()
    val sdf= SimpleDateFormat("dd/MMMM/yyyy:hh:mm:ss",Locale.UK)
    val REGEX= Pattern.compile("(.*)? - - \\[(.*)?\\ \\+.*\\]")
    init{
        val matcher = REGEX.matcher(text)
        if(matcher.find()){
            val a=matcher.results()
            for (matchResult in a) {
                seq.add(Pair(matchResult.group(1),sdf.parse(matchResult.group(2)).time))
            }
        }else{
            println("Can't parse log. Is it default nginx log format?")
        }
    }
    fun read(distance: Double): List<Pair<Long,List<String>>>{
        val result = mutableListOf<Pair<Long,List<String>>>()
        var start = 0L;
        var cache = mutableListOf<String>()
        seq.forEach{
            if(it.second - start > distance){
                // new starts.
                    if(start!=0L){
                        result.add(Pair(start,cache))
                    }
                cache = mutableListOf()
                start = it.second
            }
            cache.add(it.first)
        }
        return result
    }
}
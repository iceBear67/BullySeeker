import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import java.util.stream.Collectors
import kotlin.system.exitProcess
import java.util.HashMap




object Main {

    @JvmStatic
    fun main(arg: Array<String>){
        if(arg.size!=3){
            println("Arguments are missing. Usage: ./xx <Log File> <Unit Time> <rate>")
            exitProcess(0)
        }
        val content = File(arg[0]).readText()
        if(content.isNullOrEmpty()){
            println("Null or empty text.")
            exitProcess(0)
        }
        val unit = arg[1].toInt()
        val rate = arg[2].toInt()
        if(unit<=0 || rate<0){
            println("Illegal unit/rate. unit/rate > 0")
            exitProcess(0)
        }
        val reader = Reader(content)
        var rawResult = reader.read(unit)
        val average = rawResult.stream().map { e->e.second.size }.filter{ it!=0 }.collect(Collectors.toList()).average()
        var reportedResult = mutableListOf<Pair<Long,Double>>()
        rawResult = rawResult.filter { e->e.second.size - average > 0 }
        rawResult.filter { e->e.second.size-average>rate }.forEach { reportedResult.add(Pair(it.first,it.second.size-average))}
        rawResult = rawResult.sortedWith(compareBy{it.second.size-average})
        rawResult = rawResult.asReversed()
        reportedResult = reportedResult.sortedWith(compareBy{it.second}).toMutableList()
        reportedResult = reportedResult.asReversed()
        reportedResult.stream().forEachOrdered {
            println("Time: ${Date(it.first)} Offset: ${it.second}")
        }
        if(reportedResult.size==0){
            println("Nothing found.")
        }
        println("Exporting full-data...")
        val export = Result(average, mutableListOf())
        rawResult.stream().forEachOrdered {
            export.visits.add(Visit(it.first,it.second.size-average,it.second.size-average>rate, frequencyOfListElements(it.second)))
        }
        val f = File("bsexport-"+SimpleDateFormat("dd-MM-yyyy_hh-mm-ss").format(Date())+".json")
        f.createNewFile()
        f.writeText(Json{ prettyPrint=true }.encodeToString(export))
        println("Saved as ${f.name}")
    }
    fun frequencyOfListElements(items: List<String>): Map<String, Int> {
        val map: MutableMap<String, Int> = HashMap()
        for (temp in items) {
            val count = map[temp]
            map[temp] = if (count == null) 1 else count + 1
        }
        return map
    }
}
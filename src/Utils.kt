import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

fun getFirstDigit(line: String): Int{
    for(c in line) {
        if (c.isDigit()){
            return c.digitToInt()
        }
    }
    return 0
}
fun getLastDigit(line: String): Int{
    return getFirstDigit(line.reversed())
}
fun getCalibrationValue(line: String): Int{
    val firstNumber = getFirstDigit(line)*10
    val lastNumber = getLastDigit(line)
    return firstNumber+lastNumber
}
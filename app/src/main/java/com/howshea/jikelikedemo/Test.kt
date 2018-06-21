/**
 * Created by howshea
 * on 2017/11/21.
 */
fun main(args: Array<String>) {
    sliceText("99", "100")
}

/**
 * 切割字符串，把不需要动画的部分分出来
 */
fun sliceText(text1: String, text2: String) {
    val textFront = text1.takeWhileIndexed { i, c ->
        c == text2[i]
    }
    val textUp = text1.substringAfter(textFront)
    val textBottom = text2.substringAfter(textFront)
    println("textFront: $textFront, textUp: $textUp, textBottom: $textBottom")
}

/**
 * 修改自takeWhile,加上index
 */
inline fun String.takeWhileIndexed(predicate: (index: Int, Char) -> Boolean): String {
    var index = 0
    return (0 until length)
        .firstOrNull { !predicate(index++, get(it)) }
        ?.let { substring(0, it) }
        ?: this
}
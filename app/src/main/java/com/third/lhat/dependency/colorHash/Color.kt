@file:Suppress("unused")
package com.third.lhat.dependency.colorHash

class ColorHash(
    val string: String,
    private val lightness: List<Double> = listOf(0.35, 0.5, 0.65),
    private val saturation: List<Double> =  listOf(0.35, 0.5, 0.65),
    private val minHue: Int = 0,
    private val maxHue: Int = 360
) {
    companion object {
        const val SEED = 131L
        const val SEED2 = 137L
        const val MAX_SAFE_LONG = 65745979961613L
    }

    fun toHSL(): HSL {
        var hash = bkdrHash(string)

        val hue = ((hash % 359) / 1000f) * (maxHue - minHue) + minHue

        hash = (hash / 360)

        val sat = this.saturation[(hash % this.saturation.size).toInt()]

        hash = (hash / saturation.size)

        val light = this.lightness[(hash % this.lightness.size).toInt()]

        return HSL(hue.toDouble(), sat, light)
    }

    fun toRGB() = toHSL().toRGB()
    fun toHexString() = toRGB().toHex()
    fun toColor() = toRGB().toColor()

    private fun bkdrHash(string: String): Long {
        return (string + 'x').fold(0L) { acc, value ->
            when {
                acc > MAX_SAFE_LONG -> acc / SEED2
                else -> acc * SEED + value.code
            }
        }
    }
}
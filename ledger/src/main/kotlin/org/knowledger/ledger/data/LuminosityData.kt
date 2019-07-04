package org.knowledger.ledger.data

import com.squareup.moshi.JsonClass
import org.knowledger.common.config.LedgerConfiguration
import org.knowledger.common.data.LedgerData
import org.knowledger.common.data.SelfInterval
import org.knowledger.common.hash.Hash
import org.knowledger.common.hash.Hasher
import org.knowledger.common.misc.bytes
import java.io.InvalidClassException
import java.math.BigDecimal

/**
 * Luminosity value might be output by an ambient light
 * sensor, using lux units ([LUnit.LUX]) or a lighting unit,
 * outputting a specific amount of lumens ([LUnit.LUMENS]),
 * according to [unit].
 */
@JsonClass(generateAdapter = true)
data class LuminosityData(
    val lum: BigDecimal,
    val unit: LUnit
) : LedgerData {
    override fun digest(c: Hasher): Hash =
        c.applyHash(
            lum.bytes() + unit.ordinal.bytes()
        )


    override fun calculateDiff(
        previous: SelfInterval
    ): BigDecimal =
        when (previous) {
            is LuminosityData -> calculateDiffLum(previous)
            else -> throw InvalidClassException(
                """SelfInterval supplied is:
                    |   ${previous.javaClass.name},
                    |   not ${this::class.java.name}
                """.trimMargin()
            )
        }

    private fun calculateDiffLum(
        previous: LuminosityData
    ): BigDecimal =
        lum.subtract(previous.lum)
            .divide(
                previous.lum,
                LedgerConfiguration.GLOBALCONTEXT
            )


    override fun toString(): String {
        return "LuminosityData(lum = $lum, unit = $unit)"
    }
}
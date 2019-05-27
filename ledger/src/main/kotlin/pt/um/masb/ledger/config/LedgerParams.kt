package pt.um.masb.ledger.config

import com.squareup.moshi.JsonClass
import pt.um.masb.common.hash.AvailableHashAlgorithms
import pt.um.masb.common.hash.Hash
import pt.um.masb.common.hash.Hashable
import pt.um.masb.common.hash.Hasher
import pt.um.masb.common.misc.bytes
import pt.um.masb.common.misc.flattenBytes
import pt.um.masb.common.storage.LedgerContract

@JsonClass(generateAdapter = true)
data class LedgerParams(
    val recalcTime: Long = 1228800000,
    val recalcTrigger: Long = 2048,
    val blockParams: BlockParams = BlockParams(),
    val crypter: Hash = AvailableHashAlgorithms.SHA256Hasher.id
) : Hashable, LedgerContract {

    override fun digest(c: Hasher): Hash =
        c.applyHash(
            flattenBytes(
                crypter.bytes,
                recalcTime.bytes(),
                recalcTrigger.bytes(),
                blockParams.blockMemSize.bytes(),
                blockParams.blockLength.bytes()
            )
        )
}
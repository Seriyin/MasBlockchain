package org.knowledger.ledger.crypto.serial

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.Serializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import org.knowledger.ledger.core.data.DefaultDiff
import org.knowledger.ledger.core.hash.Hash
import org.knowledger.ledger.core.misc.classDigest
import org.knowledger.ledger.crypto.hash.Hashers

@Serializer(forClass = DefaultDiff::class)
object DefaultDataFormulaSerializer : KSerializer<DefaultDiff> {
    override val descriptor: SerialDescriptor =
        StringDescriptor.withName("DefaultDiff")

    override fun deserialize(
        decoder: Decoder
    ): DefaultDiff =
        decoder.decodeSerializableValue(Hash.serializer()).let {
            assert(
                it == DefaultDiff.classDigest(Hashers.SHA3512Hasher)
            )
            DefaultDiff
        }

    override fun serialize(
        encoder: Encoder, obj: DefaultDiff
    ) {
        encoder.encodeSerializableValue(
            Hash.serializer(),
            DefaultDiff.classDigest(Hashers.SHA3512Hasher)
        )
    }
}
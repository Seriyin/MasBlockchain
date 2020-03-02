package org.knowledger.ledger.crypto.serial

import kotlinx.serialization.Decoder
import kotlinx.serialization.Encoder
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialDescriptor
import kotlinx.serialization.internal.ByteArraySerializer
import kotlinx.serialization.internal.StringDescriptor
import kotlinx.serialization.withName
import org.knowledger.ledger.crypto.EncodedPrivateKey

object EncodedPrivateKeyByteSerializer : KSerializer<EncodedPrivateKey> {
    override val descriptor: SerialDescriptor =
        StringDescriptor.withName("EncodedPrivateKey")

    override fun deserialize(decoder: Decoder): EncodedPrivateKey =
        EncodedPrivateKey(decoder.decodeSerializableValue(ByteArraySerializer))

    override fun serialize(encoder: Encoder, obj: EncodedPrivateKey) =
        encoder.encodeSerializableValue(ByteArraySerializer, obj.bytes)
}
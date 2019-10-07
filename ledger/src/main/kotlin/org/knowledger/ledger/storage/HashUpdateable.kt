package org.knowledger.ledger.storage

import kotlinx.serialization.BinaryFormat
import org.knowledger.ledger.core.Sizeable
import org.knowledger.ledger.core.hash.Hasher
import org.knowledger.ledger.core.hash.Hashing
import org.knowledger.ledger.data.Hash

internal interface HashUpdateable : Hashing, Sizeable {
    fun updateHash(
        hasher: Hasher, encoder: BinaryFormat
    )

    fun recalculateHash(
        hasher: Hasher, encoder: BinaryFormat
    ): Hash

    fun recalculateSize(
        hasher: Hasher, encoder: BinaryFormat
    ): Long
}
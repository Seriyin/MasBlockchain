package org.knowledger.ledger.core.hash

interface Hashing {
    /**
     * Hash is a cryptographic digest calculated from
     * fields present in the Hashing instance.
     */
    val hash: Hash
}

package org.knowledger.ledger.storage.merkletree

import org.knowledger.ledger.crypto.Hash
import org.knowledger.ledger.crypto.Hashing
import org.knowledger.ledger.crypto.hash.Hashers
import org.knowledger.ledger.crypto.storage.MerkleTreeFactory
import org.knowledger.ledger.crypto.storage.MerkleTreeFactoryImpl
import org.knowledger.ledger.storage.MerkleTree
import org.knowledger.ledger.storage.MutableMerkleTree

class StorageAwareMerkleTreeFactory(
    private val merkleTreeFactory: MerkleTreeFactory = MerkleTreeFactoryImpl
) : MerkleTreeFactory {
    private fun createSA(
        merkleTree: MutableMerkleTree
    ): StorageAwareMerkleTreeImpl =
        StorageAwareMerkleTreeImpl(merkleTree = merkleTree)

    override fun create(
        hasher: Hashers, collapsedTree: List<Hash>,
        levelIndex: List<Int>
    ): MutableMerkleTree = createSA(
        merkleTreeFactory.create(
            hasher = hasher,
            collapsedTree = collapsedTree,
            levelIndex = levelIndex
        )
    )

    override fun create(
        merkleTree: MerkleTree
    ): MutableMerkleTree = createSA(
        merkleTreeFactory.create(
            merkleTree = merkleTree
        )
    )

    override fun create(
        merkleTree: MutableMerkleTree
    ): MutableMerkleTree = createSA(
        merkleTreeFactory.create(
            merkleTree = merkleTree
        )
    )

    override fun create(
        hasher: Hashers,
        data: Array<out Hashing>
    ): MutableMerkleTree = createSA(
        merkleTreeFactory.create(
            hasher = hasher, data = data
        )
    )

    override fun create(
        hasher: Hashers,
        primary: Hashing,
        data: Array<out Hashing>
    ): MutableMerkleTree = createSA(
        merkleTreeFactory.create(
            hasher = hasher, primary = primary,
            data = data
        )
    )
}
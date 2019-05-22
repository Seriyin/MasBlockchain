package pt.um.masb.ledger.test

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import mu.KLogging
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pt.um.masb.common.data.Difficulty.Companion.MIN_DIFFICULTY
import pt.um.masb.common.database.ManagedDatabase
import pt.um.masb.common.hash.Hash.Companion.emptyHash
import pt.um.masb.ledger.config.BlockParams
import pt.um.masb.ledger.data.PhysicalData
import pt.um.masb.ledger.data.TemperatureData
import pt.um.masb.ledger.data.TrafficFlowData
import pt.um.masb.ledger.data.adapters.TemperatureDataStorageAdapter
import pt.um.masb.ledger.data.adapters.TrafficFlowDataStorageAdapter
import pt.um.masb.ledger.service.ChainHandle
import pt.um.masb.ledger.service.Identity
import pt.um.masb.ledger.service.LedgerService
import pt.um.masb.ledger.storage.Block
import pt.um.masb.ledger.storage.Transaction
import pt.um.masb.ledger.storage.adapters.TransactionStorageAdapter
import pt.um.masb.ledger.test.utils.extractOrFail
import pt.um.masb.ledger.test.utils.makeXTransactions
import pt.um.masb.ledger.test.utils.moshi
import pt.um.masb.ledger.test.utils.testDB
import java.math.BigDecimal

class TestOrientDatabase {
    val database: ManagedDatabase = testDB()

    val ident = Identity("test")

    val testTransactions = makeXTransactions(ident, 10)

    val ledger = LedgerService(database)
        .newLedgerHandle("test")
        .extractOrFail()

    val hash = ledger.ledgerId.hashId

    val trunc = hash.truncated

    val transactionStorageAdapter = TransactionStorageAdapter()

    @BeforeAll
    fun `initialize DB`() {
    }

    @Nested
    inner class TestQuerying {
        val temperatureChain: ChainHandle = ledger.registerNewChainHandleOf(
            TemperatureData::class.java,
            TemperatureDataStorageAdapter()
        ).extractOrFail()

        val trafficChain: ChainHandle = ledger.registerNewChainHandleOf(
            TrafficFlowData::class.java,
            TrafficFlowDataStorageAdapter()
        ).extractOrFail()


        @Nested
        inner class TestBlocks {

            @Test
            fun `Test simple insertion`() {

                val block = Block(
                    hash,
                    emptyHash,
                    MIN_DIFFICULTY,
                    1,
                    BlockParams()
                )
                assertThat(block.addTransaction(testTransactions[0]))
                    .isTrue()
                assertThat(block.data[0])
                    .isNotNull()
                    .isEqualTo(testTransactions[0])
            }


            @Test
            fun `Test traffic insertion`() {
                val testTraffic = Transaction(
                    ident.privateKey,
                    ident.publicKey,
                    PhysicalData(
                        BigDecimal.ONE,
                        BigDecimal.ONE,
                        TrafficFlowData(
                            "FRC0",
                            20,
                            20,
                            3000,
                            3000,
                            34.5,
                            12.6
                        )
                    )
                )

                val block = Block(
                    hash,
                    emptyHash,
                    MIN_DIFFICULTY,
                    1,
                    BlockParams()
                )
                assertThat(block).isNotNull()
                assertThat(block.addTransaction(testTraffic))
                    .isTrue()
                assertThat(block.data[0])
                    .isNotNull()
                    .isEqualTo(testTraffic)
                logger.info {
                    moshi.adapter(Block::class.java).toJson(block)
                }
            }

        }
    }

    fun `close database`() {
        database.close()
    }


    companion object : KLogging()
}
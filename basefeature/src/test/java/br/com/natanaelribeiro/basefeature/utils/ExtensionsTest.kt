package br.com.natanaelribeiro.basefeature.utils

import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ExtensionsTest {

    @Test fun `Format double to price, when it is requested, then return formatted price`() {

        // ARRANGE

        val NUMBER_TO_FORMAT = 29.90

        val EXPECTED_FORMATTED_PRICE = "R$ 29,90"

        // ACT

        val formattedPrice = NUMBER_TO_FORMAT.toFormattedPrice()

        // ASSERT

        assertEquals(EXPECTED_FORMATTED_PRICE, formattedPrice)
    }

    @Test fun `Format long to date, when long value is a timestamp, then return formatted date`() {

        // ARRANGE

        val VALID_TIMESTAMP: Long = 1569781901000

        val EXPECTED_DATE = "29/09/2019"

        // ACT

        val formattedDate = VALID_TIMESTAMP.toFormattedDate("dd/MM/yyyy")

        // ASSERT

        assertEquals(EXPECTED_DATE, formattedDate)
    }

    @Test fun `Check is string is a valid email, when it is a valid email, then return true`()  {

        // ARRANGE

        val VALID_EMAIL = "email@sicredi.com.br"


        // ACT

        val isValidEmail = VALID_EMAIL.isEmail()

        // ASSERT

        assert(isValidEmail)
    }

    @Test fun `Check is string is a valid email, when it is an invalid email, then return false`()  {

        // ARRANGE

        val INVALID_EMAIL = "email@sicredi"


        // ACT

        val isValidEmail = INVALID_EMAIL.isEmail()

        // ASSERT

        assertFalse(isValidEmail)
    }
}
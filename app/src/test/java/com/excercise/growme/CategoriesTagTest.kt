package com.excercise.growme

import com.excercise.growme.constants.Constants
import com.excercise.growme.model.setDefinedTags
import org.junit.Assert.assertEquals
import org.junit.Test

class CategoriesTagTest {

    @Test
    fun `test setDefinedTags maps predefined tags correctly`() {
        val tags = listOf(
            Constants.MENS_CLOTHING,
            Constants.WOMENS_CLOTHING,
            Constants.JEWELERY,
            Constants.ELECTRONICS
        )
        val expected = listOf(
            Constants.MENS_CLOTHING_CAP,
            Constants.WOMENS_CLOTHING_CAP,
            Constants.JEWELERY_CAP,
            Constants.ELECTRONICS
        )

        val result = setDefinedTags(tags)
        assertEquals(expected, result)
    }

    @Test
    fun `test setDefinedTags maps unknown tags to OTHERS_CAP`() {
        val tags = listOf("UNKNOWN", "INVALID", "FOOD")
        val expected = listOf(Constants.OTHERS_CAP, Constants.OTHERS_CAP, Constants.OTHERS_CAP)

        val result = setDefinedTags(tags)
        assertEquals(expected, result)
    }

    @Test
    fun `test setDefinedTags handles mixed known and unknown tags`() {
        val tags = listOf(
            Constants.MENS_CLOTHING,
            "UNKNOWN",
            Constants.WOMENS_CLOTHING,
            "INVALID"
        )
        val expected = listOf(
            Constants.MENS_CLOTHING_CAP,
            Constants.OTHERS_CAP,
            Constants.WOMENS_CLOTHING_CAP,
            Constants.OTHERS_CAP
        )

        val result = setDefinedTags(tags)
        assertEquals(expected, result)
    }

    @Test
    fun `test setDefinedTags handles an empty list`() {
        val tags = emptyList<String>()
        val expected = emptyList<String>()

        val result = setDefinedTags(tags)
        assertEquals(expected, result)
    }

    @Test
    fun `test setDefinedTags handles a single known tag`() {
        val tags = listOf(Constants.JEWELERY)
        val expected = listOf(Constants.JEWELERY_CAP)

        val result = setDefinedTags(tags)
        assertEquals(expected, result)
    }

    @Test
    fun `test setDefinedTags handles a single unknown tag`() {
        val tags = listOf("UNKNOWN")
        val expected = listOf(Constants.OTHERS_CAP)

        val result = setDefinedTags(tags)
        assertEquals(expected, result)
    }
}

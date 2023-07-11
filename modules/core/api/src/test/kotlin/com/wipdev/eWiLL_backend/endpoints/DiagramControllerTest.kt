package com.wipdev.eWiLL_backend.endpoints

import com.fasterxml.jackson.databind.ObjectMapper
import com.wipdev.eWiLL_backend.database.tables.Category
import com.wipdev.eWiLL_backend.endpoints.payload.requests.CategoryPL
import com.wipdev.eWiLL_backend.endpoints.payload.requests.DiagramPL
import com.wipdev.eWiLL_backend.repository.DiagramConfigRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
class DiagramControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var iDiagramController: DiagramController

    @Autowired
    @MockBean
    lateinit var diagramConfigRepository: DiagramConfigRepository

    @Autowired
    @MockBean
    lateinit var categoryController: CategoryController


    @Test
    fun test() {
        /*val ownerId = 1L


        val diagramConfigId = 0L // 0L is the default serm diagram config
        val categoryPL = CategoryPL("TestCategory", 0L)


        val result = mockMvc.perform(post("/api/category")
            .contentType(MediaType.APPLICATION_JSON)
            .content(ObjectMapper().writeValueAsString(categoryPL)))
            .andExpect(status().isOk)
            .andReturn()

        val responseBody = result.response.contentAsByteArray
        val category = ObjectMapper().readValue(responseBody, Category::class.java)
        val categoryId = category.id
        assert(categoryId > 0)


        val pl = DiagramPL(
            null, ownerId, "DiagramNameTest", diagramConfigRepository.getReferenceById(diagramConfigId),
            emptyList(),
            emptyList(), categoryId
        )

        val diaResult =mockMvc.perform(
            post("/api/diagram")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(pl))
        )
            .andExpect(status().isOk)
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").isNumber)
            .andExpect(jsonPath("$.name").value(pl.name))

        assert(diaResult.andReturn().response.contentAsString.toLong() > 0)*/
    }


}

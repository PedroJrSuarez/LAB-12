package com.tecsup.petclinic.webs;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class VisitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("testFindAllVisits — GET /visits → 200 OK y lista JSON")
    void testFindAllVisits() throws Exception {
        mockMvc.perform(get("/visits"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$[0].id", is(1)));
    }

    @Test
    @DisplayName("testFindVisitOK — GET /visits/1 → descripción rabies shot")
    void testFindVisitOK() throws Exception {
        mockMvc.perform(get("/visits/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.description", is("rabies shot")));
    }

    @Test
    @DisplayName("testFindVisitKO — GET /visits/666 → 404 Not Found")
    void testFindVisitKO() throws Exception {
        mockMvc.perform(get("/visits/666"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

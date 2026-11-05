package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.VisitDTO;
import com.tecsup.petclinic.exceptions.VisitNotFoundException;
import com.tecsup.petclinic.services.VisitService;
import com.tecsup.petclinic.util.TObjectCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class VisitControllerMockitoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VisitService visitService;

    @Test
    @DisplayName("testFindAllVisits — GET /visits → 200 OK y lista JSON")
    void testFindAllVisits() throws Exception {
        when(visitService.findAll()).thenReturn(TObjectCreator.getVisitsDTO());

        mockMvc.perform(get("/visits"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].description", is("rabies shot")));
    }

    @Test
    @DisplayName("testFindVisitOK — GET /visits/1 → descripción rabies shot")
    void testFindVisitOK() throws Exception {
        VisitDTO expected = TObjectCreator.getVisitDTO();
        when(visitService.findById(expected.getId())).thenReturn(expected);

        mockMvc.perform(get("/visits/{id}", expected.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(expected.getId())))
                .andExpect(jsonPath("$.description", is(expected.getDescription())));
    }

    @Test
    @DisplayName("testFindVisitKO — GET /visits/666 → 404 Not Found")
    void testFindVisitKO() throws Exception {
        when(visitService.findById(666))
                .thenThrow(new VisitNotFoundException("Visit not found: 666"));

        mockMvc.perform(get("/visits/666"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}

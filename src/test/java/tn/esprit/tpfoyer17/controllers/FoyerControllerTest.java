package tn.esprit.tpfoyer17.controllers;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.services.IFoyerService;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FoyerController.class) // Remplacez par le nom de votre controller
class FoyerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IFoyerService foyerService;

    @Test
    void testGetAllFoyers() throws Exception {
        Foyer f = new Foyer();
        f.setIdFoyer(1L);
        f.setNomFoyer("Foyer Esprit");

        Mockito.when(foyerService.getAllFoyers()).thenReturn(Arrays.asList(f));

        mockMvc.perform(get("/foyer/retrieve-all-foyers")) // Adaptez l'URL selon votre RequestMapping
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0].nomFoyer").value("Foyer Esprit"));
    }
}
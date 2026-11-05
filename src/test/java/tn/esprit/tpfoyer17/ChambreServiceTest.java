package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Chambre;
import tn.esprit.tpfoyer17.repositories.ChambreRepository;
import tn.esprit.tpfoyer17.services.ChambreService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ChambreServiceTest {

    @Mock
    private ChambreRepository chambreRepository;

    @InjectMocks
    private ChambreService chambreService;

    @Test
    void testGetAllChambres() {
        Chambre c = new Chambre();
        Mockito.when(chambreRepository.findAll()).thenReturn(Arrays.asList(c));
        List<Chambre> list = chambreService.getAllChambres();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testAddChambre() {
        Chambre c = new Chambre();
        Mockito.when(chambreRepository.save(Mockito.any(Chambre.class))).thenReturn(c);
        Chambre saved = chambreService.addChambre(c);
        Assertions.assertNotNull(saved);
    }

    @Test
    void testGetChambreById() {
        Chambre c = new Chambre();
        Mockito.when(chambreRepository.findById(1L)).thenReturn(Optional.of(c));
        Chambre found = chambreService.getChambreById(1L);
        Assertions.assertNotNull(found);
    }

    @Test
    void testDeleteChambre() {
        Mockito.doNothing().when(chambreRepository).deleteById(1L);
        chambreService.deleteChambre(1L);
        Mockito.verify(chambreRepository).deleteById(1L);
    }
}
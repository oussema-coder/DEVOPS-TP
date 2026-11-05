package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.services.FoyerServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TpFoyer17ApplicationTests {

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerServiceImpl foyerService;

    @Test
    void testRetrieveAllFoyers() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.findAll()).thenReturn(Arrays.asList(f));
        List<Foyer> list = foyerService.retrieveAllFoyers();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testAddFoyer() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.save(Mockito.any(Foyer.class))).thenReturn(f);
        Foyer saved = foyerService.addFoyer(f);
        Assertions.assertNotNull(saved);
    }

    @Test
    void testUpdateFoyer() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.save(Mockito.any(Foyer.class))).thenReturn(f);
        Foyer updated = foyerService.updateFoyer(f);
        Assertions.assertNotNull(updated);
    }

    @Test
    void testRetrieveFoyer() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.findById(1L)).thenReturn(Optional.of(f));
        Foyer found = foyerService.retrieveFoyer(1L);
        Assertions.assertNotNull(found);
    }

    @Test
    void testRemoveFoyer() {
        Mockito.doNothing().when(foyerRepository).deleteById(1L);
        foyerService.removeFoyer(1L);
        Mockito.verify(foyerRepository).deleteById(1L);
    }
}
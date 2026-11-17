package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;
import tn.esprit.tpfoyer17.services.FoyerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class TpFoyer17ApplicationTests {

    @Mock
    private FoyerRepository foyerRepository;

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private FoyerService foyerService;

    @Test
    void testAddFoyer() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.save(Mockito.any(Foyer.class))).thenReturn(f);
        Foyer saved = foyerService.addFoyer(f);
        Assertions.assertNotNull(saved);
    }

    @Test
    void testGetAllFoyers() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.findAll()).thenReturn(Arrays.asList(f));
        List<Foyer> list = foyerService.getAllFoyers();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testGetFoyerById() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.findById(1L)).thenReturn(Optional.of(f));
        Foyer found = foyerService.getFoyerById(1L);
        Assertions.assertNotNull(found);
    }

    @Test
    void testDeleteFoyer() {
        Mockito.doNothing().when(foyerRepository).deleteById(1L);
        foyerService.deleteFoyer(1L);
        Mockito.verify(foyerRepository).deleteById(1L);
    }

    @Test
    void testUpdateFoyer() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.save(Mockito.any(Foyer.class))).thenReturn(f);
        Foyer updated = foyerService.updateFoyer(f);
        Assertions.assertNotNull(updated);
    }

    @Test
    void testAjouterFoyerEtAffecterAUniversite() {
        Foyer f = new Foyer();
        Universite u = new Universite();
        Mockito.when(foyerRepository.save(Mockito.any(Foyer.class))).thenReturn(f);
        Mockito.when(universiteRepository.findById(1L)).thenReturn(Optional.of(u));
        Mockito.when(universiteRepository.save(Mockito.any(Universite.class))).thenReturn(u);

        Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(f, 1L);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(f, u.getFoyer());
    }
}
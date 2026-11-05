package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Universite;
import tn.esprit.tpfoyer17.repositories.UniversiteRepository;
import tn.esprit.tpfoyer17.services.UniversiteService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class UniversiteServiceTest {

    @Mock
    private UniversiteRepository universiteRepository;

    @InjectMocks
    private UniversiteService universiteService;

    @Test
    void testGetAllUniversites() {
        Universite u = new Universite();
        Mockito.when(universiteRepository.findAll()).thenReturn(Arrays.asList(u));
        List<Universite> list = universiteService.getAllUniversites();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testAddUniversite() {
        Universite u = new Universite();
        Mockito.when(universiteRepository.save(Mockito.any(Universite.class))).thenReturn(u);
        Universite saved = universiteService.addUniversite(u);
        Assertions.assertNotNull(saved);
    }

    @Test
    void testGetUniversiteById() {
        Universite u = new Universite();
        Mockito.when(universiteRepository.findById(1L)).thenReturn(Optional.of(u));
        Universite found = universiteService.getUniversiteById(1L);
        Assertions.assertNotNull(found);
    }

    @Test
    void testDeleteUniversite() {
        Mockito.doNothing().when(universiteRepository).deleteById(1L);
        universiteService.deleteUniversite(1L);
        Mockito.verify(universiteRepository).deleteById(1L);
    }
}
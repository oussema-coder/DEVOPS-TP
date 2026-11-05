package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Etudiant;
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.services.EtudiantService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class EtudiantServiceTest {

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private EtudiantService etudiantService;

    @Test
    void testGetAllEtudiants() {
        Etudiant e = new Etudiant();
        Mockito.when(etudiantRepository.findAll()).thenReturn(Arrays.asList(e));
        List<Etudiant> list = etudiantService.getAllEtudiants();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testAddEtudiant() {
        Etudiant e = new Etudiant();
        Mockito.when(etudiantRepository.save(Mockito.any(Etudiant.class))).thenReturn(e);
        Etudiant saved = etudiantService.addEtudiant(e);
        Assertions.assertNotNull(saved);
    }

    @Test
    void testGetEtudiantById() {
        Etudiant e = new Etudiant();
        Mockito.when(etudiantRepository.findById(1L)).thenReturn(Optional.of(e));
        Etudiant found = etudiantService.getEtudiantById(1L);
        Assertions.assertNotNull(found);
    }

    @Test
    void testDeleteEtudiant() {
        Mockito.doNothing().when(etudiantRepository).deleteById(1L);
        etudiantService.deleteEtudiant(1L);
        Mockito.verify(etudiantRepository).deleteById(1L);
    }
}
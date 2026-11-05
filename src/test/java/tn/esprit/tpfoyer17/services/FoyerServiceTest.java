package tn.esprit.tpfoyer17.services; // ou le package correspondant dans test/java

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Foyer;
import tn.esprit.tpfoyer17.repositories.FoyerRepository;

@ExtendWith(MockitoExtension.class)
class FoyerServiceTest { // <-- La classe commence ici

    @Mock
    private FoyerRepository foyerRepository;

    @InjectMocks
    private FoyerService foyerService;

    @Test
    void testAddFoyer() {
        Foyer f = new Foyer();
        Mockito.when(foyerRepository.save(Mockito.any(Foyer.class))).thenReturn(f);
        Foyer saved = foyerService.addFoyer(f);
        Assertions.assertNotNull(saved);
    }

    // Vos autres méthodes de test ici...

} // <-- La classe se ferme TOUT À LA FIN du fichier par une seule accolade fermante
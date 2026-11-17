package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Bloc;
import tn.esprit.tpfoyer17.repositories.BlocRepository;
import tn.esprit.tpfoyer17.services.BlocService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class BlocServiceTest {

    @Mock
    private BlocRepository blocRepository;

    @InjectMocks
    private BlocService blocService;

    @Test
    void testRetrieveAllBlocs() {
        Bloc b = new Bloc();
        Mockito.when(blocRepository.findAll()).thenReturn(Arrays.asList(b));
        List<Bloc> list = blocService.getAllBlocs();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testAddBloc() {
        Bloc b = new Bloc();
        Mockito.when(blocRepository.save(Mockito.any(Bloc.class))).thenReturn(b);
        Bloc saved = blocService.addBloc(b);
        Assertions.assertNotNull(saved);
    }

    @Test
    void testRetrieveBloc() {
        Bloc b = new Bloc();
        Mockito.when(blocRepository.findById(1L)).thenReturn(Optional.of(b));
        Bloc found = blocService.getBlocById(1L);
        Assertions.assertNotNull(found);
    }

    @Test
    void testRemoveBloc() {
        Mockito.doNothing().when(blocRepository).deleteById(1L);
        blocService.deleteBloc(1L);
        Mockito.verify(blocRepository).deleteById(1L);
    }
}
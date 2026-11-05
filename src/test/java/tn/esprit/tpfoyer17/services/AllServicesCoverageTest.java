package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import tn.esprit.tpfoyer17.entities.*;
import tn.esprit.tpfoyer17.entities.enumerations.TypeChambre;
import tn.esprit.tpfoyer17.repositories.*;
import tn.esprit.tpfoyer17.services.*;

import java.util.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AllServicesCoverageTest {

    @Mock private BlocRepository blocRepository;
    @Mock private ChambreRepository chambreRepository;
    @Mock private EtudiantRepository etudiantRepository;
    @Mock private FoyerRepository foyerRepository;
    @Mock private UniversiteRepository universiteRepository;
    @Mock private ReservationRepository reservationRepository;

    @InjectMocks private BlocService blocService;
    @InjectMocks private ChambreService chambreService;
    @InjectMocks private EtudiantService etudiantService;
    @InjectMocks private FoyerService foyerService;
    @InjectMocks private UniversiteService universiteService;
    @InjectMocks private ReservationService reservationService;

    @Test
    void testBlocServiceFull() {
        Bloc b = new Bloc();
        Chambre c = new Chambre();
        Mockito.when(blocRepository.save(b)).thenReturn(b);
        Mockito.when(blocRepository.findById(1L)).thenReturn(Optional.of(b));
        Mockito.when(chambreRepository.findAllById(Mockito.anyList())).thenReturn(Collections.singletonList(c));

        Assertions.assertNotNull(blocService.updateBloc(b));
        Assertions.assertNotNull(blocService.affecterChambresABloc(Collections.singletonList(1L), 1L));
    }

    @Test
    void testChambreServiceFull() {
        Chambre c = new Chambre();
        Mockito.when(chambreRepository.save(c)).thenReturn(c);
        Mockito.when(chambreRepository.findByBlocFoyerUniversiteNomUniversite("Esprit")).thenReturn(Collections.singletonList(c));
        Mockito.when(chambreRepository.findByBlocIdBlocAndTypeChambre(1L, TypeChambre.SIMPLE)).thenReturn(Collections.singletonList(c));
        Mockito.when(chambreRepository.findByBlocIdBlocAndTypeChambreJPQL(1L, TypeChambre.SIMPLE)).thenReturn(Collections.singletonList(c));
        Mockito.when(chambreRepository.getChambresNonReserveParNomUniversiteEtTypeChambre("Esprit", TypeChambre.SIMPLE)).thenReturn(Collections.singletonList(c));
        Mockito.when(chambreRepository.getChambresNonReserve()).thenReturn(Collections.singletonList(c));

        Assertions.assertNotNull(chambreService.updateChambre(c));
        Assertions.assertNotNull(chambreService.getChambresParNomUniversite("Esprit"));
        Assertions.assertNotNull(chambreService.getChambresParBlocEtTypeKeyWord(1L, TypeChambre.SIMPLE));
        Assertions.assertNotNull(chambreService.getChambresParBlocEtTypeJPQL(1L, TypeChambre.SIMPLE));
        Assertions.assertNotNull(chambreService.getChambresNonReserveParNomUniversiteEtTypeChambre("Esprit", TypeChambre.SIMPLE));
        chambreService.getChambreNonReserver();
    }

    @Test
    void testEtudiantAndFoyerAndUniversiteFull() {
        Etudiant e = new Etudiant();
        Mockito.when(etudiantRepository.save(e)).thenReturn(e);
        Assertions.assertNotNull(etudiantService.updateEtudiant(e));

        Foyer f = new Foyer();
        Universite u = new Universite();
        Mockito.when(foyerRepository.save(f)).thenReturn(f);
        Mockito.when(foyerRepository.findById(1L)).thenReturn(Optional.of(f));
        Mockito.when(universiteRepository.findById(1L)).thenReturn(Optional.of(u));
        Mockito.when(universiteRepository.findByNomUniversite("Esprit")).thenReturn(u);
        Mockito.when(universiteRepository.save(u)).thenReturn(u);

        Assertions.assertNotNull(foyerService.updateFoyer(f));
        Assertions.assertNotNull(foyerService.ajouterFoyerEtAffecterAUniversite(f, 1L));
        Assertions.assertNotNull(universiteService.updateUniversite(u));
        Assertions.assertNotNull(universiteService.affecterFoyerAUniversite(1L, "Esprit"));
        Assertions.assertNotNull(universiteService.desaffecterFoyerAUniversite(1L));
    }

    @Test
    void testReservationServiceFull() {
        Reservation res = new Reservation();
        res.setIdReservation("res1");
        Set<Etudiant> etudiants = new HashSet<>();
        etudiants.add(new Etudiant());
        etudiants.add(new Etudiant());
        res.setEtudiants(etudiants);

        Etudiant e = new Etudiant();
        Chambre cTriple = new Chambre();
        cTriple.setTypeChambre(TypeChambre.TRIPLE);

        Mockito.when(reservationRepository.findForReservation(1L)).thenReturn(res);
        Mockito.when(etudiantRepository.findByCinEtudiant(123L)).thenReturn(e);
        Mockito.when(chambreRepository.findByReservationsIdReservation("res1")).thenReturn(cTriple);
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(res);

        // Test branche TRIPLE (3 étudiants -> estValide = false)
        Assertions.assertNotNull(reservationService.ajouterReservation(1L, 123L));

        // Test branche DOUBLE
        Chambre cDouble = new Chambre();
        cDouble.setTypeChambre(TypeChambre.DOUBLE);
        Mockito.when(chambreRepository.findByReservationsIdReservation("res1")).thenReturn(cDouble);
        Assertions.assertNotNull(reservationService.ajouterReservation(1L, 123L));

        // Test annulerReservation
        Mockito.when(reservationRepository.findByEtudiantsCinEtudiantAndAnneeUniversitaire(Mockito.anyLong(), Mockito.anyInt())).thenReturn(res);
        Assertions.assertNotNull(reservationService.annulerReservation(123L));

        // Test branche else (attrape le ClassCastException du code source original)
        Mockito.when(reservationRepository.findForReservation(2L)).thenReturn(null);
        Assertions.assertThrows(ClassCastException.class, () -> reservationService.ajouterReservation(2L, 123L));
    }
}
package tn.esprit.tpfoyer17.controllers;

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
import tn.esprit.tpfoyer17.repositories.EtudiantRepository;
import tn.esprit.tpfoyer17.services.*;

import java.util.Collections;
import java.util.Date;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class FoyerControllerTest {

    @Mock private IBlocService blocService;
    @InjectMocks private BlocController blocController;

    @Mock private IChambreService chambreService;
    @InjectMocks private ChambreController chambreController;

    @Mock private IEtudiantService etudiantService;
    @Mock private EtudiantRepository etudiantRepository;
    @InjectMocks private EtudiantController etudiantController;

    @Mock private IFoyerService foyerService;
    @InjectMocks private FoyerController foyerController;

    @Mock private IReservationService reservationService;
    @InjectMocks private ReservationController reservationController;

    @Mock private IUniversiteService universiteService;
    @InjectMocks private UniversiteController universiteController;

    @Test
    void testBlocController() {
        Bloc b = new Bloc();
        Mockito.when(blocService.addBloc(b)).thenReturn(b);
        Mockito.when(blocService.getAllBlocs()).thenReturn(Collections.singletonList(b));
        Mockito.when(blocService.getBlocById(1L)).thenReturn(b);
        Mockito.when(blocService.updateBloc(b)).thenReturn(b);
        Mockito.when(blocService.affecterChambresABloc(Collections.singletonList(1L), 1L)).thenReturn(b);

        Assertions.assertNotNull(blocController.addingBloc(b));
        Assertions.assertEquals(1, blocController.gettingAllBloc().size());
        Assertions.assertNotNull(blocController.gettingBloc(1L));
        blocController.deletingBloc(1L);
        Assertions.assertNotNull(blocController.updatingBloc(b));
        Assertions.assertNotNull(blocController.affecterChambresABloc(Collections.singletonList(1L), 1L));
    }

    @Test
    void testChambreController() {
        Chambre c = new Chambre();
        Mockito.when(chambreService.addChambre(c)).thenReturn(c);
        Mockito.when(chambreService.getAllChambres()).thenReturn(Collections.singletonList(c));
        Mockito.when(chambreService.getChambreById(1L)).thenReturn(c);
        Mockito.when(chambreService.updateChambre(c)).thenReturn(c);
        Mockito.when(chambreService.getChambresParNomUniversite("Esprit")).thenReturn(Collections.singletonList(c));
        Mockito.when(chambreService.getChambresParBlocEtTypeJPQL(1L, TypeChambre.SIMPLE)).thenReturn(Collections.singletonList(c));
        Mockito.when(chambreService.getChambresNonReserveParNomUniversiteEtTypeChambre("Esprit", TypeChambre.SIMPLE)).thenReturn(Collections.singletonList(c));

        Assertions.assertNotNull(chambreController.addingChambre(c));
        Assertions.assertEquals(1, chambreController.gettingAllChambre().size());
        Assertions.assertNotNull(chambreController.gettingChambre(1L));
        chambreController.deletingChambre(1L);
        Assertions.assertNotNull(chambreController.updatingChambre(c));
        Assertions.assertNotNull(chambreController.getChambresParNomUniversite("Esprit"));
        Assertions.assertNotNull(chambreController.getChambresParBlocEtTypeJPQL(1L, TypeChambre.SIMPLE));
        Assertions.assertNotNull(chambreController.getChambresNonReserveParNomUniversiteEtTypeChambre("Esprit", TypeChambre.SIMPLE));
    }

    @Test
    void testEtudiantController() {
        Etudiant e = new Etudiant();
        Mockito.when(etudiantRepository.save(e)).thenReturn(e);
        Mockito.when(etudiantService.getAllEtudiants()).thenReturn(Collections.singletonList(e));
        Mockito.when(etudiantService.getEtudiantById(1L)).thenReturn(e);
        Mockito.when(etudiantService.updateEtudiant(e)).thenReturn(e);

        Assertions.assertNotNull(etudiantController.addEtudiant(e));
        Assertions.assertEquals(1, etudiantController.gettingAllEtudiant().size());
        Assertions.assertNotNull(etudiantController.gettingEtudiant(1L));
        etudiantController.deletingEtudiant(1L);
        Assertions.assertNotNull(etudiantController.updatingEtudiant(e));
    }

    @Test
    void testFoyerController() {
        Foyer f = new Foyer();
        Mockito.when(foyerService.addFoyer(f)).thenReturn(f);
        Mockito.when(foyerService.getAllFoyers()).thenReturn(Collections.singletonList(f));
        Mockito.when(foyerService.getFoyerById(1L)).thenReturn(f);
        Mockito.when(foyerService.updateFoyer(f)).thenReturn(f);
        Mockito.when(foyerService.ajouterFoyerEtAffecterAUniversite(f, 1L)).thenReturn(f);

        Assertions.assertNotNull(foyerController.addingFoyer(f));
        Assertions.assertEquals(1, foyerController.gettingAllFoyer().size());
        Assertions.assertNotNull(foyerController.gettingFoyer(1L));
        foyerController.deletingFoyer(1L);
        Assertions.assertNotNull(foyerController.updatingFoyer(f));
        Assertions.assertNotNull(foyerController.ajouterFoyerEtAffecterAUniversite(f, 1L));
    }

    @Test
    void testReservationController() {
        Reservation r = new Reservation();
        Date d = new Date();
        Mockito.when(reservationService.getAllReservations()).thenReturn(Collections.singletonList(r));
        Mockito.when(reservationService.getReservationById("1")).thenReturn(r);
        Mockito.when(reservationService.updateReservation(r)).thenReturn(r);
        Mockito.when(reservationService.ajouterReservation(1L, 123L)).thenReturn(r);
        Mockito.when(reservationService.annulerReservation(123L)).thenReturn(r);
        Mockito.when(reservationService.getReservationParAnneeUniversitaireEtNomUniversite(d, "Esprit")).thenReturn(Collections.singletonList(r));

        Assertions.assertEquals(1, reservationController.gettingAllReservation().size());
        Assertions.assertNotNull(reservationController.gettingReservation("1"));
        reservationController.deletingReservation("1");
        Assertions.assertNotNull(reservationController.updatingReservation(r));
        Assertions.assertNotNull(reservationController.ajouterReservation(1L, 123L));
        Assertions.assertNotNull(reservationController.annulerReservation(123L));
        Assertions.assertNotNull(reservationController.getReservationParAnneeUniversitaireEtNomUniversite(d, "Esprit"));
    }

    @Test
    void testUniversiteController() {
        Universite u = new Universite();
        Mockito.when(universiteService.addUniversite(u)).thenReturn(u);
        Mockito.when(universiteService.getAllUniversites()).thenReturn(Collections.singletonList(u));
        Mockito.when(universiteService.getUniversiteById(1L)).thenReturn(u);
        Mockito.when(universiteService.updateUniversite(u)).thenReturn(u);
        Mockito.when(universiteService.affecterFoyerAUniversite(1L, "Esprit")).thenReturn(u);
        Mockito.when(universiteService.desaffecterFoyerAUniversite(1L)).thenReturn(u);

        Assertions.assertNotNull(universiteController.addingUniversite(u));
        Assertions.assertEquals(1, universiteController.gettingAllUniversite().size());
        Assertions.assertNotNull(universiteController.gettingUniversite(1L));
        universiteController.deletingUniversite(1L);
        Assertions.assertNotNull(universiteController.updatingUniversite(u));
        Assertions.assertNotNull(universiteController.affecterFoyerAUniversite(1L, "Esprit"));
        Assertions.assertNotNull(universiteController.desaffecterFoyerAUniversite(1L));
    }
}
package tn.esprit.tpfoyer17;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import tn.esprit.tpfoyer17.entities.Reservation;
import tn.esprit.tpfoyer17.repositories.ReservationRepository;
import tn.esprit.tpfoyer17.services.ReservationService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void testGetAllReservations() {
        Reservation r = new Reservation();
        Mockito.when(reservationRepository.findAll()).thenReturn(Arrays.asList(r));
        List<Reservation> list = reservationService.getAllReservations();
        Assertions.assertEquals(1, list.size());
    }

    @Test
    void testAddReservation() {
        Reservation r = new Reservation();
        Mockito.when(reservationRepository.save(Mockito.any(Reservation.class))).thenReturn(r);
        Reservation saved = reservationService.addReservation(r);
        Assertions.assertNotNull(saved);
    }

    @Test
    void testGetReservationById() {
        Reservation r = new Reservation();
        Mockito.when(reservationRepository.findById("1")).thenReturn(Optional.of(r));
        Reservation found = reservationService.getReservationById("1");
        Assertions.assertNotNull(found);
    }

    @Test
    void testDeleteReservation() {
        Mockito.doNothing().when(reservationRepository).deleteById("1");
        reservationService.deleteReservation("1");
        Mockito.verify(reservationRepository).deleteById("1");
    }
}
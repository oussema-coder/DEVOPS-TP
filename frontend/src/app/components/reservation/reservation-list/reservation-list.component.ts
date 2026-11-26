import { Component, OnInit } from '@angular/core';
import { ReservationService } from '../../../services/reservation.service';
import { EtudiantService }    from '../../../services/etudiant.service';
import { BlocService }        from '../../../services/bloc.service';
import { Reservation, Etudiant, Bloc } from '../../../models/models';

@Component({
  selector: 'app-reservation-list',
  templateUrl: './reservation-list.component.html',
  styleUrls: ['./reservation-list.component.css']
})
export class ReservationListComponent implements OnInit {
  items: Reservation[] = [];
  loading = true;
  error   = '';
  success = '';

  // For new reservation form
  showNewModal = false;
  blocs: Bloc[] = [];
  etudiants: Etudiant[] = [];
  newIdBloc      = 0;
  newCinEtudiant = 0;

  // For cancel form
  showCancelModal = false;
  cancelCin       = 0;

  constructor(
    private svc: ReservationService,
    private etudiantSvc: EtudiantService,
    private blocSvc: BlocService,
  ) {}

  ngOnInit(): void {
    this.load();
    this.blocSvc.getAll().subscribe({ next: data => this.blocs = data });
    this.etudiantSvc.getAll().subscribe({ next: data => this.etudiants = data });
  }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({
      next:  data => { this.items = data; this.loading = false; },
      error: ()   => { this.error = 'Erreur de chargement.'; this.loading = false; }
    });
  }

  openNew(): void  { this.newIdBloc = 0; this.newCinEtudiant = 0; this.showNewModal = true; }
  closeNew(): void { this.showNewModal = false; }

  submitNew(): void {
    if (!this.newIdBloc || !this.newCinEtudiant) return;
    this.svc.ajouterReservation(this.newIdBloc, this.newCinEtudiant).subscribe({
      next: () => {
        this.success = 'Réservation créée!';
        this.showNewModal = false;
        this.load();
        setTimeout(() => this.success = '', 3000);
      },
      error: () => { this.error = 'Erreur lors de la réservation.'; }
    });
  }

  openCancel(): void   { this.cancelCin = 0; this.showCancelModal = true; }
  closeCancel(): void  { this.showCancelModal = false; }

  submitCancel(): void {
    if (!this.cancelCin) return;
    this.svc.annulerReservation(this.cancelCin).subscribe({
      next: () => {
        this.success = 'Réservation annulée!';
        this.showCancelModal = false;
        this.load();
        setTimeout(() => this.success = '', 3000);
      },
      error: () => { this.error = 'Erreur lors de l\'annulation.'; }
    });
  }

  delete(id: string): void {
    if (!confirm('Supprimer cette réservation?')) return;
    this.svc.delete(id).subscribe({
      next: () => { this.success = 'Réservation supprimée!'; this.load(); setTimeout(() => this.success = '', 3000); },
      error: () => { this.error = 'Erreur lors de la suppression.'; }
    });
  }

  formatDate(d: string): string {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('fr-TN');
  }
}

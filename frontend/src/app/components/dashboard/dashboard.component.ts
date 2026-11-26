import { Component, OnInit } from '@angular/core';
import { forkJoin } from 'rxjs';
import { UniversiteService } from '../../services/universite.service';
import { FoyerService }      from '../../services/foyer.service';
import { BlocService }       from '../../services/bloc.service';
import { ChambreService }    from '../../services/chambre.service';
import { EtudiantService }   from '../../services/etudiant.service';
import { ReservationService } from '../../services/reservation.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  stats = [
    { label: 'Universités',   icon: '🎓', count: 0, route: '/universites',  color: '#2dd4bf' },
    { label: 'Foyers',        icon: '🏢', count: 0, route: '/foyers',       color: '#818cf8' },
    { label: 'Blocs',         icon: '🧱', count: 0, route: '/blocs',        color: '#fb923c' },
    { label: 'Chambres',      icon: '🛏️', count: 0, route: '/chambres',     color: '#34d399' },
    { label: 'Étudiants',     icon: '👤', count: 0, route: '/etudiants',    color: '#f472b6' },
    { label: 'Réservations',  icon: '📋', count: 0, route: '/reservations', color: '#fbbf24' },
  ];

  loading = true;
  error   = '';

  constructor(
    private universiteService: UniversiteService,
    private foyerService: FoyerService,
    private blocService: BlocService,
    private chambreService: ChambreService,
    private etudiantService: EtudiantService,
    private reservationService: ReservationService,
  ) {}

  ngOnInit(): void {
    forkJoin([
      this.universiteService.getAll(),
      this.foyerService.getAll(),
      this.blocService.getAll(),
      this.chambreService.getAll(),
      this.etudiantService.getAll(),
      this.reservationService.getAll(),
    ]).subscribe({
      next: ([universites, foyers, blocs, chambres, etudiants, reservations]) => {
        this.stats[0].count = universites.length;
        this.stats[1].count = foyers.length;
        this.stats[2].count = blocs.length;
        this.stats[3].count = chambres.length;
        this.stats[4].count = etudiants.length;
        this.stats[5].count = reservations.length;
        this.loading = false;
      },
      error: err => {
        this.error = 'Impossible de charger les statistiques. Vérifiez la connexion au backend.';
        this.loading = false;
        console.error(err);
      }
    });
  }
}

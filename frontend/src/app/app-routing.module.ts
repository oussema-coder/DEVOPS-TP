import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UniversiteListComponent } from './components/universite/universite-list/universite-list.component';
import { FoyerListComponent }      from './components/foyer/foyer-list/foyer-list.component';
import { BlocListComponent }       from './components/bloc/bloc-list/bloc-list.component';
import { ChambreListComponent }    from './components/chambre/chambre-list/chambre-list.component';
import { EtudiantListComponent }   from './components/etudiant/etudiant-list/etudiant-list.component';
import { ReservationListComponent } from './components/reservation/reservation-list/reservation-list.component';
import { DashboardComponent }      from './components/dashboard/dashboard.component';

const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'dashboard',    component: DashboardComponent },
  { path: 'universites',  component: UniversiteListComponent },
  { path: 'foyers',       component: FoyerListComponent },
  { path: 'blocs',        component: BlocListComponent },
  { path: 'chambres',     component: ChambreListComponent },
  { path: 'etudiants',    component: EtudiantListComponent },
  { path: 'reservations', component: ReservationListComponent },
  { path: '**', redirectTo: 'dashboard' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {}

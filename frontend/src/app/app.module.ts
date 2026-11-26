import { NgModule }             from '@angular/core';
import { BrowserModule }        from '@angular/platform-browser';
import { HttpClientModule }     from '@angular/common/http';
import { FormsModule }          from '@angular/forms';
import { CommonModule }         from '@angular/common';

import { AppRoutingModule }     from './app-routing.module';
import { AppComponent }         from './app.component';
import { NavbarComponent }      from './components/navbar/navbar.component';
import { DashboardComponent }   from './components/dashboard/dashboard.component';

import { UniversiteListComponent } from './components/universite/universite-list/universite-list.component';
import { FoyerListComponent }      from './components/foyer/foyer-list/foyer-list.component';
import { BlocListComponent }       from './components/bloc/bloc-list/bloc-list.component';
import { ChambreListComponent }    from './components/chambre/chambre-list/chambre-list.component';
import { EtudiantListComponent }   from './components/etudiant/etudiant-list/etudiant-list.component';
import { ReservationListComponent } from './components/reservation/reservation-list/reservation-list.component';

@NgModule({
  declarations: [
    AppComponent,
    NavbarComponent,
    DashboardComponent,
    UniversiteListComponent,
    FoyerListComponent,
    BlocListComponent,
    ChambreListComponent,
    EtudiantListComponent,
    ReservationListComponent,
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    CommonModule,
    AppRoutingModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}

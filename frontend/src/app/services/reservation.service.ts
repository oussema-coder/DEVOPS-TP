import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reservation } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ReservationService {
  private base = '/api/reservations';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Reservation[]> {
    return this.http.get<Reservation[]>(`${this.base}/getAll`);
  }

  getById(id: string): Observable<Reservation> {
    return this.http.get<Reservation>(`${this.base}/get`, { params: { idReservation: id } });
  }

  update(r: Reservation): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.base}/update`, r);
  }

  delete(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/delete/${id}`);
  }

  ajouterReservation(idBloc: number, cinEtudiant: number): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.base}/ajouter-reservation`, null, {
      params: { idBloc, cinEtudiant }
    });
  }

  annulerReservation(cinEtudiant: number): Observable<Reservation> {
    return this.http.put<Reservation>(`${this.base}/annuler-reservation`, null, {
      params: { cinEtudiant }
    });
  }
}

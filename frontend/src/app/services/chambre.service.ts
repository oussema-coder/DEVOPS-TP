import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Chambre } from '../models/models';

@Injectable({ providedIn: 'root' })
export class ChambreService {
  private base = '/api/chambres';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Chambre[]> {
    return this.http.get<Chambre[]>(`${this.base}/getAll`);
  }

  getById(id: number): Observable<Chambre> {
    return this.http.get<Chambre>(`${this.base}/get`, { params: { idChambre: id } });
  }

  add(c: Chambre): Observable<Chambre> {
    return this.http.post<Chambre>(`${this.base}/add`, c);
  }

  update(c: Chambre): Observable<Chambre> {
    return this.http.put<Chambre>(`${this.base}/update`, c);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/delete/${id}`);
  }

  getByUniversite(nomUniversite: string): Observable<Chambre[]> {
    return this.http.get<Chambre[]>(`${this.base}/get-par-nom-universite`, {
      params: { nomUniversite }
    });
  }

  getByBlocEtType(idBloc: number, typeC: string): Observable<Chambre[]> {
    return this.http.get<Chambre[]>(`${this.base}/get-par-bloc-type-jpql`, {
      params: { idBloc, typeC }
    });
  }

  getNonReservees(nomUniversite: string, type: string): Observable<Chambre[]> {
    return this.http.get<Chambre[]>(`${this.base}/get-non-reserve-par-nom-universite-typechambre`, {
      params: { nomUniversite, type }
    });
  }
}

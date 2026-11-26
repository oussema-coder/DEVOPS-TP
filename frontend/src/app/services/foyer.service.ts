import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Foyer } from '../models/models';

@Injectable({ providedIn: 'root' })
export class FoyerService {
  private base = '/api/foyers';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Foyer[]> {
    return this.http.get<Foyer[]>(`${this.base}/getAll`);
  }

  getById(id: number): Observable<Foyer> {
    return this.http.get<Foyer>(`${this.base}/get`, { params: { idFoyer: id } });
  }

  add(f: Foyer): Observable<Foyer> {
    return this.http.post<Foyer>(`${this.base}/add`, f);
  }

  update(f: Foyer): Observable<Foyer> {
    return this.http.put<Foyer>(`${this.base}/update`, f);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/delete/${id}`);
  }

  ajouterAffecter(f: Foyer, idUniversite: number): Observable<Foyer> {
    return this.http.post<Foyer>(`${this.base}/ajouter-affecter`, f, {
      params: { idUniversite }
    });
  }
}

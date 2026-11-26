import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Universite } from '../models/models';

@Injectable({ providedIn: 'root' })
export class UniversiteService {
  private base = '/api/univeristes';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Universite[]> {
    return this.http.get<Universite[]>(`${this.base}/getAll`);
  }

  getById(id: number): Observable<Universite> {
    return this.http.get<Universite>(`${this.base}/get`, { params: { idUniversite: id } });
  }

  add(u: Universite): Observable<Universite> {
    return this.http.post<Universite>(`${this.base}/add`, u);
  }

  update(u: Universite): Observable<Universite> {
    return this.http.put<Universite>(`${this.base}/update`, u);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/delete/${id}`);
  }

  affecterFoyer(idFoyer: number, nomUniversite: string): Observable<Universite> {
    return this.http.put<Universite>(`${this.base}/affecter-foyer-universite`, null, {
      params: { idFoyer, nomUniversite }
    });
  }

  desaffecterFoyer(idUniversite: number): Observable<Universite> {
    return this.http.put<Universite>(`${this.base}/desaffecter-foyer-universite`, null, {
      params: { idUniversite }
    });
  }
}

import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Etudiant } from '../models/models';

@Injectable({ providedIn: 'root' })
export class EtudiantService {
  private base = '/api/etudiants';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Etudiant[]> {
    return this.http.get<Etudiant[]>(`${this.base}/getAll`);
  }

  getById(id: number): Observable<Etudiant> {
    return this.http.get<Etudiant>(`${this.base}/get`, { params: { idEtudiant: id } });
  }

  add(e: Etudiant): Observable<Etudiant> {
    return this.http.post<Etudiant>(`${this.base}/add`, e);
  }

  update(e: Etudiant): Observable<Etudiant> {
    return this.http.put<Etudiant>(`${this.base}/update`, e);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/delete/${id}`);
  }
}

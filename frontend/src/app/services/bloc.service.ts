import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Bloc } from '../models/models';

@Injectable({ providedIn: 'root' })
export class BlocService {
  private base = '/api/blocs';

  constructor(private http: HttpClient) {}

  getAll(): Observable<Bloc[]> {
    return this.http.get<Bloc[]>(`${this.base}/getAll`);
  }

  getById(id: number): Observable<Bloc> {
    return this.http.get<Bloc>(`${this.base}/get`, { params: { idBloc: id } });
  }

  add(b: Bloc): Observable<Bloc> {
    return this.http.post<Bloc>(`${this.base}/add`, b);
  }

  update(b: Bloc): Observable<Bloc> {
    return this.http.put<Bloc>(`${this.base}/update`, b);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.base}/delete/${id}`);
  }

  affecterChambres(numChambres: number[], idBloc: number): Observable<Bloc> {
    return this.http.put<Bloc>(`${this.base}/affecter-chambre-bloc`, numChambres, {
      params: { idBloc }
    });
  }
}

import { Component, OnInit } from '@angular/core';
import { BlocService } from '../../../services/bloc.service';
import { Bloc } from '../../../models/models';

@Component({
  selector: 'app-bloc-list',
  templateUrl: './bloc-list.component.html',
  styleUrls: ['./bloc-list.component.css']
})
export class BlocListComponent implements OnInit {
  items: Bloc[] = [];
  loading = true;
  error   = '';
  success = '';

  showModal = false;
  editMode  = false;
  current: Bloc = { nomBloc: '', capaciteBloc: 0 };

  constructor(private svc: BlocService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({
      next:  data => { this.items = data; this.loading = false; },
      error: ()   => { this.error = 'Erreur de chargement.'; this.loading = false; }
    });
  }

  openAdd(): void {
    this.current   = { nomBloc: '', capaciteBloc: 0 };
    this.editMode  = false;
    this.showModal = true;
  }

  openEdit(item: Bloc): void {
    this.current   = { ...item };
    this.editMode  = true;
    this.showModal = true;
  }

  save(): void {
    const obs = this.editMode ? this.svc.update(this.current) : this.svc.add(this.current);
    obs.subscribe({
      next: () => {
        this.success  = this.editMode ? 'Bloc mis à jour!' : 'Bloc ajouté!';
        this.showModal = false;
        this.load();
        setTimeout(() => this.success = '', 3000);
      },
      error: () => { this.error = 'Erreur lors de la sauvegarde.'; }
    });
  }

  delete(id: number): void {
    if (!confirm('Supprimer ce bloc?')) return;
    this.svc.delete(id).subscribe({
      next: () => { this.success = 'Bloc supprimé!'; this.load(); setTimeout(() => this.success = '', 3000); },
      error: () => { this.error = 'Erreur lors de la suppression.'; }
    });
  }

  close(): void { this.showModal = false; }
}

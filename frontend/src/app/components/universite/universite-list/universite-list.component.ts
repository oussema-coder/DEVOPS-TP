import { Component, OnInit } from '@angular/core';
import { UniversiteService } from '../../../services/universite.service';
import { Universite } from '../../../models/models';

@Component({
  selector: 'app-universite-list',
  templateUrl: './universite-list.component.html',
  styleUrls: ['./universite-list.component.css']
})
export class UniversiteListComponent implements OnInit {
  items: Universite[] = [];
  loading = true;
  error   = '';
  success = '';

  showModal = false;
  editMode  = false;
  current: Universite = { nomUniversite: '', adresse: '' };

  constructor(private svc: UniversiteService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.error = '';
    this.svc.getAll().subscribe({
      next:  data => { this.items = data; this.loading = false; },
      error: err  => { this.error = 'Erreur de chargement.'; this.loading = false; console.error(err); }
    });
  }

  openAdd(): void {
    this.current  = { nomUniversite: '', adresse: '' };
    this.editMode = false;
    this.showModal = true;
  }

  openEdit(item: Universite): void {
    this.current   = { ...item };
    this.editMode  = true;
    this.showModal = true;
  }

  save(): void {
    const obs = this.editMode
      ? this.svc.update(this.current)
      : this.svc.add(this.current);

    obs.subscribe({
      next: () => {
        this.success  = this.editMode ? 'Université mise à jour!' : 'Université ajoutée!';
        this.showModal = false;
        this.load();
        setTimeout(() => this.success = '', 3000);
      },
      error: err => { this.error = 'Erreur lors de la sauvegarde.'; console.error(err); }
    });
  }

  delete(id: number): void {
    if (!confirm('Supprimer cette université?')) return;
    this.svc.delete(id).subscribe({
      next: () => {
        this.success = 'Université supprimée!';
        this.load();
        setTimeout(() => this.success = '', 3000);
      },
      error: err => { this.error = 'Erreur lors de la suppression.'; console.error(err); }
    });
  }

  close(): void { this.showModal = false; }
}

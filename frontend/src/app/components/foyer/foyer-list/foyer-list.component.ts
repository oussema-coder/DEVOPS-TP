import { Component, OnInit } from '@angular/core';
import { FoyerService } from '../../../services/foyer.service';
import { Foyer } from '../../../models/models';

@Component({
  selector: 'app-foyer-list',
  templateUrl: './foyer-list.component.html',
  styleUrls: ['./foyer-list.component.css']
})
export class FoyerListComponent implements OnInit {
  items: Foyer[] = [];
  loading = true;
  error   = '';
  success = '';

  showModal = false;
  editMode  = false;
  current: Foyer = { nomFoyer: '', capaciteFoyer: 0 };

  constructor(private svc: FoyerService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({
      next:  data => { this.items = data; this.loading = false; },
      error: err  => { this.error = 'Erreur de chargement.'; this.loading = false; }
    });
  }

  openAdd(): void {
    this.current   = { nomFoyer: '', capaciteFoyer: 0 };
    this.editMode  = false;
    this.showModal = true;
  }

  openEdit(item: Foyer): void {
    this.current   = { ...item };
    this.editMode  = true;
    this.showModal = true;
  }

  save(): void {
    const obs = this.editMode ? this.svc.update(this.current) : this.svc.add(this.current);
    obs.subscribe({
      next: () => {
        this.success  = this.editMode ? 'Foyer mis à jour!' : 'Foyer ajouté!';
        this.showModal = false;
        this.load();
        setTimeout(() => this.success = '', 3000);
      },
      error: () => { this.error = 'Erreur lors de la sauvegarde.'; }
    });
  }

  delete(id: number): void {
    if (!confirm('Supprimer ce foyer?')) return;
    this.svc.delete(id).subscribe({
      next: () => { this.success = 'Foyer supprimé!'; this.load(); setTimeout(() => this.success = '', 3000); },
      error: () => { this.error = 'Erreur lors de la suppression.'; }
    });
  }

  close(): void { this.showModal = false; }
}

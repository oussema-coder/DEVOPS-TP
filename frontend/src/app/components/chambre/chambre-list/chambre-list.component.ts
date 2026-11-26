import { Component, OnInit } from '@angular/core';
import { ChambreService } from '../../../services/chambre.service';
import { Chambre } from '../../../models/models';

@Component({
  selector: 'app-chambre-list',
  templateUrl: './chambre-list.component.html',
  styleUrls: ['./chambre-list.component.css']
})
export class ChambreListComponent implements OnInit {
  items: Chambre[] = [];
  loading = true;
  error   = '';
  success = '';

  showModal = false;
  editMode  = false;
  current: Chambre = { numeroChambre: 0, typeChambre: 'SIMPLE' };

  typeOptions: Array<'SIMPLE' | 'DOUBLE' | 'TRIPLE'> = ['SIMPLE', 'DOUBLE', 'TRIPLE'];

  typeBadgeClass(t: string): string {
    if (t === 'SIMPLE')  return 'badge-success';
    if (t === 'DOUBLE')  return 'badge-warning';
    if (t === 'TRIPLE')  return 'badge-danger';
    return '';
  }

  constructor(private svc: ChambreService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({
      next:  data => { this.items = data; this.loading = false; },
      error: ()   => { this.error = 'Erreur de chargement.'; this.loading = false; }
    });
  }

  openAdd(): void {
    this.current   = { numeroChambre: 0, typeChambre: 'SIMPLE' };
    this.editMode  = false;
    this.showModal = true;
  }

  openEdit(item: Chambre): void {
    this.current   = { ...item };
    this.editMode  = true;
    this.showModal = true;
  }

  save(): void {
    const obs = this.editMode ? this.svc.update(this.current) : this.svc.add(this.current);
    obs.subscribe({
      next: () => {
        this.success  = this.editMode ? 'Chambre mise à jour!' : 'Chambre ajoutée!';
        this.showModal = false;
        this.load();
        setTimeout(() => this.success = '', 3000);
      },
      error: () => { this.error = 'Erreur lors de la sauvegarde.'; }
    });
  }

  delete(id: number): void {
    if (!confirm('Supprimer cette chambre?')) return;
    this.svc.delete(id).subscribe({
      next: () => { this.success = 'Chambre supprimée!'; this.load(); setTimeout(() => this.success = '', 3000); },
      error: () => { this.error = 'Erreur lors de la suppression.'; }
    });
  }

  close(): void { this.showModal = false; }
}

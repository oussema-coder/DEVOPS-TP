import { Component, OnInit } from '@angular/core';
import { EtudiantService } from '../../../services/etudiant.service';
import { Etudiant } from '../../../models/models';

@Component({
  selector: 'app-etudiant-list',
  templateUrl: './etudiant-list.component.html',
  styleUrls: ['./etudiant-list.component.css']
})
export class EtudiantListComponent implements OnInit {
  items: Etudiant[] = [];
  loading = true;
  error   = '';
  success = '';

  showModal = false;
  editMode  = false;
  current: Etudiant = { nomEtudiant: '', prenomEtudiant: '', cinEtudiant: 0, dateNaissance: '' };

  constructor(private svc: EtudiantService) {}

  ngOnInit(): void { this.load(); }

  load(): void {
    this.loading = true;
    this.svc.getAll().subscribe({
      next:  data => { this.items = data; this.loading = false; },
      error: ()   => { this.error = 'Erreur de chargement.'; this.loading = false; }
    });
  }

  openAdd(): void {
    this.current   = { nomEtudiant: '', prenomEtudiant: '', cinEtudiant: 0, dateNaissance: '' };
    this.editMode  = false;
    this.showModal = true;
  }

  openEdit(item: Etudiant): void {
    this.current   = { ...item };
    this.editMode  = true;
    this.showModal = true;
  }

  save(): void {
    const obs = this.editMode ? this.svc.update(this.current) : this.svc.add(this.current);
    obs.subscribe({
      next: () => {
        this.success  = this.editMode ? 'Étudiant mis à jour!' : 'Étudiant ajouté!';
        this.showModal = false;
        this.load();
        setTimeout(() => this.success = '', 3000);
      },
      error: () => { this.error = 'Erreur lors de la sauvegarde.'; }
    });
  }

  delete(id: number): void {
    if (!confirm('Supprimer cet étudiant?')) return;
    this.svc.delete(id).subscribe({
      next: () => { this.success = 'Étudiant supprimé!'; this.load(); setTimeout(() => this.success = '', 3000); },
      error: () => { this.error = 'Erreur lors de la suppression.'; }
    });
  }

  close(): void { this.showModal = false; }

  formatDate(d: string): string {
    if (!d) return '—';
    return new Date(d).toLocaleDateString('fr-TN');
  }
}

import { Component } from '@angular/core';

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent {
  menuOpen = false;

  navLinks = [
    { path: '/dashboard',    label: 'Dashboard',     icon: '🏠' },
    { path: '/universites',  label: 'Universités',   icon: '🎓' },
    { path: '/foyers',       label: 'Foyers',        icon: '🏢' },
    { path: '/blocs',        label: 'Blocs',         icon: '🧱' },
    { path: '/chambres',     label: 'Chambres',      icon: '🛏️' },
    { path: '/etudiants',    label: 'Étudiants',     icon: '👤' },
    { path: '/reservations', label: 'Réservations',  icon: '📋' },
  ];

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }
}

export interface Universite {
  idUniversite?: number;
  nomUniversite: string;
  adresse: string;
  foyer?: Foyer;
}

export interface Foyer {
  idFoyer?: number;
  nomFoyer: string;
  capaciteFoyer: number;
}

export interface Bloc {
  idBloc?: number;
  nomBloc: string;
  capaciteBloc: number;
}

export interface Chambre {
  idChambre?: number;
  numeroChambre: number;
  typeChambre: 'SIMPLE' | 'DOUBLE' | 'TRIPLE';
  bloc?: Bloc;
}

export interface Etudiant {
  idEtudiant?: number;
  nomEtudiant: string;
  prenomEtudiant: string;
  cinEtudiant: number;
  dateNaissance: string;  // ISO string
}

export interface Reservation {
  idReservation?: string;
  anneeUniversitaire: string;
  estValide: boolean;
  etudiants?: Etudiant[];
}

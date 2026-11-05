@Test
void testAjouterFoyerEtAffecterAUniversite() {
    Foyer f = new Foyer();
    Universite u = new Universite();
    
    Mockito.when(foyerRepository.save(Mockito.any(Foyer.class))).thenReturn(f);
    Mockito.when(universiteRepository.findById(1L)).thenReturn(Optional.of(u));
    Mockito.when(universiteRepository.save(Mockito.any(Universite.class))).thenReturn(u);

    Foyer result = foyerService.ajouterFoyerEtAffecterAUniversite(f, 1L);
    
    Assertions.assertNotNull(result);
    Assertions.assertEquals(f, u.getFoyer());
    Mockito.verify(universiteRepository).save(u);
}
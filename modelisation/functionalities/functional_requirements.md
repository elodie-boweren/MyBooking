### Exigences fonctionnelles — MyBooking

Ce document liste les exigences fonctionnelles par acteur: Client, Administrateur d'hôtel, Employé (rôle unique et interface commune).

## Client

- **Compte & Authentification**
  - S'inscrire (création de compte client).
  - Se connecter / se déconnecter.
  - Réinitialiser le mot de passe.
  - Gérer les préférences de notification.

- **Profil**
  - Consulter et mettre à jour ses informations personnelles (nom, email, téléphone, adresse, date de naissance).

- **Chambres**
  - Rechercher et filtrer les chambres (dates, capacité, type fixe, prix).
  - Consulter les détails d'une chambre (photos, équipements, description, type, prix, devise).
  - Voir les disponibilités en temps réel.

- **Réservations**
  - Créer une réservation (sélection des dates, chambre, informations invités).
  - Modifier ou annuler une réservation selon la politique d'annulation.
  - Consulter l'historique et les réservations à venir.
  - Recevoir des confirmations(notifications). Les montants affichent la devise.

- **Programme de fidélité**
  - Consulter le solde de points.
  - Utiliser des points lors d'une réservation (selon les règles définies).
  - Consulter l'historique d'acquisition/consommation de points.

- **Feedback**
  - Laisser un commentaire et une évaluation (note 1 à 5) après un séjour.
  - Consulter ses feedbacks et les réponses éventuelles de l'hôtel.

- **Événements & Installations**
  - Consulter le calendrier/liste des événements et installations disponibles (créneaux horaires début/fin).
  - Filtrer par type d'événement (liste fixe: spa, conference, yoga_class, fitness, wedding).
  - Réserver un événement ou une installation à un horaire précis (selon capacité), avec prix et devise.

## Administrateur d'hôtel

- **Auth & Administration**
  - Se connecter / se déconnecter (accès gestion).
  - Gérer les comptes clients et employés (création, suppression, modification, réinitialisation mot de passe).

- **Chambres**
  - CRUD des chambres (créer, lire, mettre à jour, supprimer).
  - Gérer les attributs (type fixe, capacité, prix, devise, équipements, statut).
  - Visualiser l'état d'occupation et la disponibilité.

- **Réservations**
  - Consulter la liste et le détail des réservations.
  - Créer, modifier ou annuler une réservation au nom d'un client si nécessaire.
  - Gérer l'assignation/changement de chambre (supprimer une réservation et créer une autre)

- **Clients & Employés**
  - CRUD des clients.
  - CRUD des employés (profil, statut).

- **Feedback**
  - Consulter tous les feedbacks et évaluations.
  - Répondre aux commentaires des clients.


- **Événements & Installations**
  - CRUD des événements et des installations.
  - Planifier (début/fin), définir la capacité, type d'événement (liste fixe), prix et devise, gérer l'ouverture/fermeture des réservations.
  - Envoyer des notifications automatisées liées aux événements (rappels, changements).

- **Analyses & Rapports**
  - Tableau de bord des métriques (occupation jour, réservations semaine, revenu mois, commentaires reçus).
  - Indicateurs (KPI) clés (taux d'occupation, note moyenne, CA mensuel).
  - Export simple (CSV/PDF) des rapports clés.

- **Gestion des employés (RH simplifiée)**
  - Planifier les shifts/horaires.
  - Approuver/Refuser les demandes de congé.
  - Gérer les formations/certifications.

- **Notifications & Communication**
  - Configurer modèles et canaux de notifications (email/SMS/).
  - Diffuser des annonces aux employés.

## Employé (rôle unique, interface commune)

- **Auth & Profil**
  - Se connecter / se déconnecter.
  - Consulter et mettre à jour son profil (coordonnées, mot de passe).

- **Shifts (simplifié)
  - Consulter ses prochains shifts.
  - Soumettre une demande de congé.

- **Tâches (simplifié)**
  - Voir sa liste de tâches.
  - Mettre à jour l'état d'une tâche (À faire / En cours / Terminé).
  - Ajouter une note ou une photo à une tâche.

- **Chambres**
  - Rechercher une chambre par numéro/statut.
  - Mettre à jour le statut d'une chambre (Propre / Sale / Hors service).
  - Ajouter une note courte (ex. besoin maintenance).

- **Réservations (consultation)**
  - Rechercher et consulter une réservation (lecture seule) par nom/ID/date.


- **Messages & Annonces**
  - Lire les annonces de l'administration.
  - Envoyer une réponse simple à l'administration.




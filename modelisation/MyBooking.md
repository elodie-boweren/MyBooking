Sujet

Tandis que votre maîtrise de Java s'intensifie, une nouvelle aventure s'ouvre à vous : la gestion d'un hôtel.
Vous plongerez dans le monde fascinant de la gestion hôtelière en exploitant la puissance du framework Spring.

Fonctionnalités
Mise en place de l’environnement et conception

A. Configuration de Spring Boot

Initialisez un projet Spring Boot avec les dépendances nécessaires.

B. Conception de la base de données

Modélisez des entités pour les chambres, les réservations, les clients, les employés et les gestionnaires.

Établissez des relations appropriées entre ces entités.

Fournissez votre modélisation dans votre drive.

Solutions Entreprises – Modélisation d’Entités
La méthode MERISE

MERISE est une méthode d'analyse et de conception des systèmes d'information basée sur la séparation des données et des traitements.

Modèles répartis sur 3 niveaux :

MCD – Modèle conceptuel de données

MLD – Modèle logique de données

MPD – Modèle physique de données

MCD : Modèle conceptuel de données

Représentation graphique et facilement compréhensible des données et de leurs liens.

Caractéristiques :

Pas d’ID primaire

Pas de clé étrangère

Pas d’affichage du type des données

Utilisation de cardinalités (min, max) et verbes de description

MLD : Modèle logique de données

Se rapproche de la structure réelle d’une base de données.

Caractéristiques :

Suppression des cardinalités et des verbes

Relations entre les clés

Ajout de tables de liaison si relations n..n

Clé primaire = donnée unique

Clé étrangère = conserve liaison entre deux tables

MPD : Modèle physique de données

Encore plus proche de la structure finale de la base de données.

Modélise et décrit complètement son architecture.

Utilise pattes d’oie, tous les types et champs.

Solutions Entreprises – Modélisation de Données
UML, c’est quoi ?

Unified Model Language

Langage graphique

Permet de créer plusieurs types de diagrammes, comme :

Classes

Tables

États

Cas d’utilisation

Solutions Entreprises – Relations
Généralisation / Héritage

Représente une relation de type "kind-of" (est un type de).

Relie classe parent et classe enfant.

Association simple

Relation reliant deux classes ou plus.

Indique une connexion ou une interaction.

Agrégation

Représente une relation "part-of" (partie-de).

Exemple : Actor fait partie de World, mais reste indépendant.

Composition

Dépendance forte : si l’entité principale est détruite, les instances liées le sont aussi.

Exemple : Les Terrains font partie de World ; si World est détruit, les Terrains le sont aussi.

Système d’authentification et de rôles

A. Backend

Mettre en place un système d’authentification.

Implémenter des rôles distincts (clients / gestionnaires) avec autorisations spécifiques.

B. Frontend

Pages de connexion distinctes pour clients et gestionnaires.

Page d’inscription pour nouveaux clients.

Gestion des chambres

A. Backend

API CRUD pour ajouter, modifier, supprimer et afficher des chambres.

B. Frontend (gestionnaire)

Interface de gestion : ajout, modification, suppression.

Vue d’ensemble des chambres disponibles.

C. Frontend (client)

Vue des chambres disponibles avec possibilité de réservation.

Gestion des réservations

A. Backend

API pour créer, consulter, modifier et annuler des réservations.

B. Frontend (gestionnaire)

Vue d’ensemble des réservations avec options de modification/annulation.

C. Frontend (client)

Interface pour effectuer une réservation.

Vue des réservations passées et à venir.

Gestion des clients et des employés

A. Backend

API CRUD pour gérer informations des clients et employés.

B. Frontend (gestionnaire)

Interfaces pour gérer les informations.

Système de feedback et d’évaluation

A. Backend

API pour permettre aux clients de laisser commentaires et évaluations.

B. Frontend (client)

Interface pour soumettre des feedbacks.

C. Frontend (gestionnaire)

Vue des feedbacks avec possibilité d’y répondre.

Système de fidélité

A. Backend

Système de points pour récompenser les clients fidèles.

B. Frontend (client)

Interface pour visualiser et utiliser les points.

Gestion des événements et installations

A. Backend

API pour gérer événements et installations.

Système pour envoyer notifications automatisées.

B. Frontend (gestionnaire)

Interface pour gérer et planifier événements/installations.

Gestion des préférences de notification et visualisation.

C. Frontend (client)

Vue des événements à venir et option de réservation.

Analyses et rapports

A. Backend

Outils d’analyse : satisfaction client, occupation des chambres, performances financières.

B. Frontend (client et gestionnaire)

Tableau de bord avec métriques et KPI.

Mieux comprendre…

Métriques = jauges automatiques de l’hôtel

Chambres occupées aujourd’hui

Réservations de la semaine

Revenu du mois

Clients connectés

Commentaires reçus

KPI = indicateurs stratégiques

Taux d’occupation (ex. 75 %)

Note moyenne (ex. 4,2/5)

Chiffre d’affaires mensuel (ex. 50 000 €)

Taux de fidélisation (ex. 30 % des clients reviennent)

Gestion avancée des employés

A. Backend

Gestion des horaires, jours de congé, formations.

B. Frontend (client et gestionnaire)

Interface dédiée RH.

Gestion de Projet et Collaboration d’Équipe
Organisation (Trello)

Organiser et suivre l’avancement des tâches.

Tâches définies, assignées et suivies régulièrement.

Réunions d’équipe

Minimum deux réunions par semaine.

Discuter avancement, défis et prochaines étapes.

Planification à l’avance et participation active obligatoire.

Rapport de Projet

Documenter :

Phases de développement

Décisions prises

Problèmes rencontrés et solutions adoptées

Tâches assignées et réalisées chaque semaine

Stockage sur Google Drive, partagé avec l’accompagnateur.

Conseils pratiques

Divisez pour mieux régner : avancer étape par étape.

Maîtriser chaque fonctionnalité avant de passer à la suivante.

Critères d’évaluation

Fonctionnalité : l’application doit être complète et fonctionnelle.

Qualité du code : code propre, organisé, respectant les bonnes pratiques Spring.

Collaboration & gestion de projet : organisation et coopération de l’équipe évaluées.
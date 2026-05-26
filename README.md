# 🚗 DRIVE NOW

![Java](https://img.shields.io/badge/Java-21-orange)
![JavaFX](https://img.shields.io/badge/JavaFX-21-blue)
![MySQL](https://img.shields.io/badge/MySQL-8.x-green)
![Maven](https://img.shields.io/badge/Maven-Build-red)

Application de gestion de location de voitures développée en **JavaFX**, **MySQL** et **JDBC**, dans le cadre du projet de Programmation Orientée Objet (POO).

## 📖 Description

DRIVE NOW est une application desktop permettant à une agence de location de gérer efficacement :

* les véhicules ;
* les utilisateurs ;
* les réservations ;
* les profils clients ;
* les documents PDF liés aux locations.

L'application suit une architecture **MVC (Model - View - Controller)** garantissant une séparation claire entre l'interface graphique, la logique métier et l'accès aux données.

---

## ✨ Fonctionnalités

### Authentification

* Connexion sécurisée
* Gestion de session
* Contrôle des rôles

### Gestion des véhicules

* Ajouter un véhicule
* Modifier un véhicule
* Supprimer un véhicule
* Consulter le parc automobile

### Gestion des utilisateurs

* Création de comptes
* Modification des profils
* Gestion des rôles

### Réservations

* Création de réservation
* Modification de réservation
* Annulation de réservation
* Vérification automatique de disponibilité

### Profil utilisateur

* Consultation des informations personnelles
* Mise à jour du profil

### Génération PDF

* Création automatique de documents PDF liés aux réservations

---

## 🏗️ Architecture

```text
MVC

Model
├── entities
├── dao
└── services

View
└── JavaFX (FXML)

Controller
└── Gestion des événements utilisateur
```

---

## 📂 Structure du projet

```text
src/main/java
│
├── app/
│   └── Main.java
│
├── controller/
│   ├── LoginController.java
│   ├── VehiculeController.java
│   ├── ReservationController.java
│   └── ...
│
├── model/
│   ├── entities/
│   │   ├── Utilisateur.java
│   │   ├── Vehicule.java
│   │   └── Reservation.java
│   │
│   ├── dao/
│   │   ├── UtilisateurDAO.java
│   │   ├── VehiculeDAO.java
│   │   └── ReservationDAO.java
│   │
│   └── services/
│       ├── AuthService.java
│       ├── VehiculeService.java
│       ├── ReservationService.java
│       └── PdfGeneratorService.java
│
└── utils/
```

---

## 🛠️ Technologies utilisées

| Technologie  | Version                       |
| ------------ | ----------------------------- |
| Java         | 21                            |
| JavaFX       | 21.0.6                        |
| Maven        | Gestion des dépendances       |
| MySQL        | Base de données               |
| JDBC         | Accès aux données             |
| Lombok       | Réduction du code boilerplate |
| iTextPDF     | Génération PDF                |
| Git / GitHub | Gestion de versions           |

---

## ⚙️ Installation

### 1. Cloner le dépôt

```bash
git clone https://github.com/Tresor-Bilal-Projects/Java-Project.git
```

### 2. Configurer MySQL

Créer une base de données :

```sql
CREATE DATABASE drivenow;
```

### Configuration de la base de données

Modifier le fichier `DatabaseConnection.java` :

```java
private static final String URL =
    "jdbc:mysql://localhost:3306/drivenow";
private static final String USER = "root";
private static final String PASSWORD = "password";
```

### 3. Installer les dépendances

```bash
mvn clean install
```

### 4. Lancer l'application

```bash
mvn javafx:run
```

---

## 🔒 Sécurité

* Hachage des mots de passe
* Requêtes préparées JDBC
* Gestion des sessions
* Contrôle d'accès par rôles
* Validation des données utilisateur

---

## 👥 Équipe

* Mbungu Trésor
* Pakou Gakosso Nathan
* Ondo Mintsa Pierre-Thyrel
* Mbianga Grace Merveille
* Nkwemfo Wacka Lunic Anders Degrace

---

## 📄 Licence

Projet académique réalisé dans le cadre du Bachelor 2 (IREN).



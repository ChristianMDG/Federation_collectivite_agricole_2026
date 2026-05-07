# Agricultural Federation API

## 📋 Description

API REST pour la gestion d'une fédération de collectivités agricoles à Madagascar.  
Cette application permet de gérer les collectivités agricoles, leurs membres, les cotisations, les paiements, les activités et l'assiduité.

## 🚀 Technologies utilisées

- **Java 17** - Langage de programmation
- **Spring Boot 3.x** - Framework principal
- **PostgreSQL** - Base de données relationnelle
- **Maven** - Gestionnaire de dépendances
- **Lombok** - Réduction du code boilerplate

## 📦 Prérequis

- Java 17 ou supérieur
- PostgreSQL 14 ou supérieur
- Maven 3.8+
- Postman (pour les tests)

## 🔧 Installation

### 1. Cloner le repository

```bash
git clone https://github.com/ChristianMDG/Federation_collectivite_agricole_2026.git
cd federation
```

### 2. Configurer la base de données

```sql
-- Créer la base de données
CREATE DATABASE federation_db;

-- Exécuter le script SQL complet
-- Le script se trouve dans src/main/resources/schema.sql
```

### 3. Configurer application.properties

```properties
# src/main/resources/application.properties
spring.datasource.url=jdbc:postgresql://localhost:5432/federation_db
spring.datasource.username=postgres
spring.datasource.password=votre_mot_de_passe
spring.datasource.driver-class-name=org.postgresql.Driver

server.port=8080
```

### 4. Compiler et exécuter

```bash
# Compiler le projet
mvn clean install

# Exécuter l'application
mvn spring-boot:run
```

L'application démarre sur `http://localhost:8080`

## 🔐 Sécurité

Tous les endpoints sont protégés par une clé API.  
Chaque requête doit inclure l'en-tête suivant :

```
x-api-key: agri-secure-key
```

| Situation | Code HTTP | Message |
|-----------|-----------|---------|
| Pas de clé API | 401 | Bad credentials |
| Mauvaise clé API | 401 | Bad credentials |
| Clé API correcte | 200 | Accès normal |

## 📚 Endpoints API

### Collectivités

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/collectivities` | Créer une ou plusieurs collectivités |
| GET | `/collectivities/{id}` | Récupérer une collectivité par son ID |
| PUT | `/collectivities/{id}/informations` | Assigner un numéro et un nom unique |
| GET | `/collectivities/{id}/financialAccounts` | Récupérer les comptes financiers |
| GET | `/collectivities/{id}/membershipFees` | Récupérer les cotisations |
| POST | `/collectivities/{id}/membershipFees` | Créer des cotisations |
| GET | `/collectivities/{id}/transactions` | Récupérer les transactions |
| GET | `/collectivities/{id}/activities` | Récupérer les activités |
| POST | `/collectivities/{id}/activities` | Créer des activités |
| GET | `/collectivities/{id}/activities/{activityId}/attendance` | Récupérer l'assiduité |
| POST | `/collectivities/{id}/activities/{activityId}/attendance` | Enregistrer l'assiduité |

### Statistiques

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/collectivites/{id}/statistics` | Statistiques locales (encaissements, impayés, assiduité) |
| GET | `/collectivites/statistics` | Statistiques globales |

### Membres

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| POST | `/members` | Adhésion de nouveaux membres |
| POST | `/members/{id}/payments` | Créer des paiements |

## 📝 Exemples de requêtes

### Créer une collectivité

```bash
POST http://localhost:8080/collectivities
x-api-key: agri-secure-key
Content-Type: application/json

[
  {
    "location": "Antananarivo",
    "members": ["C1-M1", "C1-M2", "C1-M3", "C1-M4", "C1-M5", "C1-M6", "C1-M7", "C1-M8", "C1-NEW-1", "C1-NEW-2"],
    "federationApproval": true,
    "structure": {
      "president": "C1-M1",
      "vicePresident": "C1-M2",
      "treasurer": "C1-M3",
      "secretary": "C1-M4"
    }
  }
]
```

### Créer un membre

```bash
POST http://localhost:8080/members
x-api-key: agri-secure-key
Content-Type: application/json

[
  {
    "firstName": "Jean",
    "lastName": "Dupont",
    "birthDate": "1990-05-15",
    "gender": "MALE",
    "address": "12 rue de la Ferme",
    "profession": "Agriculteur",
    "phoneNumber": 612345678,
    "email": "jean@email.com",
    "occupation": "JUNIOR",
    "referees": ["C1-M1", "C1-M2"],
    "registrationFeePaid": true,
    "membershipDuesPaid": true
  }
]
```

### Enregistrer un paiement

```bash
POST http://localhost:8080/members/C1-M1/payments
x-api-key: agri-secure-key
Content-Type: application/json

[
  {
    "amount": 25000,
    "membershipFeeIdentifier": "cot-1",
    "accountCreditedIdentifier": "C1-A-CASH",
    "paymentMode": "CASH"
  }
]
```

### Obtenir les statistiques

```bash
GET http://localhost:8080/collectivites/col-1/statistics?from=2026-01-01&to=2026-12-31
x-api-key: agri-secure-key
```

## 📊 Codes de réponse

| Code | Signification |
|------|---------------|
| 200 | Succès (GET, PUT) |
| 201 | Créé (POST) |
| 400 | Erreur de paramètre ou règle métier non respectée |
| 401 | Non authentifié (clé API manquante ou invalide) |
| 404 | Ressource non trouvée |
| 409 | Conflit (nom ou numéro déjà existant) |
| 500 | Erreur interne du serveur |

## 🗂️ Structure du projet

```
src/main/java/com/exam/federation/
├── config/                 # Configuration (sécurité, intercepteurs)
├── controllers/            # Contrôleurs REST
├── services/              # Logique métier
├── repository/            # Accès base de données
├── dto/                   # Objets de transfert de données
├── entity/                # Entités JPA
├── Exception/             # Gestion des exceptions
└── enums/                 # Énumérations
```

## 🧪 Tests avec Postman

1. **Importer la collection** : `Agricultural_Federation_API.postman_collection.json`
2. **Configurer l'environnement** :
   - `base_url`: `http://localhost:8080`
   - `api_key`: `agri-secure-key`
3. **Exécuter les tests** dans l'ordre recommandé

## 📋 Règles métier implémentées

### A - Ouverture d'une collectivité
- Au moins 10 membres
- 5 membres avec ancienneté ≥ 6 mois
- Autorisation de la fédération
- Localité et spécialité requises

### B - Admission d'un membre
- Parrainé par un membre confirmé (SENIOR)
- Ancienneté du parrain > 90 jours
- Frais d'adhésion : 50.000 Ar
- Informations personnelles complètes

### C - Cotisations et paiements
- Cotisations périodiques (mensuelles/annuelles) ou ponctuelles
- Traçabilité des encaissements
- Transactions automatiques

### D - Situation de trésorerie
- Comptes financiers (caisse, mobile money, bancaire)
- Une seule caisse par collectivité
- Soldes consultables à une date donnée

### E & F - Activités et assiduité
- Gestion des activités (ponctuelles ou récurrentes)
- Enregistrement des présences (ATTENDED, MISSING)
- Non-modifiable après confirmation

### G & H - Statistiques
- Taux d'assiduité par membre
- Montants encaissés et impayés
- Pourcentage de membres à jour
- Nombre de nouveaux adhérents

## 👥 Auteur

- **Nom** : Christian MDG
- **Nom** : Lahatra Nomena
- **Projet** : PROG3 - Implémentation d'API REST
- **Année** : 2025-2026

## 📅 Version

**Version actuelle : 0.0.7**

- v0.0.1 (2026-04-21) : Création collectivités et membres
- v0.0.2 (2026-04-22) : Attribution nom/numéro
- v0.0.3 (2026-04-23) : Cotisations, paiements, transactions
- v0.0.4 (2026-04-23) : Récupération collectivité et comptes
- v0.0.5 (2026-05-05) : Statistiques partielles
- v0.0.6 (2026-05-05) : Activités et assiduité
- v0.0.7 (2026-05-05) : Assiduité dans les statistiques

## 🔒 Sécurité ajoutée (2026-05-07)

- Protection par clé API
- En-tête requis : `x-api-key: agri-secure-key`

## 📞 Support

Pour toute question, ouvrez une issue sur le repository GitHub.

---

**Bon test !** 🚀
```

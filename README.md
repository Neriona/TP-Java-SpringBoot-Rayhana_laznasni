# TP3 – Architecture Distribuée
## 📄 Documentation> [!IMPORTANT]
> **Pour consulter le rapport de TP :** > 1. Allez dans le fichier `Rapport_TP3_Architecture_Distribuée.pdf`
> 2. Cliquez sur le bouton **Download** (ou l'icône flèche vers le bas) en haut à droite pour l'ouvrir correctement.
## 📋 Description

Ce TP consiste à déployer une **architecture distribuée complète** avec 6 composants interconnectés. L'objectif est de comprendre le rôle de chaque composant et d'implémenter un flux complet : POST commande → stockage MongoDB → cache Redis → notification RabbitMQ.

## 🎯 Objectifs

- Déployer une architecture distribuée complète avec **6 composants interconnectés**
- Comprendre le rôle de chaque composant : Client, Load Balancer, Serveur, Cache, BDD, Bus de messages
- Implémenter un flux complet : **POST commande → MongoDB → Redis → RabbitMQ**
- Tester et observer le comportement distribué avec Postman

## 🏗️ Architecture cible

```
                    ┌──────────┐
                    │ Postman  │  (Client)
                    └────┬─────┘
                         │
                    ┌────▼─────┐
                    │  Nginx   │  (Load Balancer - port 80)
                    │  :80     │
                    └──┬────┬──┘
                       │    │
              ┌────────▼┐  ┌▼────────┐
              │  App 1  │  │  App 2  │  (Node.js / Express)
              │  :3001  │  │  :3002  │
              └──┬──┬──┬┘  └┬──┬──┬──┘
                 │  │  │    │  │  │
        ┌────────▼┐ │ ┌▼────▼┐ │ ┌▼────────┐
        │MongoDB  │ │ │Redis │ │ │RabbitMQ │
        │ :27017  │ │ │:6379 │ │ │ :5672   │
        └─────────┘ │ └──────┘ │ └─────────┘
                     └─────┬───┘
                      ┌────▼─────┐
                      │Consumer  │  (Notification)
                      └──────────┘
```

## 🛠️ Composants

| Composant | Technologie | Rôle |
|---|---|---|
| Client | Postman | Envoie les requêtes HTTP POST/GET |
| Load Balancer | Nginx | Répartit le trafic entre 2 instances Node.js |
| Serveur applicatif | Node.js / Express | Logique métier : validation, traitement |
| Base de données | MongoDB | Stockage persistant des commandes |
| Cache | Redis | Mémorise les résultats GET (TTL 30s) |
| Bus de messages | RabbitMQ | Notification asynchrone après chaque commande |

## 📂 Structure du projet

```
tp-archi-distribuee/
├── docker-compose.yml          ← Orchestre tous les conteneurs
├── nginx/
│   └── nginx.conf              ← Configuration du load balancer
├── app/
│   ├── Dockerfile
│   ├── package.json
│   ├── server.js               ← Serveur Node.js principal
│   ├── consumer.js             ← Consommateur RabbitMQ
│   ├── routes/
│   │   └── commandes.js        ← Routes API
│   └── services/
│       ├── mongodb.js          ← Connexion MongoDB
│       ├── redis.js            ← Connexion Redis
│       └── rabbitmq.js         ← Connexion RabbitMQ
```

## 🔄 Flux de données

```
1. Postman → Nginx :80 → Node.js :3001 / :3002   (Round-robin)
2. Node.js → MongoDB                               (Stockage persistant)
3. Node.js → Redis                                  (Cache GET /commandes)
4. Node.js → RabbitMQ → Consommateur → Notification console
```

## ⚙️ Prérequis

- [Node.js](https://nodejs.org) (v18+)
- [Docker Desktop](https://docker.com)
- [Postman](https://postman.com)

## 🚀 Lancement

### 1. Cloner et installer

```bash
cd app
npm init -y
npm install express mongoose ioredis amqplib
```

### 2. Démarrer tous les conteneurs

```bash
docker-compose up --build
```

### 3. Vérifier que tout est actif

| URL | Résultat attendu |
|---|---|
| http://localhost/health | `{ status: 'OK', instance: ... }` |
| http://localhost:3001/health | Instance 1 directe |
| http://localhost:3002/health | Instance 2 directe |
| http://localhost:15672 | Interface RabbitMQ (admin/admin) |

## 🧪 Tests avec Postman

### Test 1 : Créer une commande (POST)

- **Méthode :** POST
- **URL :** `http://localhost/commandes`
- **Body (JSON) :**

```json
{
  "produit": "Laptop Dell XPS",
  "quantite": 2,
  "client": "Ahmed Benali"
}
```

### Test 2 : Lister les commandes (GET)

- **Méthode :** GET
- **URL :** `http://localhost/commandes`
- **1er appel →** source: `🗄 MongoDB`
- **2ème appel →** source: `⚡ CACHE Redis` (dans les 30s)

### Test 3 : Observer le load balancing

Envoyez 4 requêtes successives et observez `traitee_par` :

```
Requête 1 → Instance-1
Requête 2 → Instance-2
Requête 3 → Instance-1
Requête 4 → Instance-2
```

## ❓ Questions de compte-rendu

| # | Question |
|---|---|
| Q1 | Quel composant reçoit en premier les requêtes du client ? Quel algorithme utilise-t-il ? |
| Q2 | Que se passe-t-il si app1 tombe en panne ? |
| Q3 | Différence entre le 1er GET (MongoDB) et le 2ème GET (Redis) ? |
| Q4 | Pourquoi invalider le cache Redis après un POST ? |
| Q5 | En quoi RabbitMQ améliore-t-il la résilience du système ? |
| Q6 | Comment ajouter une 3ème instance Node.js ? |

## 👩‍💻 Auteur

- **Rayhana Laznasni**
- ENSA Beni Mellal
- Module : Architecture des Systèmes Distribués
- Encadré par : Pr. BE ELBAGHAZAOUI

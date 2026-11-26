# Foyer Frontend — Angular 16

A dark-mode Angular 16 frontend for the **tpFoyer17** Spring Boot backend.

## 📂 Structure

```
frontend/
├── src/
│   ├── app/
│   │   ├── components/
│   │   │   ├── navbar/
│   │   │   ├── dashboard/
│   │   │   ├── universite/universite-list/
│   │   │   ├── foyer/foyer-list/
│   │   │   ├── bloc/bloc-list/
│   │   │   ├── chambre/chambre-list/
│   │   │   ├── etudiant/etudiant-list/
│   │   │   └── reservation/reservation-list/
│   │   ├── models/models.ts
│   │   ├── services/
│   │   │   ├── universite.service.ts
│   │   │   ├── foyer.service.ts
│   │   │   ├── bloc.service.ts
│   │   │   ├── chambre.service.ts
│   │   │   ├── etudiant.service.ts
│   │   │   └── reservation.service.ts
│   │   ├── app.module.ts
│   │   ├── app-routing.module.ts
│   │   └── app.component.ts
│   ├── styles.css        ← Global dark theme
│   └── index.html
├── nginx.conf            ← Proxies /api/ → backend
├── Dockerfile            ← Multi-stage: node:18 build + nginx:alpine serve
├── proxy.conf.json       ← Local dev proxy only
└── package.json
```

## 🚀 Local Development

```bash
cd frontend
npm ci --legacy-peer-deps
npm start
# App runs at http://localhost:4200
# API calls are proxied to http://localhost:8082/tpFoyer17/api/
```

> Make sure the Spring Boot backend is running on port 8082 locally.

## 🐳 Docker Build

```bash
cd frontend
docker build -t hamdiarij/devops-tp-frontend:latest .
docker run -p 80:80 hamdiarij/devops-tp-frontend:latest
# Visit http://localhost
```

## ☸️ Kubernetes

The frontend is deployed alongside the backend in `k8s/deployment.yaml`:

- **Deployment**: `foyer-frontend` (1 replica, nginx:alpine)
- **Service**: `foyer-frontend-service` (LoadBalancer, port 80)
- nginx proxies `/api/` → `http://achat-service:8082/tpFoyer17/api/`

After Jenkins deploys, get the frontend URL:
```bash
kubectl get svc foyer-frontend-service
# Copy EXTERNAL-IP and visit http://<EXTERNAL-IP>
```

## 🔗 Routes

| Path | Component |
|---|---|
| `/dashboard` | Stats overview of all entities |
| `/universites` | Manage universities |
| `/foyers` | Manage foyers |
| `/blocs` | Manage blocs |
| `/chambres` | Manage rooms |
| `/etudiants` | Manage students |
| `/reservations` | Create / cancel reservations |

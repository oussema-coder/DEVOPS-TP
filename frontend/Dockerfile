# ============================================================
# Stage 1: Build Angular 16 application
# ============================================================
FROM node:18-alpine AS build

WORKDIR /app

# Copy dependency manifests first (leverages Docker layer caching)
COPY package*.json ./
RUN npm install --legacy-peer-deps

# Copy the rest of the source code
COPY . .

# Build the production bundle
RUN npm run build:prod

# ============================================================
# Stage 2: Serve with nginx
# ============================================================
FROM nginx:1.25-alpine

# Remove the default nginx config
RUN rm /etc/nginx/conf.d/default.conf

# Copy our custom nginx config
COPY nginx.conf /etc/nginx/conf.d/default.conf

# Copy Angular build output from Stage 1
COPY --from=build /app/dist/foyer-frontend /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Health check
HEALTHCHECK --interval=15s --timeout=5s --start-period=10s --retries=3 \
  CMD wget -qO- http://localhost/health || exit 1

CMD ["nginx", "-g", "daemon off;"]

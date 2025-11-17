.PHONY: dev up down restart logs clean rebuild

# Start development environment
dev:
@echo "🚀 Starting Fit AI Challenge - Development Mode..."
docker-compose up -d --build
@echo "✅ Started! Waiting for app to be ready..."
@sleep 10
@echo "📱 App: http://localhost:8080"
@echo "🗄️  Adminer: http://localhost:8081"
@echo "🔍 Debug port: 5005"
@echo "📝 View logs: make logs"

# Start without rebuild
up:
docker-compose up -d

# Stop all containers
down:
docker-compose down

# Restart ONLY app (when you add new entity)
restart:
@echo "🔄 Restarting app..."
docker-compose restart app
@echo "✅ Done! Check logs: make logs"

# View app logs
logs:
docker-compose logs -f app

# View all logs
logs-all:
docker-compose logs -f

# Clean everything
clean:
@echo "🧹 Cleaning..."
docker-compose down -v
docker system prune -f
@echo "✅ Cleaned!"

# Rebuild only when changing dependencies
rebuild:
@echo "🔨 Rebuilding app..."
docker-compose build --no-cache app
docker-compose up -d app
@echo "✅ Done!"

# Database commands
db-reset:
docker-compose down db
docker volume rm fit_ai_challenge_web-app_be_db_data
docker-compose up -d db

# Check status
status:
docker-compose ps
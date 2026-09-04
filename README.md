# LinkNest

LinkNest is a URL shortener with a Spring Boot/MongoDB API and a Vue/Vite web client. Short links are stored in MongoDB and remain available after backend restarts when the MongoDB data is persistent.

## Requirements

- Java 21 or newer
- Maven 3.9 or newer
- Node.js 20 or newer and npm
- MongoDB 7 or newer, running locally or through MongoDB Atlas
- Git

Docker Desktop is optional. The included `docker-compose.yml` starts MongoDB with a named persistent volume.

## Get the source

```bash
git clone https://github.com/SUBHAJIT-mallickbnj/Simple-URL-Shortener-java-springboot-vue.git
cd Simple-URL-Shortener-java-springboot-vue
```

## Start MongoDB locally

Use Docker:

```bash
docker compose up -d mongodb
```

Or start a local MongoDB service yourself. The default connection is `mongodb://localhost:27017/linknest`.

The Docker volume is named `linknest-data`; do not run `docker compose down -v` unless you intentionally want to delete the database.

## Run the backend

From the repository root:

```bash
cd backend
mvn spring-boot:run
```

The API starts at `http://localhost:8080`.

To build and run a JAR instead:

```bash
cd backend
mvn clean package
java -jar target/linknest-api-1.0.0.jar
```

## Run the frontend

Open a second terminal in the repository root:

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173`. The frontend uses `http://localhost:8080` by default for the API.

For a different API address, set `VITE_API_URL` before building. On Windows PowerShell:

```powershell
$env:VITE_API_URL = "https://your-api.example.com"
npm run build
```

## API endpoints

Health check:

```text
GET http://localhost:8080/api/urls/health
```

Create a short URL:

```powershell
Invoke-RestMethod http://localhost:8080/api/urls -Method Post -ContentType "application/json" -Body '{"url":"https://example.com"}'
```

Inspect a mapping with `GET /api/urls/{shortCode}`. Redirect with `GET /{shortCode}`.

## Render and Vercel deployment

Create a MongoDB Atlas cluster and database user first. Atlas provides the connection string; no separate database ID is required. Keep the password private.

Deploy the `backend` directory as a Render Web Service:

- Build command: `mvn clean package -DskipTests`
- Start command: `java -jar target/linknest-api-1.0.0.jar`

Set these Render environment variables:

```text
MONGODB_URI=mongodb+srv://USERNAME:PASSWORD@CLUSTER.mongodb.net/linknest?retryWrites=true&w=majority
PUBLIC_URL=https://your-render-service.onrender.com
FRONTEND_URL=https://your-vercel-app.vercel.app
```

Deploy the `frontend` directory to Vercel and set:

```text
VITE_API_URL=https://your-render-service.onrender.com
```

The Vercel build command is `npm run build` and the output directory is `dist`.

MongoDB Atlas must remain active for links to remain available after Render restarts or redeployments. Never commit `MONGODB_URI` or any password to this repository.

## Project layout

```text
backend/              Spring Boot API and MongoDB persistence
frontend/             Vue/Vite web application
docker-compose.yml    Local MongoDB with persistent storage
```
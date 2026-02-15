# documents

В репозитории добавлена Go-версия сервиса документов в формате, близком к production-структуре.

## Go-приложение (`golang-documents`)

### Структура
- `cmd/documents-api/main.go` — точка входа и graceful shutdown по SIGINT/SIGTERM.
- `internal/app` — сборка приложения и жизненный цикл сервера.
- `internal/config` — конфигурация через переменные окружения.
- `internal/domain` — доменные модели.
- `internal/service` — бизнес-логика и валидация.
- `internal/store/memory` — потокобезопасное in-memory хранилище (репозиторий).
- `internal/transport/httpapi` — HTTP-слой, middleware (логирование, panic recovery, timeout, request-id).

### API
- `GET /healthz`
- `POST /directories`
- `POST /documents`
- `GET /documents`
- `POST /moderation/{id}`

### Переменные окружения
- `APP_PORT` (по умолчанию `8081`)
- `APP_REQUEST_TIMEOUT` (по умолчанию `5s`)
- `APP_SHUTDOWN_TIMEOUT` (по умолчанию `10s`)
- `APP_LOG_LEVEL` (`DEBUG|INFO|WARN|ERROR`, по умолчанию `INFO`)

## Запуск
```bash
cd golang-documents
go run ./cmd/documents-api
```

## Тесты
```bash
cd golang-documents
go test ./...
```

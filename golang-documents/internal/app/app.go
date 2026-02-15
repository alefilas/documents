package app

import (
	"context"
	"fmt"
	"log/slog"

	"github.com/alefilas/documents/golang-documents/internal/config"
	"github.com/alefilas/documents/golang-documents/internal/service"
	"github.com/alefilas/documents/golang-documents/internal/store/memory"
	"github.com/alefilas/documents/golang-documents/internal/transport/httpapi"
)

type App struct {
	cfg    config.Config
	logger *slog.Logger
	server *httpapi.Server
}

func New(cfg config.Config, logger *slog.Logger) *App {
	repo := memory.New()
	svc := service.New(repo)
	srv := httpapi.New(cfg, logger, svc)
	return &App{cfg: cfg, logger: logger, server: srv}
}

func (a *App) Run(ctx context.Context) error {
	errCh := make(chan error, 1)
	go func() {
		errCh <- a.server.Run()
	}()

	select {
	case <-ctx.Done():
		a.logger.Info("shutdown signal received")
		shutdownCtx, cancel := context.WithTimeout(context.Background(), a.cfg.ShutdownTimeout)
		defer cancel()
		if err := a.server.Shutdown(shutdownCtx); err != nil {
			return fmt.Errorf("shutdown error: %w", err)
		}
		if err := <-errCh; err != nil {
			return fmt.Errorf("server run error: %w", err)
		}
		return nil
	case err := <-errCh:
		if err != nil {
			return fmt.Errorf("server run error: %w", err)
		}
		return nil
	}
}

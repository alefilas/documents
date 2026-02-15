package main

import (
	"context"
	"log/slog"
	"os"
	"os/signal"
	"syscall"

	"github.com/alefilas/documents/golang-documents/internal/app"
	"github.com/alefilas/documents/golang-documents/internal/config"
)

func main() {
	cfg := config.MustLoad()

	logger := slog.New(slog.NewJSONHandler(os.Stdout, &slog.HandlerOptions{Level: cfg.LogLevel}))
	slog.SetDefault(logger)

	application := app.New(cfg, logger)

	ctx, stop := signal.NotifyContext(context.Background(), syscall.SIGINT, syscall.SIGTERM)
	defer stop()

	if err := application.Run(ctx); err != nil {
		logger.Error("application terminated with error", slog.String("error", err.Error()))
		os.Exit(1)
	}
}

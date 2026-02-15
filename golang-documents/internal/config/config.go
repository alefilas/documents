package config

import (
	"log"
	"log/slog"
	"os"
	"strconv"
	"time"
)

type Config struct {
	HTTPPort        int
	ShutdownTimeout time.Duration
	RequestTimeout  time.Duration
	LogLevel        slog.Level
	AllowedOrigins  []string
}

func MustLoad() Config {
	port := getInt("APP_PORT", 8081)
	shutdownTimeout := getDuration("APP_SHUTDOWN_TIMEOUT", 10*time.Second)
	requestTimeout := getDuration("APP_REQUEST_TIMEOUT", 5*time.Second)
	logLevel := getLogLevel("APP_LOG_LEVEL", slog.LevelInfo)

	cfg := Config{
		HTTPPort:        port,
		ShutdownTimeout: shutdownTimeout,
		RequestTimeout:  requestTimeout,
		LogLevel:        logLevel,
		AllowedOrigins:  []string{"*"},
	}

	if cfg.HTTPPort <= 0 || cfg.HTTPPort > 65535 {
		log.Fatalf("invalid APP_PORT: %d", cfg.HTTPPort)
	}

	return cfg
}

func getInt(key string, fallback int) int {
	raw := os.Getenv(key)
	if raw == "" {
		return fallback
	}
	value, err := strconv.Atoi(raw)
	if err != nil {
		log.Fatalf("invalid integer env %s=%q: %v", key, raw, err)
	}
	return value
}

func getDuration(key string, fallback time.Duration) time.Duration {
	raw := os.Getenv(key)
	if raw == "" {
		return fallback
	}
	value, err := time.ParseDuration(raw)
	if err != nil {
		log.Fatalf("invalid duration env %s=%q: %v", key, raw, err)
	}
	return value
}

func getLogLevel(key string, fallback slog.Level) slog.Level {
	raw := os.Getenv(key)
	if raw == "" {
		return fallback
	}
	var level slog.Level
	if err := level.UnmarshalText([]byte(raw)); err != nil {
		log.Fatalf("invalid log level env %s=%q: %v", key, raw, err)
	}
	return level
}

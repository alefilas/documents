package httpapi

import (
	"context"
	"fmt"
	"log/slog"
	"math/rand"
	"net/http"
	"runtime/debug"
	"time"
)

type requestIDKey struct{}

var reqIDKey requestIDKey

type statusWriter struct {
	http.ResponseWriter
	status int
}

func (w *statusWriter) WriteHeader(status int) {
	w.status = status
	w.ResponseWriter.WriteHeader(status)
}

func loggingMiddleware(logger *slog.Logger, next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		started := time.Now()
		sw := &statusWriter{ResponseWriter: w, status: http.StatusOK}
		next.ServeHTTP(sw, r)
		logger.Info("request handled",
			slog.String("method", r.Method),
			slog.String("path", r.URL.Path),
			slog.Int("status", sw.status),
			slog.Duration("duration", time.Since(started)),
		)
	})
}

func recoverMiddleware(logger *slog.Logger, next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		defer func() {
			if rec := recover(); rec != nil {
				logger.Error("panic recovered",
					slog.Any("panic", rec),
					slog.String("stack", string(debug.Stack())),
				)
				http.Error(w, "internal error", http.StatusInternalServerError)
			}
		}()
		next.ServeHTTP(w, r)
	})
}

func timeoutMiddleware(timeout time.Duration, next http.Handler) http.Handler {
	return http.TimeoutHandler(next, timeout, "request timeout")
}

func requestIDMiddleware(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		id := fmt.Sprintf("req-%d-%d", time.Now().UnixNano(), rand.Int63())
		ctx := context.WithValue(r.Context(), reqIDKey, id)
		w.Header().Set("X-Request-ID", id)
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

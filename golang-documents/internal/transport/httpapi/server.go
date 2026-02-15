package httpapi

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"log/slog"
	"net/http"
	"strconv"
	"strings"
	"time"

	"github.com/alefilas/documents/golang-documents/internal/config"
	"github.com/alefilas/documents/golang-documents/internal/domain"
	"github.com/alefilas/documents/golang-documents/internal/service"
	"github.com/alefilas/documents/golang-documents/internal/store/memory"
)

type Server struct {
	httpServer *http.Server
	logger     *slog.Logger
}

func New(cfg config.Config, logger *slog.Logger, svc *service.Service) *Server {
	mux := http.NewServeMux()

	h := &handler{svc: svc}
	mux.HandleFunc("/healthz", h.health)
	mux.HandleFunc("/directories", h.createDirectory)
	mux.HandleFunc("/documents", h.documents)
	mux.HandleFunc("/moderation/", h.moderate)

	wrapped := requestIDMiddleware(recoverMiddleware(logger, loggingMiddleware(logger, timeoutMiddleware(cfg.RequestTimeout, mux))))

	httpServer := &http.Server{
		Addr:              fmt.Sprintf(":%d", cfg.HTTPPort),
		Handler:           wrapped,
		ReadHeaderTimeout: 3 * time.Second,
	}

	return &Server{httpServer: httpServer, logger: logger}
}

func (s *Server) Run() error {
	s.logger.Info("starting HTTP server", slog.String("addr", s.httpServer.Addr))
	if err := s.httpServer.ListenAndServe(); err != nil && !errors.Is(err, http.ErrServerClosed) {
		return err
	}
	return nil
}

func (s *Server) Shutdown(ctx context.Context) error {
	return s.httpServer.Shutdown(ctx)
}

type handler struct {
	svc *service.Service
}

func (h *handler) health(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodGet {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}
	writeJSON(w, http.StatusOK, map[string]string{"status": "ok"})
}

func (h *handler) createDirectory(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}
	var req struct {
		Name string `json:"name"`
	}
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid payload", http.StatusBadRequest)
		return
	}

	dir, err := h.svc.CreateDirectory(r.Context(), req.Name)
	if err != nil {
		if errors.Is(err, service.ErrValidation) {
			http.Error(w, "invalid payload", http.StatusBadRequest)
			return
		}
		http.Error(w, "internal error", http.StatusInternalServerError)
		return
	}
	writeJSON(w, http.StatusCreated, dir)
}

func (h *handler) documents(w http.ResponseWriter, r *http.Request) {
	switch r.Method {
	case http.MethodPost:
		var req struct {
			Title       string `json:"title"`
			Content     string `json:"content"`
			DirectoryID int64  `json:"directory_id"`
		}
		if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
			http.Error(w, "invalid payload", http.StatusBadRequest)
			return
		}
		doc, err := h.svc.CreateDocument(r.Context(), req.Title, req.Content, req.DirectoryID)
		if err != nil {
			switch {
			case errors.Is(err, service.ErrValidation):
				http.Error(w, "invalid payload", http.StatusBadRequest)
			case errors.Is(err, memory.ErrDirectoryNotFound):
				http.Error(w, "directory not found", http.StatusNotFound)
			default:
				http.Error(w, "internal error", http.StatusInternalServerError)
			}
			return
		}
		writeJSON(w, http.StatusCreated, doc)
	case http.MethodGet:
		docs, err := h.svc.ListDocuments(r.Context())
		if err != nil {
			http.Error(w, "internal error", http.StatusInternalServerError)
			return
		}
		writeJSON(w, http.StatusOK, docs)
	default:
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
	}
}

func (h *handler) moderate(w http.ResponseWriter, r *http.Request) {
	if r.Method != http.MethodPost {
		http.Error(w, "method not allowed", http.StatusMethodNotAllowed)
		return
	}
	docIDPart := strings.TrimPrefix(r.URL.Path, "/moderation/")
	docID, err := strconv.ParseInt(docIDPart, 10, 64)
	if err != nil {
		http.Error(w, "invalid document id", http.StatusBadRequest)
		return
	}
	var req domain.ModerationRequest
	if err := json.NewDecoder(r.Body).Decode(&req); err != nil {
		http.Error(w, "invalid payload", http.StatusBadRequest)
		return
	}
	doc, err := h.svc.ModerateDocument(r.Context(), docID, req)
	if err != nil {
		switch {
		case errors.Is(err, service.ErrValidation):
			http.Error(w, "invalid payload", http.StatusBadRequest)
		case errors.Is(err, memory.ErrDocumentNotFound):
			http.Error(w, "document not found", http.StatusNotFound)
		default:
			http.Error(w, "internal error", http.StatusInternalServerError)
		}
		return
	}
	writeJSON(w, http.StatusOK, doc)
}

func writeJSON(w http.ResponseWriter, status int, v any) {
	w.Header().Set("Content-Type", "application/json")
	w.WriteHeader(status)
	_ = json.NewEncoder(w).Encode(v)
}

package service

import (
	"context"
	"errors"
	"strings"

	"github.com/alefilas/documents/golang-documents/internal/domain"
)

var (
	ErrValidation = errors.New("validation error")
)

type Repository interface {
	CreateDirectory(ctx context.Context, name string) (*domain.Directory, error)
	CreateDocument(ctx context.Context, title, content string, directoryID int64) (*domain.Document, error)
	ListDocuments(ctx context.Context) ([]*domain.Document, error)
	ModerateDocument(ctx context.Context, docID int64, status string) (*domain.Document, error)
}

type Service struct {
	repo Repository
}

func New(repo Repository) *Service {
	return &Service{repo: repo}
}

func (s *Service) CreateDirectory(ctx context.Context, name string) (*domain.Directory, error) {
	if strings.TrimSpace(name) == "" {
		return nil, ErrValidation
	}
	return s.repo.CreateDirectory(ctx, name)
}

func (s *Service) CreateDocument(ctx context.Context, title, content string, directoryID int64) (*domain.Document, error) {
	if strings.TrimSpace(title) == "" || directoryID <= 0 {
		return nil, ErrValidation
	}
	return s.repo.CreateDocument(ctx, title, content, directoryID)
}

func (s *Service) ListDocuments(ctx context.Context) ([]*domain.Document, error) {
	return s.repo.ListDocuments(ctx)
}

func (s *Service) ModerateDocument(ctx context.Context, docID int64, req domain.ModerationRequest) (*domain.Document, error) {
	if docID <= 0 || strings.TrimSpace(req.Status) == "" {
		return nil, ErrValidation
	}
	return s.repo.ModerateDocument(ctx, docID, req.Status)
}

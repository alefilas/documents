package memory

import (
	"context"
	"errors"
	"sync"
	"time"

	"github.com/alefilas/documents/golang-documents/internal/domain"
)

var (
	ErrDirectoryNotFound = errors.New("directory not found")
	ErrDocumentNotFound  = errors.New("document not found")
)

type Store struct {
	mu          sync.RWMutex
	nextDocID   int64
	nextDirID   int64
	documents   map[int64]*domain.Document
	directories map[int64]*domain.Directory
}

func New() *Store {
	return &Store{
		nextDocID:   1,
		nextDirID:   1,
		documents:   make(map[int64]*domain.Document),
		directories: make(map[int64]*domain.Directory),
	}
}

func (s *Store) CreateDirectory(_ context.Context, name string) (*domain.Directory, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	d := &domain.Directory{ID: s.nextDirID, Name: name}
	s.directories[d.ID] = d
	s.nextDirID++
	copyDir := *d
	return &copyDir, nil
}

func (s *Store) CreateDocument(_ context.Context, title, content string, directoryID int64) (*domain.Document, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	if _, ok := s.directories[directoryID]; !ok {
		return nil, ErrDirectoryNotFound
	}

	d := &domain.Document{
		ID:          s.nextDocID,
		Title:       title,
		Content:     content,
		DirectoryID: directoryID,
		CreatedAt:   time.Now().UTC(),
		Status:      "DRAFT",
	}
	s.documents[d.ID] = d
	s.nextDocID++

	copyDoc := *d
	return &copyDoc, nil
}

func (s *Store) ListDocuments(_ context.Context) ([]*domain.Document, error) {
	s.mu.RLock()
	defer s.mu.RUnlock()

	out := make([]*domain.Document, 0, len(s.documents))
	for _, d := range s.documents {
		copyDoc := *d
		out = append(out, &copyDoc)
	}
	return out, nil
}

func (s *Store) ModerateDocument(_ context.Context, docID int64, status string) (*domain.Document, error) {
	s.mu.Lock()
	defer s.mu.Unlock()

	doc, ok := s.documents[docID]
	if !ok {
		return nil, ErrDocumentNotFound
	}

	doc.Status = status
	copyDoc := *doc
	return &copyDoc, nil
}

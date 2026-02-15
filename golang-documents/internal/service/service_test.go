package service

import (
	"context"
	"testing"

	"github.com/alefilas/documents/golang-documents/internal/domain"
	"github.com/alefilas/documents/golang-documents/internal/store/memory"
)

func TestCreateAndModerateDocument(t *testing.T) {
	repo := memory.New()
	svc := New(repo)

	dir, err := svc.CreateDirectory(context.Background(), "Contracts")
	if err != nil {
		t.Fatalf("create directory: %v", err)
	}

	doc, err := svc.CreateDocument(context.Background(), "Agreement", "content", dir.ID)
	if err != nil {
		t.Fatalf("create document: %v", err)
	}
	if doc.Status != "DRAFT" {
		t.Fatalf("expected DRAFT, got %s", doc.Status)
	}

	updated, err := svc.ModerateDocument(context.Background(), doc.ID, domain.ModerationRequest{Status: "APPROVED"})
	if err != nil {
		t.Fatalf("moderate document: %v", err)
	}
	if updated.Status != "APPROVED" {
		t.Fatalf("expected APPROVED, got %s", updated.Status)
	}
}

func TestValidationErrors(t *testing.T) {
	repo := memory.New()
	svc := New(repo)

	if _, err := svc.CreateDirectory(context.Background(), "  "); err != ErrValidation {
		t.Fatalf("expected ErrValidation, got %v", err)
	}

	if _, err := svc.CreateDocument(context.Background(), "", "", 1); err != ErrValidation {
		t.Fatalf("expected ErrValidation, got %v", err)
	}
}

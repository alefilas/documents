package domain

import "time"

type Document struct {
	ID          int64     `json:"id"`
	Title       string    `json:"title"`
	Content     string    `json:"content"`
	DirectoryID int64     `json:"directory_id"`
	CreatedAt   time.Time `json:"created_at"`
	Status      string    `json:"status"`
}

type Directory struct {
	ID   int64  `json:"id"`
	Name string `json:"name"`
}

type ModerationRequest struct {
	Status  string `json:"status"`
	Comment string `json:"comment"`
}

package models

import "errors"

var (
	ErrInvalidUser = errors.New("invalid user data")
)

type NewUser struct {
	Email    string `json:"email"`
	Password string `json:"password"`
	Hash     string `json:"hash_auth"`
}

func (u *NewUser) Valide() error {
	if u.Email == "" || u.Password == "" {
		return ErrInvalidUser
	}
	return nil
}

type User struct {
	Email        string `json:"email" pg:"email"`
	PasswordHash string `json:"password_hash" pg:"password_hash"`
	Hash         string `json:"hash_auth" pg:"hash_auth"`
}

func (u *User) Valide() error {
	if u.Email == "" || u.PasswordHash == "" {
		return ErrInvalidUser
	}
	return nil
}

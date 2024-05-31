package db

import (
	Users "api/models"
	"fmt"
	pshash "github.com/vzglad-smerti/password_hash"
)

func (d *DB) Register(NewUser *Users.NewUser) (*Users.User, error) {
	connection, err := d.Connect()
	if err != nil {
		return nil, err
	}
	defer connection.Close()

	if err := NewUser.Valide(); err != nil {
		return nil, err
	}

	err = d.ExistEmail(NewUser.Email)
	if err != nil {
		return nil, err
	}

	bytes, err := pshash.Hash(NewUser.Password)
	if err != nil {
		return nil, err
	}
	passwordHash := string(bytes)

	bytes, err = pshash.Hash(NewUser.Email + NewUser.Password)
	if err != nil {
		return nil, err
	}
	authHash := string(bytes)

	query := fmt.Sprintf("INSERT INTO users VALUES ('%s', '%s', '%s')", NewUser.Email, passwordHash, authHash)
	rows, err := connection.Query(query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	user := &Users.User{
		Email:        NewUser.Email,
		PasswordHash: passwordHash,
		Hash:         authHash,
	}

	return user, nil
}

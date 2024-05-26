package db

import (
	Users "api/models"
	pshash "github.com/vzglad-smerti/password_hash"
)

func (d *DB) Login(NewUser *Users.NewUser) (*Users.User, error) {
	var psVerify bool
	var emailBD, hashBD, hashPasswordBD string
	connection, err := d.Connect()
	if err != nil {
		return nil, err
	}
	defer connection.Close()

	query := "SELECT * FROM users WHERE email = $1"

	err = connection.QueryRow(query, NewUser.Email).Scan(&emailBD, &hashPasswordBD, &hashBD)
	if err != nil {
		return nil, ErrWrongPassword
	}

	User := &Users.User{
		Email:        emailBD,
		PasswordHash: hashPasswordBD,
		Hash:         hashBD,
	}

	psVerify, err = pshash.Verify(User.PasswordHash, NewUser.Password)

	if err != nil {
		return nil, err
	}

	if !psVerify {
		return nil, ErrWrongPassword
	}

	return User, nil
}

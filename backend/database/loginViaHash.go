package db

import Users "api/models"

func (d *DB) LoginViaHash(hash string) (*Users.User, error) {
	var emailBD, hashBD, hashPasswordBD string

	connection, err := d.Connect()
	if err != nil {
		return nil, err
	}
	defer connection.Close()

	query := "SELECT * FROM users WHERE auth_hash = $1"

	err = connection.QueryRow(query, hash).Scan(&emailBD, &hashPasswordBD, &hashBD)
	if err != nil {
		return nil, err
	}

	User := &Users.User{
		Email:        emailBD,
		PasswordHash: hashPasswordBD,
		Hash:         hashBD,
	}

	return User, nil

}

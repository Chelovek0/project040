package db

import (
	Users "api/models"
	"database/sql"
	"errors"
	"fmt"

	_ "github.com/lib/pq"
	"golang.org/x/crypto/bcrypt"
)

var (
	ErrExistEmail = errors.New("email is already busy")
)

const (
	host     = "localhost"
	port     = 5432
	user     = "postgres"
	password = "123"
	dbname   = "postgres"
)

type DB struct{}

func (d *DB) Connect() (*sql.DB, error) {
	psqlInfo := fmt.Sprintf("host=%s port=%d user=%s "+
		"password=%s dbname=%s sslmode=disable",
		host, port, user, password, dbname)
	db, err := sql.Open("postgres", psqlInfo)
	if err != nil {
		return nil, err
	}
	err = db.Ping()
	if err != nil {
		return nil, err
	}
	return db, nil
}

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

	bytes, err := bcrypt.GenerateFromPassword([]byte(NewUser.Password), 14)
	if err != nil {
		return nil, err
	}
	password_hash := string(bytes)

	bytes, err = bcrypt.GenerateFromPassword([]byte(NewUser.Email+NewUser.Password), 14)
	if err != nil {
		return nil, err
	}
	auth_hash := string(bytes)

	query := fmt.Sprintf("INSERT INTO users VALUES ('%s', '%s', '%s')", NewUser.Email, password_hash, auth_hash)
	rows, err := connection.Query(query)
	if err != nil {
		return nil, err
	}
	defer rows.Close()

	user := &Users.User{
		Email:        NewUser.Email,
		PasswordHash: password_hash,
	}

	return user, nil
}

func (d *DB) ExistEmail(email string) error {
	connection, err := d.Connect()
	if err != nil {
		return err
	}
	defer connection.Close()

	query := "SELECT EXISTS ( SELECT 1 FROM users WHERE email = $1 )"
	var exists bool
	err = connection.QueryRow(query, email).Scan(&exists)
	if err != nil {
		return err
	}

	if exists {
		return ErrExistEmail
	}

	return nil
}

func (d *DB) InitTables() {

}

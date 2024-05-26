package db

import (
	"database/sql"
	"errors"
	"fmt"
	_ "github.com/lib/pq"
)

var (
	ErrExistEmail    = errors.New("email is already busy")
	ErrWrongPassword = errors.New("password is wrong")
	ErrUndetected    = errors.New("Undetected error")
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

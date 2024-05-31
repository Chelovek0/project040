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
	tables           = map[string]string{
		"users":     "CREATE TABLE users (email TEXT, password_hash TEXT, hash_auth TEXT);",
		"statistic": "CREATE TABLE statistic (hash_auth TEXT, mood SMALLINT, weather SMALLINT, whereiam SMALLINT, whom SMALLINT, comment TEXT, date TEXT);",
	}
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

func (d *DB) ExistTable(table string) (bool, error) {
	connection, err := d.Connect()
	if err != nil {
		return false, err
	}
	defer connection.Close()

	query := "SELECT EXISTS (SELECT 1 FROM information_schema.columns WHERE table_name = $1);"
	var exists bool
	err = connection.QueryRow(query, table).Scan(&exists)

	return exists, err
}

func (d *DB) CreateTable(table string) error {
	connection, err := d.Connect()
	if err != nil {
		return err
	}
	defer connection.Close()

	_ = connection.QueryRow(tables[table])

	return nil
}

func (d *DB) InitTables() error {
	for table, _ := range tables {
		exists, err := d.ExistTable(table)
		if err != nil {
			return err
		}
		if !exists {
			err := d.CreateTable(table)
			fmt.Print(table)
			if err != nil {
				return err
			}
		}
	}
	return nil
}

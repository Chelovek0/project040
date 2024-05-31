package db

import "fmt"

type RequestGet struct {
	Hash string `json:"hash_auth" pg:"hash"`
	Date string `json:"date" pg:"date"`
}

type RequestSet struct {
	Hash     string `json:"hash_auth" pg:"hash"`
	Date     string `json:"date" pg:"date"`
	Mood     int    `json:"mood" pg:"mood"`
	Weather  int    `json:"weather" pg:"weather"`
	Whereiam int    `json:"whereiam" pg:"whereiam"`
	Whom     int    `json:"whom" pg:"whom"`
	Comment  string `json:"comment" pg:"comment"`
}

type ResponseGet struct {
	HashAuth string `json:"hash_auth" pg:"hashAuth"`
	Mood     int    `json:"mood" pg:"mood"`
	Weather  int    `json:"weather" pg:"weather"`
	Whereiam int    `json:"whereiam" pg:"whereiam"`
	Whom     int    `json:"whom" pg:"whom"`
	Comment  string `json:"comment" pg:"comment"`
	Date     string `json:"date" pg:"date"`
}

type ResponseSet struct {
	Success bool `json:"success"`
}

func (d *DB) GetStatistic(hash string, date string) (*ResponseGet, error) {
	//var hash_auth, mood, weather, whereiam, whom, comment, date string
	var st ResponseGet
	connection, err := d.Connect()
	if err != nil {
		return nil, err
	}
	defer connection.Close()

	query := "SELECT * FROM statistic WHERE hash_auth = $1 AND date = $2"

	err = connection.QueryRow(query, hash, date).Scan(&st.HashAuth, &st.Mood, &st.Weather, &st.Whereiam, &st.Whom, &st.Comment, &st.Date)

	fmt.Print(err, st.Mood)

	if err != nil {
		return nil, err
	}

	return &st, nil
}

func (d *DB) SetStatistic(set RequestSet) (*ResponseSet, error) {
	var st ResponseSet
	connection, err := d.Connect()
	if err != nil {
		return nil, err
	}
	defer connection.Close()

	query := "INSERT INTO statistic (hash_auth, mood, weather, whereiam, whom, comment, date) VALUES ($1, $2, $3, $4, $5, $6, $7)"

	err = connection.QueryRow(query, set.Hash, set.Mood, set.Weather, set.Whereiam, set.Whom, set.Comment, set.Date).Scan(&st)

	if err != nil {
		return nil, err
	}

	st.Success = true

	return &st, nil
}

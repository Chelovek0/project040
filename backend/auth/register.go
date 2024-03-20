package auth

import (
	db "api/database"
	Users "api/models"
	"encoding/json"
	"errors"
	"net/http"
)

var database *db.DB

func Register(w http.ResponseWriter, r *http.Request) {
	var user Users.NewUser
	err := json.NewDecoder(r.Body).Decode(&user)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	err = database.Register(&user)

	if errors.Is(err, db.ErrExistEmail) {
		w.WriteHeader(http.StatusBadRequest)
	} else if err == nil {
		w.WriteHeader(http.StatusOK)
	} else {
		panic(err)
	}
}

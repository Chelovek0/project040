package auth

import (
	db "api/database"
	Users "api/models"
	"encoding/json"
	"errors"
	"net/http"
)

type Response struct {
	Hash string `json:"hash_auth"`
}

var database *db.DB

func Register(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	var NewUser Users.NewUser
	err := json.NewDecoder(r.Body).Decode(&NewUser)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	User, err := database.Register(&NewUser)

	response := &Response{}

	if errors.Is(err, db.ErrExistEmail) || errors.Is(err, Users.ErrInvalidUser) {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	} else if err == nil {
		w.WriteHeader(http.StatusCreated)
		response.Hash = User.PasswordHash
	} else {
		panic(err)
	}

	json.NewEncoder(w).Encode(response)
}

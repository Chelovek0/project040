package app

import (
	db "api/database"
	Users "api/models"
	"encoding/json"
	"net/http"
)

type Request struct {
	Hash string `json:"hash_auth"`
}
type Response struct {
	User *Users.User `json:"user"`
}

var database *db.DB

func Info(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var request Request
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	User, err := database.LoginViaHash(request.Hash)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	response := &Response{}

	w.WriteHeader(http.StatusOK)
	response.User = User

	err = json.NewEncoder(w).Encode(response)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
	}
}

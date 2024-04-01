package auth

import (
	db "api/database"
	Users "api/models"
	"encoding/json"
	"errors"
	"net/http"
)

func Login(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")
	var NewUser Users.NewUser

	err := json.NewDecoder(r.Body).Decode(&NewUser)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}
	response := &Response{}
	User, err := database.Login(&NewUser)
	if errors.Is(err, db.ErrWrongPassword) {
		w.WriteHeader(http.StatusUnauthorized)
		err = json.NewEncoder(w).Encode(response)
		return
	} else if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	w.WriteHeader(http.StatusOK)
	response.Hash = User.Hash

	err = json.NewEncoder(w).Encode(response)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
	}
}

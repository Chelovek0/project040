package app

import (
	db "api/database"
	"encoding/json"
	"fmt"
	"net/http"
)

var database *db.DB

func SetStatistic(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var request db.RequestSet
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	fmt.Print(request.Mood)

	_, err = database.LoginViaHash(request.Hash)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	sr, err := database.SetStatistic(request)

	w.WriteHeader(http.StatusOK)
	err = json.NewEncoder(w).Encode(sr)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
	}
}

func GetStatistic(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/json")

	var request db.RequestGet
	err := json.NewDecoder(r.Body).Decode(&request)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	_, err = database.LoginViaHash(request.Hash)
	if err != nil {
		w.WriteHeader(http.StatusBadRequest)
		http.Error(w, err.Error(), http.StatusBadRequest)
		return
	}

	sr, err := database.GetStatistic(request.Hash, request.Date)

	w.WriteHeader(http.StatusOK)

	err = json.NewEncoder(w).Encode(sr)
	if err != nil {
		http.Error(w, err.Error(), http.StatusBadRequest)
	}
}

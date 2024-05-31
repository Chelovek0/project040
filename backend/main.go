package main

import (
	"api/app"
	"api/auth"
	db "api/database"
	"log"
	"net/http"
)

var database *db.DB

func main() {
	err := database.InitTables()
	if err != nil {
		log.Fatal(err)
	}
	http.HandleFunc("/register", auth.Register)
	http.HandleFunc("/login", auth.Login)
	http.HandleFunc("/info/set", app.SetStatistic)
	http.HandleFunc("/info/get", app.GetStatistic)
	http.ListenAndServe(":32673", nil)
}

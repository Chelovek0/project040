package main

import (
	"api/app"
	"api/auth"
	"net/http"
)

func main() {
	http.HandleFunc("/register", auth.Register)
	http.HandleFunc("/login", auth.Login)
	http.HandleFunc("/info", app.Info)
	http.ListenAndServe(":32673", nil)
}

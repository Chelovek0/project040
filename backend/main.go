package main

import (
	"api/auth"
	"net/http"
)

func main() {
	http.HandleFunc("/register", auth.Register)
	http.HandleFunc("/login", auth.Login)
	http.ListenAndServe(":8080", nil)
}

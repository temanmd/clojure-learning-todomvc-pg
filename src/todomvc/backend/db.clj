(ns todomvc.backend.db
  (:require
    [next.jdbc :as jdbc]))

(def db-config
  {:dbtype "postgresql"
   :host "localhost"
   :user "postgres"
   :password "postgres"})

(def db
  (jdbc/get-datasource db-config))

(comment
  db-config
  db
  (jdbc/execute! db ["CREATE TABLE users(
                        id SERIAL,
                        username TEXT,
                        email TEXT,
                        passowrd TEXT)"]))

(ns todomvc.backend.db
  (:require
    [honey.sql :as hsql]
    [honey.sql.helpers :as hsql-helpers]
    [next.jdbc :as jdbc]
    [next.jdbc.result-set :as result-set]))

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

  (def select-sql
    (hsql/format
      {:select [:*]
       :from [:users]
       :where [:= :users.email
               "tema@email.example"]}))
  select-sql
  (jdbc/execute! db
    select-sql
    {:return-keys true
     :builder-fn result-set/as-unqualified-maps})

  (jdbc/execute! db
    ["CREATE TABLE users(
        id SERIAL,
        username TEXT,
        email TEXT,
        password TEXT
      )"])
  (jdbc/execute! db ["DROP TABLE users"])
  (jdbc/execute! db
    ["INSERT INTO users
      (username, email, password) values
      ('tema', 'tema@email.example', 'password')"]
    {:return-keys true
     :builder-fn result-set/as-unqualified-maps})
  (jdbc/execute! db
    ["SELECT * FROM users"]
    {:return-keys true
     :builder-fn result-set/as-unqualified-maps}))

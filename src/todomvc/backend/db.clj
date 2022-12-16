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

(def datasource
  (jdbc/get-datasource db-config))

(def db
  (jdbc/with-options datasource
    {:return-keys true
     :builder-fn result-set/as-unqualified-lower-maps}))

(defn db-query
  [sql]
  (jdbc/execute! db sql))

(defn db-query-one
  [sql]
  (jdbc/execute-one! db sql))

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
    select-sql)
  (jdbc/execute-one! db
    select-sql)

  (db-query select-sql)
  (db-query-one select-sql)

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
      ('tema', 'tema@email.example', 'password')"])
  (jdbc/execute! db
    ["SELECT * FROM users"]))

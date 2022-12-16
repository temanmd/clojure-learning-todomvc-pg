(ns todomvc.backend.db
  (:require
    [honey.sql :as hsql]
    [next.jdbc :as jdbc]
    [next.jdbc.result-set :as result-set]))

(def db-config
  {:dbtype "postgresql"
   :host "localhost"
   :user "postgres"
   :password "postgres"
   :dbname "todomvc"})

(def datasource
  (jdbc/get-datasource db-config))

(def db
  (jdbc/with-options datasource
    {:return-keys true
     :builder-fn result-set/as-unqualified-lower-maps}))

(defn create-todos-table
  []
  (db-query
    (hsql/format
      {:create-table :todos
       :with-columns
       [[:id :serial :primary-key]
        [:title :text [:not nil]]
        [:done :boolean [:not nil]]]})))

(defn add-todo
  [title]
  (db-query-one
    (hsql/format
      {:insert-into [:todos]
       :columns [:title :done]
       :values [[title false]]})))

(defn delete-todo
  [id]
  (db-query-one
    (hsql/format
      {:delete-from [:todos]
       :where [:= :id id]})))

(defn toggle-todo
  [id]
  (db-query-one
    (hsql/format
      {:update :todos
       :set {:done [:not :done]}
       :where [:= :id id]})))

(defn change-todo
  [id title]
  (db-query-one
    (hsql/format
      {:update :todos
       :set {:title title}
       :where [:= :id id]})))

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
    ["SELECT * FROM users"])

  (create-todos-table)

  (add-todo "Make a sandwich")
  (delete-todo 1)
  (toggle-done 3)
  (change-todo 3 "Make a salad"))

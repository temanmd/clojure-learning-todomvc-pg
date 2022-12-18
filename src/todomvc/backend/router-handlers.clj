(ns todomvc.backend.router-handlers
  (:require [todomvc.backend.db :as db]))

(defn get-all-todos
  [_]
  {:status 200
   :headers {"Content-Type" "application/json"}
   :body (db/get-all-todos)})

(comment
  (get-all-todos nil))

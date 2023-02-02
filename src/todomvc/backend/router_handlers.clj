(ns todomvc.backend.router-handlers
  (:require
    [todomvc.backend.db :as db]
    [todomvc.backend.html-page :as html]))

(defn frontend-handler
  [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html/html-page)})

(defn get-all-todos
  [_]
  {:status 200
   :body (db/get-all-todos)})

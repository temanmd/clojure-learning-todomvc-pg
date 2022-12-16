(ns todomvc.backend.test-helpers
  (:require
    [clojure.test :refer :all]
    [next.jdbc :as jdbc]
    [todomvc.backend.db :as db]))

(defn create-test-db
  []
  (db/db-query ["CREATE DATABASE \"todomvc-test\""]))

(defn destroy-test-db
  []
  (db/db-query ["DROP DATABASE \"todomvc-test\""]))

(defn db-test-setup-fixture
  [f]
  (create-test-db)
  (f)
  (destroy-test-db))

(defn run-with-test-db-fixture
  [f]
  (with-redefs
    [db/db-config (merge
                    db/db-config
                    {:dbname "todomvc-test"})]
    (f)))

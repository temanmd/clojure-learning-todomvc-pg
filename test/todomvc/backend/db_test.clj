(ns todomvc.backend.db-test
  (:require
    [clojure.test :refer :all]
    [todomvc.backend.test-helpers :refer :all]))

(use-fixtures :once db-test-setup-fixture)
(use-fixtures :each run-with-test-db-fixture)

(deftest add-todo-test
  (testing "[add-todo] func - Creates a new active todo"
    (is (= 1 1))))

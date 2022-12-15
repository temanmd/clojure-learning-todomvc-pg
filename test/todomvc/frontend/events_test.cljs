(ns todomvc.frontend.events-test
  (:require
    [cljs.test :refer-macros [deftest
                              is
                              testing
                              use-fixtures]]
    [reagent.dom :as rdom]
    [re-frame.core :refer [dispatch-sync
                           subscribe
                           reg-event-fx]]
    [todomvc.frontend.effects]
    [todomvc.frontend.events]
    [todomvc.frontend.subs]
    [todomvc.frontend.db :refer [todos->local-store]]))

(defn initialize-app-db [f]
  (todos->local-store {})
  (dispatch-sync [:initialize])
  (f))

(use-fixtures :each initialize-app-db)

(deftest initialize-event-test
  (testing "Initializing the app-db state"
    (let [app-db (subscribe [:app-db])]
      (is (= @app-db
            {:todos {} :showing :all})))))

(deftest set-showing-event-test
  (testing "[:set-showing] event - Showing only done todos"
    (let [showing (subscribe [:showing])]
      (is (= @showing :all))
      (dispatch-sync [:set-showing :done])
      (is (= @showing :done)))))

(deftest add-todo-event-test
  (testing "[:add-todo] event - Adding a new todo"
    (let [todos (subscribe [:todos])]
      (is (= @todos nil))
      (dispatch-sync [:add-todo "Make a breakfast"])
      (is (= (first @todos)
            {:id 1 :title "Make a breakfast" :done false})))))

(deftest save-event-test
  (testing "[:save] event - Changing existed todo's title"
    (let [todos (subscribe [:todos])]
      (dispatch-sync [:add-todo "Make a breakfast"])
      (dispatch-sync [:save 1 "Make a diner"])
      (is (= (first @todos)
            {:id 1 :title "Make a diner" :done false})))))

(deftest delete-todo-event-test
  (testing "[:delete-todo] event - Deleting existed todo"
    (let [todos (subscribe [:todos])]
      (dispatch-sync [:add-todo "Make a breakfast"])
      (dispatch-sync [:delete-todo 1])
      (is (= @todos nil)))))

(deftest toggle-done-event-test
  (testing "[:toggle-done] event - Toggle existed todo's done flag"
    (let [todos (subscribe [:todos])
          todo-done? #(-> @todos
                        first
                        :done)]
      (dispatch-sync [:add-todo "Make a breakfast"])
      (dispatch-sync [:toggle-done 1])
      (is (= (todo-done?) true))
      (dispatch-sync [:toggle-done 1])
      (is (= (todo-done?) false)))))

(deftest complete-all-toggle-event-test
  (testing "[:complete-all-toggle] event - Toggle all todos done flag"
    (let [todos (subscribe [:todos])
          all-todos-done? #(every? :done @todos)]
      (dispatch-sync [:add-todo "Make a breakfast"])
      (dispatch-sync [:add-todo "Make a dinner"])
      (dispatch-sync [:add-todo "Make a lunch"])
      (dispatch-sync [:toggle-done 2])
      (dispatch-sync [:complete-all-toggle])
      (is (= (all-todos-done?) true))
      (dispatch-sync [:complete-all-toggle])
      (is (= (all-todos-done?) false)))))

(deftest clear-completed-event-test
  (testing "[:clear-completed] event - Remove all done todos"
    (let [todos (subscribe [:todos])
          no-done-todos? #(empty? (filter :done @todos))]
      (dispatch-sync [:add-todo "Make a breakfast"])
      (dispatch-sync [:add-todo "Make a dinner"])
      (dispatch-sync [:add-todo "Make a lunch"])
      (dispatch-sync [:toggle-done 2])
      (dispatch-sync [:toggle-done 3])
      (dispatch-sync [:clear-completed])
      (is (= (no-done-todos?) true)))))

; (reg-event-fx
;   :event-with-coeffect
;   (fn [{:keys [db]} _]
;     {:fx [[:dispatch [:add-todo "Make a lunch"]]
;           [:dispatch [:set-showing :active]]]}
;     {:db db}))

; (deftest event-with-effect-test
;   (testing "[:event-with-effect] example event - Do a side-effect.
;             First add a todo, then change showing filter to :active"
;     (let [todos (subscribe [:todos])
;           showing (subscribe [:showing])]
;       (dispatch-sync [:event-with-coeffect])
;       (is (= (count @todos) 1))
;       (is (= @showing :active)))))

(comment
  (let [a {1 {:b 2} 2 {:b 3}}
        empty-c {}]
    (println "qwdqwdqwd")
    (println (vals empty-c))
    (println (vals a))
    (count {})
    (any? ())))
(ns todomvc.frontend.subs-test
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

(deftest visible-todos-sub-test-all
  (testing "[:visible-todos] sub - Show all todos"
    (let [visible-todos (subscribe [:visible-todos])]
      (dispatch-sync [:add-todo "Make a breakfast"])
      (dispatch-sync [:add-todo "Make a diner"])
      (dispatch-sync [:add-todo "Make a lunch"])
      (dispatch-sync [:toggle-done 1])
      (dispatch-sync [:set-showing :all])
      (is (= @visible-todos
            [{:id 1, :title "Make a breakfast", :done true}
             {:id 2, :title "Make a diner", :done false}
             {:id 3, :title "Make a lunch", :done false}])))))

(deftest visible-todos-sub-test-active
  (testing "[:visible-todos] sub - Show only active todos"
    (let [visible-todos (subscribe [:visible-todos])]
      (dispatch-sync [:add-todo "Make a breakfast"])
      (dispatch-sync [:add-todo "Make a diner"])
      (dispatch-sync [:add-todo "Make a lunch"])
      (dispatch-sync [:toggle-done 1])
      (dispatch-sync [:set-showing :active])
      (is (= @visible-todos
            [{:id 2, :title "Make a diner", :done false}
             {:id 3, :title "Make a lunch", :done false}])))))

(deftest visible-todos-sub-test-done
  (testing "[:visible-todos] sub - Show only done todos"
    (let [visible-todos (subscribe [:visible-todos])]
      (dispatch-sync [:add-todo "Make a breakfast"])
      (dispatch-sync [:add-todo "Make a diner"])
      (dispatch-sync [:add-todo "Make a lunch"])
      (dispatch-sync [:toggle-done 1])
      (dispatch-sync [:toggle-done 2])
      (dispatch-sync [:set-showing :done])
      (is (= @visible-todos
            [{:id 1, :title "Make a breakfast", :done true}
             {:id 2, :title "Make a diner", :done true}])))))
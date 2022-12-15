(ns todomvc.frontend.events
  (:require
    [re-frame.core :refer [reg-event-db
                           reg-event-fx
                           inject-cofx
                           path
                           after]]
    [todomvc.frontend.db :refer [default-db
                                 todos->local-store]]
    [cljs.spec.alpha :as s]))

;;; Interceptors --------------------------------------

(defn check-and-throw
  "Throws an exception if db does not match the Spec a-spec"
  [a-spec db]
  (when-not (s/valid? a-spec db)
    (throw (ex-info
             (str "Spec check failed: "
               (s/explain-str a-spec db))
             {}))))

(def check-spec-interceptor
  (after (partial check-and-throw :todomvc.frontend.db/db)))

(def ->local-store (after todos->local-store))

(def todo-interceptors [check-spec-interceptor
                        (path :todos)
                        ->local-store])

;;; Helpers -------------------------------------------

(defn allocate-next-id
  "Returns the next todo id.
  Assumes todos are sorted.
  Returns one more than the current largest id."
  [todos]
  ((fnil inc 0) (last (keys todos))))

;;; Events --------------------------------------------

;; Initialize DB

(reg-event-fx
  :initialize
  [(inject-cofx :local-store-todos)
   check-spec-interceptor]
  (fn [{:keys [db local-store-todos]} _]
    {:db (assoc default-db :todos local-store-todos)}))

;; Todos

(reg-event-db
  :set-showing
  [check-spec-interceptor (path :showing)]
  (fn [old-showing-value [_ new-showing-value]]
    new-showing-value))

(reg-event-db
  :add-todo
  todo-interceptors
  (fn [todos [_ text]]
    (let [id (allocate-next-id todos)]
      (assoc todos id {:id id :title text :done false}))))

(reg-event-db
  :save
  todo-interceptors
  (fn [todos [_ id title]]
    (assoc-in todos [id :title] title)))

(reg-event-db
  :delete-todo
  todo-interceptors
  (fn [todos [_ id]]
    (dissoc todos id)))

(reg-event-db
  :toggle-done
  todo-interceptors
  (fn [todos [_ id]]
    (update-in todos [id :done] not)))

(reg-event-db
  :complete-all-toggle
  todo-interceptors
  (fn [todos _]
    (let [todos-vals (vals todos)
          todos-keys (keys todos)
          new-done (not-every? :done todos-vals)]
      (reduce #(assoc-in %1 [%2 :done] new-done)
        todos
        todos-keys))))

(reg-event-db
  :clear-completed
  todo-interceptors
  (fn [todos _]
    (let [done-ids (->> (vals todos)
                     (filter :done)
                     (map :id))]
      (reduce dissoc todos done-ids))))

;; Routes

(reg-event-fx
  :navigate
  (fn [_ [_ route]]
    {:fx [[:dispatch [:navigate! route]]]}))

(reg-event-db
  :navigated
  (fn [db [_ new-match]]
    (assoc db :current-route new-match)))

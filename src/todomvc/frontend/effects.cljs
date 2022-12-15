(ns todomvc.frontend.effects
  (:require
    [re-frame.core :refer [reg-fx]]
    [reitit.frontend.easy :as rfe]))

;; (reg-fx
;;  :navigate!
;;  (fn [k params query]
;;    (rfe/push-state k params query)))

(reg-fx
  :navigate!
  (fn [k params query]
    (rfe/push-state k params query)))

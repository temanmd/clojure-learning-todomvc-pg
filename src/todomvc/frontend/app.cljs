(ns todomvc.frontend.app
  (:require
    [re-frame.core :as re-frame]
    [reagent.core :as r]
    [reitit.frontend :as rf]
    [reitit.frontend.easy :as rfe]
    [reitit.coercion.spec :as rcs]
    [reitit.core :as ring-core]
    [todomvc.shared.routes :as shared-routes]
    [todomvc.frontend.events]
    [todomvc.frontend.subs]
    [todomvc.frontend.effects]
    [todomvc.frontend.views :as views]))

(defn init-routes []
  (rfe/start!
    (rf/router shared-routes/routes {:data {:coercion rcs/coercion}})

    (fn [new-match] (re-frame/dispatch [:navigated new-match]))

    {:use-fragment false}))

(defn mount-root []
  (.render views/root (r/as-element [views/component])))

(defn init []
  (init-routes)
  (re-frame/dispatch-sync [:initialize])
  (mount-root))

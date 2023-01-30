(ns todomvc.shared.routes
  (:require
    #?@(:clj [[todomvc.backend.router-handlers :as handlers]]
        :cljs [[todomvc.frontend.views :as views]])))

(def routes
  [["/api"
    ["/todos"
     {:name :todos
      #?@(:clj [:get handlers/get-all-todos])}]]
   ["/app"
    {:name :app-index
     #?@(:cljs [:view views/home-page])
     #?@(:clj [:get handlers/frontend-handler])}]])

(comment
  routes)

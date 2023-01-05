(ns todomvc.backend.routes
  (:require
    [reitit.ring :as ring]
    [reitit.core :as ring-core]
    [todomvc.backend.html-page :as html]
    [todomvc.shared.routes :as shared-routes]
    [clojure.pprint :refer [pprint]]))

(defn frontend-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html/html-page)})

(defn api-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello, World! Its API."})

; (defn my-expand
;   [data opts]
;   (if (keyword? data)
;     (case data
;       :todos {:handler api-handler}
;       {:handler frontend-handler})
;     (ring-core/expand data opts)))

(defn my-expand [registry]
  (fn [data opts]
    (if (keyword? data)
      (some-> data
        registry
        (ring-core/expand opts)
        (assoc :name data))
      (ring-core/expand data opts))))

(def router
  (ring/ring-handler
    (ring/router
      shared-routes/routes
      {:expand (my-expand {:todos {:get api-handler}
                           :app-index {:get frontend-handler}})})
    (ring/routes
      (ring/create-resource-handler {:path "/" :root "/public"})
      (ring/create-default-handler
        {:not-found (constantly {:status 404 :body "Not found"})}))))

(comment
  (pprint shared-routes/routes)
  (pprint {:a 1 :b 2}))

(ns todomvc.backend.routes
  (:require
    [reitit.ring :as ring]
    [reitit.core :as ring-core]
    [todomvc.backend.html-page :as html]
    [todomvc.shared.routes :as shared-routes]))

(defn frontend-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body (html/html-page)})

(defn api-handler [request]
  {:status 200
   :headers {"Content-Type" "text/html"}
   :body "Hello, World! Its API."})

(defn my-expand [data opts]
  (if (keyword? data)
    (case data
      :api {:handler api-handler}
      {:handler frontend-handler})
    (ring-core/expand data opts)))

(def router
  (ring/ring-handler
    (ring/router
      shared-routes/routes
      {:expand my-expand})
    (ring/routes
      (ring/create-resource-handler {:path "/" :root "/public"})
      (ring/create-default-handler
        {:not-found (constantly {:status 404 :body "Not found"})}))))
